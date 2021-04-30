package io.github.matirosen.guis;

import io.github.matirosen.ReportPlugin;
import io.github.matirosen.reports.BugReport;
import io.github.matirosen.utils.ConfigHandler;
import io.github.matirosen.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LabelsMenu implements InventoryHolder {


    public Inventory create(int actualPage, BugReport bugReport){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        ConfigHandler configHandler = ReportPlugin.getConfigHandler();
        Map<String, Object> inventoryMap = configHandler.getInventoryMap();

        List<String> labels = (List<String>) configHandler.getConfigMap().get("labels");

        Inventory inventory = Bukkit.createInventory(this, 54, "labels");

        List<String> arrowLore = new ArrayList<>();
        arrowLore.add("");

        int maxSize = Math.min(labels.size(), 44 * actualPage + actualPage);
        int minSize = 44 * actualPage - 45 + actualPage;

        if (actualPage != 1) {
            arrowLore.set(0, MessageHandler.format(messageHandler.getMessage("go-to-page") + (actualPage - 1)));

            ItemStack previous = new CustomGuiItem(Material.valueOf((String) inventoryMap.get("previousMaterial")),
                    actualPage - 1)
                    .displayName((String) inventoryMap.get("previousName"))
                    .lore(arrowLore)
                    .glow((boolean) inventoryMap.get("previousGlow"))
                    .create();

            inventory.setItem(48, previous);
        }

        if (maxSize != labels.size()){
            arrowLore.set(0, MessageHandler.format(messageHandler.getMessage("go-to-page") + (actualPage + 1)));

            ItemStack next = new CustomGuiItem(Material.valueOf((String) inventoryMap.get("nextMaterial")),
                    actualPage + 1)
                    .displayName((String) inventoryMap.get("nextName"))
                    .lore(arrowLore)
                    .glow((boolean) inventoryMap.get("nextGlow"))
                    .create();

            inventory.setItem(50, next);
        }

        for (String label : labels.subList(minSize, maxSize)){
            List<String> lore = new ArrayList<>();
            lore.add("&7Click to add this label");

            ItemStack itemStack = new CustomGuiItem(Material.valueOf((String) inventoryMap.get("reportMaterial")), 1)
                    .displayName(label)
                    .lore(lore)
                    .glow((boolean) inventoryMap.get("reportGlow"))
                    .create();
            inventory.addItem(itemStack);
        }

        ItemStack bugId = new CustomGuiItem(Material.COMPASS, 1)
                .displayName(MessageHandler.format("&7" + bugReport.getId()))
                .create();

        inventory.setItem(49, bugId);

        return inventory;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
