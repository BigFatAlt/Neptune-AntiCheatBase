package cc.bigfatman.anticheat.packet.wrapped.client;

import cc.bigfatman.anticheat.packet.NeptunePacket;
import org.bukkit.entity.Player;

//TODO add BlockPosition and Direction
public class CBlockDigPacket extends NeptunePacket {

    public EnumPlayerDigType enumPlayerDigType;

    public CBlockDigPacket(Player player) {
        super(player);
    }

    public enum EnumPlayerDigType {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM
    }
}
