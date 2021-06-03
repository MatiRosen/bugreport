package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class PriorityMenu {

    @Inject
    private BugReportSecondMenu bugReportSecondMenu;
    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private ReportPlugin plugin;

    public Inventory create(BugReport bugReport){
        FileConfiguration config = plugin.getConfig();

        return GUIBuilder.builder(Utils.format(config.getString("priority-menu.title")), 1)
                .addItem(getItemClickable(bugReport, 1))
                .addItem(getItemClickable(bugReport, 2))
                .addItem(getItemClickable(bugReport, 3))
                .addItem(getItemClickable(bugReport, 4))
                .addItem(getItemClickable(bugReport, 5))
                .build();
    }

    private ItemClickable getItemClickable(BugReport bugReport, int priority){
        FileConfiguration config = plugin.getConfig();
        String key = "priority-menu.items." + priority + ".";

        Material material = Material.valueOf(config.getString(key + "material"));
        String name = Utils.format(config.getString(key + "name"));
        List<String> lore = Arrays.asList(Utils.format(config.getStringList(key + "lore")));

        return ItemClickable.builder(priority - 1)
                .setItemStack(ItemBuilder.newBuilder(material)
                        .setName(name)
                        .setLore(lore)
                        .build())
                .setAction(event -> {
                    if (!(event.getWhoClicked() instanceof Player)) return false;

                    bugReport.setPriority(priority);
                    bugReport.setExist(true);
                    bugReportManager.saveReport(bugReport);
                    event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                    event.setCancelled(true);
                    return true;
                })
                .build();
    }
}