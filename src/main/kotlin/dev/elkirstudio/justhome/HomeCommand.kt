package dev.elkirstudio.justhome

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import dev.helight.kotale.uuid

/**
 * Command to teleport to a home location.
 * Usage: /home <name>
 * Example: /home base
 */
class HomeCommand(
    private val plugin: JustHomePlugin
) : AbstractPlayerCommand("home", "home <name>") {

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
        
        // Get player UUID using the store extension
        @Suppress("UNCHECKED_CAST")
        val entityStore = store as Store<EntityStore>
        @Suppress("UNCHECKED_CAST")
        val entityRef = ref as Ref<EntityStore>
        val playerUuid = entityStore.uuid(entityRef)
        
        // Get home location
        val homeLocation = plugin.homeManager.getHome(playerUuid, homeName)
        
        if (homeLocation == null) {
            context.sendMessage(lang.format(lang.lang.homeNotFound, "home" to homeName))
            context.sendMessage(lang.format(lang.lang.useHomesCommand))
            return
        }
        
        // Check if the home is in the same world
        if (homeLocation.worldName != world.name) {
            context.sendMessage(lang.format(lang.lang.homeInDifferentWorld))
            context.sendMessage(lang.format(lang.lang.homeWorldInfo, "home" to homeName, "world" to homeLocation.worldName))
            return
        }
        
        // Create teleport position and rotation
        val position = Vector3d(homeLocation.x, homeLocation.y, homeLocation.z)
        val rotation = Vector3f(homeLocation.pitch, homeLocation.yaw, 0f)
        
        // Create the teleport component and add it to the player entity
        val teleport = Teleport(position, rotation)
        store.putComponent(ref, Teleport.getComponentType(), teleport)
        
        context.sendMessage(lang.format(lang.lang.teleportedToHome, "home" to homeName))
        context.sendMessage(lang.format(lang.lang.homeLocation,
            "x" to homeLocation.x.toInt(),
            "y" to homeLocation.y.toInt(),
            "z" to homeLocation.z.toInt()
        ))
    }
}
