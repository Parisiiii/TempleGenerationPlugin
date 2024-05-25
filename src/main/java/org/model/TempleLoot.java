package org.model;

import org.TempleGenerationPlugin;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TempleLoot {
    private TempleGenerationPlugin plugin;

    public TempleLoot(TempleGenerationPlugin plugin) {
        this.plugin = plugin;
    }

    public List<ItemStack> getItems() {
        return plugin.getTempleConfiguration().getList("items") == null ? Collections.emptyList() :  plugin.getTempleConfiguration().getList("items").stream().map(i -> ItemStack.deserialize((Map<String, Object>) i)).collect(Collectors.toList());
    }
}
