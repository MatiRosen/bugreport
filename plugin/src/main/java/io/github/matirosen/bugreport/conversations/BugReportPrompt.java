package io.github.matirosen.bugreport.conversations;

import io.github.matirosen.bugreport.ReportPlugin;
import io.github.matirosen.bugreport.managers.BugReportManager;
import io.github.matirosen.bugreport.utils.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.Objects;

public class BugReportPrompt extends StringPrompt {

    private final FileConfiguration config;
    private String bugReportMessage;
    private boolean started;
    private final BugReportManager bugReportManager;
    private final MessageHandler messageHandler;
    private final int totalReports;

    public BugReportPrompt(FileConfiguration config, String bugReportMessage, boolean started, BugReportManager bugReportManager, int totalReports){
        this.config = config;
        this.bugReportMessage = bugReportMessage;
        this.started = started;
        this.bugReportManager = bugReportManager;
        this.messageHandler = ReportPlugin.getMessageHandler();
        this.totalReports = totalReports;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return started ? messageHandler.getMessage("finish-message-report").replace("%done%", config.getString("done-word"))
                : messageHandler.getMessage("cancel-any-time").replace("%cancel%", config.getString("cancel-word")) + "\n"
                + messageHandler.getMessage("give-bug-information");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String s) {
        Objects.requireNonNull(s);


        if (s.equalsIgnoreCase(config.getString("cancel-word"))){
            context.getForWhom().sendRawMessage(messageHandler.getMessage("bug-information-cancelled"));
            return Prompt.END_OF_CONVERSATION;
        }

        if (s.equalsIgnoreCase(config.getString("done-word"))){
            return new ConfirmationPrompt(config, bugReportMessage, bugReportManager, totalReports);
        }

        bugReportMessage = bugReportMessage + " " + s;
        started = true;
        return this;
    }
}

