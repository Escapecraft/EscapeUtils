package net.serubin;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapeutil.EscapeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by seru on 8/9/15.
 */


@ComponentDescriptor(name = "posreport", slug = "pos", version = "1.0")
@BukkitCommand(command = {"pos"})
public class PosComponent extends AbstractComponent implements CommandExecutor {
    private EscapeUtil plugin;

    // permissions slugs
    private final String PERMISSION_SELF = "escapeutil.pos.self";
    private final String PERMISSION_OTHER = "escapeutil.pos.other";

    // Message strings
    private final String PERMISSION_ERROR = ChatColor.RED + "You don't have permission.";
    private final String NO_PLAYER = ChatColor.RED + "Player not found.";

    /**
     * Called upon being enabled.
     *
     * @param plugin instance of EscapePlug
     */
    @Override
    public boolean enable(EscapeUtil plugin) {
        this.plugin = plugin;
        plugin.getComponentManager().registerCommands(this);
        return true;
    }

    /**
     * Called during onDisable().
     */
    @Override
    public void disable() {

    }

    public boolean onCommand(CommandSender sender, Command cmd,
                             String commandLabel, String[] args) {

        if (args.length < 1 && sender.hasPermission(this.PERMISSION_SELF)) { // No args and has perm
            if (!(sender instanceof Player)) { // Ensure player
                sender.sendMessage(this.PERMISSION_ERROR);
                return true;
            }
            sendLocation(sender, (Player) sender);
        } else if (args.length == 1 && sender.hasPermission(this.PERMISSION_OTHER)) { // One Arg and has perm
            Player player = plugin.getServer().getPlayer(args[0]);
            if (player == null) { // Ensure player is online
                sender.sendMessage(this.NO_PLAYER);
                return true;
            }

            sendLocation(sender, player);
        }

        return true;
    }

    /**
     * Sends location via chat message
     *
     * @param sender user to send result too
     * @param target target of check
     */
    private void sendLocation(CommandSender sender, Player target) {
        Location loc = target.getLocation();

        // Coords
        sender.sendMessage(ChatColor.YELLOW + "Current location for " + ChatColor.GREEN + target.getDisplayName() +
                ChatColor.YELLOW + "is X: " + ChatColor.GREEN + loc.getBlockX() + ChatColor.YELLOW + ", Y: " +
                ChatColor.GREEN + loc.getBlockZ() + ChatColor.YELLOW + ", Z: " + ChatColor.GREEN + loc.getBlockZ());
        // World
        sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.GREEN + target.getWorld().getName());
    }
}
