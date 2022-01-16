package cc.bigfatman.anticheat.check;

import cc.bigfatman.anticheat.check.impl.ImpossiblePitch;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

public class CheckManager {
    public final PlayerProfile playerProfile;

    public ClassToInstanceMap<Check> checkClassToInstanceMap;

    public CheckManager(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;

        checkClassToInstanceMap = new ImmutableClassToInstanceMap
                .Builder<Check>()
                .put(ImpossiblePitch.class, new ImpossiblePitch(playerProfile))
                .build();
    }
}
