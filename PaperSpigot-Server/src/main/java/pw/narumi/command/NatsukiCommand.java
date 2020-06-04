package pw.narumi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pw.narumi.Natsuki;
import pw.narumi.object.Holder;

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
        sender.sendMessage(" §8» §7Server UID: §d" + Natsuki.getInstance().getUID()[0]);
        sender.sendMessage(" §8» §7Blacklisted joins: §d" + Holder.getBlacklistedJoins().get());
        sender.sendMessage(" ");
        return false;
    }
}
