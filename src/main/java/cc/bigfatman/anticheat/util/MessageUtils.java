package cc.bigfatman.anticheat.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class MessageUtils {

    public String translateColour(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
