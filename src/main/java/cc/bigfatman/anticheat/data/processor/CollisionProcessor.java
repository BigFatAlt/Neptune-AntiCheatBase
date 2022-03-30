package cc.bigfatman.anticheat.data.processor;

import cc.bigfatman.anticheat.data.processor.PositionProcessor;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.util.BoundingBox;
import cc.bigfatman.anticheat.util.MiscUtils;
import cc.bigfatman.anticheat.util.WrappedBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.List;


/**
 * @author Salers
 * made on cc.bigfatman.anticheat.profile.processor
 * Arctic collision processor <3
 */

public class CollisionProcessor {

    private final PlayerProfile profile;
    private BoundingBox boundingBox, bonkingBoundingBox, fenceBoundingBox;

    private List<WrappedBlock> blockCollisions, bonkingCollisions, fenceCollisions;

    public boolean clientOnGround, mathOnGround, collisionOnGround,
            onIce, onSlime, onSoulSand, onClimbable,
            inWeb, inWater, inLava, nearPiston, bonkingHead,
            lastClientOnGround,lastLastClientOnGround, lastMathOnGround, lastCollisionOnGround,
            lastOnIce, lastOnSlime, lastOnSoulSand, lastOnClimbable, lastNearPiston,
            lastInWeb, lastInWater, lastInLava,  lastBonkingHead,
            lastOnGroundSlime, lastOnGroundIce;


    public CollisionProcessor(PlayerProfile profile) {
        this.profile = profile;
    }

    public void handle(final PositionProcessor pos) {


        final Vector location = new Vector(pos.toX, pos.toY, pos.toZ);


        boundingBox = new BoundingBox(location).expand(0, 0.01, 0);

        blockCollisions = boundingBox.getBlocks(profile.getPlayer());

        bonkingBoundingBox = new BoundingBox(location).expandSpecific(0, 0, -1.81, 0.01, 0, 0);

        bonkingCollisions = bonkingBoundingBox.getBlocks(profile.getPlayer());

        fenceBoundingBox = new BoundingBox(location).expandSpecific(0, 0, 0.61, -2.41, 0, 0);

        fenceCollisions = fenceBoundingBox.getBlocks(profile.getPlayer());

        updateLastVariables();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        clientOnGround = pos.clientOnGround;
        // All blocks in minecraft have a y divisible by 1/64
        mathOnGround = pos.serverOnGround;
        // Not sure if I have to do <= instead of just < but it doesn't really matter
        collisionOnGround = blockCollisions.stream().anyMatch(block -> block.isSolid() && block.getY() <= y)
                || fenceCollisions.stream().anyMatch(block -> block.isFence() || block.isFenceGate() || block.isWall());
        onIce = blockCollisions.stream().anyMatch(block -> block.isIce() && block.getY() <= y);
        onSlime = blockCollisions.stream().anyMatch(block -> block.isSlime() && block.getY() <= y);
        onSoulSand = blockCollisions.stream().anyMatch(block -> block.isSoulSand() && block.getY() <= y);
        inWeb = blockCollisions.stream().anyMatch(WrappedBlock::isWeb);
        inWater = blockCollisions.stream().anyMatch(WrappedBlock::isWater);
        inLava = blockCollisions.stream().anyMatch(WrappedBlock::isLava);
        nearPiston = blockCollisions.stream().anyMatch(WrappedBlock::isPiston);

        int flooredX = NumberConversions.floor(x);
        int flooredY = NumberConversions.floor(y);
        int flooredZ = NumberConversions.floor(z);

        Block climbableBlock = MiscUtils.getBlock(new Location(profile.getPlayer().getWorld(), flooredX, flooredY, flooredZ));

        onClimbable = climbableBlock != null && (climbableBlock.getType() == Material.LADDER
                || climbableBlock.getType() == Material.VINE);

        bonkingHead = bonkingCollisions.stream().anyMatch(WrappedBlock::isSolid);


        if (lastClientOnGround) {
            lastOnGroundSlime = lastOnSlime;
            lastOnGroundIce = lastOnIce;
        }

    }


    private void updateLastVariables() {
        lastLastClientOnGround = lastClientOnGround;
        lastClientOnGround = clientOnGround;
        lastMathOnGround = mathOnGround;
        lastCollisionOnGround = collisionOnGround;
        lastOnIce = onIce;
        lastOnSlime = onSlime;
        lastOnSoulSand = onSoulSand;
        lastOnClimbable = onClimbable;
        lastNearPiston = nearPiston;
        lastInWeb = inWeb;
        lastInWater = inWater;
        lastInLava = inLava;
        lastBonkingHead = bonkingHead;
    }
}
