package nickultracraft.vender.listeners;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import nickultracraft.vender.nVender;
import nickultracraft.vender.manager.CaptchaManager;
import nickultracraft.vender.manager.VendaCore;
import nickultracraft.vender.manager.VendaCore.VendaType;
import nickultracraft.vender.manager.VendaMenu;

/**
 * A class VendaMenuListener.java da package (nickultracraft.vender.listeners) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class VendaMenuListener implements Listener {
	
	private static ArrayList<String> delay = new ArrayList<>();
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() != null && (e.getInventory() != null)) {
			Player p = (Player)e.getWhoClicked();
			if(e.getInventory().getName().equalsIgnoreCase("§8Opções de Venda")) {
				e.setCancelled(true);
				ItemStack item = e.getCurrentItem();
				if(item != null && (item.hasItemMeta() && (item.getItemMeta().hasDisplayName()))) {
					String display = item.getItemMeta().getDisplayName();
					switch (display) {
					case "§7Auto-Venda":
						if(!p.hasPermission(nVender.permissaoAutomatico)) {
							p.sendMessage("§cVocê não tem permissão para usar a venda automática.");
							return;
						}
						if(nVender.autoVenda.contains(p.getName())) {
							p.sendMessage("§cVocê desativou o modo de venda automática.");
							nVender.autoVenda.remove(p.getName());
							p.closeInventory();
							new VendaMenu(p);
						} else {
							p.sendMessage("§aVocê ativou o modo de venda automática.");
							nVender.autoVenda.add(p.getName());
							double delayDouble = nVender.m.getConfig().getDouble("Config.DelayAutoVenda");
							if(delayDouble < 0.5) {
								delayDouble = 2.5;
							}
							long timeLong = (long) (1000 * delayDouble);
							Timer timer = new Timer(true);
							timer.scheduleAtFixedRate(new TimerTask() {
								@Override
								public void run() {
									if(nVender.autoVenda.contains(p.getName())) {
										new VendaCore(p, VendaType.AUTO_VENDA);
									} else {
										cancel();
									}
								}
							}, timeLong, timeLong);
							p.closeInventory();
							new VendaMenu(p);
						}
						return;
					case "§7Vender":
						new VendaCore(p, VendaType.VENDA_NORMAL);
						return;
					case "§7Venda Shift":
						if(!p.hasPermission(nVender.permissaoShift)) {
							p.sendMessage("§cVocê não tem permissão para usar a venda por shift.");
							return;
						}
						if(nVender.vendaShift.contains(p.getName())) {
							p.sendMessage("§cVocê desativou o modo de venda por shift.");
							nVender.vendaShift.remove(p.getName());
							p.closeInventory();
							new VendaMenu(p);
						} else {
							p.sendMessage("§aVocê ativou o modo de venda por shift.");
							nVender.vendaShift.add(p.getName());
							p.closeInventory();
							new VendaMenu(p);
						}
						return;
					}
				}
			}
		}
	}
	@EventHandler
	public void onShift(PlayerToggleSneakEvent e) {
		if(nVender.vendaShift.contains(e.getPlayer().getName())) {
			double delayDouble = nVender.m.getConfig().getDouble("Config.DelayShift");
			if(delayDouble <= 0) {
				if(nVender.m.getConfig().getBoolean("Config.Captcha")) {
					new CaptchaManager(e.getPlayer());
				} else {
					new VendaCore(e.getPlayer(), VendaType.VENDA_SHIFT);
				}
			} else {
				long timeLong = (long) (20 * delayDouble);
				if(!delay.contains(e.getPlayer().getName())) {
					if(nVender.m.getConfig().getBoolean("Config.Captcha")) {
						new CaptchaManager(e.getPlayer());
					} else {
						new VendaCore(e.getPlayer(), VendaType.VENDA_SHIFT);
					}
					delay.add(e.getPlayer().getName());
					new BukkitRunnable() {
						@Override
						public void run() {
							delay.remove(e.getPlayer().getName());
							cancel();
						}
					}.runTaskLater(nVender.m, timeLong);
				} 
			}
		}
	}
	@EventHandler
	public void onSair(PlayerQuitEvent e) {
		if(nVender.vendaShift.contains(e.getPlayer().getName())) { 
			nVender.vendaShift.remove(e.getPlayer().getName());
		}
		if(nVender.autoVenda.contains(e.getPlayer().getName())) { 
			nVender.autoVenda.remove(e.getPlayer().getName());
		}
	}
}
