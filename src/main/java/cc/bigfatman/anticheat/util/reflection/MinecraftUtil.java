package cc.bigfatman.anticheat.util.reflection;

import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MinecraftUtil {
    private static final String BASE = Bukkit.getServer().getClass().getPackage().getName();
    private static final String NMS = MinecraftUtil.BASE.replace("org.bukkit.craftbukkit", "net.minecraft.server");

    private static Field CONNECTION_FIELD;
    private static Field NETWORK_MANAGER_FIELD;
    private static Field CHANNEL_FIELD;

    public static Class<?> nms(String className) throws ClassNotFoundException {
        return Class.forName(MinecraftUtil.NMS + "." + className);
    }

    public static Channel getChannelFromPlayer(Player player) throws Exception {
        Method getHandle = player.getClass().getDeclaredMethod("getHandle");
        Object handle = getHandle.invoke(player);

        // Player Connection
        if (MinecraftUtil.CONNECTION_FIELD == null) {
            MinecraftUtil.CONNECTION_FIELD = ReflectionUtil.getFieldByClassNames(handle.getClass(), "PlayerConnection");
        }

        Object connection = MinecraftUtil.CONNECTION_FIELD.get(handle);

        // Network Manager
        if (MinecraftUtil.NETWORK_MANAGER_FIELD == null) {
            MinecraftUtil.NETWORK_MANAGER_FIELD = ReflectionUtil.getFieldByClassNames(connection.getClass(), "NetworkManager");
        }

        Object networkManager = MinecraftUtil.NETWORK_MANAGER_FIELD.get(connection);

        // Channel
        if (MinecraftUtil.CHANNEL_FIELD == null) {
            MinecraftUtil.CHANNEL_FIELD = ReflectionUtil.getFieldByType(networkManager.getClass(), Channel.class);
        }

        return (Channel) MinecraftUtil.CHANNEL_FIELD.get(networkManager);
    }
}