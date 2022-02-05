package cc.bigfatman.anticheat.packet.injection;

import cc.bigfatman.anticheat.util.reflection.ReflectionUtil;
import cc.bigfatman.anticheat.util.reflection.type.FieldAccessor;
import org.bukkit.entity.Player;

public abstract class PacketInjector {
    public String old_channel = "packet_handler";
    public String new_channel = "neptune_packet_handler";

    public abstract void inject(Player player) throws Exception;
    public abstract void eject(Player player) throws Exception;


    public static <T> FieldAccessor<T> fetchField(String className, Class<T> fieldType, int index) {
        return ReflectionUtil.getFieldSafe(ReflectionUtil.NMS_PREFIX + "." + className, fieldType, index);
    }

    public static <T> FieldAccessor<T> fetchField(String className, String fieldType, int index) {
        return ReflectionUtil.getFieldSafe(ReflectionUtil.NMS_PREFIX + "." + className, (Class<T>) ReflectionUtil.getClass(fieldType), index);
    }

    public <T> T fetch(FieldAccessor<T> field, Object obj) {
        return field.get(obj);
    }
}
