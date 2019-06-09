package nickultracraft.vender.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import nickultracraft.vender.nVender;
import nickultracraft.vender.api.ConsoleLogger;
import nickultracraft.vender.objects.nVenderItem;

/**
 * A class Vender.java da package (nickultracraft.vender.handler) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class VendaCore {
	
	private Player player;
	private VendaType vendaType;
	private int quantidade = 0;
	private double ganhos = 0;
	
	public enum VendaType {
		
		AUTO_VENDA("Venda automática"), VENDA_SHIFT("Venda por Shift"), VENDA_NORMAL("Venda"), VENDA_CAPTCHA("Venda por Captcha");
		
		private String nome;
		
		VendaType(String nome) {
			this.nome = nome;
		}
		public String getNome() {
			return nome;
		}
		
	}
	public static enum MultiplicadorMethodType {
		
		DETECCAO_GRUPO("Detecção por Grupo", "grupo"), DETECCAO_PERMISSAO("Detecção por Permissão", "permissao"), NENHUM("Nenhum", "nenhum");
		
		private String nome;
		private String config;
		
		MultiplicadorMethodType(String nome, String config) {
			this.nome = nome;
			this.config = config;
		}
		public String getNome() {
			return nome;
		}
		public String getConfigNome() {
			return config;
		}
		public static MultiplicadorMethodType getByConfig(String config) {
			if(nVender.permission == null) {
				return DETECCAO_PERMISSAO;
			}
			for(MultiplicadorMethodType multiplicadorMethodType : MultiplicadorMethodType.values()) {
				if(multiplicadorMethodType.getConfigNome().equals(config.toLowerCase())) return multiplicadorMethodType;
			}
			if(nVender.permission == null) {
				return MultiplicadorMethodType.DETECCAO_GRUPO;
			} else {
				return MultiplicadorMethodType.DETECCAO_PERMISSAO;
			}
		}
		
	}
	
	private boolean inventoryIsEmpty() {
		PlayerInventory inv = player.getInventory();
		for (ItemStack i : inv.getContents()) {
			if(i != null && !(i.getType() == Material.AIR)) {
				return false;
			}
		}
		return true;
	}
	public VendaCore(Player player, VendaType vendaType) {
		try {
			this.player = player;
			this.vendaType = vendaType;
			vender();
		} catch (Exception e) {
			player.sendMessage("§cUm erro desconhecido ocorreu...");
			e.printStackTrace();
		}
	}
	private double calcularMultiplicador(double preçoOriginal) {
		if(nVender.multiplicadorType == MultiplicadorMethodType.DETECCAO_GRUPO) {
			for(String multiplicador : nVender.multiplicadores) {
				boolean inGroup = nVender.permission.playerInGroup(player, multiplicador.split("-")[0]);
				if(inGroup) {
					double calc = Double.valueOf(multiplicador.split("-")[2])/100; 
					ConsoleLogger.debug(" [Multiplicador] The price original is: " + preçoOriginal + ". The final is: " + preçoOriginal*calc + ". The multiplicator is " + multiplicador.split("-")[2] + "X for group " + multiplicador.split("-")[0]);
					return preçoOriginal*calc;
				}
			}
		} else if(nVender.multiplicadorType == MultiplicadorMethodType.DETECCAO_PERMISSAO) {
			for(String multiplicador : nVender.multiplicadores) {
				boolean hasperm = player.hasPermission(multiplicador.split("-")[1]);
				if(hasperm) {
					double calc = Double.valueOf(multiplicador.split("-")[2])/100; 
					ConsoleLogger.debug(" [Multiplicador] The price original is: " + preçoOriginal + ". The final is: " + preçoOriginal*calc + ". The multiplicator is " + multiplicador.split("-")[2] + "X  for group " + multiplicador.split("-")[0]);
					return preçoOriginal*calc;
				}
			}
		}
		return preçoOriginal;
	}
	private void vender() {
		if(inventoryIsEmpty()) {
			if(vendaType == VendaType.VENDA_NORMAL || (vendaType == VendaType.VENDA_CAPTCHA)) {
				player.sendMessage(Mensagens.INVENTÓRIO_VAZIO);
			}
			return;
		}
		if(nVender.loadedItens == null || nVender.loadedItens.size() == 0) {
			player.sendMessage("§cAinda não existem itens para serem vendidos.");
			return;
		}
		for(ItemStack item : getItensInInventory()) {
			int amount = item.getAmount();
			
			for(ItemStack itemStackConfig : nVender.loadedItens) {
				if(item.getType() == itemStackConfig.getType() && (item.getDurability() == itemStackConfig.getDurability())) {
					ConsoleLogger.debug(" [Player Inventory] The player contains " + item.getType().name() + ".");
					nVenderItem nvenderitem = nVenderItem.valueOf(item);
					double itemValue = nvenderitem.getPreço();
					double dinheiroAdicionado = itemValue * amount;
					dinheiroAdicionado = dinheiroAdicionado/nvenderitem.getQuantidade();
					quantidade = quantidade + amount;
					ganhos = ganhos + calcularMultiplicador(dinheiroAdicionado);
					player.updateInventory();
					nVender.eco.depositPlayer(player, calcularMultiplicador(dinheiroAdicionado));
				}	
			}
		}
		ConsoleLogger.debug(" [Player Inventory] Removing itens after sell...");
		for(ItemStack item : getItensInInventory()) {
			for(ItemStack itemStack : nVender.loadedItens) {
				if(item.getType() == itemStack.getType() && (item.getDurability() == itemStack.getDurability())) {
					for(int i = 0; i < quantidade; i++) {
						player.getInventory().removeItem(item);
					}
					player.updateInventory();
				}
			}
		}
		if(quantidade == 0) {
			if(vendaType == VendaType.VENDA_NORMAL || (vendaType == VendaType.VENDA_CAPTCHA)) { 
				player.sendMessage(Mensagens.SEM_ITENS);
			}
			return;
		}
		player.sendMessage(Mensagens.VENDIDO.replace("%itens%", String.valueOf(quantidade)).replace("%dinheiro%", String.valueOf(ganhos)));
	}
	private ArrayList<ItemStack> getItensInInventory() {
		ConsoleLogger.debug("Loading itens in " + player.getName() + " inventory...");
		ItemStack[] contents = player.getInventory().getContents();
		ArrayList<ItemStack> itens = new ArrayList<>();
		for(ItemStack item : contents) {
			if(item != null) {
				itens.add(item);
				ConsoleLogger.debug(" [Player Inventory] " + item.getType().name() + " X" + item.getAmount());
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
