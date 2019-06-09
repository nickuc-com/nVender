package nickultracraft.vender.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import nickultracraft.vender.nVender;

/**
 * A class nVenderItemItem.java da package (nickultracraft.vender.objects) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class nVenderItem {
	
	private int id;
	private int data;
	private double valor = 1.0;
	private int quantidade = 1;
	
	public nVenderItem(int id, int data, double valor, int quantidade) {
		this.id = id;
		this.data = data;
		this.valor = valor;
		this.quantidade = quantidade;
	}
	@SuppressWarnings("deprecation")
	public nVenderItem(ItemStack itemStack) {
		for(String item : nVender.m.getConfig().getConfigurationSection("Itens").getKeys(false)) {
			this.id = Integer.valueOf(nVender.m.getConfig().getString("Itens." + item + ".ID").split(":")[0]);
			this.data = Integer.valueOf(nVender.m.getConfig().getString("Itens." + item + ".ID").split(":")[1]);
			if(id == itemStack.getType().getId() && data == itemStack.getDurability()) {
				this.valor = nVender.m.getConfig().getDouble("Itens." + item + ".Valor");
				this.quantidade = nVender.m.getConfig().getInt("Itens." + item + ".Quantidade");
			}
		}
	}
	public static nVenderItem valueOf(ItemStack item) {
		return new nVenderItem(item);
	}
	public double getPreço() {
		return valor;
	}
	public int getID() {
		return id;
	}
	public int getData() {
		return data;
	}
	public int getQuantidade() {
		return quantidade;
	}
	@SuppressWarnings("deprecation")
	public Material getMaterial() {
		return Material.getMaterial(id);
	}

}
