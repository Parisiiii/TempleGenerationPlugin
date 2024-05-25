package org.events;

import org.TempleGenerationPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.model.TempleLootHolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SaveTempleLootEvent implements Listener {

    private File templeConfigurationYml;
    private TempleGenerationPlugin plugin;

    public SaveTempleLootEvent(File templeConfigurationYml, TempleGenerationPlugin plugin) {
        this.templeConfigurationYml = templeConfigurationYml;
        this.plugin = plugin;
    }

    @EventHandler
    public void onCloseTempleLootInventory(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof TempleLootHolder) {
            setItemsInYmlFromInventory(e.getInventory());
        }
    }

    private void setItemsInYmlFromInventory(Inventory inventory) {
        List<Map<String, Object>> itemsToSet = new ArrayList<>();

        Arrays.stream(inventory.getContents()).forEach(item -> {
            if (item != null) {
                itemsToSet.add(item.serialize());
            }
        });

        plugin.getTempleConfiguration().set("items", itemsToSet);
        try {
            plugin.getTempleConfiguration().save(templeConfigurationYml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
