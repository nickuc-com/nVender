package me.NickUltracraft.Vender.Core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.NickUltracraft.Vender.Main;
import me.NickUltracraft.Vender.Cache.Players;
import me.NickUltracraft.Vender.Captcha.CaptchaManager;

public class Comando implements CommandExecutor {

	private String link = "https://github.com/NickUltracraft/nVender";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			String name = p.getName();
			if(args.length == 0) {
				if(!p.hasPermission("nvender.usar")) {
					p.sendMessage(Mensagens.SEM_PERMISSÃO);
					return true;
				}
				if(!Players.captchaItem.containsKey(name)) {
					if(Main.m.getConfig().getBoolean("Config.Captcha")) {
						new CaptchaManager(p);
					} else {
						new Gui(p);
					}
					return true;
				} else {
					p.sendMessage("§cUm erro desconhecido ocorreu...");
					return true;
				}
			} else {
				String subcmd = args[0];
				if(subcmd.equalsIgnoreCase("menu")) {
					new Gui(p);
				}
				if(subcmd.equalsIgnoreCase("version") || (subcmd.equalsIgnoreCase("v"))) {
					p.sendMessage("");
					p.sendMessage("  §a§lnVender §av " + Main.m.getDescription().getVersion());
					p.sendMessage("  §aPlugin gratuito e open-source!");
					p.sendMessage("  §aLink para download: §fyoutube.com/c/nickultracraft");
					p.sendMessage("  §aSource code: §f" + link);
					p.sendMessage("");
					return true;
				}
				if(subcmd.equalsIgnoreCase("reload") || (subcmd.equalsIgnoreCase("r"))) {
					if(p.hasPermission("nvender.admin")) {
						Main.m.reloadConfig();
						new Mensagens();
						p.sendMessage(Mensagens.CONFIGURAÇÃO_RECARREGADA);
						return true;
					} else {
						p.sendMessage(Mensagens.SEM_PERMISSÃO);
						return true;
					}
				}
				p.sendMessage("§cSub-comando inexistente.");
				return true;
			}
		}
		return false;
	}
	
	

}
