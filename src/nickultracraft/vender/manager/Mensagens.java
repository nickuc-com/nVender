package nickultracraft.vender.manager;

import org.bukkit.configuration.file.FileConfiguration;

import nickultracraft.vender.nVender;

/**
 * A class Mensagens.java da package (nickultracraft.vender.handler) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class Mensagens {
	
	public Mensagens() {
		FileConfiguration config = nVender.m.getConfig();
		if(loadMessage(config, "Sem-Permissao") != null) {
			Mensagens.SEM_PERMISSÃO = config.getString("Mensagens.Sem-Permissao").replace("&", "§");
		}
		if(loadMessage(config, "Configuracao-Recarregada") != null) {
			Mensagens.CONFIGURAÇÃO_RECARREGADA = config.getString("Mensagens.Configuracao-Recarregada").replace("&", "§");
		}
		if(loadMessage(config, "Captcha-Invalido") != null) {
			Mensagens.CAPTCHA_INVÁLIDO = config.getString("Mensagens.Captcha-Invalido").replace("&", "§");
		}
		if(loadMessage(config, "Vendido-Sucesso") != null) {
			Mensagens.VENDIDO = config.getString("Mensagens.Vendido-Sucesso").replace("&", "§");
		}
		if(loadMessage(config, "Sem-Itens") != null) {
			Mensagens.SEM_ITENS = config.getString("Mensagens.Sem-Itens").replace("&", "§");
		}
		if(loadMessage(config, "Inventario-Vazio") != null) {
			Mensagens.INVENTÓRIO_VAZIO = config.getString("Mensagens.Inventario-Vazio").replace("&", "§");
		}
	}
	private String loadMessage(FileConfiguration config, String path) {
		if(config.isSet("Mensagens." + path)) {
			return config.getString("Mensagens." + path);
		}
		return null;
	}
	public static String SEM_PERMISSÃO = "§cVocê não tem permissão para executar este comando.";
	public static String CONFIGURAÇÃO_RECARREGADA = "§aConfiguração e arquivos de linguagem foram recarregados.";
	public static String CAPTCHA_INVÁLIDO = "§cO captcha inserido é inválido.";
	public static String VENDIDO = "§aVocê vendeu §7%itens% §aitens por §7$%dinheiro%§a.";
	public static String SEM_ITENS = "§cVocê não possui itens para serem vendidos.";
	public static String INVENTÓRIO_VAZIO = "§cVocê não pode estar com um inventário vazio.";
}
