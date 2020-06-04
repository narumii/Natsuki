package pw.narumi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import pw.narumi.Natsuki;

public class NatsukiReloadCommand extends Command {

    public NatsukiReloadCommand() {
        super("NatsukiReload");
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Natsuki.getInstance().reload();
            sender.sendMessage(" §8» §7Reloaded natsuki");
            return false;
        }else {
            sender.sendMessage(" §8» §7Command only for console");
        }
        return false;
    }
}
