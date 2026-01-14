package dev.elkirstudio.justhome

import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

/**
 * Manages home data persistence and operations.
 */
class HomeManager(private val dataFolder: File) {
    
    private val homesFile = File(dataFolder, "homes.json")
    private var homesData = HomesData()
    
    private val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true
    }
    
    init {
        load()
    }
    
    /**
     * Load homes from file.
     */
    fun load() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
        
        if (homesFile.exists()) {
            try {
                val content = homesFile.readText()
                homesData = json.decodeFromString<HomesData>(content)
                println("[JustHome] Loaded ${homesData.players.size} player home records.")
            } catch (e: Exception) {
                println("[JustHome] Error loading homes: ${e.message}")
                homesData = HomesData()
            }
        } else {
            homesData = HomesData()
            save()
        }
    }
    
    /**
     * Save homes to file.
     */
    fun save() {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs()
            }
            val content = json.encodeToString(HomesData.serializer(), homesData)
            homesFile.writeText(content)
        } catch (e: Exception) {
            println("[JustHome] Error saving homes: ${e.message}")
        }
    }
    
    /**
     * Set a home for a player.
     */
    fun setHome(playerUuid: UUID, name: String, location: HomeLocation) {
        val uuidString = playerUuid.toString()
        val playerHomes = homesData.players.getOrPut(uuidString) { PlayerHomes() }
        playerHomes.homes[name.lowercase()] = location
        save()
    }
    
    /**
     * Get a home for a player.
     */
    fun getHome(playerUuid: UUID, name: String): HomeLocation? {
        val uuidString = playerUuid.toString()
        return homesData.players[uuidString]?.homes?.get(name.lowercase())
    }
    
    /**
     * Get all homes for a player.
     */
    fun getHomes(playerUuid: UUID): Map<String, HomeLocation> {
        val uuidString = playerUuid.toString()
        return homesData.players[uuidString]?.homes ?: emptyMap()
    }
    
    /**
     * Delete a home for a player.
     */
    fun deleteHome(playerUuid: UUID, name: String): Boolean {
        val uuidString = playerUuid.toString()
        val playerHomes = homesData.players[uuidString] ?: return false
        val removed = playerHomes.homes.remove(name.lowercase()) != null
        if (removed) {
            save()
        }
        return removed
    }
    
    /**
     * Check if a player has a specific home.
     */
    fun hasHome(playerUuid: UUID, name: String): Boolean {
        val uuidString = playerUuid.toString()
        return homesData.players[uuidString]?.homes?.containsKey(name.lowercase()) == true
    }
}
