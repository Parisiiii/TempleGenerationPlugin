package org.generators;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import lombok.Getter;
import lombok.Setter;
import org.TempleGenerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.model.CountdownHologram;
import org.model.TempleLoot;
import org.utils.Cuboid;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Setter
@Getter
public class TempleGenerator {
    private FileConfiguration fileConfiguration;
    private String schematicPath;
    private TempleGenerationPlugin plugin;
    private EditSession session;
    private String worldName;
    @Getter
    private Cuboid templeCuboid;
    @Getter
    private Item spawnedItem;
    @Getter
    private Entity hologram;
    private Clipboard schematic;

    public TempleGenerator(String schematicPath, TempleGenerationPlugin plugin, String worldName) {
        this.schematicPath = schematicPath;
        this.plugin = plugin;
        this.worldName = worldName;
    }

    public void generateTemple(int startTime) throws IOException {
        SchematicGenerator generator = new SchematicGenerator(schematicPath, worldName);

        session = generator.generateSchematic();
        templeCuboid = generator.getCuboidFromSchematicInitSpawn(generator.getClipboardFromSchematicFile(), generator.getBlockVector());
        schematic = generator.getClipboardFromSchematicFile();
        hologram = new CountdownHologram(worldName, plugin).showCountdown(templeCuboid.getCenter(), schematic.getRegion().getHeight(), startTime);

        List<ItemStack> items = new TempleLoot(plugin).getItems();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!items.isEmpty()) {
                spawnedItem = Bukkit.getWorld(worldName).dropItem(templeCuboid.getCenter(), items.get(new Random().nextInt(items.size())));
            }
        }, 20L * startTime);

    }

    public void undoTemple() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "O templo foi capturado por um player com sucesso! Em uma hora irá nascer outro");
            session.undo(session);
        });
    }

}
