package cc.bigfatman.anticheat.command.impl;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.command.AbstractCommand;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.util.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand extends AbstractCommand {

    public AlertsCommand() {
        super("alerts", "neptune.staff.alerts", "toggle alerts on/off");
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        PlayerProfile playerProfile = Neptune.getInstance().getProfileManager().getPlayerProfile(player.getUniqueId());

        if (playerProfile.getAlerts().contains(player.getUniqueId())) {
            playerProfile.getAlerts().remove(player.getUniqueId());
            player.sendMessage(MessageUtils.translateColour("&cYou have disabled alerts."));
        } else if (!playerProfile.getAlerts().contains(player.getUniqueId())) {
            playerProfile.getAlerts().add(player.getUniqueId());
            player.sendMessage(MessageUtils.translateColour("&aYou have toggled alerts."));
        }
    }
}
