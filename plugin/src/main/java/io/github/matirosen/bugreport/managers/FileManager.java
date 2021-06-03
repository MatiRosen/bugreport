package io.github.matirosen.bugreport.managers;

import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.io.*;
import java.util.HashMap;

public class FileManager {

    @Inject
    private JavaPlugin plugin;

    private final HashMap<String, FileConfiguration> configurationMap = new HashMap<>();
    private File reportsFolder;

    private static final String LANG_FORMAT = "language-%s.yml";


    public void loadAllFileConfigurations(){
        reportsFolder = new File(plugin.getDataFolder(), "reports");
        reportsFolder.mkdirs();

        configurationMap.clear();

        saveDefaultResources("config.yml");
        saveDefaultResources("language-en.yml");
        saveDefaultResources("language-es.yml");

        FileConfiguration config = plugin.getConfig();
        configurationMap.put("config", config);

        String lang = String.format(LANG_FORMAT, config.getString("language"));
        FileConfiguration langFileConfiguration = loadFileConfiguration(lang);

        if (langFileConfiguration == null) {
            Bukkit.getConsoleSender().sendMessage(Utils.format("&c[Bug-Report] Language file not found. Using 'language-en.yml'"));
            langFileConfiguration = loadFileConfiguration(String.format(LANG_FORMAT, "en"));

            if (langFileConfiguration == null) {
                Bukkit.getConsoleSender().sendMessage(Utils.format("&c[Bug-Report] language-en.yml file not found. Disabling..."));
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }
        }
        configurationMap.put("language", langFileConfiguration);
    }

    private void saveDefaultResources(String name){
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()){
            plugin.saveResource(name, true);
        }
    }

    public FileConfiguration get(String name){
        return configurationMap.get(name);
    }

    public FileConfiguration loadFileConfiguration(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            return null;
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public File getReportsFolder(){
        return reportsFolder;
    }
}