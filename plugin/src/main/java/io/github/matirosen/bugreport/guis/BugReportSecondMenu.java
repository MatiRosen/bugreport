package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BookReport;
import io.github.matirosen.bugreport.reports.BookReportFactory;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BugReportSecondMenu {

    @Inject
    private BookReportFactory bookReportFactory;
    @Inject
    private BugReportMainMenu bugReportMainMenu;
    @Inject
    private LabelsMenu labelsMenu;
    @Inject
    private PriorityMenu priorityMenu;
    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private ReportPlugin plugin;

    public Inventory build(BugReport bugReport){
        FileConfiguration config = plugin.getConfig();


        List<String> labelLore = new ArrayList<>();
        for (String label : config.getStringList("report-menu.items.labels.lore")){
            if (label.contains("%labels%")){
                bugReport.getLabels().forEach(s -> labelLore.add(label.replace("%labels%", s)));
            } else{
                labelLore.add(Utils.format(label));
            }
        }

        return GUIBuilder.builder(Utils.format(config.getString("report-menu.title").replace("%report_id%", String.valueOf(bugReport.getId()))), 1)
                .addItem(ItemClickable.builder(0)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("report-menu.items.report.material")))
                                .setName(Utils.format(config.getString("report-menu.items.report.name").replace("%report_id%",
                                        String.valueOf(bugReport.getId()))))
                                .setLore(Arrays.asList(Utils.format(config.getStringList("report-menu.items.report.lore"))))
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
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("report-menu.items.labels.material")))
                                .setName(Utils.format(config.getString("report-menu.items.labels.name")))
                                .setLore(Utils.format(labelLore))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                            event.getWhoClicked().openInventory(labelsMenu.create(bugReport));
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(2)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("report-menu.items.priority.material")))
                                .setName(Utils.format(config.getString("report-menu.items.priority.name").replace("%bug_priority%",
                                        String.valueOf(bugReport.getPriority()))))
                                .setLore(Utils.format(config.getStringList("report-menu.items.priority.lore")))
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
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("report-menu.items.main-menu.material")))
                                .setName(Utils.format(config.getString("report-menu.items.main-menu.name")))
                                .setLore(Utils.format(config.getStringList("report-menu.items.main-menu.lore")))
                                .build())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                           // event.getWhoClicked().openInventory(bugReportMainMenu.build());
                            return true;
                        })
                        .build())
                .build();
    }

    private ItemStack solvedItem(boolean isSolved){
        FileConfiguration config = plugin.getConfig();
        String solved = isSolved ? "solved" : "unsolved";

        Material material = Material.valueOf(config.getString("report-menu.items." + solved + ".material"));
        String name = config.getString("report-menu.items." + solved + ".name");
        List<String> lore = new ArrayList<>(config.getStringList("report-menu.items." + solved + ".lore"));

        return new CustomGuiItem(material, 1)
                .displayName(Utils.format(name))
                .lore(Arrays.asList(Utils.format(lore)))
                .create();
    }
}
