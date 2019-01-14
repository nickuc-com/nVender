package me.NickUltracraft.Vender.Core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui {
	
	private Player p;
	public static ArrayList<String> vendaShift = new ArrayList<>();
	public static ArrayList<String> autoVenda = new ArrayList<>();
	
	private enum VendaType {
		AUTO_VENDA, VENDA_SHIFT, VENDA;
	}
	private enum VendaStatus {
		ATIVADO, DESATIVADO, NEUTRO;
	}
	public Gui(Player p) {
		this.p = p;
		open();
	}
	private VendaStatus getVendaType(VendaType vendaType) {
		switch (vendaType) {
		case AUTO_VENDA:
			if(autoVenda.contains(p.getName())) {
				return VendaStatus.ATIVADO;
			} else {
				return VendaStatus.DESATIVADO;
			}
		case VENDA_SHIFT:
			if(vendaShift.contains(p.getName())) {
				return VendaStatus.ATIVADO;
			} else {
				return VendaStatus.DESATIVADO;
			}
		case VENDA:
			return VendaStatus.NEUTRO;
		}
		return VendaStatus.DESATIVADO;
	}
	private String getDisplayName(VendaType status) {
		switch (status) {
		case AUTO_VENDA:
			return "§7Auto-Venda";
		case VENDA:
			return "§7Vender";
		case VENDA_SHIFT:
			return "§7Venda Shift";
		}
		return "";
	}
	@SuppressWarnings("deprecation")
	private ItemStack getItemStack(VendaType vendaType, VendaStatus vendaStatus) {
		ItemStack item;
		if(vendaType == VendaType.AUTO_VENDA || (vendaType == VendaType.VENDA_SHIFT)) {
			item = new ItemStack(Material.getMaterial(351));
		} else {
			item = new ItemStack(Material.NAME_TAG);
		}
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(getDisplayName(vendaType));
		if(vendaStatus == VendaStatus.DESATIVADO) {
			item.setDurability((short)8);
		} else if(vendaStatus == VendaStatus.ATIVADO) {
			item.setDurability((short)10);
		} 
		item.setItemMeta(itemMeta);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		return item;
	}
	private void open() {
		Inventory inv = Bukkit.createInventory(null, 27, "§8Opções de Venda");
		inv.setItem(11, getItemStack(VendaType.AUTO_VENDA, getVendaType(VendaType.AUTO_VENDA)));
		inv.setItem(13, getItemStack(VendaType.VENDA, getVendaType(VendaType.VENDA)));
		inv.setItem(15, getItemStack(VendaType.VENDA_SHIFT, getVendaType(VendaType.VENDA_SHIFT)));
		p.openInventory(inv);
	}

}
