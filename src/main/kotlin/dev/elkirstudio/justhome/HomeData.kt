package dev.elkirstudio.justhome

import kotlinx.serialization.Serializable

/**
 * Represents a single home location.
 */
@Serializable
data class HomeLocation(
    val x: Double,
    val y: Double,
    val z: Double,
    val worldName: String,
    val yaw: Float = 0f,
    val pitch: Float = 0f
)

/**
 * Represents all homes for a single player.
 */
@Serializable
data class PlayerHomes(
    val homes: MutableMap<String, HomeLocation> = mutableMapOf()
)

/**
 * Root data structure for all homes.
 */
@Serializable
data class HomesData(
    val players: MutableMap<String, PlayerHomes> = mutableMapOf()
)
