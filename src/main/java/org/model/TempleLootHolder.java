package org.model;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class TempleLootHolder implements InventoryHolder {
    private Inventory inventory;
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
