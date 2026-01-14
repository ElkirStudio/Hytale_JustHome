package dev.elkirstudio.justhome

import kotlinx.serialization.Serializable

/**
 * Language messages data class.
 * All messages support placeholders: {home}, {count}, {x}, {y}, {z}, {world}, {max}
 */
@Serializable
data class Lang(
    // SetHome messages
    val homeSet: String = "Home '{home}' has been set!",
    val homeUpdated: String = "Home '{home}' has been updated!",
    val homeNameTooLong: String = "Home name is too long! Maximum 32 characters.",
    val homeNameInvalid: String = "Home name can only contain letters, numbers, underscores and hyphens.",
    val homeLocation: String = "Location: {x}, {y}, {z}",
    val maxHomesReached: String = "You have reached the maximum number of homes ({max})!",
    
    // Home (teleport) messages
    val teleportedToHome: String = "Teleported to home '{home}'!",
    val homeNotFound: String = "Home '{home}' not found!",
    val homeInDifferentWorld: String = "Cannot teleport to a home in a different world!",
    val homeWorldInfo: String = "Home '{home}' is in world '{world}'.",
    
    // DelHome messages
    val homeDeleted: String = "Home '{home}' has been deleted!",
    
    // Homes list messages
    val noHomes: String = "You don't have any homes set.",
    val homesHeader: String = "=== Your Homes ({count}) ===",
    val homeEntry: String = "{home}: {x}, {y}, {z} ({world})",
    
    // Help messages
    val useHomesCommand: String = "Use /homes to see your homes list.",
    val useHomeCommand: String = "Use /home <name> to teleport.",
    val useSetHomeCommand: String = "Use /sethome <name> to create one.",
    val useDelHomeCommand: String = "Use /delhome <name> to delete a home."
)
