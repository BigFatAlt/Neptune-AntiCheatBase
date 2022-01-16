package cc.bigfatman.anticheat.data;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager {
    private final Neptune plugin;

    public List<Class<? extends Profile>> profiles;
    public Map<UUID, PlayerProfile> profileMap = new ConcurrentHashMap<>();

    public ProfileManager(Neptune plugin) {
        this.plugin = plugin;

        profiles = Arrays.asList(PlayerProfile.class);
    }

    public void addPlayer(UUID uuid) {
        profileMap.put(uuid, new PlayerProfile(uuid));
    }

    public void removePlayer(UUID uuid) {
        profileMap.remove(uuid);
    }

    public PlayerProfile getPlayerProfile(UUID uuid) {
        return profileMap.get(uuid);
    }
}
