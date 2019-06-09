package nickultracraft.vender.objects;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nickultracraft.vender.nVender;

/**
 * A class CaptchaItemConfig.java da package (nickultracraft.vender.objects) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class CaptchaItemConfig {
	
	private String name;
	private int id;
	private short durability;
	
	public CaptchaItemConfig(String item) {
		FileConfiguration config = nVender.m.getConfig();
		this.name = config.getString("Captcha." + item + ".Nome");
		this.id = config.getInt("Captcha." + item + ".ItemID");
		this.durability = (short)config.getInt("Captcha." + item + ".ItemData");
	}
	@SuppressWarnings("deprecation")
	public Material getMaterial() {
		return Material.getMaterial(id);
	}
	public String getName() {
		return name;
	}
	public short getDurability() {
		return durability;
	}
	@SuppressWarnings("deprecation")
	public ItemStack getItemStack() {
		ItemStack item = new ItemStack(id);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§7" + name);
		item.setDurability(durability);
		item.setItemMeta(itemMeta);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		return item;
	}

}
