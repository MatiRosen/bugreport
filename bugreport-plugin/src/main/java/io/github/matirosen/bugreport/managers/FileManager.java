package io.github.matirosen.bugreport.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class FileManager {

    @Inject
    private JavaPlugin plugin;

    private final HashMap<String, FileConfiguration> configurationMap = new HashMap<>();
    private File reportsFolder;


    public void loadAllFileConfigurations(){
        reportsFolder = new File(plugin.getDataFolder(), "reports");
        reportsFolder.mkdirs();


        configurationMap.clear();
        configurationMap.put("config", loadFileConfiguration("config.yml", plugin.getDataFolder()));
        configurationMap.put("language", loadFileConfiguration("language.yml", plugin.getDataFolder()));
        configurationMap.put("info", loadFileConfiguration("info.yml", reportsFolder));
    }

    public FileConfiguration get(String name){
        return configurationMap.get(name);
    }

    public FileConfiguration loadFileConfiguration(String name, File folder){
        File file = new File(folder, name);
        if (!file.exists()){
            file.getParentFile().mkdirs();
            saveResource(name, true, folder);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public File getReportsFolder(){
        return reportsFolder;
    }

    private void saveResource(String resourcePath, boolean replace, File folder) {
        InputStream in = plugin.getResource(resourcePath);
        File outFile = new File(folder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf(47);
        File outDir = new File(folder, resourcePath.substring(0, Math.max(lastIndex, 0)));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        try {
            if (outFile.exists() && !replace) {
                plugin.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            } else {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while((len = Objects.requireNonNull(in).read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (IOException var10) {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
        }
    }
}