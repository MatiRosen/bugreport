package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.ConfigHandler;
import io.github.matirosen.bugreport.utils.MessageHandler;
import io.github.matirosen.bugreport.managers.BugReportManager;
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
import java.util.Objects;

public class BugReportMainMenu {

    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;


    public Inventory build(){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        ConfigHandler configHandler = ReportPlugin.getConfigHandler();
        Map<String, Object> inventoryMap = configHandler.getInventoryMap();
        List<ItemStack> entities = new ArrayList<>();

        List<String> reportLore = (List<String>) configHandler.getInventoryMap().get("reportLore");
        String solved = messageHandler.getMessage("solved");
        String unsolved = messageHandler.getMessage("unsolved");
        for (BugReport bugReport : bugReportManager.getBugReportList()) {
            List<String> lore = new ArrayList<>();
            String labels = "";

            for (String s : bugReport.getLabels()){
                labels = s + ", " + labels;
            }

            lore.add(MessageHandler.format("&7" + bugReport.getId()));
            String finalLabels = labels;
            reportLore.forEach(s ->
                    lore.add(s.replace("%solved%", bugReport.isSolved() ? solved : unsolved)
                        .replace("%priority%", bugReport.getPriority() + "")
                        .replace("%labels%", finalLabels)));

            entities.add(new CustomGuiItem(Material.valueOf((String) inventoryMap.get("reportMaterial")), 1)
                    .displayName(((String) inventoryMap.get("reportName")).replace("%report_id%",
                            String.valueOf(bugReport.getId())))
                    .lore(lore)
                    .glow((boolean) inventoryMap.get("reportGlow"))
                    .create());
        }

        return GUIBuilder.builderPaginated(ItemStack.class, MessageHandler.format((String)
                inventoryMap.get("mainInventoryTitle")))
                .setBounds(0, 45)
                .setEntities(entities)
                .setItemParser(item -> ItemClickable.builder()
                        .setItemStack(item)
                        .setAction(event -> {
                            if (!(event.getWhoClicked() instanceof Player)) return false;

                            int reportId = Integer.parseInt(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLore().get(0).substring(2));
                            BugReport bugReport = bugReportManager.getBugReportById(reportId);
                            Player player = (Player) event.getWhoClicked();
                            player.closeInventory();

                            if (bugReport == null){
                                player.sendMessage(ReportPlugin.getMessageHandler().getMessage("not-find-report"));
                                return false;
                            }
                            player.openInventory(bugReportSecondMenu.build(bugReport));
                            event.setCancelled(true);
                            return true;
                        })
                        .build())
                .addItem(ItemClickable.builder(49)
                        .setItemStack(new CustomGuiItem(Material.WATCH, 1)
                                .displayName("set filter")
                                .create())
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
}

