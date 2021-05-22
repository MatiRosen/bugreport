package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BookReport;
import io.github.matirosen.bugreport.reports.BookReportFactory;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BugReportSecondMenu {

    @Inject
    private BookReportFactory bookReportFactory;
    @Inject
    private LabelsMenu labelsMenu;
    @Inject
    private PriorityMenu priorityMenu;
    @Inject
    private BugReportManager bugReportManager;

    public Inventory build(BugReport bugReport){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();

        List<String> reportLore = new ArrayList<>();
        reportLore.add(messageHandler.getMessage("report-details"));


        return GUIBuilder.builder(messageHandler.getMessage("report-title")
                .replace("%report_id%", String.valueOf(bugReport.getId())), 1)
                .addItem(ItemClickable.builder(0)
                        .setItemStack(ItemBuilder.newBuilder(Material.PAPER)
                                .setName(messageHandler.getMessage("report-material-name").replace("%report_id%",
                                        String.valueOf(bugReport.getId())))
                                .setLore(reportLore)
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                            Player player = (Player) event.getWhoClicked();

                            player.closeInventory();
                            BookReport bookReport = bookReportFactory.create(bugReport);
                            bookReport.give(player);
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(1)
                        .setItemStack(ItemBuilder.newBuilder(Material.WRITTEN_BOOK)
                                .setName(messageHandler.getMessage("labels"))
                                .setLore(bugReport.getLabels())
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                            event.getWhoClicked().openInventory(labelsMenu.create(bugReport));
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(2)
                        .setItemStack(ItemBuilder.newBuilder(Material.DIAMOND)
                                .setName(messageHandler.getMessage("priority").replace("%bug_priority%",
                                        String.valueOf(bugReport.getPriority())))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                            event.getWhoClicked().openInventory(priorityMenu.create(bugReport));
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(3)
                        .setItemStack(solvedItem(bugReport.isSolved()))
                        .setAction(event -> {
                            bugReport.setSolved(!bugReport.isSolved());
                            event.setCurrentItem(solvedItem(bugReport.isSolved()));
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);
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

    private ItemStack solvedItem(boolean solved){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();


        Material material = solved ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK;
        String name = solved ? messageHandler.getMessage("bug-solved")
                : messageHandler.getMessage("bug-unsolved");
        List<String> lore = new ArrayList<>();
        String solvedString = solved ? messageHandler.getMessage("set-unsolved")
                : messageHandler.getMessage("set-solved");
        lore.add(solvedString);

        return new CustomGuiItem(material, 1)
                .displayName(name)
                .lore(lore)
                .create();
    }
}
