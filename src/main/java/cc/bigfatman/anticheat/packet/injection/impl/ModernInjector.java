package cc.bigfatman.anticheat.packet.injection.impl;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.packet.injection.PacketInjector;
import cc.bigfatman.anticheat.packet.wrapped.client.CFlyingPacket;
import cc.bigfatman.anticheat.util.reflection.MinecraftUtil;
import cc.bigfatman.anticheat.util.reflection.type.FieldAccessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ModernInjector extends PacketInjector {
    @Override
    public void inject(Player player) throws Exception {
        Channel channel = MinecraftUtil.getChannelFromPlayer(player);

        Neptune.getInstance().getExecutor().submit(() -> {
            channel.pipeline().addBefore(old_channel, new_channel, new ChannelDuplexHandler() {
                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    super.write(ctx, msg, promise);
                }

                @Override
                public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
                    onIncomingPacket(object, player);
                    super.channelRead(ctx, object);
                }
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

            fireChecks(flyingPacket, playerProfile);
        }
    }

    public void fireChecks(NeptunePacket neptunePacket, PlayerProfile playerProfile) {
        synchronized (neptunePacket) {
            playerProfile.getCheckManager().checkClassToInstanceMap.values()
                    .stream().filter(check -> check.enabled)
                    .forEach(check -> check.handleCheck(neptunePacket));
        }
    }
}
