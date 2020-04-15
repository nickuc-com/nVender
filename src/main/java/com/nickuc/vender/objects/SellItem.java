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

package com.nickuc.vender.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.nickuc.vender.nVender;

@AllArgsConstructor @Getter
public class SellItem {
	
	private int id;
	private int data;
	private double price = 1.0;
	private int quantidade = 1;

	@SuppressWarnings("deprecation")
	public SellItem(nVender nvender, ItemStack itemStack) {
		for(String item : nvender.getConfig().getConfigurationSection("Itens").getKeys(false)) {
			this.id = Integer.parseInt(nvender.getConfig().getString("Itens." + item + ".ID").split(":")[0]);
			this.data = Integer.parseInt(nvender.getConfig().getString("Itens." + item + ".ID").split(":")[1]);
			if(id == itemStack.getType().getId() && data == itemStack.getDurability()) {
				this.price = nvender.getConfig().getDouble("Itens." + item + ".Valor");
				this.quantidade = nvender.getConfig().getInt("Itens." + item + ".Quantidade");
			}
		}
	}

	public static SellItem valueOf(nVender nvender, ItemStack item) {
		return new SellItem(nvender, item);
	}

	@SuppressWarnings("deprecation")
	public Material getMaterial() {
		return Material.getMaterial(id);
	}

}
