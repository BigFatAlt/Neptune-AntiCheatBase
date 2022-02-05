package cc.bigfatman.anticheat.check.impl.killaura;

import cc.bigfatman.anticheat.check.Check;
import cc.bigfatman.anticheat.check.CheckData;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CUseEntityPacket;

@CheckData(checkName = "KillAura", checkID = "Post", description = "Checks to see if there is a delay between packets sent.")
public class KillAuraA extends Check {

    public long lastFlying;

    public KillAuraA(PlayerProfile playerProfile) {
        super(playerProfile);
    }

    @Override
    public void handleCheck(NeptunePacket neptunePacket) {
        if (neptunePacket instanceof CUseEntityPacket) {
            CUseEntityPacket cUseEntityPacket = (CUseEntityPacket) neptunePacket;

            if (cUseEntityPacket.enumEntityUseAction.equals(CUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                long lastUseEntity = System.currentTimeMillis() - lastFlying;

                if (lastUseEntity <= 10L) {
                    verbose("delay+=" + lastUseEntity, 10);
                }
            }
        } else if (neptunePacket instanceof CFlyingPacket) lastFlying = System.currentTimeMillis();
    }
}
