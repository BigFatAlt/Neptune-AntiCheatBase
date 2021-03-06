package cc.bigfatman.anticheat.listener;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.data.profiles.PlayerProfile;
import cc.bigfatman.anticheat.packet.injection.impl.ModernInjector;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    public final Neptune plugin;

    public ConnectionListener(Neptune plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = plugin.getProfileManager().getPlayerProfile(player.getUniqueId());

        //injecting the player into the packet stream/data profile
        plugin.getPacketInjector().inject(player);
        plugin.getProfileManager().addPlayer(player.getUniqueId());
    }

    @SneakyThrows
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = plugin.getProfileManager().getPlayerProfile(player.getUniqueId());

        //Stops memory leaks
        if (playerProfile.alerts.contains(player.getUniqueId())) playerProfile.alerts.remove(player.getUniqueId());
        if (playerProfile.verboseAlerts.contains(player.getUniqueId())) playerProfile.verboseAlerts.remove(player.getUniqueId());

        //removing players packets/data
        plugin.getPacketInjector().eject(player);
        plugin.getProfileManager().removePlayer(player.getUniqueId());
    }
}
