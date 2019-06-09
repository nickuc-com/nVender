package nickultracraft.vender;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import nickultracraft.vender.api.ConsoleLogger;
import nickultracraft.vender.api.Metrics;
import nickultracraft.vender.api.UpdaterAPI;
import nickultracraft.vender.commands.VenderCommand;
import nickultracraft.vender.listeners.CaptchaListener;
import nickultracraft.vender.listeners.VendaMenuListener;
import nickultracraft.vender.manager.Mensagens;
import nickultracraft.vender.manager.VendaCore.MultiplicadorMethodType;
import nickultracraft.vender.objects.CaptchaItemConfig;
import nickultracraft.vender.objects.nVenderItem;

public class nVender extends JavaPlugin {
	
	public static boolean isDebug = false;
	
	public static nVender m;
	public static UpdaterAPI updaterAPI; 
	public static Economy eco;
	public static Permission permission;
	public static MultiplicadorMethodType multiplicadorType;
	
	public static HashMap<String, CaptchaItemConfig> captchaItem = new HashMap<>();
	public static ArrayList<String> vendaShift = new ArrayList<>();
	public static ArrayList<String> autoVenda = new ArrayList<>();
	public static List<ItemStack> loadedItens = new ArrayList<>();
	public static List<String> multiplicadores = new ArrayList<>();
	
	public static String permissaoShift = "nvender.shift";
	public static String permissaoAutomatico = "nvender.automatico";
	public static String permissaoVender = "nvender.usar";
	
	public void onEnable() {
		m = this;
		try {
			manageConfig();
		} catch (Exception e) {
			e.printStackTrace();
			ConsoleLogger.warning("Nao foi possivel carregar o arquivo de configuração. Desativando plugin...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		new Metrics(this);
		updaterAPI = new UpdaterAPI(this, "nVender");
		updaterAPI.defaultEnableExecute();
		if(updaterAPI.isUpdateAvailable()) {
			ConsoleLogger.warning(" Uma nova versao do nVender esta disponivel " + getDescription().getVersion() + " -> " + updaterAPI.getLastVersion());
			ConsoleLogger.info("");
		}
		new Mensagens();
		registerListeners();
		registerCommands();
		if(setupEconomy()) {
			ConsoleLogger.info("Hooked with Vault (Economy Plugin: " + eco.getName() + ")");
		} else {
			ConsoleLogger.warning("Nao foi possivel dar hook no Vault. Desligando plugin...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if(setupPermissions()) {
			ConsoleLogger.info("Hooked with Vault (Permission Plugin : " + permission.getName() + ")");
		} else {
			ConsoleLogger.info("Nao foi encontrado nenhum plugin de permissions. Usando multiplicador por permissoes...");
		}
		ConsoleLogger.info("Inicializacao completa com sucesso");
	}
	private boolean setupEconomy() {
		PluginManager pm = Bukkit.getPluginManager();
		if(pm.getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		    if (economyProvider != null) {
		    	eco = (Economy)economyProvider.getProvider();
		    }
		    return eco != null;
		}
		return false;
	}
	private boolean setupPermissions() {
		PluginManager pm = Bukkit.getPluginManager();
		if(pm.getPlugin("Vault") != null) {
			RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		    if (permissionProvider != null) {
		    	permission = (Permission)permissionProvider.getProvider();
		    }
		    return permission != null;
		}
		return false;
	}
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new CaptchaListener(), this);
		getServer().getPluginManager().registerEvents(new VendaMenuListener(), this);
	}
	private void registerCommands() {
		getCommand("vender").setExecutor(new VenderCommand());
	}
	private void manageConfig() throws Exception {
		File file = new File(getDataFolder(), "config.yml");
		if(!file.exists()) { saveDefaultConfig(); }
		multiplicadorType = MultiplicadorMethodType.getByConfig(getConfig().getString("Multiplicador.Tipo").toLowerCase());
		isDebug = getConfig().getBoolean("Config.debug-mode");
		if(getConfig().isSet("permissao-shift")) permissaoShift = getConfig().getString("permissao-shift");
		if(getConfig().isSet("permissao-automatico")) permissaoAutomatico = getConfig().getString("permissao-automatico");
		if(getConfig().isSet("permissao-vender")) permissaoVender = getConfig().getString("permissao-vender");
		ConsoleLogger.info("Foram carregados " + loadItensFromConfig() + " item(s) da config."); 
		ConsoleLogger.info("Foram carregados " + loadMultiplicadoresFromConfig() + " multiplicador(es) da config."); 
	}
	private int loadItensFromConfig() {
		ConsoleLogger.debug("Starting load itens from config...");
		FileConfiguration config = nVender.m.getConfig();
		ArrayList<ItemStack> itens = new ArrayList<>();
		for(String itemConfig : nVender.m.getConfig().getConfigurationSection("Itens").getKeys(false)) {
			int id = Integer.valueOf(config.getString("Itens." + itemConfig + ".ID").split(":")[0]);
			int data = Integer.valueOf(config.getString("Itens." + itemConfig + ".ID").split(":")[1]);
			double valor = config.getDouble("Itens." + itemConfig + ".Valor");
			int quantidade = config.getInt("Itens." + itemConfig + ".Quantidade");

			nVenderItem item = new nVenderItem(id, data, valor, quantidade);
			@SuppressWarnings("deprecation")
			Material material = Material.getMaterial(item.getID());
			ItemStack itemStack = new ItemStack(material);
			itemStack.setDurability((short) item.getData());			
			itens.add(itemStack);
			ConsoleLogger.debug("Item " + item.getMaterial().name() + " - $" + item.getPreço() + " loaded from config.");
		}
		nVender.loadedItens = itens;
		return itens.size();
	}
	public int loadMultiplicadoresFromConfig() {
		List<String> multiplicadorCache = new ArrayList<>();
		for(String grupo : nVender.m.getConfig().getConfigurationSection("Multiplicador").getKeys(false)) {
			if(!grupo.equalsIgnoreCase("Tipo")) {
				String grupoPex = nVender.m.getConfig().getString("Multiplicador." + grupo + ".Grupo");
				String permissao = nVender.m.getConfig().getString("Multiplicador." + grupo + ".Permissao");
				String multiplicador = nVender.m.getConfig().getString("Multiplicador." + grupo + ".Multiplicador");
				multiplicadorCache.add(grupoPex + "-" + permissao + "-" + multiplicador.split("%")[0]);
				ConsoleLogger.debug("Multiplicador for group/permission " + grupo + "/" + permissao + " - " + multiplicador + " loaded from config.");
			}
		}
		nVender.multiplicadores = multiplicadorCache;
		return multiplicadorCache.size();
	}

}
