package dev.elkirstudio.justhome

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import dev.helight.kotale.uuid

/**
 * Command to list all homes.
 * Usage: /homes
 */
class HomesCommand(
    private val plugin: JustHomePlugin
) : AbstractPlayerCommand("homes", "List all your saved homes") {

    override fun execute(
        context: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        playerRef: PlayerRef,
        world: World
    ) {
        val lang = plugin.langManager
        
        // Get player UUID using the store extension
        @Suppress("UNCHECKED_CAST")
        val entityStore = store as Store<EntityStore>
        @Suppress("UNCHECKED_CAST")
        val entityRef = ref as Ref<EntityStore>
        val playerUuid = entityStore.uuid(entityRef)
        
        val homes = plugin.homeManager.getHomes(playerUuid)
        
        if (homes.isEmpty()) {
            context.sendMessage(lang.format(lang.lang.noHomes))
            context.sendMessage(lang.format(lang.lang.useSetHomeCommand))
            return
        }
        
        context.sendMessage(lang.format(lang.lang.homesHeader, "count" to homes.size))
        homes.forEach { (name, location) ->
            context.sendMessage(lang.format(lang.lang.homeEntry,
                "home" to name,
                "x" to location.x.toInt(),
                "y" to location.y.toInt(),
                "z" to location.z.toInt(),
                "world" to location.worldName
            ))
        }
        context.sendMessage(lang.format(lang.lang.useHomeCommand))
        context.sendMessage(lang.format(lang.lang.useDelHomeCommand))
    }
}
