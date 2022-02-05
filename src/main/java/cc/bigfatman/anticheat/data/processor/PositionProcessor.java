package cc.bigfatman.anticheat.data.processor;

import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;

public class PositionProcessor {
    private final PlayerProfile playerProfile;

    public double toX, toY, toZ, fromX, fromY, fromZ;
    public boolean clientOnGround, serverOnGround;

    public PositionProcessor(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public void processPositionProcessor(CFlyingPacket cFlyingPacket) {
        if (cFlyingPacket.hasPosition) {
            this.toX = cFlyingPacket.x;
            this.toY = cFlyingPacket.y;
            this.toZ = cFlyingPacket.z;

            this.fromX = this.toX;
            this.fromY = this.toY;
            this.fromZ = this.toZ;

            this.clientOnGround = cFlyingPacket.onGround;
            this.serverOnGround = toY % 1 /64 < 0.0001;
        }
    }
}
