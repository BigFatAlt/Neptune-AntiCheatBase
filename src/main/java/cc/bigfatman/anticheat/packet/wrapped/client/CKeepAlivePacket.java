package cc.bigfatman.anticheat.packet.wrapped.client;

import cc.bigfatman.anticheat.packet.NeptunePacket;
import org.bukkit.entity.Player;

public class CKeepAlivePacket extends NeptunePacket {

    public long time;

    public CKeepAlivePacket(Player player) {
        super(player);
    }
}
