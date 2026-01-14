package dev.elkirstudio.justhome

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import dev.helight.kotale.uuid

/**
 * Command to set a home location.
 * Usage: /sethome <name>
 * Example: /sethome base
 */
class SetHomeCommand(
    private val plugin: JustHomePlugin
) : AbstractPlayerCommand("sethome", "sethome <name>") {

    private lateinit var homeNameArg: RequiredArg<String>
    
    init {
        homeNameArg = withRequiredArg("name", "The name of the home", ArgTypes.STRING)
    }

    override fun execute(
        context: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World
    ) {
        val lang = plugin.langManager
        
        // Get home name from the argument
        val homeName: String = context.get(homeNameArg)
        
        // Validate home name
        if (homeName.length > 32) {
            context.sendMessage(lang.format(lang.lang.homeNameTooLong))
            return
        }
        
        if (!homeName.matches(Regex("^[a-zA-Z0-9_-]+$"))) {
            context.sendMessage(lang.format(lang.lang.homeNameInvalid))
            return
        }
        
        // Get player UUID using the store extension
        @Suppress("UNCHECKED_CAST")
        val entityStore = store as Store<EntityStore>
        @Suppress("UNCHECKED_CAST")
        val entityRef = ref as Ref<EntityStore>
        val playerUuid = entityStore.uuid(entityRef)
        
        // Check if home already exists
        val exists = plugin.homeManager.hasHome(playerUuid, homeName)
        
        // Check max homes limit (only if creating new home)
        if (!exists) {
            val currentHomes = plugin.homeManager.getHomes(playerUuid).size
            val maxHomes = plugin.configManager.config.maxHomes
            if (currentHomes >= maxHomes) {
                context.sendMessage(lang.format(lang.lang.maxHomesReached, "max" to maxHomes))
                return
            }
        }
        
        // Get player position and rotation
        val position = playerRef.transform.position
        val rotation = playerRef.transform.rotation
        
        // Create home location
        val homeLocation = HomeLocation(
            x = position.x,
            y = position.y,
            z = position.z,
            worldName = world.name,
            yaw = rotation.y,
            pitch = rotation.x
        )
        
        // Save home
        plugin.homeManager.setHome(playerUuid, homeName, homeLocation)
        
        if (exists) {
            context.sendMessage(lang.format(lang.lang.homeUpdated, "home" to homeName))
        } else {
            context.sendMessage(lang.format(lang.lang.homeSet, "home" to homeName))
        }
        
        context.sendMessage(lang.format(lang.lang.homeLocation, 
            "x" to position.x.toInt(),
            "y" to position.y.toInt(),
            "z" to position.z.toInt()
        ))
    }
}
