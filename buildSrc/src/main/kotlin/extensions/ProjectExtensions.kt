package extensions

import org.gradle.api.Project
import utils.getLocalProperty
import utils.getLocalPropertyOrNull

/**
 * Obtain property declared on `$projectRoot/local.properties` file.
 *
 * @param propertyName the name of declared property
 */
fun Project.getLocalProperty(propertyName: String): String =
    getLocalProperty(propertyName, this)