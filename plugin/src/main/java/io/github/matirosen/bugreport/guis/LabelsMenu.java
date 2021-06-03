package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LabelsMenu {

    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;
    @Inject
    private ReportPlugin plugin;

    public Inventory create(BugReport bugReport){
        FileConfiguration config = plugin.getConfig();
        List<ItemStack> entities = new ArrayList<>();


        for (String label : config.getStringList("labels")){
            entities.add(getItemStack(label, bugReport.getLabels().contains(label)));
        }

        return GUIBuilder.builderPaginated(ItemStack.class, Utils.format(config.getString("labels-menu.title")))
                .setBounds(0, 45)
                .setEntities(entities)
                .setItemParser(item -> ItemClickable.builder()
                        .setItemStack(item)
                        .setAction(event -> {
                            String label = event.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                            boolean isSelected = bugReport.getLabels().contains(label);
                            if (!isSelected){
                                bugReport.addLabel(label);
                            } else{
                                bugReport.removeLabel(label);
                            }
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);

                            event.setCurrentItem(getItemStack(label, !isSelected));
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(49)
                        .setItemStack(new CustomGuiItem(Material.valueOf(config.getString("labels-menu.items.back.material")), 1)
                                .displayName(Utils.format(config.getString("labels-menu.items.back.name")).replace("%report_id%",
                                        bugReport.getId() + ""))
                                .lore(Arrays.asList(Utils.format(config.getStringList("labels-menu.items.back.lore"))))
                                .create())
                        .setAction(event -> {
                            event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .setNextPageItem(Utils.getChangePageItem(plugin, "labels-menu.items.", "next"))
                .setPreviousPageItem(Utils.getChangePageItem(plugin, "labels-menu.items.", "previous"))
                .build();
    }

    private ItemStack getItemStack(String label, boolean isSelected){
        FileConfiguration config = plugin.getConfig();
        String selected = isSelected ? "selected" : "unselected";

        return new CustomGuiItem(Material.valueOf(config.getString("labels-menu.items.labels." + selected + ".material")), 1)
                .displayName(Utils.format(config.getString("labels-menu.items.labels." + selected + ".color-name") + label))
                .lore(Arrays.asList(Utils.format(config.getStringList("labels-menu.items.labels." + selected + ".lore"))))
                .glow(isSelected)
                .create();
    }
}
