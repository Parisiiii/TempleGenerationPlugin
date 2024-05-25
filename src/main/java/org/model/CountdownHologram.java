package org.model;

import org.TempleGenerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.concurrent.TimeUnit;

public class CountdownHologram {
    private String worldName;
    private TempleGenerationPlugin plugin;

    public CountdownHologram(String worldName, TempleGenerationPlugin plugin) {
        this.worldName = worldName;
        this.plugin = plugin;
    }

    public Entity showCountdown(Location locationToSpawn, double heightOfSchematic, long startTime) {
        ArmorStand armorStand = (ArmorStand) Bukkit.getWorld(worldName).spawnEntity(locationToSpawn.subtract(new Location(Bukkit.getWorld(worldName), 0,3, 0)), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCanPickupItems(false);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            long time = startTime;

            @Override
            public void run() {
                if (this.time == 0) {
                    armorStand.setCustomName(ChatColor.BOLD + "O loot foi dropado!");
                    Bukkit.getScheduler().cancelAllTasks();
                } else {
                    armorStand.setCustomName(ChatColor.GREEN + "Faltam " + calculateTime(time) + " para o loot ser liberado");
                    this.time--;
                }
            }
        }, 0L, 20L);

        return armorStand;
    }


    public static String calculateTime(long seconds) {
        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                (TimeUnit.SECONDS.toHours(seconds) * 60);

        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        String minutes = minute < 10 ? "0" + minute : minute + "";
        String formatedSeconds = second < 10 ? "0" + second : second + "";
        return minutes + ":" + formatedSeconds;
    }
}
