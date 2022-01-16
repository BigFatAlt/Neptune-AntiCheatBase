package cc.bigfatman.anticheat.command.impl;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.command.AbstractCommand;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.util.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VerboseCommand extends AbstractCommand {

    public VerboseCommand() {
        super("verbose", "neptune.staff.verbose", "toggle verbose alerts on/off");
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        PlayerProfile playerProfile = Neptune.getInstance().getProfileManager().getPlayerProfile(player.getUniqueId());

        if (playerProfile.getVerboseAlerts().contains(player.getUniqueId())) {
            playerProfile.getVerboseAlerts().remove(player.getUniqueId());
            player.sendMessage(MessageUtils.translateColour("&5&l[NAC] &fYou have &cdisabled &fverbose alerts."));
        } else if (!playerProfile.getVerboseAlerts().contains(player.getUniqueId())) {
            playerProfile.getVerboseAlerts().add(player.getUniqueId());
            player.sendMessage(MessageUtils.translateColour("&5&l[NAC] &fYou have &atoggled &fverbose alerts."));
        }
    }
}
