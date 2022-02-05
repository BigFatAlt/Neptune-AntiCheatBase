package cc.bigfatman.anticheat.check.impl.badpackets;

import cc.bigfatman.anticheat.check.Check;
import cc.bigfatman.anticheat.check.CheckData;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CArmAnimationPacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;

@CheckData(checkName = "BadPackets", checkID = "A")
public class BadPacketsA extends Check {

    public long lastFlying;

    public BadPacketsA(PlayerProfile playerProfile) {
        super(playerProfile);
    }

    @Override
    public void handleCheck(NeptunePacket neptunePacket) {
        if (neptunePacket instanceof CArmAnimationPacket) {
            long lastArmAnimation = System.currentTimeMillis() - lastFlying;

            if (lastArmAnimation <= 10L) {
                verbose("delay+=" + lastArmAnimation, 10);
            }
        } else if (neptunePacket instanceof CFlyingPacket) lastFlying = System.currentTimeMillis();
    }
}
