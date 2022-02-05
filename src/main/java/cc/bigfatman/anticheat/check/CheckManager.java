package cc.bigfatman.anticheat.check;

import cc.bigfatman.anticheat.check.impl.ImpossiblePitch;
import cc.bigfatman.anticheat.check.impl.autoclicker.AutoclickerA;
import cc.bigfatman.anticheat.check.impl.badpackets.BadPacketsA;
import cc.bigfatman.anticheat.check.impl.killaura.KillAuraA;
import cc.bigfatman.anticheat.check.impl.killaura.KillAuraB;
import cc.bigfatman.anticheat.check.impl.killaura.KillAuraC;
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
                .put(AutoclickerA.class, new AutoclickerA(playerProfile))
                .put(BadPacketsA.class, new BadPacketsA(playerProfile))
                .put(KillAuraA.class, new KillAuraA(playerProfile))
                .put(KillAuraB.class, new KillAuraB(playerProfile))
                .put(KillAuraC.class, new KillAuraC(playerProfile))
                .put(ImpossiblePitch.class, new ImpossiblePitch(playerProfile))
                .build();
    }
}
