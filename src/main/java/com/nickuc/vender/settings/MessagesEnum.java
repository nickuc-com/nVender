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
import com.nickuc.ncore.api.settings.IMessagesEnum;
import com.nickuc.ncore.api.settings.Messages;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum MessagesEnum implements IMessagesEnum {

    NO_PERMISSION("Mensagens.Sem-Permissao", "§cVocê não tem permissão para executar este comando."),
    CONFIG_RELOADED("Mensagens.Configuracao-Recarregada", "§aConfiguração e arquivos de linguagem foram recarregados."),
    INVALID_CAPTCHA("Mensagens.Captcha-Invalido", "§cO captcha inserido é inválido."),
    VENDIDO("Mensagens.Vendido-Sucesso","§aVocê vendeu §7%itens% §aitens por §7$%dinheiro%§a."),
    NO_ITENS("Mensagens.Sem-Itens", "§cVocê não possui itens para serem vendidos."),
    INVENTORY_EMPTY("Mensagens.Inventario-Vazio", "§cVocê não pode estar com um inventário vazio."),
    ;

    private final String key;
    private final String defaultValue;

    public static void reload(nConfig config) throws Exception {
        Messages.loadMessages(MessagesEnum.values(), config);
    }

}
