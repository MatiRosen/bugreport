package io.github.matirosen.conversations;

import io.github.matirosen.ReportPlugin;
import io.github.matirosen.managers.BugReportManager;
import io.github.matirosen.utils.MessageHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.Objects;

public class BugReportPrompt extends StringPrompt {

    private String bugReportMessage;
    private boolean started;
    private final BugReportManager bugReportManager;
    private final MessageHandler messageHandler;


    public BugReportPrompt(String bugReportMessage, boolean started, BugReportManager bugReportManager){
        this.bugReportMessage = bugReportMessage;
        this.started = started;
        this.bugReportManager = bugReportManager;
        this.messageHandler = ReportPlugin.getMessageHandler();
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return started ? messageHandler.getMessage("finish-message-report")
                : messageHandler.getMessage("cancel-any-time") + "\n"
                + messageHandler.getMessage("give-bug-information");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String s) {
        Objects.requireNonNull(s);

        if (s.equalsIgnoreCase("cancel")){
            context.getForWhom().sendRawMessage(messageHandler.getMessage("bug-information-cancelled"));
            return Prompt.END_OF_CONVERSATION;
        }

        if (s.equalsIgnoreCase("done")){
            return new ConfirmationPrompt(bugReportMessage, bugReportManager);
        }

        bugReportMessage = bugReportMessage + " " + s;
        started = true;
        return this;
    }
}

