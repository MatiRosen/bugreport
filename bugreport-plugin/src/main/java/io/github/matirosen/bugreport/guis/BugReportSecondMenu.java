package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BugReportSecondMenu implements InventoryHolder {


    public Inventory build(BugReport bugReport){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        Inventory inventory = Bukkit.createInventory(this, 9, messageHandler.getMessage("report-title")
                .replace("%report_id%", String.valueOf(bugReport.getId())));


        List<String> reportLore = new ArrayList<>();
        reportLore.add(messageHandler.getMessage("report-details"));

        ItemStack reportItem = new CustomGuiItem(Material.PAPER, 1)
                .displayName(messageHandler.getMessage("report-material-name").replace("%report_id%",
                        String.valueOf(bugReport.getId())))
                .lore(reportLore)
                .create();

        inventory.setItem(0, reportItem);


        ItemStack labelItem = new CustomGuiItem(Material.WRITTEN_BOOK, 1)
                .displayName(messageHandler.getMessage("labels"))
                .lore(bugReport.getLabels())
                .create();

        inventory.setItem(1, labelItem);


        int priority = bugReport.getPriority();
        if (priority < 1) priority = 1;

        ItemStack priorityItem = new CustomGuiItem(Material.DIAMOND, priority)
                .displayName(messageHandler.getMessage("priority").replace("%bug_priority%", String.valueOf(priority)))
                .create();

        inventory.setItem(2, priorityItem);


        List<String> solvedLore = new ArrayList<>();
        String solvedString = bugReport.isSolved() ? messageHandler.getMessage("set-unsolved")
                : messageHandler.getMessage("set-solved");
        solvedLore.add(solvedString);

        String solved = bugReport.isSolved() ? messageHandler.getMessage("bug-solved")
                : messageHandler.getMessage("bug-unsolved");

        Material solvedMaterial = bugReport.isSolved() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK;
        ItemStack solvedItem = new CustomGuiItem(solvedMaterial, 1)
                .displayName(solved)
                .lore(solvedLore)
                .create();

        inventory.setItem(3, solvedItem);

        ItemStack bugId = new CustomGuiItem(Material.PAPER, 1)
                .displayName(MessageHandler.format("&7" + bugReport.getId()))
                .create();

        inventory.setItem(8, bugId);

        return inventory;
    }


    @Override
    public Inventory getInventory() {
        return null;
    }
}
