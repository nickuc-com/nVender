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

package com.nickuc.vender.manager;

import com.nickuc.vender.nVender;
import com.nickuc.vender.objects.SellItem;
import com.nickuc.vender.settings.Settings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class VendaCore {
	
	private Player player;
	private Type vendaType;
	private nVender nvender;
	@Getter private int quantidade;
	private double ganhos;

	public VendaCore(Player player, Type vendaType, nVender nvender) {
		try {
			this.player = player;
			this.vendaType = vendaType;
			this.nvender = nvender;
			if (Settings.RUN_SELL_ASYNC.getBoolean()) {
				nvender.getServer().getScheduler().runTaskAsynchronously(nvender, this::performSell);
			} else {
				nvender.getServer().getScheduler().runTask(nvender, this::performSell);
			}
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage("§cUm erro desconhecido ocorreu...");
		}
	}

	public void performSell() {
		if (checkInventoryIsEmpty()) {
			if (vendaType == Type.VENDA_NORMAL) player.sendMessage(Settings.Messages.INVENTORY_EMPTY.getMessage());
			return;
		}
		if (Settings.loadedItens == null || Settings.loadedItens.size() == 0) {
			player.sendMessage("§cAinda não existem itens configurados para serem vendidos.");
			return;
		}
		for (ItemStack item : getItensInInventory()) {
			int amount = item.getAmount();
			for (ItemStack itemStackConfig : Settings.loadedItens) {
				if (item.getType() == itemStackConfig.getType() && (item.getDurability() == itemStackConfig.getDurability())) {
					SellItem nvenderitem = SellItem.valueOf(nvender, item);
					double itemValue = nvenderitem.getPrice();
					double dinheiroAdicionado = itemValue * amount;
					dinheiroAdicionado = dinheiroAdicionado/nvenderitem.getQuantidade();
					quantidade = quantidade + amount;
					ganhos = ganhos + processMultiplicador(dinheiroAdicionado);
					player.updateInventory();
					nVender.economy.depositPlayer(player, processMultiplicador(dinheiroAdicionado));
				}
			}
		}
		for (ItemStack item : getItensInInventory()) {
			for (ItemStack itemStack : Settings.loadedItens) {
				if (item.getType() == itemStack.getType() && (item.getDurability() == itemStack.getDurability())) {
					for (int i = 0; i < quantidade; i++) {
						player.getInventory().removeItem(item);
					}
					player.updateInventory();
				}
			}
		}
		if (quantidade == 0) {
			if (vendaType == Type.VENDA_NORMAL) {
				player.sendMessage(Settings.Messages.NO_ITENS.getMessage());
			}
			return;
		}
		player.sendMessage(Settings.Messages.VENDIDO.getMessage().replace("%itens%", String.valueOf(quantidade)).replace("%dinheiro%", String.valueOf(ganhos)));
	}

	private boolean checkInventoryIsEmpty() {
		PlayerInventory inv = player.getInventory();
		for (ItemStack i : inv.getContents()) {
			if (i != null && !(i.getType() == Material.AIR)) {
				return false;
			}
		}
		return true;
	}

	private double processMultiplicador(double valorOriginal) {
		switch (Settings.getMultiplicadorType()) {

			case DETECCAO_GRUPO:
				for (String multiplicador : Settings.multiplicadores) {
					boolean inGroup = nVender.permission.playerInGroup(player, multiplicador.split("-")[0]);
					if (inGroup) {
						double calc = Double.parseDouble(multiplicador.split("-")[2])/100;
						return valorOriginal*calc;
					}
				}
				break;
			case DETECCAO_PERMISSAO:
				for (String multiplicador : Settings.multiplicadores) {
					boolean hasperm = player.hasPermission(multiplicador.split("-")[1]);
					if (hasperm) {
						double calc = Double.parseDouble(multiplicador.split("-")[2])/100;
						return valorOriginal*calc;
					}
				}
				break;
		}
		return valorOriginal;
	}

	private ArrayList<ItemStack> getItensInInventory() {
		ItemStack[] contents = player.getInventory().getContents();
		ArrayList<ItemStack> itens = new ArrayList<>();
		for (ItemStack item : contents) {
			if (item != null) {
				itens.add(item);
			}
		}
		return itens;
	}

	public String getGanhos() {
		return new DecimalFormat("#,##0").format(ganhos);
	}

	@AllArgsConstructor @Getter
	public enum Type {
		AUTO_VENDA, VENDA_SHIFT, VENDA_NORMAL
	}

	@AllArgsConstructor @Getter
	public enum MultiplicadorType {

		DETECCAO_GRUPO("Detecção por Grupo", "grupo"), DETECCAO_PERMISSAO("Detecção por Permissão", "permissao"), NENHUM("Nenhum", "nenhum");

		private final String nome;
		private final String configNome;

		public static MultiplicadorType getByConfig(String config) {
			if (config == null || nVender.permission == null) return DETECCAO_PERMISSAO;

			for (MultiplicadorType multiplicadorMethodType : MultiplicadorType.values()) {
				if (multiplicadorMethodType.configNome.equals(config.toLowerCase())) return multiplicadorMethodType;
			}
			return nVender.permission != null && nVender.permission.isEnabled() ?  MultiplicadorType.DETECCAO_GRUPO :  MultiplicadorType.DETECCAO_PERMISSAO;
		}

	}

}
