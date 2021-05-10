package io.github.matirosen.bugreport.listeners;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.guis.*;
import io.github.matirosen.bugreport.reports.BookReport;
import io.github.matirosen.bugreport.reports.BookReportFactory;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryListener implements Listener {


    @Inject
    private BugReportMainMenu bugReportMainMenu;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;
    @Inject
    private PriorityMenu priorityMenu;
    @Inject
    private ObjectRepository<BugReport, Integer> bugReportRepository;
    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private BookReportFactory bookReportFactory;
    @Inject
    private LabelsMenu labelsMenu;



    @Inject
    public InventoryListener(JavaPlugin javaPlugin){
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();

        if (!(inventoryHolder instanceof BugReportMainMenu) && !(inventoryHolder instanceof BugReportSecondMenu)
                && !(inventoryHolder instanceof PriorityMenu) && !(inventoryHolder instanceof LabelsMenu)) return;

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        event.setCancelled(true);


        if (inventoryHolder instanceof BugReportMainMenu){
            mainMenuLogic(slot, player, currentItem);
        }
        else if (inventoryHolder instanceof BugReportSecondMenu){
            int id = Integer.parseInt(inventory.getItem(8).getItemMeta().getDisplayName().substring(2));
            secondMenuLogic(id, slot, inventory, player);
        }
        else if (inventoryHolder instanceof PriorityMenu){
            int id = Integer.parseInt(inventory.getItem(8).getItemMeta().getDisplayName().substring(2));

            priorityMenuLogic(id, slot, player);
        } else if (inventoryHolder instanceof LabelsMenu){
            int id = Integer.parseInt(inventory.getItem(49).getItemMeta().getDisplayName().substring(2));

            labelsMenuLogic(id, slot, player, currentItem);
        }
    }

    private void mainMenuLogic(int slot, Player player, ItemStack currentItem){
        if (slot == 48 || slot == 50) {
            int actualPage = currentItem.getAmount();
            player.openInventory(bugReportMainMenu.create(actualPage));
            return;
        } else if (slot == 49){

            return;
        }

        int reportId = Integer.parseInt(Objects.requireNonNull(currentItem.getItemMeta()).getLore().get(0).substring(2));
        BugReport bugReport = bugReportManager.getBugReportById(reportId);
        player.closeInventory();

        if (bugReport == null){
            player.sendMessage(ReportPlugin.getMessageHandler().getMessage("not-find-report"));
            return;
        }

        player.openInventory(bugReportSecondMenu.build(bugReport));
    }

    private void secondMenuLogic(int id, int slot, Inventory inventory, Player player){
        BugReport bugReport = bugReportManager.getBugReportById(id);


        if (slot == 3){
            MessageHandler messageHandler = ReportPlugin.getMessageHandler();
            bugReport.setSolved(!bugReport.isSolved());
            List<String> solvedLore = new ArrayList<>();
            String solvedString = bugReport.isSolved() ? messageHandler.getMessage("set-unsolved")
                    : messageHandler.getMessage("set-solved");
            solvedLore.add(solvedString);

            Material solvedMaterial = bugReport.isSolved() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK;

            String solved = bugReport.isSolved() ? messageHandler.getMessage("bug-solved")
                    : messageHandler.getMessage("bug-unsolved");

            ItemStack solvedItem = new CustomGuiItem(solvedMaterial, 1)
                    .displayName(solved)
                    .lore(solvedLore)
                    .create();

            ItemStack bugId = new CustomGuiItem(Material.PAPER, 1)
                    .displayName(MessageHandler.format("&7" + bugReport.getId()))
                    .create();

            inventory.setItem(8, bugId);

            inventory.setItem(3, solvedItem);

            bugReportManager.saveReport(bugReport);

        } else if (slot == 0){
            player.closeInventory();
            BookReport bookReport = bookReportFactory.create(bugReport);
            bookReport.give(player);

        } else if (slot == 2){
            player.openInventory(priorityMenu.create(bugReport));
        } else if (slot == 1){
            player.openInventory(labelsMenu.create(1 , bugReport));
        }
    }

    private void priorityMenuLogic(int id, int slot, Player player){
        BugReport bugReport = bugReportManager.getBugReportById(id);

        if (slot >= 0 && slot <= 4){
            bugReport.setPriority(slot+1);
            bugReportManager.saveReport(bugReport);
        }
        player.openInventory(bugReportSecondMenu.build(bugReport));
    }

    private void labelsMenuLogic(int id, int slot, Player player, ItemStack currentItem) {
        BugReport bugReport = bugReportManager.getBugReportById(id);

        if (slot == 48 || slot == 50) {
            int actualPage = currentItem.getAmount();
            player.openInventory(labelsMenu.create(actualPage, bugReport));
            return;
        } if (slot == 49){
            player.openInventory(bugReportSecondMenu.build(bugReport));
            return;
        }

        bugReport.addLabel(currentItem.getItemMeta().getDisplayName());
        bugReportManager.saveReport(bugReport);
    }
}