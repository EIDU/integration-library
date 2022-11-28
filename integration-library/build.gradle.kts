plugins {
    id("com.android.library")
    id("maven-publish")
    id("com.diffplug.spotless") version "5.14.2"
    id("de.mannodermaus.android-junit5")
    id("signing")
    id("com.palantir.git-version") version "0.12.2"
}

val gitVersion: groovy.lang.Closure<String> by extra

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        version = gitVersion()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        enable(
            "MissingPermission",
            "SuspiciousImport",
            "UsesMinSdkAttributes",
            "Proguard"
        )
        isCheckTestSources = true
        isCheckAllWarnings = true
        isWarningsAsErrors = true
    }

    useLibrary("android.test.mock")
}

dependencies {
    implementation("androidx.annotation:annotation:1.5.0")

    androidTestImplementation("androidx.test:runner:1.5.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    androidTestImplementation("de.mannodermaus.junit5:android-test-core:1.3.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    androidTestRuntimeOnly("de.mannodermaus.junit5:android-test-runner:1.3.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")

    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.mockito:mockito-core:4.0.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Javadoc>("javadoc") {
    val variant = android.libraryVariants.first { it.name == "release" }
    description = "Generates Javadoc for ${variant.name}."
    source = fileTree(variant.sourceSets.first { it.name == "main" }.javaDirectories.first())
    classpath = files(variant.javaCompile.classpath.files) +
            files("${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar")
    (options as StandardJavadocDocletOptions).apply {
        source = "8" // workaround for https://bugs.openjdk.java.net/browse/JDK-8212233
        links(
            "https://docs.oracle.com/javase/7/docs/api/",
            "https://d.android.com/reference/"
        )
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.named<Javadoc>("javadoc"))
}

fun libraryArtifactId(): String = "integration-library"

publishing {
    repositories {
        maven {
            name = "MavenCentral"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_CENTRAL_USERNAME")
                password = System.getenv("MAVEN_CENTRAL_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.eidu"
            artifactId = libraryArtifactId()
            version = gitVersion()
            artifact("$buildDir/outputs/aar/${libraryArtifactId()}-release.aar")
            artifact(sourcesJar)
            artifact(javadocJar)

            pom {
                name.value(libraryArtifactId())
                description.value("EIDU Integration Library")
                url.value("https://github.com/EIDU/integration-library")
                licenses {
                    license {
                        name.value("MIT License")
                        url.value("https://raw.githubusercontent.com/EIDU/integration-library/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.value("berlix")
                        name.value("Felix Engelhardt")
                        url.value("https://github.com/berlix/")
                    }
                }
                scm {
                    url.value("https://github.com/EIDU/integration-library")
                    connection.value("scm:git:git://github.com/EIDU/integration-library.git")
                    developerConnection.value("scm:git:ssh://git@github.com/EIDU/integration-library.git")
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        System.getenv("SIGNING_KEY_ID"),
        System.getenv("SIGNING_KEY"),
        System.getenv("SIGNING_PASSWORD")
    )
    sign(publishing.publications)
}

spotless {
    java {
        target("src/*/java/**/*.java")
        importOrder()
        removeUnusedImports()
        googleJavaFormat().aosp()
    }
}
