package cc.bigfatman.anticheat.data.profiles;

import cc.bigfatman.anticheat.data.Profile;
import cc.bigfatman.anticheat.packet.wrapped.client.CBlockDigPacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CUseEntityPacket;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ActionProfile implements Profile {
    private final PlayerProfile playerProfile;

    private boolean digging;
    private int hitTicks, totalTicks;

    public ActionProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public void onUseEntity(CUseEntityPacket cUseEntityPacket) {
        if (cUseEntityPacket.enumEntityUseAction.equals(CUseEntityPacket.EnumEntityUseAction.ATTACK)) {
            hitTicks = 0;
        }
    }

    public void onDigPacket(CBlockDigPacket blockDigPacket) {
        switch (blockDigPacket.enumPlayerDigType) {
            case START_DESTROY_BLOCK:
                setDigging(true);
                break;
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                setDigging(false);
        }
    }

    public void onFlying(CFlyingPacket cFlyingPacket) {
        hitTicks++;
        totalTicks++;
    }
}
