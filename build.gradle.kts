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
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.7.1.1")

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
            url = uri("https://maven.pkg.github.com/EIDU/content-app-library")
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

ProcessBuilder("git config --local core.hooksPath git-hooks".split(" ")).start()
