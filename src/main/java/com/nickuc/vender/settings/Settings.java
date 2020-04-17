/**
 * Copyright NickUC
 * -
 * Esta class pertence ao projeto de NickUC
 * Mais informações: https://nickuc.com
 * -
 * É expressamente proibido alterar o nome do proprietário do código, sem
 * expressar e deixar claramente o link para acesso da source original.
 * -
 * Este aviso não pode ser removido ou alterado de qualquer distribuição de origem.
 */

package com.nickuc.vender.settings;

import com.nickuc.vender.manager.VendaCore;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum Settings {

    PERMISSION_SHIFT("permissao-shift", "nvender.shift"),
	PERMISSION_AUTOMATICO("permissao-automatico", "nvender.automatico"),
	PERMISSION_VENDER("permissao-vender", "nvender.usar"),
    MULTIPLICADOR_TYPE("Multiplicador.Tipo", "grupo"),
    RUN_SELL_ASYNC("Config.run-sell-async", true),
    ;

    Settings(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    private static VendaCore.MultiplicadorType multiplicadorType;

    public static ArrayList<String> vendaShift = new ArrayList<>();
    public static ArrayList<String> autoVenda = new ArrayList<>();
    public static List<ItemStack> loadedItens = new ArrayList<>();
    public static List<String> multiplicadores = new ArrayList<>();

    private final String key;
    private final Object defaultValue;
    private static FileConfiguration config;

    public String getString() {
        return config.getString(key, (String) defaultValue);
    }

    public boolean getBoolean() {
        return config.getBoolean(key, (Boolean) defaultValue);
    }

    public static void reload(FileConfiguration config) throws Exception {
        Settings.config = config;
        multiplicadorType = VendaCore.MultiplicadorType.getByConfig(Settings.MULTIPLICADOR_TYPE.getString().toLowerCase());
    }

    public static VendaCore.MultiplicadorType getMultiplicadorType() {
        return multiplicadorType;
    }

    public enum Messages {

        NO_PERMISSION("Mensagens.Sem-Permissao", "§cVocê não tem permissão para executar este comando."),
        CONFIG_RELOADED("Mensagens.Configuracao-Recarregada", "§aConfiguração e arquivos de linguagem foram recarregados."),
        INVALID_CAPTCHA("Mensagens.Captcha-Invalido", "§cO captcha inserido é inválido."),
        VENDIDO("Mensagens.Vendido-Sucesso","§aVocê vendeu §7%itens% §aitens por §7$%dinheiro%§a."),
        NO_ITENS("Mensagens.Sem-Itens", "§cVocê não possui itens para serem vendidos."),
        INVENTORY_EMPTY("Mensagens.Inventario-Vazio", "§cVocê não pode estar com um inventário vazio."),
        ;

        private final String key;
        private final String defaultValue;

        Messages(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getMessage() {
            return ChatColor.translateAlternateColorCodes('&', config.getString(key, defaultValue));
        }

    }


}
