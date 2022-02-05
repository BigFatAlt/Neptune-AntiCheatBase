package cc.bigfatman.anticheat.command;

import cc.bigfatman.anticheat.Neptune;
import cc.bigfatman.anticheat.command.impl.AlertsCommand;
import cc.bigfatman.anticheat.command.impl.VerboseCommand;
import cc.bigfatman.anticheat.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class NeptuneCommand implements CommandExecutor {
    public final Neptune plugin;
    public List<AbstractCommand> commands = new ArrayList<>();

    public NeptuneCommand(Neptune plugin) {
        this.plugin = plugin;

        this.commands.add(new VerboseCommand());
        this.commands.add(new AlertsCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < commands.size(); i++) {
                final AbstractCommand abstractCommand = commands.get(i);
                if (args[0].equalsIgnoreCase(abstractCommand.commandName)) {
                    if (!sender.hasPermission(abstractCommand.permission)) {
                        sender.sendMessage(MessageUtils.translateColour("&cYou do not have enough permissions to execute this command."));
                        return true;
                    }
                    abstractCommand.executeCommand(sender, args);
                    return true;
                }
            }
        } else {
            helpMessage(sender);
            return true;
        }
        helpMessage(sender);
        return true;
    }

    public void helpMessage(CommandSender commandSender) {
        commandSender.sendMessage(MessageUtils.translateColour(""));
        commandSender.sendMessage(MessageUtils.translateColour("&7* &B&lNeptune &3[AntiCheat] &7* "));
        commandSender.sendMessage(MessageUtils.translateColour(""));
        for (int i = 0; i < commands.size(); i++) {
            if (!commandSender.hasPermission(commands.get(i).permission)) return;
            commandSender.sendMessage(MessageUtils.translateColour("&7* &B/Neptune " + commands.get(i).commandName + " &7- " + commands.get(i).description));
        }
        commandSender.sendMessage("");
    }
}
