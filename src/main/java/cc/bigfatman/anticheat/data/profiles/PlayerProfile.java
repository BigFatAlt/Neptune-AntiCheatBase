package cc.bigfatman.anticheat.data.profiles;

import cc.bigfatman.anticheat.check.CheckManager;
import cc.bigfatman.anticheat.data.Profile;
import cc.bigfatman.anticheat.data.processor.CollisionProcessor;
import cc.bigfatman.anticheat.data.processor.PositionProcessor;
import cc.bigfatman.anticheat.data.processor.RotationProcessor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class PlayerProfile implements Profile {
    private final UUID uuid;
    private Player player;

    private CheckManager checkManager;

    private PositionProcessor positionProcessor;
    private RotationProcessor rotationProcessor;
    private CollisionProcessor collisionProcessor;

    private ActionProfile actionProfile;

    public List<UUID> verboseAlerts = new ArrayList<>();
    public List<UUID> alerts = new ArrayList<>();

    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);

        this.checkManager = new CheckManager(this);

        this.positionProcessor = new PositionProcessor(this);
        this.rotationProcessor = new RotationProcessor(this);
        this.collisionProcessor = new CollisionProcessor(this);

        this.actionProfile = new ActionProfile(this);

        if (player.hasPermission("neptune.staff.alerts")) alerts.add(player.getUniqueId());
    }
}
