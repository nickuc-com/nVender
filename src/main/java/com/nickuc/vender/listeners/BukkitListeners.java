/**
 * Copyright NickUC
 * -
 * Esta class pertence ao projeto de NickUC
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.com
 * -
 * É expressamente proibido alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link para acesso da source original.
 * -
 * Este aviso não pode ser removido ou alterado de qualquer distribuição de origem.
 */

package com.nickuc.vender.listeners;

import com.nickuc.ncore.api.plugin.bukkit.listener.Listener;
import com.nickuc.ncore.api.settings.Settings;
import com.nickuc.vender.manager.VendaCore;
import com.nickuc.vender.manager.VendaMenu;
import com.nickuc.vender.nVender;
import com.nickuc.vender.settings.SettingsEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BukkitListeners extends Listener<nVender> {
	
	private static final ArrayList<String> delay = new ArrayList<>();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		String invName = e.getView().getTitle();
		if (e.getView() != null && invName != null && invName.equalsIgnoreCase("§8Opções de Venda")) {
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null && (item.hasItemMeta() && (item.getItemMeta().hasDisplayName()))) {
				Player p = (Player)e.getWhoClicked();
				String display = item.getItemMeta().getDisplayName();
				switch (display) {
					case "§7Auto-Venda":
						if (!p.hasPermission(Settings.getString(SettingsEnum.PERMISSION_AUTOMATICO))) {
							p.sendMessage("§cVocê não tem permissão para usar a venda automática.");
							return;
						}
						if (SettingsEnum.autoVenda.contains(p.getName())) {
							p.sendMessage("§cVocê desativou o modo de venda automática.");
							SettingsEnum.autoVenda.remove(p.getName());
						} else {
							p.sendMessage("§aVocê ativou o modo de venda automática.");
							SettingsEnum.autoVenda.add(p.getName());
							double delayDouble = plugin.getConfig().getDouble("Config.DelayAutoVenda");
							if (delayDouble < 0.5) {
								delayDouble = 2.5;
							}
							long timeLong = (long) (1000 * delayDouble);

							Timer timer = new Timer(true);
							timer.scheduleAtFixedRate(new TimerTask() {
								@Override
								public void run() {
									if (SettingsEnum.autoVenda.contains(p.getName())) {
										new VendaCore(p, VendaCore.Type.AUTO_VENDA, plugin);
									} else {
										cancel();
									}
								}
							}, timeLong, timeLong);
						}
						p.closeInventory();
						VendaMenu.openMenu(p);
						return;
					case "§7Vender":
						new VendaCore(p, VendaCore.Type.VENDA_NORMAL, plugin);
						return;
					case "§7Venda Shift":
						if (!p.hasPermission(Settings.getString(SettingsEnum.PERMISSION_SHIFT))) {
							p.sendMessage("§cVocê não tem permissão para usar a venda por shift.");
							return;
						}
						if (SettingsEnum.vendaShift.contains(p.getName())) {
							p.sendMessage("§cVocê desativou o modo de venda por shift.");
							SettingsEnum.vendaShift.remove(p.getName());
						} else {
							p.sendMessage("§aVocê ativou o modo de venda por shift.");
							SettingsEnum.vendaShift.add(p.getName());
						}
						p.closeInventory();
						VendaMenu.openMenu(p);
				}
			}
		}
	}
	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
		if (SettingsEnum.vendaShift.contains(e.getPlayer().getName())) {
			double delayDouble = plugin.getConfig().getDouble("Config.DelayShift");
			if (delayDouble <= 0) {
				new VendaCore(e.getPlayer(), VendaCore.Type.VENDA_SHIFT, plugin);
			} else {
				long timeLong = (long) (20 * delayDouble);
				if (!delay.contains(e.getPlayer().getName())) {
					new VendaCore(e.getPlayer(), VendaCore.Type.VENDA_SHIFT, plugin);
					delay.add(e.getPlayer().getName());
					plugin.runTaskLater(false, () -> delay.remove(e.getPlayer().getName()), timeLong, TimeUnit.SECONDS);
				} 
			}
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		SettingsEnum.vendaShift.remove(e.getPlayer().getName());
		SettingsEnum.autoVenda.remove(e.getPlayer().getName());
	}
}
