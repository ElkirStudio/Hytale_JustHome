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
 * Command to delete a home location.
 * Usage: /delhome <name>
 * Example: /delhome base
 */
class DelHomeCommand(
    private val plugin: JustHomePlugin
) : AbstractPlayerCommand("delhome", "delhome <name>") {

    private lateinit var homeNameArg: RequiredArg<String>
    
    init {
        homeNameArg = withRequiredArg("name", "The name of the home to delete", ArgTypes.STRING)
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
        
        // Delete home
        val deleted = plugin.homeManager.deleteHome(playerUuid, homeName)
        
        if (deleted) {
            context.sendMessage(lang.format(lang.lang.homeDeleted, "home" to homeName))
        } else {
            context.sendMessage(lang.format(lang.lang.homeNotFound, "home" to homeName))
            context.sendMessage(lang.format(lang.lang.useHomesCommand))
        }
    }
}
