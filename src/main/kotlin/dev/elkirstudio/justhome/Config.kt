package dev.elkirstudio.justhome

import kotlinx.serialization.Serializable

/**
 * Plugin configuration data class.
 */
@Serializable
data class Config(
    val maxHomes: Int = 5,
    val language: String = "en"
)
