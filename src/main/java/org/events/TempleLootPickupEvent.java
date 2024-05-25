package org.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.generators.TempleGenerator;

public class TempleLootPickupEvent implements Listener {

    private TempleGenerator templeGenerator;

    public TempleLootPickupEvent(TempleGenerator templeGenerator) {
        this.templeGenerator = templeGenerator;
    }

    @EventHandler
    public void onLootPickup(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        Item spawnedItem = templeGenerator.getSpawnedItem();
        Entity hologram = templeGenerator.getHologram();
        if (spawnedItem == null) return;
        if (spawnedItem.getLocation().equals(item.getLocation())) {
            hologram.remove();
            templeGenerator.undoTemple();
        }
    }
}
