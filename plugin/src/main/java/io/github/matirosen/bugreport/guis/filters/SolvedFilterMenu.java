package io.github.matirosen.bugreport.guis.filters;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.guis.BugReportMainMenu;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SolvedFilterMenu {

    @Inject
    private BugReportMainMenu bugReportMainMenu;
    @Inject
    private ReportPlugin plugin;

    public Inventory build(List<BugReport> bugReportList){
        FileConfiguration config = plugin.getConfig();

        return GUIBuilder.builder(Utils.format(config.getString("filters-menu.solved.title")), 1)
                .addItem(getConfigItem(bugReportList, "solved"))
                .addItem(getConfigItem(bugReportList, "unsolved"))
                .build();
    }

    private ItemClickable getConfigItem(List<BugReport> bugReportList, String solved){
        FileConfiguration config = plugin.getConfig();

        Material material = Material.valueOf(config.getString("filters-menu.solved.items." + solved + ".material"));
        String name = Utils.format(config.getString("filters-menu.solved.items." + solved + ".name"));
        List<String> lore = Arrays.asList(Utils.format(config.getStringList("filters-menu.solved.items." + solved + ".lore")));

        int slot = solved.equalsIgnoreCase("solved") ? 0 : 1;

        return ItemClickable.builder(slot)
                .setItemStack(ItemBuilder.newBuilder(material).setName(name).setLore(lore).build())
                .setAction(event -> {
                    List<BugReport> filteredList = bugReportList.stream().filter(bugReport ->
                           bugReport.isSolved() == solved.equalsIgnoreCase("solved")).collect(Collectors.toList());

                    event.getWhoClicked().openInventory(bugReportMainMenu.build(filteredList));
                    event.setCancelled(true);
                    return true;
                })
                .build();
    }
}
