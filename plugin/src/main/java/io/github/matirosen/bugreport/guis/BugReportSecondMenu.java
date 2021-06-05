package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BookReport;
import io.github.matirosen.bugreport.reports.BookReportFactory;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Bukkit;
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
                        .setItemStack(getItemMenu("report", String.valueOf(bugReport.getId())))
                        .setAction(event -> {
                            Player player = (Player) event.getWhoClicked();

                            player.closeInventory();
                            BookReport bookReport = bookReportFactory.create(bugReport);
                            bookReport.give(player);

                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(1)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("report-menu.items.labels.material")))
                                .setName(Utils.format(config.getString("report-menu.items.labels.name")))
                                .setLore(Utils.format(labelLore))
                                .build())
                        .setAction(event -> {
                            event.getWhoClicked().openInventory(labelsMenu.create(bugReport));
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(2)
                        .setItemStack(getItemMenu("priority", String.valueOf(bugReport.getPriority())))
                        .setAction(event -> {
                            event.getWhoClicked().openInventory(priorityMenu.create(bugReport));
                            event.setCancelled(true);
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
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(8)
                        .setItemStack(getItemMenu("main-menu", ""))
                        .setAction(event -> {
                            bugReportManager.getBugReportList(bugReportList -> {
                                Bukkit.getScheduler().runTask(plugin, () ->
                                        event.getWhoClicked().openInventory(bugReportMainMenu.build(bugReportList, false, false, false)));
                            });
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .build();
    }

    private ItemStack getItemMenu(String s, String replace){
        FileConfiguration config = plugin.getConfig();

        Material material = Material.valueOf(config.getString("report-menu.items." + s + ".material"));
        String name = Utils.format(config.getString("report-menu.items." + s + ".name")
                .replace("%report_id%", replace)
                .replace("%bug_priority%", replace));
        List<String> lore = Arrays.asList(Utils.format(config.getStringList("report-menu.items." + s + ".lore")));

        return ItemBuilder.newBuilder(material)
                .setName(name)
                .setLore(lore)
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
