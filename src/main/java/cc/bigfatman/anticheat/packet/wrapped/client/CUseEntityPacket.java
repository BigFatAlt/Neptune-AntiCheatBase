package cc.bigfatman.anticheat.packet.wrapped.client;

import cc.bigfatman.anticheat.packet.NeptunePacket;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CUseEntityPacket extends NeptunePacket {

    public int id;

    public Entity entity;
    public EnumEntityUseAction enumEntityUseAction;

    public CUseEntityPacket(Player player) {
        super(player);
    }

    public enum EnumEntityUseAction {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }
}
