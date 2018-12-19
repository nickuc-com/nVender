package me.NickUltracraft.Vender.Captcha;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.NickUltracraft.Vender.Cache.Players;
import me.NickUltracraft.Vender.Core.Mensagens;
import me.NickUltracraft.Vender.Core.Vender;

public class CaptchaListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() == null) {
			return;
		}
		Player p = (Player)e.getWhoClicked();
		String n = p.getName();
		if(e.getInventory().getName().startsWith("§8VENDA Clique no(a)")) {
			ItemStack item = e.getCurrentItem();
			if(item != null && (item.hasItemMeta() && (item.getItemMeta().hasDisplayName()))) {
				e.setCancelled(true);
				String itemDisplay = e.getInventory().getName().replace("§8VENDA Clique no(a) ", "").replace("§8", "");
				String itemAtual = item.getItemMeta().getDisplayName().replace("§7", "");
				if(itemAtual.equalsIgnoreCase(itemDisplay)) {
					p.closeInventory();
					new Vender(p);
					if(Players.captchaItem.containsKey(n)) {
						Players.captchaItem.remove(n);
					}
				} else {
					p.closeInventory();
					p.sendMessage(Mensagens.CAPTCHA_INVÁLIDO);
					if(Players.captchaItem.containsKey(n)) {
						Players.captchaItem.remove(n);
					}
				}
			}
		}
	}
	@EventHandler
	public void onSair(PlayerQuitEvent e) {
		String n = e.getPlayer().getName();
		if(Players.captchaItem.containsKey(n)) {
			Players.captchaItem.remove(n);
		}
	}
}
