package org.commands;

import org.TempleGenerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.model.TempleLoot;
import org.model.TempleLootHolder;

import java.util.List;

public class TemplesLootCommand implements CommandExecutor {
    private TempleGenerationPlugin plugin;

    public TemplesLootCommand(TempleGenerationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory inventory = Bukkit.getServer().createInventory(new TempleLootHolder(), InventoryType.CHEST, "Loot do templo");
            List<ItemStack> items = new TempleLoot(plugin).getItems();
            items.forEach(inventory::addItem);
            player.openInventory(inventory);
        }
        return false;
    }
}
