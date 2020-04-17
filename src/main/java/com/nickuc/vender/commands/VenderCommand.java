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

package com.nickuc.vender.commands;

import com.nickuc.vender.manager.VendaMenu;
import com.nickuc.vender.nVender;
import com.nickuc.vender.settings.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VenderCommand implements CommandExecutor {

	private final nVender nvender;

	public VenderCommand(nVender nvender) {
		this.nvender = nvender;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		try {
			if (!sender.hasPermission(Settings.PERMISSION_VENDER.getString())) {
				sender.sendMessage(Settings.Messages.NO_PERMISSION.getMessage());
				return true;
			}

			if (!(sender instanceof Player)) {
				reload(sender);
				return true;
			}

			Player player = (Player) sender;
			if(args.length == 0) {
				VendaMenu.openMenu(player);
				return true;
			}

			String subcmd = args[0];
			if(subcmd.equalsIgnoreCase("reload") || subcmd.equalsIgnoreCase("r")) {
				if(!sender.hasPermission("nvender.admin")) {
					sender.sendMessage(Settings.Messages.NO_PERMISSION.getMessage());
					return true;
				}
				reload(sender);
				return true;
			}
			sender.sendMessage("§cSub-comando inexistente.");
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage("§cFailed to execute this command :c");
		}
		return true;
	}

	private void reload(CommandSender sender) throws Exception {
		nvender.config();
		sender.sendMessage(Settings.Messages.CONFIG_RELOADED.getMessage());
	}
}
