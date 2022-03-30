package cc.bigfatman.anticheat.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.block.Block;

@UtilityClass
public class MiscUtils {

    public Block getBlock(Location location) {
        if (location.getChunk().isLoaded()) {
            return location.getBlock();
        }

        return null;
    }
}
