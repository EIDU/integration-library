import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import extensions.getLocalProperty

plugins {
    id("com.github.ben-manes.versions") version "0.33.0"
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/EIDU/main-app")
            credentials {
                username = System.getenv("READPACKAGES_GITHUB_USER")
                    ?: System.getenv("GITHUB_READPACKAGES_USER")
                            ?: getLocalProperty("githubReadPackagesUser")
                password = System.getenv("READPACKAGES_GITHUB_TOKEN")
                    ?: System.getenv("GITHUB_READPACKAGES_TOKEN")
                            ?: getLocalProperty("githubReadPackagesToken")
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class).configure {
    gradleReleaseChannel = "current"
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}
