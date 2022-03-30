package cc.bigfatman.anticheat.check.impl.fly;

import cc.bigfatman.anticheat.check.Check;
import cc.bigfatman.anticheat.check.CheckData;
import cc.bigfatman.anticheat.data.processor.CollisionProcessor;
import cc.bigfatman.anticheat.data.processor.PositionProcessor;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;

/**
 * @author Salers
 * made on cc.bigfatman.anticheat.check.impl.fly
 */

@CheckData(checkName = "Fly", checkID = "Gravity", description = "Makes sure that player follows gravity rule.")
public class FlyA extends Check {

    public FlyA(PlayerProfile playerProfile) {
        super(playerProfile);
    }

    @Override
    public void handleCheck(NeptunePacket neptunePacket) {
        if (neptunePacket instanceof CFlyingPacket) {
            final PositionProcessor positionProcessor = playerProfile.getPositionProcessor();
            final CollisionProcessor collisionProcessor = playerProfile.getCollisionProcessor();

            final boolean exempt = collisionProcessor.inLava || collisionProcessor.inWater ||
                    collisionProcessor.inWeb || collisionProcessor.onClimbable || collisionProcessor.nearPiston;
            final boolean inAir = !collisionProcessor.clientOnGround && !collisionProcessor.lastClientOnGround
                    && !collisionProcessor.lastLastClientOnGround;

            final double distY = positionProcessor.distY;
            final double lastDistY = positionProcessor.lastDistY;

            final double threshold = distY <= 0 ? 1.0E-6 : 5.0E-3;
            final double estimation = (lastDistY - 0.08) * 0.98F;
            final double shiftedEstimation = Math.abs(estimation) > 0.005 ? estimation : 0;

            if (Math.abs(distY - shiftedEstimation) > threshold && !exempt && inAir
                    && playerProfile.getActionProfile().getTotalTicks() > 30) {
                //huge threshold, idfc
                if (this.threshold < 10) this.threshold++;
                if (this.threshold > 5.f)
                    fail("dist=" + distY + " sE=" + shiftedEstimation);
            } else decreaseThreshold(0.2f);


        }
    }
}
