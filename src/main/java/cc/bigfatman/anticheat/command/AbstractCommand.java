package cc.bigfatman.anticheat.command;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public abstract class AbstractCommand {

    public final String commandName, permission, description;

    public abstract void executeCommand(CommandSender commandSender, String[] args);
}
