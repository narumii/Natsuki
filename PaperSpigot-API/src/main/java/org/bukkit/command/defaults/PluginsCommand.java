package org.bukkit.command.defaults;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class PluginsCommand extends BukkitCommand {
    public PluginsCommand(String name) {
        super(name);
        this.description = "Gets a list of plugins running on the server";
        this.usageMessage = "/plugins";
        this.setPermission("bukkit.command.plugins");
        this.setAliases(Arrays.asList("pl"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (sender.isOp() && (args.length > 0 && args[0].equalsIgnoreCase("toggle"))) {
            final String plugin = args[1];
            final Plugin p = Bukkit.getServer().getPluginManager().getPlugin(plugin);
            if (p.isEnabled()) {
                Bukkit.getServer().getPluginManager().disablePlugin(p);
                sender.sendMessage(" §8» §7Disabled plugin: §d" + plugin);
            } else {
                Bukkit.getServer().getPluginManager().enablePlugin(p);
                sender.sendMessage(" §8» §7Enabled plugin: §d" + plugin);
            }
        } else {
            if (sender instanceof ConsoleCommandSender)
                sender.sendMessage(getConsolePluginList());
            else
                ((Player) sender).sendMessage(getPluginList());
        }
        return true;
    }

    private TextComponent[] getPluginList() {
        final Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int enabled = 0, disbaled = 0, offset = 1;
        final TextComponent[] textComponents = new TextComponent[plugins.length + 1];
        for (final Plugin plugin : plugins) {
            if (plugin.isEnabled()) enabled++;
            else disbaled++;
            final ChatColor color = plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED;
            textComponents[offset] = new TextComponent(color + plugin.getDescription().getName() + ChatColor.WHITE + ", ");
            textComponents[offset].setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + plugin.getDescription().getDescription()).create()));
            textComponents[offset].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pl toggle " + plugin.getDescription().getName()));
            offset++;
        }
        if (plugins.length != 0) {
            textComponents[plugins.length].setText(textComponents[plugins.length].getText().replace(",", ""));
            textComponents[0] = new TextComponent(" §8» §7Plugins: §8(§a" + enabled + "§8/§c" + disbaled + "§8/§7" + plugins.length + "§8)§7: ");
        }
        if (plugins.length == 0)
            return new TextComponent[]{new TextComponent(" §8» §7Server doesn't contains any plugins.")};
        else return textComponents;
    }

    private String getConsolePluginList() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int enabled = 0, disbaled = 0;

        for (final Plugin plugin : plugins) {
            if (plugin.isEnabled()) enabled++;
            else disbaled++;

            if (stringBuilder.length() > 0) {
                stringBuilder.append(ChatColor.WHITE);
                stringBuilder.append(", ");
            }

            stringBuilder.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
            stringBuilder.append(plugin.getDescription().getName());
        }

        if (plugins.length == 0) return " §8» §7Server doesn't contains any plugins.";
        else
            return " §8» §7Plugins: §8(§a" + enabled + "§8/§c" + disbaled + "§8/§7" + plugins.length + "§8)§7: " + stringBuilder.toString();
    }

    /*private String getPluginList() {
        StringBuilder pluginList = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

        for (Plugin plugin : plugins) {
            if (pluginList.length() > 0) {
                pluginList.append(ChatColor.WHITE);
                pluginList.append(", ");
            }

            pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
            pluginList.append(plugin.getDescription().getName());
        }

        return "(" + plugins.length + "): " + pluginList.toString();
    }*/

    // Spigot Start
    @Override
    public java.util.List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return java.util.Collections.emptyList();
    }
    // Spigot End
}
