package me.NickUltracraft.Vender.Objetos;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCaptcha {
	
	private CAPTCHA_ITENS item;
	
	public static enum CAPTCHA_ITENS {
		Bloco_de_Neve, Baú, Madeira, Dinamite;
	}
	public ItemCaptcha(CAPTCHA_ITENS item) {
		this.item = item;
	}
	public static ItemCaptcha valueOf(String displayName) {
		try {
			displayName.replace("§8", "");
			CAPTCHA_ITENS item = CAPTCHA_ITENS.valueOf(displayName);
			return new ItemCaptcha(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ItemCaptcha(CAPTCHA_ITENS.Madeira);
	}
	public String getDisplayName() {
		String name = getName();
		if(name.equalsIgnoreCase("Bloco_de_Neve")) {
			name = "Bloco de Neve";
		}
		return "§7" + name;
	}
	public String getTitleName() {
		String name = getName();
		if(name.equalsIgnoreCase("Bloco_de_Neve")) {
			name = "Bloco de Neve";
		}
		return "§8" + name;
	}
	public String getName() {
		return item.name();
	}
	public Material getMaterial() {
		switch (item) {
		case Baú:
			return Material.CHEST;
		case Bloco_de_Neve:
			return Material.SNOW_BLOCK;
		case Madeira:
			return Material.WOOD;
		case Dinamite:
			return Material.TNT;
		}
		return Material.BARRIER;
	}
	public ItemStack getItemStack() {
		ItemStack item = new ItemStack(getMaterial());
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(getDisplayName());
		item.setItemMeta(itemMeta);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		return item;
	}

}
