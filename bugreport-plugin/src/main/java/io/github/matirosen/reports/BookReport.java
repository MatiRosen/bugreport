package io.github.matirosen.reports;

import io.github.matirosen.ReportPlugin;
import io.github.matirosen.nms.common.NMS;
import io.github.matirosen.utils.MessageHandler;
import me.yushust.inject.assisted.Assist;
import me.yushust.inject.assisted.Assisted;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import javax.inject.Inject;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class BookReport {

    private final BugReport bugReport;

    private ItemStack book;

    @Inject
    private NMS nmsImplementation;

    @Assisted
    public BookReport(@Assist BugReport bugReport){
        this.bugReport = bugReport;
    }

    public void give(Player player){
        build();

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack item = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book);

        nmsImplementation.sendBook(player);

        player.getInventory().setItem(slot, item);
    }

    private void build(){
        MessageHandler messageHandler = ReportPlugin.getMessageHandler();

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        Objects.requireNonNull(bookMeta);

        String id = String.valueOf(bugReport.getId());
        String playerName = bugReport.getPlayerName();
        String date = DateFormat.getDateTimeInstance().format(new Date(bugReport.getCurrentTimeMillis()));
        String message = bugReport.getReportMessage();
        String solved = bugReport.isSolved() ? messageHandler.getMessage("first-page-solved")
                : messageHandler.getMessage("first-page-unsolved");
        String priority = String.valueOf(bugReport.getPriority());

        String page1 = MessageHandler.format(messageHandler.getMessage("first-page-bug-id")
                .replace("%report_id%", id) + "\n\n" + messageHandler.getMessage("first-page-player")
                .replace("%player_name%", playerName) + "\n" + messageHandler.getMessage("first-page-date")
                .replace("%report_date%", date) + "\n" + messageHandler.getMessage("first-page-priority")
                .replace("%report_priority", priority) + "\n" + messageHandler.getMessage("first-page-bug-status")
                .replace("%bug_solved%", solved));

        String page2 = messageHandler.getMessage("second-page-report-message")
                .replace("%report_message%", message);


        bookMeta.addPage(page1, page2);
        bookMeta.setTitle(messageHandler.getMessage("report-material-name").replace("%report_id%", String.valueOf(id)));
        bookMeta.setAuthor(bugReport.getPlayerName());
        book.setItemMeta(bookMeta);


        this.book = book;
    }
}
