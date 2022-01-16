package cc.bigfatman.anticheat;

import cc.bigfatman.anticheat.command.NeptuneCommand;
import cc.bigfatman.anticheat.data.ProfileManager;
import cc.bigfatman.anticheat.listener.ConnectionListener;
import cc.bigfatman.anticheat.packet.injection.PacketInjector;
import cc.bigfatman.anticheat.packet.injection.impl.ModernInjector;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Neptune extends JavaPlugin {
    @Getter private static Neptune instance;

    @Getter private ExecutorService executor;
    @Getter private PacketInjector packetInjector;

    @Getter private ProfileManager profileManager;
    @Getter private NeptuneCommand neptuneCommand;

    @Override
    public void onEnable() {
        instance = this;

        // Create executor service
        // Determine thread count based on available CPU cores/threads, max of 4
        final int threads = Math.max(Math.min(Runtime.getRuntime().availableProcessors() / 4, 4), 1);
        this.executor = Executors.newFixedThreadPool(threads);

        //Starting packet injection
        this.packetInjector = new ModernInjector();

        //Cleaner and better way to register events
        Arrays.asList(new ConnectionListener(this)).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        //Registering each profile we will need.
        this.profileManager = new ProfileManager(this);

        //Registering the command manager.
        this.neptuneCommand = new NeptuneCommand(this);
        this.getCommand("neptune").setExecutor(neptuneCommand);
    }

    @Override
    public void onDisable() {
        instance = null;

        this.packetInjector = null;
        this.profileManager.profileMap.clear();
    }
}
