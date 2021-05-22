package io.github.matirosen.bugreport.utils;

import io.github.matirosen.bugreport.managers.FileManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigHandler {

    public static int totalReports;

    private final FileManager fileManager;
    private final Map<String, Object> configMap = new HashMap<>();
    private final Map<String, Object> inventoryMap = new HashMap<>();

    public ConfigHandler(FileManager fileManager){
        this.fileManager = fileManager;
    }

    public void initializeConfig(){
        ConfigurationSection configSection = fileManager.get("config");

        configMap.put("inactiveSeconds", configSection.getInt("time-out"));
        configMap.put("labels", configSection.getStringList("labels"));
        configMap.put("usePermission", configSection.getString("use-permission"));

        totalReports = fileManager.get("info").getInt("report-number");
        if (totalReports == 0) totalReports++;


        ConfigurationSection inventorySection = configSection.getConfigurationSection("main-inventory");
        inventoryMap.put("mainInventoryTitle", MessageHandler.format(inventorySection.getString("title")));


        ConfigurationSection itemsReports = inventorySection.getConfigurationSection("items.reports");
        inventoryMap.put("reportMaterial", MessageHandler.format(itemsReports.getString("material")));
        inventoryMap.put("reportName", MessageHandler.format(itemsReports.getString("name")));
        List<String> reportLore = new ArrayList<>();
        for (String lore : itemsReports.getStringList("lore")) reportLore.add(MessageHandler.format(lore));
        inventoryMap.put("reportLore", reportLore);
        inventoryMap.put("reportGlow", itemsReports.getBoolean("glow"));


        ConfigurationSection itemNext = inventorySection.getConfigurationSection("items.next-page");
        inventoryMap.put("nextMaterial", MessageHandler.format(itemNext.getString("material")));
        inventoryMap.put("nextName", MessageHandler.format(itemNext.getString("name")));
        List<String> nextLore = new ArrayList<>();
        for (String lore : itemNext.getStringList("lore")) nextLore.add(MessageHandler.format(lore));
        inventoryMap.put("nextLore", nextLore);
        inventoryMap.put("nextGlow", itemNext.getBoolean("glow"));


        ConfigurationSection itemPrevious = inventorySection.getConfigurationSection("items.previous-page");
        inventoryMap.put("previousMaterial", MessageHandler.format(itemPrevious.getString("material")));
        inventoryMap.put("previousName", MessageHandler.format(itemPrevious.getString("name")));
        List<String> previousLore = new ArrayList<>();
        for (String lore : itemPrevious.getStringList("lore")) previousLore.add(MessageHandler.format(lore));
        inventoryMap.put("previousLore", previousLore);
        inventoryMap.put("previousGlow", itemPrevious.getBoolean("glow"));
    }

    public Map<String, Object> getConfigMap(){
        return configMap;
    }

    public Map<String, Object> getInventoryMap(){
        return inventoryMap;
    }
}