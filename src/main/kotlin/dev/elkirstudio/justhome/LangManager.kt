package dev.elkirstudio.justhome

import com.hypixel.hytale.server.core.Message
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Manages language files and message formatting.
 */
class LangManager(private val configDir: File, private val configManager: ConfigManager) {
    
    private val json = Json { 
        prettyPrint = true 
        encodeDefaults = true
        ignoreUnknownKeys = true
    }
    
    var lang: Lang = Lang()
        private set
    
    // Default languages
    private val defaultEnglish = Lang()
    private val defaultFrench = Lang(
        homeSet = "Le home '{home}' a été créé !",
        homeUpdated = "Le home '{home}' a été mis à jour !",
        homeNameTooLong = "Le nom du home est trop long ! Maximum 32 caractères.",
        homeNameInvalid = "Le nom du home ne peut contenir que des lettres, chiffres, tirets et underscores.",
        homeLocation = "Position: {x}, {y}, {z}",
        maxHomesReached = "Vous avez atteint le nombre maximum de homes ({max}) !",
        
        teleportedToHome = "Téléporté au home '{home}' !",
        homeNotFound = "Le home '{home}' n'existe pas !",
        homeInDifferentWorld = "Impossible de se téléporter vers un home dans un autre monde !",
        homeWorldInfo = "Le home '{home}' est dans le monde '{world}'.",
        
        homeDeleted = "Le home '{home}' a été supprimé !",
        
        noHomes = "Vous n'avez aucun home défini.",
        homesHeader = "=== Vos Homes ({count}) ===",
        homeEntry = "{home}: {x}, {y}, {z} ({world})",
        
        useHomesCommand = "Utilisez /homes pour voir la liste de vos homes.",
        useHomeCommand = "Utilisez /home <nom> pour vous téléporter.",
        useSetHomeCommand = "Utilisez /sethome <nom> pour en créer un.",
        useDelHomeCommand = "Utilisez /delhome <nom> pour supprimer un home."
    )
    
    init {
        createDefaultLanguageFiles()
        load()
    }
    
    /**
     * Create default language files if they don't exist.
     */
    private fun createDefaultLanguageFiles() {
        if (!configDir.exists()) {
            configDir.mkdirs()
        }
        
        val enFile = File(configDir, "lang_en.json")
        val frFile = File(configDir, "lang_fr.json")
        
        if (!enFile.exists()) {
            try {
                val content = json.encodeToString(Lang.serializer(), defaultEnglish)
                enFile.writeText(content)
                println("[JustHome] Created default English language file.")
            } catch (e: Exception) {
                println("[JustHome] Error creating English language file: ${e.message}")
            }
        }
        
        if (!frFile.exists()) {
            try {
                val content = json.encodeToString(Lang.serializer(), defaultFrench)
                frFile.writeText(content)
                println("[JustHome] Created default French language file.")
            } catch (e: Exception) {
                println("[JustHome] Error creating French language file: ${e.message}")
            }
        }
    }
    
    /**
     * Load language file based on config setting.
     */
    fun load() {
        val langCode = configManager.config.language
        val langFile = File(configDir, "lang_$langCode.json")
        
        if (langFile.exists()) {
            try {
                val content = langFile.readText()
                lang = json.decodeFromString<Lang>(content)
                println("[JustHome] Loaded language: $langCode")
            } catch (e: Exception) {
                println("[JustHome] Error loading language file, using English: ${e.message}")
                lang = defaultEnglish
            }
        } else {
            println("[JustHome] Language file not found for '$langCode', using English.")
            lang = defaultEnglish
        }
    }
    
    /**
     * Format a message with placeholders.
     * Supports: {home}, {count}, {x}, {y}, {z}, {world}, {max}
     */
    fun format(message: String, vararg replacements: Pair<String, Any>): Message {
        var formatted = message
        
        // Replace placeholders
        for ((key, value) in replacements) {
            formatted = formatted.replace("{$key}", value.toString())
        }
        
        return Message.raw(formatted)
    }
}
