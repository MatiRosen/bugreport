package io.github.matirosen.commands;

import io.github.matirosen.ReportPlugin;
import io.github.matirosen.conversations.BugReportPrompt;
import io.github.matirosen.guis.BugReportMainMenu;
import io.github.matirosen.guis.BugReportSecondMenu;
import io.github.matirosen.managers.BugReportManager;
import io.github.matirosen.reports.BookReport;
import io.github.matirosen.reports.BookReportFactory;
import io.github.matirosen.reports.BugReport;
import io.github.matirosen.storage.repositories.ObjectRepository;
import io.github.matirosen.utils.ConfigHandler;
import io.github.matirosen.utils.MessageHandler;
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
    private BookReportFactory bookReportFactory;


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


        if (player.isConversing()) return false;

        if (args.length >= 1 && args[0].equalsIgnoreCase("xd")){
            player.openInventory(bugReportMainMenu.create(1));
            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("report")){
            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conversation = cf
                    .withFirstPrompt(new BugReportPrompt("", false, bugReportManager))
                    .withLocalEcho(false)
                    .withTimeout((int) ReportPlugin.getConfigHandler().getConfigMap().get("inactiveSeconds"))
                    .buildConversation(player);
            conversation.begin();
            return false;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("get")){
            int count = Integer.parseInt(args[1]);

            BugReport bugReport = bugReportManager.getBugReportById(count);

            if (bugReport != null){
                player.openInventory(bugReportSecondMenu.build(bugReport));
                return false;
            }

            bugReportRepository.loadAsync(count, report ->{
                if (report == null){
                    ReportPlugin.getMessageHandler().send(player, "not-find-report");
                    return;
                }
                player.openInventory(bugReportSecondMenu.build(report));
            });

            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("test")){
            int counter = Integer.parseInt(args[1]);

            for (int i = 0; i < counter; i++){
                BugReport bugReport = new BugReport(player.getName(), "holaxd", System.currentTimeMillis());
                bugReport.setId(ConfigHandler.totalReports);
                bugReportManager.addReport(bugReport);
            }
            return false;
        }
        return false;
    }
}