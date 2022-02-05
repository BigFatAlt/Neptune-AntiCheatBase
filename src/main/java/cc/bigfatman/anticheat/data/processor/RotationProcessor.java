package cc.bigfatman.anticheat.data.processor;

import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;

public class RotationProcessor {
    public final PlayerProfile playerProfile;

    public float toYaw, fromYaw, toPitch, fromPitch;

    public RotationProcessor(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public void processRotationProcessor(CFlyingPacket cFlyingPacket) {
        if (cFlyingPacket.hasRotation) {
            this.fromPitch = this.toPitch;
            this.fromYaw = this.toYaw;

            this.toPitch = cFlyingPacket.pitch;
            this.toYaw = cFlyingPacket.yaw;
        }
    }
}
