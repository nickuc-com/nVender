package nickultracraft.vender.api;

import org.bukkit.Bukkit;

import nickultracraft.vender.nVender;

/**
 * A class Console.java da package (nickultracraft.protect) pertence ao NickUltracraft
 * Discord: NickUltracraft#4550
 * Mais informações: https://nickuc.tk 
 *
 * É expressamente proibído alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link do download/source original.
*/

public class ConsoleLogger {
	
	public static void warning(String message) {
		send("§6" + message);
	}
	public static void error(String message) {
		send("§c" + message);
	}
	public static void invasion(String message) {
		send("§c[Invasao] " + message);
	}
	public static void info(String message) {
		send(message);
	}
	public static void debug(String message) {
		if(nVender.isDebug) send("§b[DEBUG] " + message);
	}
	public static void send(String message) {
		Bukkit.getConsoleSender().sendMessage("[nProtect] " + message);
	}
}
