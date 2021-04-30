package io.github.matirosen.guis;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class CustomGuiItem {

    private final ItemStack itemStack;

    public CustomGuiItem(Material material, int amount){
        this.itemStack = new ItemStack(material, amount);
    }

    public CustomGuiItem displayName(String name){
        if (name == null) return this;
        ItemMeta meta = itemStack.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(name);
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomGuiItem lore(List<String> lore){
        if (lore.isEmpty()) return this;
        ItemMeta meta = itemStack.getItemMeta();
        Objects.requireNonNull(meta).setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public CustomGuiItem glow(boolean glow){
        if (!glow) return this;
        ItemMeta meta = itemStack.getItemMeta();
        Objects.requireNonNull(meta).addEnchant(Enchantment.KNOCKBACK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack create(){
        return itemStack;
    }
}
