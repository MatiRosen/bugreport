package io.github.matirosen.guis;

import io.github.matirosen.ReportPlugin;
import io.github.matirosen.managers.BugReportManager;
import io.github.matirosen.reports.BugReport;
import io.github.matirosen.utils.ConfigHandler;
import io.github.matirosen.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BugReportMainMenu implements InventoryHolder {

    @Inject
    private BugReportManager bugReportManager;


    public Inventory create(int actualPage){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        ConfigHandler configHandler = ReportPlugin.getConfigHandler();
        Map<String, Object> inventoryMap = configHandler.getInventoryMap();

        Inventory inventory = Bukkit.createInventory(this, 54, MessageHandler.format((String)
                inventoryMap.get("mainInventoryTitle")).replace("%actual-page%", String.valueOf(actualPage)));

        List<String> arrowLore = new ArrayList<>();
        arrowLore.add("");
        List<BugReport> bugReportList = bugReportManager.getBugReportList();

        int maxSize = Math.min(bugReportList.size(), 44 * actualPage + actualPage);
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

        if (maxSize != bugReportList.size()){
            arrowLore.set(0, MessageHandler.format(messageHandler.getMessage("go-to-page") + (actualPage + 1)));

            ItemStack next = new CustomGuiItem(Material.valueOf((String) inventoryMap.get("nextMaterial")),
                    actualPage + 1)
                    .displayName((String) inventoryMap.get("nextName"))
                    .lore(arrowLore)
                    .glow((boolean) inventoryMap.get("nextGlow"))
                    .create();

            inventory.setItem(50, next);
        }

        for (BugReport bugReport : bugReportList.subList(minSize, maxSize)){
            List<String> lore = new ArrayList<>();
            lore.add(MessageHandler.format("&7" + bugReport.getId()));
            lore.add("");
            lore.add("Priority: " + bugReport.getPriority());
            lore.add(bugReport.isSolved() ? messageHandler.getMessage("solved") : messageHandler.getMessage("unsolved"));
            lore.add("");
            lore.addAll(bugReport.getLabels());

            ItemStack itemStack = new CustomGuiItem(Material.valueOf((String) inventoryMap.get("reportMaterial")), 1)
                    .displayName(((String) inventoryMap.get("reportName")).replace("%report_id%",
                            String.valueOf(bugReport.getId())))
                    .lore(lore)
                    .glow((boolean) inventoryMap.get("reportGlow"))
                    .create();
            inventory.addItem(itemStack);
        }

        ItemStack filter = new CustomGuiItem(Material.WATCH, 1)
                .displayName("Set filter")
                .create();

        inventory.setItem(49, filter);

        return inventory;
    }

    public Inventory getInventory() {
        return null;
    }
}

