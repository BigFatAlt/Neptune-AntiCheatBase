package cc.bigfatman.anticheat.check;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.data.profiles.ActionProfile;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.NeptunePacket;
import cc.bigfatman.anticheat.util.MessageUtils;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public abstract class Check {

    public String checkName, checkID, checkDescription;
    public boolean enabled, rubberband;
    public float threshold, violations;

    public CheckData checkData;
    public final PlayerProfile playerProfile;


    public Check(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
        this.checkData = this.getClass().getAnnotation(CheckData.class);

        if (!this.getClass().isAnnotationPresent(CheckData.class)) {
            System.out.println("@CheckData missing in " + getClass().getSimpleName());
        }

        this.checkName = checkData.checkName();
        this.checkID = checkData.checkID();
        this.checkDescription = checkData.description();

        this.enabled = checkData.enabled();
        this.rubberband = checkData.rubberband();
    }

    public void verbose(String information, float maxThreshold) {
        ++threshold;
        Neptune.getInstance().getProfileManager().profileMap.values().stream()
                .filter(player -> player.getPlayer().hasPermission("neptune.staff.verbose") && player.getVerboseAlerts().contains(player.getPlayer().getUniqueId())).forEach(player -> {
                    String message = MessageUtils.translateColour("&bVerbose | &f" + playerProfile.getPlayer().getName() + " &b| " + checkName  + " (" + checkID + ")" + " &b| &7" + information);
                    player.getPlayer().sendMessage(message);
                });

        if (threshold > maxThreshold) {
            fail(information);
        }
    }

    public void fail(String info) {
        Neptune.getInstance().getProfileManager().profileMap.values().stream()
                .filter(player -> player.getPlayer().hasPermission("neptune.staff.alerts") && player.getAlerts().contains(player.getPlayer().getUniqueId())).forEach(player -> {
                    violations++;
                    String message = MessageUtils.translateColour("&3[&bNAC&3] &b" + playerProfile.getPlayer().getName() + " &ffailed| &b" + checkName + " (" + checkID + ") &f| information: &b" + info + " &f| vl: &b" + violations);
                    player.getPlayer().sendMessage(message);
                });
    }

    public abstract void handleCheck(NeptunePacket neptunePacket);

    public void decreaseThreshold(float amount) {
        threshold = Math.max(0, threshold - amount);
    }
}
