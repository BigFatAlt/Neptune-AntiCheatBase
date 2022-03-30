package cc.bigfatman.anticheat.check.impl.autoclicker;

import cc.bigfatman.anticheat.check.Check;
import cc.bigfatman.anticheat.check.CheckData;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CArmAnimationPacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;

@CheckData(checkName = "AutoClicker", checkID = "CPS")
public class AutoclickerA extends Check {

    private int maxCPS = 20;
    private int swings, movements;

    public AutoclickerA(PlayerProfile playerProfile) {
        super(playerProfile);
    }

    @Override
    public void handleCheck(NeptunePacket neptunePacket) {
        if (neptunePacket instanceof CArmAnimationPacket) {
            if (playerProfile.getActionProfile().isDigging()) {
                swings = 0;
            } else ++swings;
        } else if (neptunePacket instanceof CFlyingPacket) {
            if (++movements > 20) {
                if (swings > maxCPS) {
                    verbose("CPS+=" + swings, 10);
                }
                swings = movements = 0;
            }
        }
    }
}
