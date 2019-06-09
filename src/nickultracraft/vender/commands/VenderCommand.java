package nickultracraft.vender.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nickultracraft.vender.nVender;
import nickultracraft.vender.manager.CaptchaManager;
import nickultracraft.vender.manager.Mensagens;
import nickultracraft.vender.manager.VendaMenu;

/**
 * A class Comando.java da package (nickultracraft.vender.handler) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class VenderCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			String name = p.getName();
			if(args.length == 0) {
				if(!p.hasPermission(nVender.permissaoVender)) {
					p.sendMessage(Mensagens.SEM_PERMISSÃO);
					return true;
				}
				if(!nVender.captchaItem.containsKey(name)) {
					if(nVender.m.getConfig().getBoolean("Config.Captcha")) {
						new CaptchaManager(p);
					} else {
						new VendaMenu(p);
					}
					return true;
				} else {
					if(nVender.m.getConfig().getBoolean("Config.Captcha")) {
						new CaptchaManager(p, false);
					} else {
						new VendaMenu(p);
					}
					return true;
				}
			} else {
				String subcmd = args[0];
				if(subcmd.equalsIgnoreCase("menu")) {
					new VendaMenu(p);
					return true;
				}
				if(subcmd.equalsIgnoreCase("version") || (subcmd.equalsIgnoreCase("v"))) {
					p.sendMessage("");
					p.sendMessage("  §a§lnVender §av " + nVender.m.getDescription().getVersion());
					p.sendMessage("  §aPlugin gratuito e open-source!");
					p.sendMessage("  §aLink para download: §fhttps://www.nickuc.tk");
					p.sendMessage("  §aSource code: §fhttps://github.com/NickUltracraft/nVender");
					p.sendMessage("");
					return true;
				}
				if(subcmd.equalsIgnoreCase("reload") || (subcmd.equalsIgnoreCase("r"))) {
					if(p.hasPermission("nvender.admin")) {
						nVender.m.reloadConfig();
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
