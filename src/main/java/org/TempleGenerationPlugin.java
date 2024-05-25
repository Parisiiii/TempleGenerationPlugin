package org;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.commands.GenerateTempleCommand;
import org.commands.TemplesLootCommand;
import org.events.SaveTempleLootEvent;
import org.events.TempleLootPickupEvent;
import org.generators.TempleGenerator;

import java.io.File;
import java.io.IOException;

@Getter
public class TempleGenerationPlugin extends JavaPlugin {

    private FileConfiguration templeConfiguration;

    @Override
    public void onEnable() {
        templeConfiguration = YamlConfiguration.loadConfiguration(getTempleConfigurationFile());

        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new SaveTempleLootEvent(getTempleConfigurationFile(), this), this);
        getCommand("gerartemplo").setExecutor(new GenerateTempleCommand(this));
        getCommand("temploloot").setExecutor(new TemplesLootCommand(this));

        autoSpawnTemple();
    }

    @Override
    public void saveDefaultConfig() {
        saveResource("config.yml", true);
    }

    private File getTempleConfigurationFile() {
        File file =new File(getDataFolder(), "templeConfiguration.yml");
        if(!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Não é possivel carregar a configuração do templo. Error");
            }
        }
        return file;
    }


    private void autoSpawnTemple(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            String schematicPath = templeConfiguration.getString("templeSchematic");;
            int timeToSpawnLoot = 300; // in seconds
            String worldName = templeConfiguration.getString("worldName");
            TempleGenerator templeGenerator = new TempleGenerator(schematicPath, this, worldName);
            try {
                templeGenerator.generateTemple(timeToSpawnLoot);
                Bukkit.getPluginManager().registerEvents(new TempleLootPickupEvent(templeGenerator), this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 20L, 20L * 3600 );
    }
}