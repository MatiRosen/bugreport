package io.github.matirosen.bugreport.utils;

import io.github.matirosen.bugreport.ReportPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.item.type.ItemBuilder;

import java.util.Arrays;
import java.util.List;

public class Utils {
    public static int totalReports;

    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static String[] format(List<String> list){
        String[] formatted = new String[list.size()];

        for (int i = 0; i < list.size(); i ++){
            formatted[i] = format(list.get(i));
        }
        return formatted;
    }

    public static ItemClickable getChangePageItem(ReportPlugin plugin,String menu, String next){
        FileConfiguration config = plugin.getConfig();

        int slot = next.equalsIgnoreCase("next") ? 50 : 48;
        Material material = Material.valueOf(config.getString(menu + next + "-page.material"));
        String name = Utils.format(config.getString(menu + next + "-page.name"));
        List<String> lore = Arrays.asList(Utils.format(config.getStringList(menu + next + "-page.lore")));

        return ItemClickable.builder(slot)
                .setItemStack(ItemBuilder
                        .newBuilder(material)
                        .setName(name)
                        .setLore(lore)
                        .build())
                .build();
    }
}