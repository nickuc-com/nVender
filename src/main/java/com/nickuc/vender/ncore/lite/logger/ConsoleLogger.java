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

package com.nickuc.vender.ncore.lite.logger;

import com.nickuc.vender.nVender;

public final class ConsoleLogger {

    private static nVender nvender;

    public static void init(nVender nvender) {
        ConsoleLogger.nvender = nvender;
    }

    public static void info(String message) {
        nvender.getServer().getConsoleSender().sendMessage("[" + nvender.getName() + "] " + message);
    }

    public static void criticalwarning(String message) {
        nvender.getServer().getConsoleSender().sendMessage("§4[" + nvender.getName() + "] " + message);
    }
}
