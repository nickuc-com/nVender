package me.NickUltracraft.Vender.Captcha; 

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.NickUltracraft.Vender.Main;
import me.NickUltracraft.Vender.Cache.Players;
import me.NickUltracraft.Vender.Objetos.ItemCaptcha;
import me.NickUltracraft.Vender.Objetos.ItemCaptcha.CAPTCHA_ITENS;

public class CaptchaManager {
	
	private Player player;
	private String name;
	private ItemCaptcha.CAPTCHA_ITENS[] captchaItens = ItemCaptcha.CAPTCHA_ITENS.values();
	private Random random = new Random();

	public CaptchaManager(Player player) {
		this.player = player;
		this.name = player.getName();
		generateItem();
		open();
	}
	public CaptchaManager(Player player, boolean autoGenerate) {
		this.player = player;
		this.name = player.getName();
		if(autoGenerate) {
			generateItem();
		}
		open();
	}
	private void generateItem() {
		CAPTCHA_ITENS itemGenerate = captchaItens[random.nextInt(captchaItens.length)];
		Players.captchaItem.put(name, new ItemCaptcha(itemGenerate));
	}
	public ItemCaptcha getItem() {
		return Players.captchaItem.get(name);
	}
	public static String getTitleName() {
		return Main.m.getConfig().getString("Config.Title-Name").replace("&", "§");
	}
 	private void open() {
		Inventory inv = Bukkit.createInventory(null, 27, getTitleName().replace("%item%", getItem().getName()));
		inv.setItem(10, new ItemCaptcha(CAPTCHA_ITENS.Baú).getItemStack());
		inv.setItem(12, new ItemCaptcha(CAPTCHA_ITENS.Bloco_de_Neve).getItemStack());
		inv.setItem(14, new ItemCaptcha(CAPTCHA_ITENS.Madeira).getItemStack());
		inv.setItem(16, new ItemCaptcha(CAPTCHA_ITENS.Dinamite).getItemStack());
		player.openInventory(inv);
	}

}
