package io.github.matirosen.bugreport.guis.filters;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.guis.BugReportMainMenu;
import io.github.matirosen.bugreport.guis.CustomGuiItem;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LabelsFilterMenu {

    @Inject
    private BugReportMainMenu bugReportMainMenu;
    @Inject
    private ReportPlugin plugin;


    public Inventory build(List<BugReport> bugReportList){
        FileConfiguration config = plugin.getConfig();
        List<ItemStack> entities = new ArrayList<>();

        for (String label : config.getStringList("labels")){
            entities.add(getLabelItem(label, false));
        }

        String backItem = "filters-menu.labels.items.back.";
        List<String> labels = new ArrayList<>();

        return GUIBuilder.builderPaginated(ItemStack.class, Utils.format(config.getString("filters-menu.labels.title")))
                .setBounds(0, 45)
                .setEntities(entities)
                .setItemParser(item -> ItemClickable.builder()
                        .setItemStack(item)
                        .setAction(event -> {
                            String label = item.getItemMeta().getLore().get(0);

                            event.setCancelled(true);
                            event.setCurrentItem(getLabelItem(label, !labels.contains(label)));
                            if (!labels.contains(label)){
                                labels.add(label);
                            } else{
                                labels.remove(label);
                            }
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(49)
                        .setItemStack(ItemBuilder
                                .newBuilder(Material.valueOf(config.getString(backItem + "material")))
                                .setName(Utils.format(config.getString(backItem + "name")))
                                .setLore(Arrays.asList(Utils.format(config.getStringList(backItem + "lore"))))
                                .build())
                        .setAction(event -> {
                            List<BugReport> filteredList = bugReportList.stream().filter(bugReport ->
                                    bugReport.getLabels().containsAll(labels)).collect(Collectors.toList());

                            event.getWhoClicked().openInventory(bugReportMainMenu.build(filteredList));
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .setNextPageItem(Utils.getChangePageItem(plugin, "filters-menu.labels.items.", "next"))
                .setPreviousPageItem(Utils.getChangePageItem(plugin, "filters-menu.labels.items.", "previous"))
                .build();
    }

    private ItemStack getLabelItem(String label, boolean isSelected){
        FileConfiguration config = plugin.getConfig();

        Material material = Material.valueOf(config.getString("filters-menu.labels.items.label.material"));
        String name = Utils.format(config.getString("filters-menu.labels.items.label.name").replace("%label%", label));
        List<String> lore = new ArrayList<>();
        lore.add(label);
        for (String s : config.getStringList("filters-menu.labels.items.label.lore")){
            lore.add(Utils.format(s.replace("%label%", label)));
        }

        return new CustomGuiItem(material, 1)
                .displayName(name)
                .lore(lore)
                .glow(isSelected)
                .create();
    }
}
