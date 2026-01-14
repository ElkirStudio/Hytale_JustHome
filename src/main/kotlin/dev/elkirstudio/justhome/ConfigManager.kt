package dev.elkirstudio.justhome

import kotlinx.serialization.json.Json
import java.io.File

/**
 * Manages plugin configuration loading and saving.
 */
class ConfigManager(private val configDir: File) {
    
    private val configFile = File(configDir, "config.json")
    private val json = Json { 
        prettyPrint = true 
        encodeDefaults = true
    }
    
    var config: Config = Config()
        private set
    
    init {
        load()
    }
    
    /**
     * Load configuration from file, or create default if not exists.
     */
    fun load() {
        if (!configDir.exists()) {
            configDir.mkdirs()
        }
        
        if (configFile.exists()) {
            try {
                val content = configFile.readText()
                config = json.decodeFromString<Config>(content)
            } catch (e: Exception) {
                println("[JustHome] Error loading config, using defaults: ${e.message}")
                config = Config()
                save()
            }
        } else {
            config = Config()
            save()
        }
    }
    
    /**
     * Save current configuration to file.
     */
    fun save() {
        if (!configDir.exists()) {
            configDir.mkdirs()
        }
        
        try {
            val content = json.encodeToString(Config.serializer(), config)
            configFile.writeText(content)
        } catch (e: Exception) {
            println("[JustHome] Error saving config: ${e.message}")
        }
    }
}
