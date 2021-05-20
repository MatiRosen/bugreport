package io.github.matirosen.bugreport.commands;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.conversations.BugReportPrompt;
import io.github.matirosen.bugreport.guis.BugReportMainMenu;
import io.github.matirosen.bugreport.guis.BugReportSecondMenu;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.reports.BookReportFactory;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.utils.ConfigHandler;
import io.github.matirosen.bugreport.utils.MessageHandler;
import io.github.matirosen.bugreport.storage.repositories.ObjectRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Objects;
import java.util.logging.Level;

public class MainCommand implements CommandExecutor {

    @Inject
    private ReportPlugin plugin;
    @Inject
    private BugReportManager bugReportManager;
    @Inject
    private ObjectRepository<BugReport, Integer> bugReportRepository;

    @Inject
    private BugReportMainMenu bugReportMainMenu;
    @Inject
    private BugReportSecondMenu bugReportSecondMenu;


    @Inject
    public MainCommand(ReportPlugin plugin){
        Objects.requireNonNull(plugin.getCommand("bug")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Bukkit.getLogger().log(Level.INFO, MessageHandler.format("&cBug command can only be executed in-game!"));
            return false;
        }
        Player player = (Player) sender;
        if (player.isConversing()) {
            ReportPlugin.getMessageHandler().send(player, "already-doing-report");
            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("menu")){
            player.openInventory(bugReportMainMenu.create(1));
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("report")){
            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conversation = cf
                    .withFirstPrompt(new BugReportPrompt("", false, bugReportManager))
                    .withLocalEcho(false)
                    .withTimeout((int) ReportPlugin.getConfigHandler().getConfigMap().get("inactiveSeconds"))
                    .buildConversation(player);
            conversation.begin();
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("get")){
            int count = Integer.parseInt(args[1]);

            BugReport bugReport = bugReportManager.getBugReportById(count);

            if (bugReport != null){
                player.openInventory(bugReportSecondMenu.build(bugReport));
                return true;
            }

            bugReportRepository.loadAsync(count, report -> {
                if (report == null){
                    ReportPlugin.getMessageHandler().send(player, "not-find-report");
                    return;
                }
                player.openInventory(bugReportSecondMenu.build(report));
            });
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("help")){
            ReportPlugin.getMessageHandler().sendList(player, "help-command");
            return true;
        }

        player.sendMessage(MessageHandler.format("&d------ &a&l[&6&lBUG-REPORT&a&l] &d------\n\n"
                + "&9Author: &eMatiRosen\n"
                + "&3Version: &e" + plugin.getDescription().getVersion())
                + "\n\n");

        ReportPlugin.getMessageHandler().send(player, "use-help");

        player.sendMessage(MessageHandler.format("\n&bhttps://github.com/MatiRosen/bugreport\n"
                + "&d------ &a&l[&6&lBUG-REPORT&a&l] &d------"));

        if (args.length >= 1 && args[0].equalsIgnoreCase("test")){
            int counter = Integer.parseInt(args[1]);

            for (int i = 0; i < counter; i++){
                BugReport bugReport = new BugReport(player.getName(), "hola xd", System.currentTimeMillis(), false);
                bugReport.setId(ConfigHandler.totalReports);
                bugReportManager.addReport(bugReport);
            }
            return true;
        }
        return true;
    }
}