package cc.bigfatman.anticheat.packet.injection.impl;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.injection.PacketInjector;
import cc.bigfatman.anticheat.packet.wrapped.client.*;
import cc.bigfatman.anticheat.util.ProtocolVersion;
import cc.bigfatman.anticheat.util.reflection.MinecraftUtil;
import cc.bigfatman.anticheat.util.reflection.type.FieldAccessor;
import io.netty.channel.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ModernInjector extends PacketInjector {
    @Override
    public void inject(Player player) throws Exception {
        Channel channel = MinecraftUtil.getChannelFromPlayer(player);

        Neptune.getInstance().getExecutor().submit(() -> {
            channel.pipeline().addBefore(old_channel, new_channel, new ChannelInboundHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
                    onIncomingPacket(object, player);
                    super.channelRead(ctx, object);
                }


                /*@Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    super.write(ctx, msg, promise);
                }

                @Override
                public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
                    onIncomingPacket(object, player);
                    super.channelRead(ctx, object);
                }*/
            });
        });
    }

    @Override
    public void eject(Player player) throws Exception {
        Channel channel = MinecraftUtil.getChannelFromPlayer(player);

        Neptune.getInstance().getExecutor().submit(() -> {
            channel.pipeline().remove(new_channel);
        });
    }

    public void onIncomingPacket(Object object, Player player) {
        PlayerProfile playerProfile = Neptune.getInstance().getProfileManager().getPlayerProfile(player.getUniqueId());

        if (object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInArmAnimation")) {
            CArmAnimationPacket armAnimationPacket = new CArmAnimationPacket(player);

            fireChecks(armAnimationPacket, playerProfile);
        }

        if (object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInFlying")
                || object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInPosition")
                || object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInLook")
                || object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInPositionLook")) {
            CFlyingPacket flyingPacket = new CFlyingPacket(player);

            FieldAccessor<Double> fieldX = fetchField("PacketPlayInFlying", double.class, 0);
            FieldAccessor<Double> fieldY = fetchField("PacketPlayInFlying", double.class, 1);
            FieldAccessor<Double> fieldZ = fetchField("PacketPlayInFlying", double.class, 2);

            FieldAccessor<Float> fieldYaw = fetchField("PacketPlayInFlying", float.class, 0);
            FieldAccessor<Float> fieldPitch = fetchField("PacketPlayInFlying", float.class, 1);

            FieldAccessor<Boolean> fieldGround = fetchField("PacketPlayInFlying", boolean.class, 0);
            FieldAccessor<Boolean> fieldPosition = fetchField("PacketPlayInFlying", boolean.class, 1);
            FieldAccessor<Boolean> fieldLook = fetchField("PacketPlayInFlying", boolean.class, 2);

            flyingPacket.x = fetch(fieldX, object);
            flyingPacket.y = fetch(fieldY, object);
            flyingPacket.z = fetch(fieldZ, object);

            flyingPacket.yaw = fetch(fieldYaw, object);
            flyingPacket.pitch = fetch(fieldPitch, object);

            flyingPacket.onGround = fetch(fieldGround, object);
            flyingPacket.hasPosition = fetch(fieldPosition, object);
            flyingPacket.hasRotation = fetch(fieldLook, object);

            playerProfile.getActionProfile().onFlying(flyingPacket);
            playerProfile.getPositionProcessor().processPositionProcessor(flyingPacket);
            playerProfile.getRotationProcessor().processRotationProcessor(flyingPacket);
            fireChecks(flyingPacket, playerProfile);
        }

        if (object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInBlockDig")) {
            CBlockDigPacket cBlockDigPacket = new CBlockDigPacket(player);

            FieldAccessor<Object> fieldEnum = fetchField("PacketPlayInBlockDig", Object.class, 2);
            cBlockDigPacket.enumPlayerDigType = CBlockDigPacket.EnumPlayerDigType.values()[((Enum) fetch(fieldEnum, object)).ordinal()];


            playerProfile.getActionProfile().onDigPacket(cBlockDigPacket);
            fireChecks(cBlockDigPacket, playerProfile);
        }

        if (object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            CUseEntityPacket useEntityPacket = new CUseEntityPacket(player);

            FieldAccessor<Integer> fieldId = fetchField("PacketPlayInUseEntity", int.class, 0);
            FieldAccessor<Enum> fieldEnum = fetchField("PacketPlayInUseEntity", Enum.class, 0);

            useEntityPacket.enumEntityUseAction = CUseEntityPacket.EnumEntityUseAction.values()[fetch(fieldEnum, object).ordinal()];
            useEntityPacket.id = fetch(fieldId, object);

            for (Entity entity : player.getWorld().getEntities()) {
                if (useEntityPacket.id == entity.getEntityId()) {
                    useEntityPacket.entity = entity;
                }
            }

            playerProfile.getActionProfile().onUseEntity(useEntityPacket);
            fireChecks(useEntityPacket, playerProfile);
        }


        //TODO fix issue of spamming console with errors ?
        /*if (object.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInKeepAlive")) {
            CKeepAlivePacket cKeepAlivePacket = new CKeepAlivePacket(player);

            FieldAccessor<Integer> fieldLegacy = fetchField("PacketPlayInKeepAlive", int.class, 0);
           FieldAccessor<Long> field = fetchField("PacketPlayInKeepAlive", long.class, 0);

            if (ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_12)) {
                cKeepAlivePacket.time = fetch(fieldLegacy, object);
            } else cKeepAlivePacket.time = fetch(field, object);
        } */
    }

    public void fireChecks(NeptunePacket neptunePacket, PlayerProfile playerProfile) {
        synchronized (neptunePacket) {
                  playerProfile.getCheckManager().checkClassToInstanceMap.values()
                    .stream().filter(check -> check.enabled)
                    .forEach(check -> check.handleCheck(neptunePacket));
        }
    }
}
