package cc.bigfatman.anticheat.check.impl.killaura;

import cc.bigfatman.anticheat.check.Check;
import cc.bigfatman.anticheat.check.CheckData;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CUseEntityPacket;
import cc.bigfatman.anticheat.util.MathUtils;
import cc.bigfatman.anticheat.util.Raytrace;
import org.bukkit.util.Vector;

/*
 * Basic angle check comparing the direction of the player to there position
 * if the angle is smaller the 0.9 then flag as in testing the smallest I got it
 * was 0.94 or something.
 *
 * This check will probably false as did not check with optifine and cinimatic
 * but it's in a open source base so don't expect the best.
 *
 */
@CheckData(checkName = "KillAura", checkID = "angle")
public class KillAuraB extends Check {

    public KillAuraB(PlayerProfile playerProfile) {
        super(playerProfile);
    }

    @Override
    public void handleCheck(NeptunePacket neptunePacket) {
        if (neptunePacket instanceof CUseEntityPacket) {
            CUseEntityPacket cUseEntityPacket = (CUseEntityPacket) neptunePacket;

            if (cUseEntityPacket.enumEntityUseAction.equals(CUseEntityPacket.EnumEntityUseAction.ATTACK)) {
                float deltaPitch = Math.abs(playerProfile.getRotationProcessor().fromPitch - playerProfile.getRotationProcessor().toPitch);
                float deltaYaw = Math.abs(playerProfile.getRotationProcessor().fromYaw - playerProfile.getRotationProcessor().toYaw);


                Vector direction = MathUtils.getDirection(deltaYaw, deltaPitch);
                Vector origin = new Vector(playerProfile.getPositionProcessor().fromX, playerProfile.getPositionProcessor().fromY, playerProfile.getPositionProcessor().fromZ);

                double angle = MathUtils.angle(direction, origin);

                if (angle < 0.9 && playerProfile.getPositionProcessor().distXZ > 0.12f) {
                    verbose("angle=" + angle, 5);
                } else decreaseThreshold(1f);
            }
        }
    }
}
