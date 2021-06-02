package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.guis.filters.LabelsFilterMenu;
import io.github.matirosen.bugreport.guis.filters.PriorityFilterMenu;
import io.github.matirosen.bugreport.guis.filters.SolvedFilterMenu;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.MessageHandler;
import io.github.matirosen.bugreport.managers.BugReportManager;
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
import java.util.Objects;

public class BugReportMainMenu {

    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;
    @Inject
    private ReportPlugin reportPlugin;
    @Inject
    private PriorityFilterMenu priorityFilterMenu;
    @Inject
    private SolvedFilterMenu solvedFilterMenu;
    @Inject
    private LabelsFilterMenu labelsFilterMenu;


    public Inventory build(List<BugReport> bugReportList){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        List<ItemStack> entities = new ArrayList<>();

        FileConfiguration config = reportPlugin.getConfig();
        List<String> reportLore = Arrays.asList(Utils.format(config.getStringList("main-menu.items.reports.lore")));
        String solved = messageHandler.getMessage("solved");
        String unsolved = messageHandler.getMessage("unsolved");


        for (BugReport bugReport : bugReportList) {
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

                            int id = Integer.parseInt(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLore().get(0).substring(2));

                            Player player = (Player) event.getWhoClicked();
                            player.closeInventory();
                            event.setCancelled(true);

                            bugReportManager.getBugReportById(id, bugReport -> {
                                if (bugReport == null){
                                    player.sendMessage(ReportPlugin.getMessageHandler().getMessage("not-find-report"));
                                    return;
                                }
                                Bukkit.getScheduler().runTask(reportPlugin, () -> player.openInventory(bugReportSecondMenu.build(bugReport)));
                            });
                            return true;
                        })
                        .build())
                .addItem(getFilterItem("priority", 45, bugReportList))
                .addItem(getFilterItem("solved", 46, bugReportList))
                .setNextPageItem(Utils.getChangePageItem(reportPlugin, "main-menu.items.", "next"))
                .setPreviousPageItem(Utils.getChangePageItem(reportPlugin, "main-menu.items.","previous"))
                .build();
        //TODO filter by player reputation. For example, if a player does a report and a staff set a high priority,
        // then its a good report, so player gets reputation for future reports. (Same with bad reputation)
    }

    private ItemClickable getFilterItem(String filter, int slot, List<BugReport> bugReportList){
        FileConfiguration config = reportPlugin.getConfig();

        Material material = Material.valueOf(config.getString("main-menu.items." + filter + "-filter.material"));
        String name = Utils.format(config.getString("main-menu.items." + filter + "-filter.name"));
        List<String> lore = Arrays.asList(Utils.format(config.getStringList("main-menu.items." + filter + "-filter.lore")));

        return ItemClickable.builder(slot)
                .setItemStack(ItemBuilder
                        .newBuilder(material)
                        .setName(name)
                        .setLore(lore)
                        .build())
                .setAction(event -> {
                    if (!(event.getWhoClicked() instanceof Player)) return false;
                    Player player = (Player) event.getWhoClicked();

                    if (filter.equalsIgnoreCase("priority")){
                        player.openInventory(priorityFilterMenu.build(bugReportList));
                    } else if (filter.equalsIgnoreCase("solved")){
                        player.openInventory(solvedFilterMenu.build(bugReportList));
                    } else if (filter.equalsIgnoreCase("label")){
                        player.openInventory(labelsFilterMenu.build(bugReportList));
                    }

                    event.setCancelled(true);
                    return true;
                })
                .build();
    }
}

