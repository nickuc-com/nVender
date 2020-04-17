/**
 * Copyright NickUC
 * -
 * Esta class pertence ao projeto de NickUC
 * Mais informações: https://nickuc.com
 * -
 * É expressamente proibido alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link para acesso da source original.
 * -
 * Este aviso não pode ser removido ou alterado de qualquer distribuição de origem.
 */

package com.nickuc.vender.ncore.lite.itemstack;

import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {

    private static final Builder builder = new Builder();

    private final Material material;
    private final String displayName;
    @Setter private int durability;

    public Item(Material material, String displayName, int durability) {
        this.material = material;
        this.displayName = displayName;
        this.durability = durability;
    }

    public ItemStack createItem() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        itemStack.setDurability((short) durability);
        return itemStack;
    }

    public static Builder builder() {
        return builder;
    }

    public static class Builder {

        private Material material;
        private String displayName;
        private int durability;

        public Material material() {
            return material;
        }

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public String displayName() {
            return displayName;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public int durability() {
            return durability;
        }

        public Builder durability(int durability) {
            this.durability = durability;
            return this;
        }

        public Item build() {
            return new Item(material, displayName, durability);
        }
    }

}
