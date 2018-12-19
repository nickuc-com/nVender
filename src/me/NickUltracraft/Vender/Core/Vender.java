package me.NickUltracraft.Vender.Core;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.NickUltracraft.Vender.Main;
import me.NickUltracraft.Vender.Objetos.Item;

public class Vender {
	
	private Player player;
	private int quantidade = 0;
	private double ganhos = 0;
	
	private boolean inventoryIsEmpty() {
		PlayerInventory inv = player.getInventory();
		for (ItemStack i : inv.getContents()) {
			if(i != null && !(i.getType() == Material.AIR)) {
				return false;
			}
		}
		return true;
	}
	public Vender(Player player) {
		try {
			this.player = player;
			vender();
		} catch (Exception e) {
			player.sendMessage("§cUm erro desconhecido ocorreu...");
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	private void vender() {
		if(inventoryIsEmpty()) {
			player.sendMessage(Mensagens.INVENTÁRIO_VAZIO);
			return;
		}
		if(getMaterials() == null) {
			player.sendMessage("§cErro na configuração do plugin. Contate um admin.");
			return;
		}
		for(ItemStack item : getItensInInventory()) {
			int amount = item.getAmount();
			Material material = Material.getMaterial(item.getTypeId());
			if(getMaterials().contains(material)) {
				double itemValue = Item.valueOf(item).getPreço();
				double dinheiroAdicionado = itemValue * amount;
				quantidade = quantidade + amount;
				ganhos = ganhos + dinheiroAdicionado;
				player.updateInventory();
				Main.eco.depositPlayer(player, dinheiroAdicionado);
			}
		}
		for(Material material : getMaterials()) {
			player.getInventory().remove(material);
		}
		if(quantidade == 0) {
			player.sendMessage(Mensagens.SEM_ITENS);
			return;
		}
		player.sendMessage(Mensagens.VENDIDO.replace("%itens%", String.valueOf(quantidade)).replace("%dinheiro%", String.valueOf(ganhos)));
	}
	@SuppressWarnings("deprecation")
	private ArrayList<Material> getMaterials() {
		FileConfiguration config = Main.m.getConfig();
		ArrayList<Material> itens = new ArrayList<>();
		for(String dadoPuro : config.getStringList("Itens")) {
			Item item = new Item(dadoPuro);
			Material material = Material.getMaterial(item.getID());
			itens.add(material);
		}
		return itens;
	}
	private ArrayList<ItemStack> getItensInInventory() {
		ItemStack[] contents = player.getInventory().getContents();
		ArrayList<ItemStack> itens = new ArrayList<>();
		for(ItemStack item : contents) {
			if(item != null) {
				itens.add(item);
			}
		}
		return itens;
	}
	public String formatar(String valor) {
		DecimalFormat f = new DecimalFormat("#,##0");
        return f.format(Double.valueOf(valor));
	}
	public String getGanhos() {
		return formatar(String.valueOf(ganhos));
	}
	public int getItensVendidos() {
		return quantidade;
	}

}
