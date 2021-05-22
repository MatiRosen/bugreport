package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.ConfigHandler;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.Material;
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

    public Inventory create(BugReport bugReport){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        ConfigHandler configHandler = ReportPlugin.getConfigHandler();

        return GUIBuilder.builder("Priority", 1)
                .addItem(ItemClickable.builder(0)
                        .setItemStack(ItemBuilder.newBuilder(Material.COAL)
                                .setName("Set priority to 1")
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
                        .setItemStack(ItemBuilder.newBuilder(Material.IRON_INGOT)
                                .setName("Set priority to 2")
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
                        .setItemStack(ItemBuilder.newBuilder(Material.GOLD_INGOT)
                                .setName("Set priority to 3")
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
                        .setItemStack(ItemBuilder.newBuilder(Material.EMERALD)
                                .setName("Set priority to 4")
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
                        .setItemStack(ItemBuilder.newBuilder(Material.DIAMOND)
                                .setName("Set priority to 5")
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
                .addItem(ItemClickable.builder(8)
                        .setItemStack(ItemBuilder.newBuilder(Material.PAPER)
                                .setName(MessageHandler.format("&7" + bugReport.getId()))
                                .build())
                        .build())
                .build();
    }
}
