package cc.bigfatman.anticheat.check.impl.killaura;

import cc.bigfatman.anticheat.check.Check;
import cc.bigfatman.anticheat.check.CheckData;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CArmAnimationPacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CUseEntityPacket;

/*
 * NoSwing check
 *
 * checks to see if the player sent @CArmAnimationPacket and there swings is above one
 * but if they didn't they flag
 *
 * will probably false with lag.
 *
 */
@CheckData(checkName = "KillAura", checkID = "invalidSwing")
public class KillAuraC extends Check {

    public int swings;

    public KillAuraC(PlayerProfile playerProfile) {
        super(playerProfile);
    }

    @Override
    public void handleCheck(NeptunePacket neptunePacket) {
        if (neptunePacket instanceof CUseEntityPacket) {
            CUseEntityPacket cUseEntityPacket = (CUseEntityPacket) neptunePacket;

            if (cUseEntityPacket.enumEntityUseAction.equals(CUseEntityPacket.EnumEntityUseAction.ATTACK) && swings == 0) {
                verbose("swings-=" + swings, 2);
            } else decreaseThreshold(.5f);
        } else if (neptunePacket instanceof CArmAnimationPacket) swings++;
          else if (neptunePacket instanceof CFlyingPacket) swings = 0;
    }
}
