package org.generators;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.utils.Cuboid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SchematicGenerator {
    private final String schematicName;
    @Getter
    private BlockVector blockVector;
    private final String worldName;
    private Clipboard schema;


    public SchematicGenerator(String schematicName, String worldName) {
        this.schematicName = schematicName;
        this.worldName = worldName;
    }

    public EditSession generateSchematic() throws IOException {
        int min = 2000;
        int max = 6000;
        int x = randomNumber(min, max);
        int z = randomNumber(min, max);

        WorldData worldData = new BukkitWorld(Bukkit.getWorld(worldName)).getWorldData();
        schema = getClipboardFromSchematicFile();
        blockVector = new BlockVector(x, getYPosition(x, z), z);
        Bukkit.getServer().broadcastMessage("POSIÇÃO DO SCHEMATIC " + "X :" + x + " Z : " + z);

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(Bukkit.getWorld(worldName)), -1);
        if (schema != null) {
            try {
                Operations.complete(createSchema(schema, worldData, session, blockVector));
                session.flushQueue();
                Bukkit.getServer().broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Templo gerado em X: " + blockVector.getBlockX() + " Y: " + blockVector.getBlockY() + " Z: " + blockVector.getBlockZ());
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }

        return session;
    }

    private static int randomNumber(int from, int to) {
        return (int) (Math.random() * (to - from)) + from;
    }

    public Clipboard getClipboardFromSchematicFile() throws IOException {
        File schematicFile = new File("plugins/WorldEdit/schematics/" + schematicName);
        WorldData worldData = new BukkitWorld(Bukkit.getWorld(worldName)).getWorldData();
        ClipboardReader reader = ClipboardFormat.findByFile(schematicFile).getReader(new FileInputStream(schematicFile));
        return reader.read(worldData);
    }

    public Cuboid getCuboidFromSchematicInitSpawn(Clipboard schema, BlockVector initSpawn) {
        Vector min = schema.getRegion().getMinimumPoint();
        Vector origin = schema.getOrigin();
        Vector max = schema.getRegion().getMaximumPoint();

        Cuboid cuboid = new Cuboid(
                new Location(Bukkit.getWorld(worldName), min.getX() - origin.getX() + initSpawn.getX(), min.getY() - origin.getY() + initSpawn.getY(), min.getZ() - origin.getZ() + initSpawn.getZ()),
                new Location(Bukkit.getWorld(worldName), max.getX() - origin.getX() + initSpawn.getX(), max.getY() - origin.getY() + initSpawn.getY(), max.getZ() - origin.getZ() + initSpawn.getZ()
                ));
        return cuboid;
    }


    private Operation createSchema(Clipboard schema, WorldData worldData, EditSession session, BlockVector blockVector) {
        ClipboardHolder toCreate = new ClipboardHolder(schema, worldData);
        return toCreate
                .createPaste(session, worldData)
                .to(blockVector)
                .ignoreAirBlocks(true)
                .build();
    }

    private int getYPosition(int x, int z) {
        int iterator = 255;
        while (Bukkit.getWorld(worldName).getBlockAt(x, iterator, z).isEmpty()) {
            iterator--;
        }
        System.out.println("ALTURA DO SCHEMA MANO " + schema.getRegion().getHeight());
        int altura = (schema.getRegion().getHeight() + 1);

        return iterator + altura;
    }

}