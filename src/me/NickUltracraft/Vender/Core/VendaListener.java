package me.NickUltracraft.Vender.Core;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.NickUltracraft.Vender.Main;

public class VendaListener implements Listener {
	
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
						if(!p.hasPermission("nvender.autovenda")) {
							p.sendMessage("§cVocê não tem permissão para usar a venda automática.");
							return;
						}
						if(Gui.autoVenda.contains(p.getName())) {
							p.sendMessage("§cVocê desativou o modo de venda automática.");
							Gui.autoVenda.remove(p.getName());
							p.closeInventory();
							new Gui(p);
						} else {
							p.sendMessage("§aVocê ativou o modo de venda automática.");
							Gui.autoVenda.add(p.getName());
							Timer timer = new Timer(true);
							timer.scheduleAtFixedRate(new TimerTask() {
								@Override
								public void run() {
									if(Gui.autoVenda.contains(p.getName())) {
										new Vender(p);
									} else {
										cancel();
									}
								}
							}, 1000*5, 1000*5);
							p.closeInventory();
							new Gui(p);
						}
						return;
					case "§7Vender":
						new Vender(p);
						return;
					case "§7Venda Shift":
						if(!p.hasPermission("nvender.vendashift")) {
							p.sendMessage("§cVocê não tem permissão para usar a venda por shift.");
							return;
						}
						if(Gui.vendaShift.contains(p.getName())) {
							p.sendMessage("§cVocê desativou o modo de venda por shift.");
							Gui.vendaShift.remove(p.getName());
							p.closeInventory();
							new Gui(p);
						} else {
							p.sendMessage("§aVocê ativou o modo de venda por shift.");
							Gui.vendaShift.add(p.getName());
							p.closeInventory();
							new Gui(p);
						}
						return;
					}
				}
			}
		}
	}
	@EventHandler
	public void onShift(PlayerToggleSneakEvent e) {
		if(Gui.vendaShift.contains(e.getPlayer().getName())) {
			if(!delay.contains(e.getPlayer().getName())) {
				new Vender(e.getPlayer());
				delay.add(e.getPlayer().getName());
				new BukkitRunnable() {
					@Override
					public void run() {
						delay.remove(e.getPlayer().getName());
						cancel();
					}
				}.runTaskLater(Main.m, 20);
			} 
		}
	}
}
