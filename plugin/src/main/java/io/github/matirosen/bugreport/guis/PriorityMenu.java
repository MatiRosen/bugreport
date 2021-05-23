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
                .addItem(ItemClickable.builder(0)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("priority-menu.items.1.material")))
                                .setName(Utils.format(config.getString("priority-menu.items.1.name")))
                                .setLore(Utils.format(config.getStringList("priority-menu.items.1.lore")))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;

                            bugReport.setPriority(1);
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);
                            event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(1)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("priority-menu.items.2.material")))
                                .setName(Utils.format(config.getString("priority-menu.items.2.name")))
                                .setLore(Utils.format(config.getStringList("priority-menu.items.2.lore")))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                            bugReport.setPriority(2);
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);
                            event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(2)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("priority-menu.items.3.material")))
                                .setName(Utils.format(config.getString("priority-menu.items.3.name")))
                                .setLore(Utils.format(config.getStringList("priority-menu.items.3.lore")))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;

                            bugReport.setPriority(3);
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);
                            event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(3)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("priority-menu.items.4.material")))
                                .setName(Utils.format(config.getString("priority-menu.items.4.name")))
                                .setLore(Utils.format(config.getStringList("priority-menu.items.4.lore")))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;

                            bugReport.setPriority(4);
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);
                            event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(4)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("priority-menu.items.5.material")))
                                .setName(Utils.format(config.getString("priority-menu.items.5.name")))
                                .setLore(Utils.format(config.getStringList("priority-menu.items.5.lore")))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;

                            bugReport.setPriority(5);
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);
                            event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                            return true;
                        })
                        .build())
                .build();
    }
}
