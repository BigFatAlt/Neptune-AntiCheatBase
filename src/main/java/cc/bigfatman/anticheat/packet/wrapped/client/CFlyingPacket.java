package cc.bigfatman.anticheat.packet.wrapped.client;

import cc.bigfatman.anticheat.packet.NeptunePacket;
import org.bukkit.entity.Player;

public class CFlyingPacket extends NeptunePacket {

    public double x, y, z;
    public float yaw, pitch;
    public boolean onGround, hasPosition, hasRotation;


    public CFlyingPacket(Player player) {
        super(player);
    }
}
