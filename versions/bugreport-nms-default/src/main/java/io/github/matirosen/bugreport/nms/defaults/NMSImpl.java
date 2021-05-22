package io.github.matirosen.bugreport.nms.defaults;

import io.github.matirosen.bugreport.nms.common.NMS;
import org.bukkit.entity.Player;

public class NMSImpl implements NMS {

    @Override
    public void sendBook(Player player){
        player.openBook(player.getInventory().getItemInMainHand());
    }
}
