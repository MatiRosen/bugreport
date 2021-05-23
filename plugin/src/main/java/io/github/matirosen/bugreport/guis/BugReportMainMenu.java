package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.MessageHandler;
import io.github.matirosen.bugreport.managers.BugReportManager;
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
import java.util.Objects;

public class BugReportMainMenu {

    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;
    @Inject
    private ReportPlugin reportPlugin;


    public Inventory build(){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        List<ItemStack> entities = new ArrayList<>();

        FileConfiguration config = reportPlugin.getConfig();

        List<String> reportLore = Arrays.asList(Utils.format(config.getStringList("main-menu.items.reports.lore")));
        String solved = messageHandler.getMessage("solved");
        String unsolved = messageHandler.getMessage("unsolved");
        for (BugReport bugReport : bugReportManager.getBugReportList()) {
            List<String> lore = new ArrayList<>();
            String labels = bugReport.getLabels().toString().replace("[", "").replace("]", "");

            lore.add(Utils.format("&7" + bugReport.getId()));
            reportLore.forEach(s ->
                    lore.add(s.replace("%solved%", bugReport.isSolved() ? solved : unsolved)
                        .replace("%priority%", bugReport.getPriority() + "")
                        .replace("%labels%", labels)));

            entities.add(new CustomGuiItem(Material.valueOf(config.getString("main-menu.items.reports.material")), 1)
                    .displayName(Utils.format(config.getString("main-menu.items.reports.name").replace("%report_id%",
                            String.valueOf(bugReport.getId()))))
                    .lore(lore)
                    .glow(config.getBoolean("main-menu.items.reports.glow"))
                    .create());
        }

        return GUIBuilder.builderPaginated(ItemStack.class, Utils.format(config.getString("main-menu.title")))
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
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("main-menu.items.next-page.material")))
                                .setName(Utils.format(config.getString("main-menu.items.next-page.name")))
                                .setLore(Utils.format(config.getStringList("main-menu.items.next-page.lore")))
                                .build()
                        )
                        .build()
                )
                .setPreviousPageItem(ItemClickable.builder(48)
                        .setItemStack(ItemBuilder.newBuilder(Material.valueOf(config.getString("main-menu.items.previous-page.material")))
                                .setName(Utils.format(config.getString("main-menu.items.previous-page.name")))
                                .setLore(Utils.format(config.getStringList("main-menu.items.previous-page.lore")))
                                .build()
                        )
                        .build()
                )
                .build();
    }
}

