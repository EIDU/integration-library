import java.util.Base64

plugins {
    id("com.android.library")
    id("maven-publish")
    id("com.diffplug.spotless") version "6.25.0"
    id("de.mannodermaus.android-junit5")
    id("signing")
    id("com.palantir.git-version") version "3.0.0"
    id("com.github.jk1.dependency-license-report") version "2.8"
    id("tech.yanand.maven-central-publish").version("1.3.0")
}

val gitVersion: groovy.lang.Closure<String> by extra

android {
    namespace = "com.eidu.integration"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
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

    lint {
        enable += "MissingPermission"
        enable += "SuspiciousImport"
        enable += "UsesMinSdkAttributes"
        enable += "Proguard"
        checkTestSources = true
        checkAllWarnings = true
    }

    useLibrary("android.test.mock")
}

licenseReport {
    configurations = arrayOf("releaseRuntimeClasspath")
    allowedLicensesFile = File("$projectDir/allowed-licenses.json")
}

tasks.named("checkLicense") {
    // The checkLicense task does not declare this input itself, so we do it here. This ensures
    // that a modification of the file causes the checkLicense task to be re-evaluated.
    inputs.file("$projectDir/allowed-licenses.json")
}

tasks.named("check") {
    dependsOn("checkLicense")
}

dependencies {
    implementation("androidx.annotation:annotation:1.8.0")

    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    androidTestImplementation("de.mannodermaus.junit5:android-test-core:1.4.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-params:5.10.3")
    androidTestRuntimeOnly("de.mannodermaus.junit5:android-test-runner:1.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.3")

    testImplementation("androidx.test:core:1.6.1")
    testImplementation("org.mockito:mockito-core:5.12.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Javadoc>("javadoc") {
    val variant = android.libraryVariants.first { it.name == "release" }
    description = "Generates Javadoc for ${variant.name}."
    source = fileTree(variant.sourceSets.first { it.name == "main" }.javaDirectories.first())
    classpath = files(variant.javaCompileProvider.map { it.classpath }) +
            files("${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar")
    (options as StandardJavadocDocletOptions).apply {
        source = "8" // workaround for https://bugs.openjdk.java.net/browse/JDK-8212233
        links(
            "https://docs.oracle.com/javase/7/docs/api/",
            "https://d.android.com/reference/"
        )
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.named<Javadoc>("javadoc"))
}

fun libraryArtifactId(): String = "integration-library"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.eidu"
            artifactId = libraryArtifactId()
            version = gitVersion()
            artifact("${layout.buildDirectory.get()}/outputs/aar/${libraryArtifactId()}-release.aar")
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

mavenCentral {
    authToken.set(Base64.getEncoder().encodeToString("${System.getenv("MAVEN_CENTRAL_USERNAME")}:${System.getenv("MAVEN_CENTRAL_PASSWORD")}".toByteArray()))
    publishingType.set("USER_MANAGED")
    maxWait.set(300)
}

spotless {
    java {
        target("src/*/java/**/*.java")
        importOrder()
        removeUnusedImports()
        googleJavaFormat().aosp()
    }
}
