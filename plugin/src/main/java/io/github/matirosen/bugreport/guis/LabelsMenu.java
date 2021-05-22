package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.ConfigHandler;
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
import java.util.Map;

public class LabelsMenu {

    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;

    public Inventory create(BugReport bugReport){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        ConfigHandler configHandler = ReportPlugin.getConfigHandler();
        Map<String, Object> inventoryMap = configHandler.getInventoryMap();

        List<ItemStack> entities = new ArrayList<>();

        List<String> lore = new ArrayList<>();
        lore.add(MessageHandler.format("&7Click to add this label"));

        for (String label : (List<String>) configHandler.getConfigMap().get("labels")){
            boolean isSelected = bugReport.getLabels().contains(label);
            entities.add(getItemStack(label, lore, isSelected));
        }

        return GUIBuilder.builderPaginated(ItemStack.class, "labels", 1)
                .setBounds(0, 45)
                .setEntities(entities)
                .setItemParser(item -> ItemClickable.builder()
                        .setItemStack(item)
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                            String label = event.getCurrentItem().getItemMeta().getDisplayName();

                            boolean hasLabel = bugReport.getLabels().contains(label);

                            if (!hasLabel){
                                bugReport.addLabel(label);
                            } else{
                                bugReport.removeLabel(label);
                            }
                            bugReport.setExist(true);
                            bugReportManager.saveReport(bugReport);

                            event.setCurrentItem(getItemStack(label, lore, !hasLabel));
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(49)
                        .setItemStack(new CustomGuiItem(Material.COMPASS, 1)
                                .displayName(MessageHandler.format("&7" + bugReport.getId()))
                                .create())
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;
                            event.getWhoClicked().openInventory(bugReportSecondMenu.build(bugReport));
                            return true;
                        })
                        .build())
                .setNextPageItem(ItemClickable.builder(50)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf((String) inventoryMap.get("previousMaterial")))
                                .setName((String) inventoryMap.get("previousName"))
                                .setLore(MessageHandler.format(messageHandler.getMessage("go-to-page")))
                                .build()
                        )
                        .build()
                )
                .setPreviousPageItem(ItemClickable.builder(48)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf((String) inventoryMap.get("previousMaterial")))
                                .setName((String) inventoryMap.get("previousName"))
                                .setLore(MessageHandler.format(messageHandler.getMessage("go-to-page")))
                                .build()
                        )
                        .build()
                )
                .build();
    }

    private ItemStack getItemStack(String label, List<String> lore, boolean isSelected){
        return new CustomGuiItem(Material.PAPER, 1)
                .displayName(label)
                .lore(lore)
                .glow(isSelected)
                .create();
    }
}
