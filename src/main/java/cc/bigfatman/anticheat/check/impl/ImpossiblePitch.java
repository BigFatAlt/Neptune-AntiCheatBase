package cc.bigfatman.anticheat.check.impl;

import cc.bigfatman.anticheat.check.Check;
import cc.bigfatman.anticheat.check.CheckData;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;
import org.bukkit.Bukkit;

@CheckData(checkName = "ImpossiblePitch", checkID = "ImpossiblePitch")
public class ImpossiblePitch extends Check {

    public ImpossiblePitch(PlayerProfile playerProfile) {
        super(playerProfile);
    }

    @Override
    public void handleCheck(NeptunePacket neptunePacket) {
        if (neptunePacket instanceof CFlyingPacket) {
            CFlyingPacket cFlyingPacket = (CFlyingPacket) neptunePacket;

            float pitch = Math.abs(cFlyingPacket.pitch);

            if (pitch > 90.f) {
                verbose(pitch + "> 90.f", 50);
            }
        }
    }
}
