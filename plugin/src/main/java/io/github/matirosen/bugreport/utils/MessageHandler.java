package io.github.matirosen.bugreport.utils;

import io.github.matirosen.bugreport.managers.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;


public class MessageHandler {

    private final FileManager fileManager;

    public MessageHandler(FileManager fileManager){
       this.fileManager = fileManager;
    }

    public void send(Player player, String id){
        ConfigurationSection messageSection = fileManager.get("language");

        String message = messageSection.getString(id);
        if (message == null || message.isEmpty()) return;

        player.sendMessage(format(message.replace("%prefix%", messageSection.getString("prefix"))
                .replace("%player_name%", player.getName())));
    }

    public void sendList(Player player, String id){
        ConfigurationSection messageSection = fileManager.get("language");

        List<String> messageList = messageSection.getStringList(id);

        if (messageList == null || messageList.isEmpty()) return;

        for (String message : messageList){
            player.sendMessage(format(message.replace("%prefix%", messageSection.getString("prefix"))
                    .replace("%player_name%", player.getName())));
        }
    }

    public String getMessage(String id){
        ConfigurationSection messageSection = fileManager.get("language");

        String message = messageSection.getString(id);
        if (message == null || message.isEmpty()) return "";

        return format(message.replace("%prefix%", messageSection.getString("prefix")));
    }

    private String format(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }
}
