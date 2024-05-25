package org.commands;

import org.TempleGenerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.events.TempleLootPickupEvent;
import org.generators.TempleGenerator;

import java.io.IOException;

public class GenerateTempleCommand implements CommandExecutor {
    private final TempleGenerationPlugin plugin;

    public GenerateTempleCommand(TempleGenerationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission(player.getName())){
                if(strings.length == 2){
                    String schematicPath = strings[0];
                    int timeToDropLoot = Integer.parseInt(strings[1]);
                    if (!schematicPath.contains("schematic")) return false;
                    TempleGenerator templeGenerator = new TempleGenerator(schematicPath, plugin, player.getWorld().getName());
                    try {
                        templeGenerator.generateTemple(timeToDropLoot);
                        Bukkit.getPluginManager().registerEvents(new TempleLootPickupEvent(templeGenerator), plugin);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Erro! Utilize /gerartemplo <nome do schematic> <tempo em segundos para dropar o item>");
                }
            }else{
                player.sendMessage(ChatColor.DARK_RED + "Você não possui permissão.");
            }
        }
        return false;
    }
}
