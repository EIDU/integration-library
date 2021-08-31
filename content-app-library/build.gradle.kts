import utils.version

plugins {
    id("com.android.library")
    id("maven-publish")
    id("com.diffplug.spotless") version "5.14.2"
    id("de.mannodermaus.android-junit5")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30
        version = version()

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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    androidTestImplementation("de.mannodermaus.junit5:android-test-core:1.2.2")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")
    androidTestRuntimeOnly("de.mannodermaus.junit5:android-test-runner:1.2.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")

    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito:mockito-core:3.12.4")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun libraryArtifactId(): String = "content-app-library"

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/EIDU/content-app-library")
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.eidu"
            artifactId = libraryArtifactId()
            version = version()
            artifact("$buildDir/outputs/aar/${libraryArtifactId()}-release.aar")
        }
    }
}

spotless {
    java {
        target("src/*/java/**/*.java")
        importOrder()
        removeUnusedImports()
        googleJavaFormat().aosp()
    }
}
