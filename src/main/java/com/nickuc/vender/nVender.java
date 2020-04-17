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

package com.nickuc.vender;

import com.nickuc.vender.commands.VenderCommand;
import com.nickuc.vender.listeners.BukkitListeners;
import com.nickuc.vender.ncore.lite.logger.ConsoleLogger;
import com.nickuc.vender.objects.SellItem;
import com.nickuc.vender.settings.Settings;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class nVender extends JavaPlugin {

	public static Economy economy;
	public static Permission permission;

	@Override
	public void onEnable() {

		ConsoleLogger.init(this);
		
		/**
		 * Plugin startup
		 */
		String c = "§a";
		ConsoleLogger.info(c+"                          _           ");
		ConsoleLogger.info(c+" _ __/\\   /\\___ _ __   __| | ___ _ __ ");
		ConsoleLogger.info(c+"| '_ \\ \\ / / _ \\ '_ \\ / _` |/ _ \\ '__|");
		ConsoleLogger.info(c+"| | | \\ V /  __/ | | | (_| |  __/ |   ");
		ConsoleLogger.info(c+"|_| |_|\\_/ \\___|_| |_|\\__,_|\\___|_|   ");
		ConsoleLogger.info(c+"                                      ");
		ConsoleLogger.info(c+" By: www.nickuc.com - V " + getDescription().getVersion() + " RELEASE version");
		ConsoleLogger.info("");
		ConsoleLogger.info(c+"Inicializando tarefas para inicialização...");

		try {
			if (!config()) {
				ConsoleLogger.criticalwarning("POR FAVOR, LEIA ANTES DE REPORTAR COMO UM BUG:");
				ConsoleLogger.criticalwarning("Existe um erro em seu arquivo de configuração. Copie e cole a 'config.yml' e verifique o erro no site: https://onlineyamltools.com/validate-yaml");
				ConsoleLogger.criticalwarning("O nVender será desativado automaticamente.");
				Thread.sleep(5000);
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConsoleLogger.criticalwarning("POR FAVOR, LEIA ANTES DE REPORTAR COMO UM BUG:");
			ConsoleLogger.criticalwarning("Existe um erro em seu arquivo de configuração. Copie e cole a 'config.yml' e verifique o erro no site: https://onlineyamltools.com/validate-yaml");
			ConsoleLogger.criticalwarning("O nVender será desativado automaticamente.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		/**
		 * Register commands
		 */
		getCommand("vender").setExecutor(new VenderCommand(this));

		/**
		 * Register listeners
		 */
		getServer().getPluginManager().registerEvents(new BukkitListeners(this), this);

		/**
		 * Vault hook service
		 */
		if (!setupEconomy()) {
			ConsoleLogger.criticalwarning("Não foi possivel dar hook no Vault. Desligando plugin...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		ConsoleLogger.info("Hooked with Vault (Economy Plugin: " + economy.getName() + ")");
		ConsoleLogger.info(setupPermissions() ? "Hooked with Vault (Permission Plugin : " + permission.getName() + ")" : "Não foi encontrado nenhum plugin de gestão de permissões. Usando multiplicador por permissões do Bukkit...");
	}

	private boolean setupEconomy() {
		PluginManager pm = Bukkit.getPluginManager();
		if(pm.getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		    if (economyProvider != null) {
		    	economy = economyProvider.getProvider();
		    }
		    return economy != null;
		}
		return false;
	}

	private boolean setupPermissions() {
		PluginManager pm = Bukkit.getPluginManager();
		if(pm.getPlugin("Vault") != null) {
			RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		    if (permissionProvider != null) {
		    	permission = permissionProvider.getProvider();
		    }
		    return permission != null;
		}
		return false;
	}

	public boolean config() throws Exception {
		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists())  {
			saveDefaultConfig();
		}
		reloadConfig();
		FileConfiguration config = getConfig();

		/**
		 * Load settings
		 */
		Settings.reload(config);

		/**
		 * Loads itens & multiplicadores
		 */
		ConsoleLogger.info("Foram carregados " + loadItens() + " item(s) da config.");
		ConsoleLogger.info("Foram carregados " + loadMultiplicadores() + " multiplicador(es) da config.");
		return config.isSet("Config.debug-mode");
	}

	private int loadItens() {
		FileConfiguration config = getConfig();
		ArrayList<ItemStack> itens = new ArrayList<>();
		for(String itemConfig : config.getConfigurationSection("Itens").getKeys(false)) {
			int id = Integer.parseInt(config.getString("Itens." + itemConfig + ".ID").split(":")[0]);
			int data = Integer.parseInt(config.getString("Itens." + itemConfig + ".ID").split(":")[1]);
			double valor = config.getDouble("Itens." + itemConfig + ".Valor");
			int quantidade = config.getInt("Itens." + itemConfig + ".Quantidade");

			SellItem item = new SellItem(id, data, valor, quantidade);
			@SuppressWarnings("deprecation")
			Material material = Material.getMaterial(item.getId());
			ItemStack itemStack = new ItemStack(material);
			itemStack.setDurability((short) item.getData());
			itens.add(itemStack);
		}
		Settings.loadedItens = itens;
		return itens.size();
	}

	private int loadMultiplicadores() {
		FileConfiguration config = getConfig();
		List<String> multiplicadorCache = new ArrayList<>();
		for(String grupo : config.getConfigurationSection("Multiplicador").getKeys(false)) {
			if(!grupo.equalsIgnoreCase("Tipo")) {
				String grupoPex = config.getString("Multiplicador." + grupo + ".Grupo");
				String permissao = config.getString("Multiplicador." + grupo + ".Permissao");
				String multiplicador = config.getString("Multiplicador." + grupo + ".Multiplicador");
				multiplicadorCache.add(grupoPex + "-" + permissao + "-" + multiplicador.split("%")[0]);
			}
		}
		Settings.multiplicadores = multiplicadorCache;
		return multiplicadorCache.size();
	}

}
