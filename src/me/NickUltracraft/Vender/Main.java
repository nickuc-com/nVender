package me.NickUltracraft.Vender;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.NickUltracraft.Vender.Captcha.CaptchaListener;
import me.NickUltracraft.Vender.Core.Comando;
import me.NickUltracraft.Vender.Core.Mensagens;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Main m;
	public static Economy eco = null;
	
	public void console(String mensagem) {
		Bukkit.getConsoleSender().sendMessage(mensagem);
	}
	public void onEnable() {
		m = this;
		console("Carregando nVender v " + getDescription().getVersion());
		if(!(new File(getDataFolder(), "config.yml").exists())) {
			saveDefaultConfig();
		}
		setupVault();
		new Metrics(this);
		new Mensagens();
		getCommand("vender").setExecutor(new Comando());
		Bukkit.getPluginManager().registerEvents(new CaptchaListener(), this);
		console("§anVender foi carregado com sucesso.");
	}
	private boolean setupVault() {
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

}
