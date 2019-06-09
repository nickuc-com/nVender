package nickultracraft.vender.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import nickultracraft.vender.nVender;
import nickultracraft.vender.manager.CaptchaManager;
import nickultracraft.vender.manager.Mensagens;
import nickultracraft.vender.manager.VendaCore;
import nickultracraft.vender.manager.VendaCore.VendaType;

/**
 * A class CaptchaListener.java da package (nickultracraft.vender.listeners) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/
public class CaptchaListener implements Listener {

	private String removerCores(String string) {
		string = string.replace("&", "§");
		string = string.replace("§0", "").replace("§1", "").replace("§2", "").replace("§3", "").replace("§4", "").replace("§5", "").replace("§6", "").replace("§7", "").replace("§8", "").replace("§9", "").replace("§a", "").replace("§b", "").replace("§c", "").replace("§d", "").replace("§e", "").replace("§f", "").replace("§l", "");
		return string;
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() == null) {
			return;
		}
		Player p = (Player)e.getWhoClicked();
		String n = p.getName();
		if(e.getInventory().getName().startsWith(CaptchaManager.getTitleName().replace("%item%", ""))) {
			ItemStack item = e.getCurrentItem();
			if(item != null && (item.hasItemMeta() && (item.getItemMeta().hasDisplayName()))) {
				e.setCancelled(true);
				String itemDisplay = removerCores(e.getInventory().getName().replace(CaptchaManager.getTitleName().replace("%item%", ""), ""));
				String itemAtual = removerCores(item.getItemMeta().getDisplayName());
				if(itemAtual.equalsIgnoreCase(itemDisplay)) {
					p.closeInventory();
					new VendaCore(p, VendaType.VENDA_CAPTCHA);
					if(nVender.captchaItem.containsKey(n)) {
						nVender.captchaItem.remove(n);
					}
				} else {
					p.closeInventory();
					p.sendMessage(Mensagens.CAPTCHA_INVÁLIDO);
					if(nVender.captchaItem.containsKey(n)) {
						nVender.captchaItem.remove(n);
					}
				}
			}
		}
	}
	@EventHandler
	public void onSair(PlayerQuitEvent e) {
		String n = e.getPlayer().getName();
		if(nVender.captchaItem.containsKey(n)) {
			nVender.captchaItem.remove(n);
		}
	}
}
