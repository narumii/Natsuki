package pw.narumi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.narumi.Natsuki;
import pw.narumi.common.Holder;

public class NatsukiCommand extends Command {

    public NatsukiCommand() {
        super("Natsuki");
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        sender.sendMessage(" ");
        sender.sendMessage(" §8» §7Version: §d" + Natsuki.getInstance().getVersion());
        sender.sendMessage(" §8» §7Developer: §dなるみ#4977");
        sender.sendMessage(" §8» §7Discord: §dhttps://discord.gg/amutHux");
        sender.sendMessage(" ");

        return false;
    }
}
