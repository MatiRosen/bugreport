package io.github.matirosen.bugreport.guis;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.utils.ConfigHandler;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class FilterMenu implements InventoryHolder {

    public Inventory create(){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();
        ConfigHandler configHandler = ReportPlugin.getConfigHandler();

        Inventory inventory = Bukkit.createInventory(this, 9, "Filter");

        ItemStack priorityFilter = new CustomGuiItem(Material.DIAMOND, 1)
                .displayName("set priority filter")
                .create();

        ItemStack labelFilter = new CustomGuiItem(Material.TRIPWIRE_HOOK, 1)
                .displayName("set label filter")
                .create();

        ItemStack solvedFilter = new CustomGuiItem(Material.EMERALD, 1)
                .displayName("set solved filter")
                .create();

        inventory.setItem(0, priorityFilter);
        inventory.setItem(1, labelFilter);
        inventory.setItem(2, solvedFilter);


        return inventory;
    }


    @Override
    public Inventory getInventory() {
        return null;
    }
}
