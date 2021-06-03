package io.github.matirosen.bugreport.guis.filters;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.guis.BugReportMainMenu;
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
import java.util.stream.Collectors;

public class PriorityFilterMenu {

    @Inject
    private BugReportMainMenu bugReportMainMenu;
    @Inject
    private ReportPlugin plugin;

    public Inventory build(List<BugReport> bugReportList){
        FileConfiguration config = plugin.getConfig();

        return GUIBuilder.builder(Utils.format(config.getString("filters-menu.priority.title")), 1)
                .addItem(getConfigItem(bugReportList, 1))
                .addItem(getConfigItem(bugReportList, 2))
                .addItem(getConfigItem(bugReportList, 3))
                .addItem(getConfigItem(bugReportList, 4))
                .addItem(getConfigItem(bugReportList, 5))
                .build();
    }

    private ItemClickable getConfigItem(List<BugReport> bugReportList, int priority){
        FileConfiguration config = plugin.getConfig();
        Material material = Material.valueOf(config.getString("filters-menu.priority.items." + priority + ".material"));
        String name = Utils.format(config.getString("filters-menu.priority.items." + priority + ".name"));
        List<String> lore = Arrays.asList(Utils.format(config.getStringList("filters-menu.priority.items." + priority + ".lore")));

        return ItemClickable.builder(priority - 1)
                .setItemStack(ItemBuilder.newBuilder(material).setName(name).setLore(lore).build())
                .setAction(event -> {
                    List<BugReport> filteredList = bugReportList.stream().filter(bugReport ->
                            bugReport.getPriority() == priority).collect(Collectors.toList());

                    event.getWhoClicked().openInventory(bugReportMainMenu.build(filteredList));
                    event.setCancelled(true);
                    return true;
                })
                .build();
    }
}
