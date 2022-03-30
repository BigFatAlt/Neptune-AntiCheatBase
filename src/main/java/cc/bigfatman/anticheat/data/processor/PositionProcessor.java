package cc.bigfatman.anticheat.data.processor;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;
import org.bukkit.Bukkit;

public class PositionProcessor {
    private final PlayerProfile playerProfile;

    public double toX, toY, toZ, fromX, fromY, fromZ, distX,distY,distZ ,distXZ,lastDistY,lastDistXZ;
    public boolean clientOnGround, serverOnGround;


    public PositionProcessor(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public void processPositionProcessor(CFlyingPacket cFlyingPacket) {
        if (cFlyingPacket.hasPosition) {
            this.fromX = this.toX;
            this.fromY = this.toY;
            this.fromZ = this.toZ;

            this.toX = cFlyingPacket.x;
            this.toY = cFlyingPacket.y;
            this.toZ = cFlyingPacket.z;

            this.clientOnGround = cFlyingPacket.onGround;
            this.serverOnGround = this.toY % 1 /64 < 0.0001;

            this.lastDistY = this.distY;
            this.lastDistXZ = this.distXZ;

            this.distX = this.toX - this.fromX;
            this.distY = this.toY - this.fromY;
            this.distZ = this.toZ - this.fromZ;
            this.distXZ = Math.sqrt((this.distX * this.distX) + (this.distZ * this.distZ));
        }


        //fixes kick
        Bukkit.getScheduler().runTask(Neptune.getInstance(),() ->  playerProfile.getCollisionProcessor().handle(this));


    }
}
