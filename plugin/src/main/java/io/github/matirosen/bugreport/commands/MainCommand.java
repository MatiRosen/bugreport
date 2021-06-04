package io.github.matirosen.bugreport.commands;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.conversations.BugReportPrompt;
import io.github.matirosen.bugreport.guis.BugReportMainMenu;
import io.github.matirosen.bugreport.guis.BugReportSecondMenu;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.managers.FileManager;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import io.github.matirosen.bugreport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class MainCommand implements TabExecutor {

    @Inject
    private ReportPlugin plugin;
    @Inject
    private BugReportManager bugReportManager;

    @Inject
    private BugReportMainMenu bugReportMainMenu;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;
    @Inject
    private ObjectRepository<BugReport, Integer> bugReportRepository;
    @Inject
    private FileManager fileManager;


    public void start(){
        Objects.requireNonNull(plugin.getCommand("bug")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("reload")){
                plugin.reloadConfig();
                fileManager.loadAllFileConfigurations();
                System.out.println(Utils.format("&a[Bug-Report] plugin reloaded!"));
                return true;
            }
            Bukkit.getLogger().log(Level.INFO, Utils.format("&cBug command can only be executed in-game!"));
            return false;
        }
        Player player = (Player) sender;
        if (player.isConversing()) {
            ReportPlugin.getMessageHandler().send(player, "already-doing-report");
            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("menu")){
            if (!player.hasPermission("bugreport.menu")){
                ReportPlugin.getMessageHandler().send(player, "no-permission");
                return false;
            }
            player.sendMessage("Loading reports...");
            bugReportManager.getBugReportList(bugReportList ->
                    Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(bugReportMainMenu.build(bugReportList)))
            );

            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("report")){
            FileConfiguration config = plugin.getConfig();
            if (!player.hasPermission(config.getString("use-permission")) && !config.getString("use-permission").isEmpty()){
                ReportPlugin.getMessageHandler().send(player, "no-permission");
                return false;
            }
            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conversation = cf
                    .withFirstPrompt(new BugReportPrompt(config,"", false, bugReportManager, bugReportRepository.getTotalReports()))
                    .withLocalEcho(false)
                    .withTimeout(config.getInt("time-out"))
                    .buildConversation(player);
            conversation.begin();
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("get")){
            if (!player.hasPermission("bugreport.get")){
                ReportPlugin.getMessageHandler().send(player, "no-permission");
                return false;
            }
            int id = 0;
            try {
                id = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception){
                ReportPlugin.getMessageHandler().send(player, "not-number");
                return false;
            }

            bugReportManager.getBugReportById(id, bugReport -> {
                if (bugReport == null){
                    ReportPlugin.getMessageHandler().send(player, "not-find-report");
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(bugReportSecondMenu.build(bugReport)));
            });
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("help")){
            if (!player.hasPermission("bugreport.help")){
                ReportPlugin.getMessageHandler().send(player, "no-permission");
                return false;
            }
            ReportPlugin.getMessageHandler().sendList(player, "help-command");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")){
            if (!player.hasPermission("bugreport.reload")){
                ReportPlugin.getMessageHandler().send(player, "no-permission");
                return false;
            }
            plugin.reloadConfig();
            fileManager.loadAllFileConfigurations();
            ReportPlugin.getMessageHandler().send(player, "plugin-reloaded");
            return true;
        }

        player.sendMessage(Utils.format("&d------ &a&l[&6&lBUG-REPORT&a&l] &d------\n\n"
                + "&9Author: &eMatiRosen\n"
                + "&3Version: &e" + plugin.getDescription().getVersion())
                + "\n\n");

        ReportPlugin.getMessageHandler().send(player, "use-help");

        player.sendMessage(Utils.format("\n&bhttps://github.com/MatiRosen/bugreport\n"
                + "&d------ &a&l[&6&lBUG-REPORT&a&l] &d------"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if (!(sender instanceof Player)) return tab;

        if (args.length < 2){
            if (sender.hasPermission("bugreport.get")) tab.add("get");

            if (sender.hasPermission("bugreport.menu")) tab.add("menu");

            if (sender.hasPermission("bugreport.help")) tab.add("help");

            if (sender.hasPermission("bugreport.reload")) tab.add("reload");

            FileConfiguration config = plugin.getConfig();
            if (sender.hasPermission(config.getString("use-permission")) || config.getString("use-permission").isEmpty()) tab.add("report");
        }

        if (args[0].equalsIgnoreCase("get") && sender.hasPermission("bugreport.get") && (args.length == 2 && args[1].isEmpty())){
            int totalReports = bugReportRepository.getTotalReports() - 1;
            tab.add("[" + 1 + "-" + totalReports + "]");
        }

        return tab;
    }
}