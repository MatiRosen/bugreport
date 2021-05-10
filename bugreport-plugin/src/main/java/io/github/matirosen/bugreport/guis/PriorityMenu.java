package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.ConfigHandler;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PriorityMenu implements InventoryHolder {


    public Inventory create(BugReport bugReport){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        ConfigHandler configHandler = ReportPlugin.getConfigHandler();

        Inventory inventory = Bukkit.createInventory(this, 9, "Priority");

        ItemStack priority1 = new CustomGuiItem(Material.COAL, 1)
                .displayName("Set priority to 1")
                .create();

        ItemStack priority2 = new CustomGuiItem(Material.IRON_INGOT, 2)
                .displayName("Set priority to 2")
                .create();

        ItemStack priority3 = new CustomGuiItem(Material.GOLD_INGOT, 3)
                .displayName("Set priority to 3")
                .create();

        ItemStack priority4 = new CustomGuiItem(Material.EMERALD, 4)
                .displayName("Set priority to 4")
                .create();

        ItemStack priority5 = new CustomGuiItem(Material.DIAMOND, 5)
                .displayName("Set priority to 5")
                .create();

        ItemStack bugId = new CustomGuiItem(Material.PAPER, 1)
                .displayName(MessageHandler.format("&7" + bugReport.getId()))
                .create();

        inventory.setItem(0, priority1);
        inventory.setItem(1, priority2);
        inventory.setItem(2, priority3);
        inventory.setItem(3, priority4);
        inventory.setItem(4, priority5);
        inventory.setItem(8, bugId);

        return inventory;
    }


    @Override
    public Inventory getInventory() {
        return null;
    }
}
