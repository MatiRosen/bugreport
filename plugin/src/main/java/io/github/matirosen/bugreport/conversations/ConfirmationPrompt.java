package io.github.matirosen.bugreport.conversations;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.reports.BugReport;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.utils.Utils;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;


import java.util.Objects;

public class ConfirmationPrompt extends StringPrompt {

    private final FileConfiguration config;
    private final String bugReportMessage;
    private final BugReportManager bugReportManager;
    private final MessageHandler messageHandler;
    private final int totalReports;

    public ConfirmationPrompt(FileConfiguration config, String bugReportMessage, BugReportManager bugReportManager, int totalReports){
        this.config = config;
        this.bugReportMessage = bugReportMessage;
        this.bugReportManager = bugReportManager;
        this.totalReports = totalReports;

        this.messageHandler = ReportPlugin.getMessageHandler();
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return messageHandler.getMessage("bug-description") + Utils.format("\n&b"+bugReportMessage+"\n")
                + messageHandler.getMessage("bug-confirmation").replace("%yes%", config.getString("yes-word"))
                .replace("%no%", config.getString("no-word"));
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String s) {
        Objects.requireNonNull(s);

        if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase(config.getString("yes-word"))){
            return new BugReportPrompt(config, bugReportMessage, true, bugReportManager, totalReports);
        }

        else if (s.equalsIgnoreCase("n") || s.equalsIgnoreCase(config.getString("no-word"))){
            Player player = (Player) context.getForWhom();
            player.sendRawMessage(messageHandler.getMessage("report-completed"));

            BugReport report = new BugReport(totalReports, player.getName(), bugReportMessage, System.currentTimeMillis(), false);
            bugReportManager.saveReport(report);

            return Prompt.END_OF_CONVERSATION;
        }

        else {
            context.getForWhom().sendRawMessage(messageHandler.getMessage("write-y-n").replace("%yes%", config.getString("yes-word"))
                    .replace("%no%", config.getString("no-word")));
            return this;
        }
    }
}
