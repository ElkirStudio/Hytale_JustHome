package dev.elkirstudio.justhome

import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import dev.helight.kotale.KotlinPlugin
import java.io.File

/**
 * JustHome Plugin
 * A home management plugin for Hytale.
 * 
 * Made by razday - Elkir Studio
 * 
 * Commands:
 * - /sethome <name> - Set a home at your current location
 * - /home <name> - Teleport to a saved home
 * - /homes - List all your homes
 * - /delhome <name> - Delete a home
 */
class JustHomePlugin(init: JavaPluginInit) : KotlinPlugin(init) {
    
    lateinit var homeManager: HomeManager
        private set
    
    lateinit var configManager: ConfigManager
        private set
    
    lateinit var langManager: LangManager
        private set
    
    override fun setup() {
        super.setup()
        
        // Initialize config folder
        val configFolder = File("mods/JustHome")
        
        // Initialize config manager
        configManager = ConfigManager(configFolder)
        
        // Initialize language manager
        langManager = LangManager(configFolder, configManager)
        
        // Initialize home manager
        homeManager = HomeManager(configFolder)
        
        // Register commands
        this.commandRegistry.registerCommand(SetHomeCommand(this))
        this.commandRegistry.registerCommand(HomeCommand(this))
        this.commandRegistry.registerCommand(HomesCommand(this))
        this.commandRegistry.registerCommand(DelHomeCommand(this))
        
        println("[JustHome] Plugin loaded successfully!")
        println("[JustHome] Language: ${configManager.config.language}, Max homes: ${configManager.config.maxHomes}")
        println("[JustHome] Commands: /sethome, /home, /homes, /delhome")
    }
    
    override fun shutdown() {
        // Save homes before shutdown
        if (::homeManager.isInitialized) {
            homeManager.save()
            println("[JustHome] Homes saved.")
        }
        
        super.shutdown()
        println("[JustHome] Plugin unloaded.")
    }
}
