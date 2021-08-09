package utils

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun Project.run(command: String): String {
    ByteArrayOutputStream().use { output ->
        exec {
            commandLine("sh", "-c", command)
            standardOutput = output
        }
        return output.toString().trim()
    }
}