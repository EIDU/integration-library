package utils

import org.gradle.api.Project

fun Project.version(): String = System.getenv("GITHUB_RUN_NUMBER")?.let {
    "1.0.$it" + (
            run("git rev-parse --abbrev-ref HEAD").takeIf { it != "main" }?.let { "-$it" } ?: ""
            )
} ?: "snapshot"

