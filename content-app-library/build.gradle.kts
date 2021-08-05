import java.io.ByteArrayOutputStream

plugins {
    id("com.android.library")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30
        version = version()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    ktlintRuleset("com.eidu:ktlint-rules:1.0.4")
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

// This is taken from the domain lib and could be externalized to some utility package
fun run(command: String): String {
    ByteArrayOutputStream().use { output ->
        exec {
            commandLine("sh", "-c", command)
            standardOutput = output
        }
        return output.toString().trim()
    }
}

fun version(): String = System.getenv("GITHUB_RUN_NUMBER")?.let {
    "1.0.$it" + (
            run("git rev-parse --abbrev-ref HEAD").takeIf { it != "main" }?.let { "-$it" } ?: ""
            )
} ?: "snapshot"
