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

import com.nickuc.ncore.api.config.nConfig;
import com.nickuc.ncore.api.settings.ISettingsEnum;
import com.nickuc.ncore.api.settings.Settings;
import com.nickuc.vender.manager.VendaCore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @Getter
public enum SettingsEnum implements ISettingsEnum {

    PERMISSION_SHIFT("permissao-shift", "nvender.shift"),
	PERMISSION_AUTOMATICO("permissao-automatico", "nvender.automatico"),
	PERMISSION_VENDER("permissao-vender", "nvender.usar"),
    MULTIPLICADOR_TYPE("Multiplicador.Tipo", "grupo"),
    RUN_SELL_ASYNC("Config.run-sell-async", true),
    ;

    private static VendaCore.MultiplicadorType multiplicadorType;

    public static ArrayList<String> vendaShift = new ArrayList<>();
    public static ArrayList<String> autoVenda = new ArrayList<>();
    public static List<ItemStack> loadedItens = new ArrayList<>();
    public static List<String> multiplicadores = new ArrayList<>();

    private final String key;
    private final Object defaultValue;

    public static void reload(nConfig config) throws Exception {
        Settings.loadSettings(SettingsEnum.values(), config);
        multiplicadorType = VendaCore.MultiplicadorType.getByConfig(Settings.getString(SettingsEnum.MULTIPLICADOR_TYPE).toLowerCase());
    }

    public static VendaCore.MultiplicadorType getMultiplicadorType() {
        return multiplicadorType;
    }

}
