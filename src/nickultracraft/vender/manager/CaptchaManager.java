package nickultracraft.vender.manager; 

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import nickultracraft.vender.nVender;
import nickultracraft.vender.objects.CaptchaItemConfig;

/**
 * A class CaptchaManager.java da package (nickultracraft.vender.captcha) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class CaptchaManager {
	
	public enum CaptchaItens {
		ITEM1, ITEM2, ITEM3, ITEM4;
	}
	private Player player;
	private String name;
	private CaptchaItens[] captchaItens = CaptchaItens.values();
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
		CaptchaItens itemGenerate = captchaItens[random.nextInt(captchaItens.length)];
		nVender.captchaItem.put(name, new CaptchaItemConfig(itemGenerate.name()));
	}
	public CaptchaItemConfig getItem() {
		return nVender.captchaItem.get(name);
	}
	public static String getTitleName() {
		return nVender.m.getConfig().getString("Config.Title-Name").replace("&", "§");
	}
 	private void open() {
		Inventory inv = Bukkit.createInventory(null, 27, getTitleName().replace("%item%", getItem().getName()));
		inv.setItem(10, new CaptchaItemConfig("ITEM1").getItemStack());
		inv.setItem(12, new CaptchaItemConfig("ITEM2").getItemStack());
		inv.setItem(14, new CaptchaItemConfig("ITEM3").getItemStack());
		inv.setItem(16, new CaptchaItemConfig("ITEM4").getItemStack());
		player.openInventory(inv);
	}

}
