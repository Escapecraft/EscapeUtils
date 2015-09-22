package net.serubin.LastComponent;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapeutils.EscapeUtils;
import net.serubin.utilities.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by seru on 8/9/15.
 */


@ComponentDescriptor(name = "LastReport", slug = "last", version = "1.0")
@BukkitCommand(command = {"last"})
public class LastComponent extends AbstractComponent implements CommandExecutor {
	private EscapeUtils plugin;

	// command slugs
	private final String COMMAND_HELP = "help";
	private final String COMMAND_DEATH = "death";
	private final String COMMAND_LOGIN = "login";

	// permissions slugs
	private final String PERMISSION_DEATH_SELF = "EscapeUtils.last.death.self";
	private final String PERMISSION_DEATH_OTHER = "EscapeUtils.last.death.other";

	private final String PERMISSION_LOGIN_SELF = "EscapeUtils.last.login.self";
	private final String PERMISSION_LOGIN_OTHER = "EscapeUtils.last.login.other";

	// Message strings
	private final String PERMISSION_ERROR = ChatColor.RED + "You don't have permission.";

	// Data Provider
	protected LastDataProvider lastData;
	protected LastPlayerListener lastListener;

	/**
	 * Called upon being enabled.
	 *
	 * @param plugin instance of EscapePlug
	 */
	@Override
	public boolean enable(EscapeUtils plugin) {
		this.plugin = plugin;

		plugin.getComponentManager().registerCommands(this);

		lastData = new LastDataProvider(plugin, this);
		lastData.fetchData();
		lastListener = new LastPlayerListener(plugin, this);

		return true;
	}

	/**
	 * Called during onDisable().
	 */
	@Override
	public void disable() {
		lastData.pushData();
	}

	public boolean onCommand(CommandSender sender, Command cmd,
							 String commandLabel, String[] args) {
		// Handle not enough arguments
		if (args.length < 1)
			return false;

		String command = args[0];

		if (command.equalsIgnoreCase(this.COMMAND_HELP)) {  // Help!

		} else if (command.equalsIgnoreCase(this.COMMAND_LOGIN)) {  // Login command
			if (args.length > 1) {
				String playerName = args[1];

				UUID uuid = null;

				try {
					uuid = UUIDFetcher.getUUIDOf(playerName);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "No player could be located");
					e.printStackTrace();
					return true;
				}

				if (uuid == null) {
					sender.sendMessage(ChatColor.RED + "No player could be located");
					return true;
				}

				if (!lastData.logins.containsKey(uuid)) {
					sender.sendMessage(ChatColor.RED + "No death recorded");
					return true;
				}

				Date lastSeen = new Date(new Double(lastData.logins.get(uuid) * 1000).longValue());
				SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yy HH:mm:yy");

				sender.sendMessage(ChatColor.GREEN + playerName + ChatColor.YELLOW + " was last seen " + ChatColor.GREEN + date_format.format(lastSeen));

			}
		} else if (command.equalsIgnoreCase(this.COMMAND_DEATH)) {  // Death command

			if (args.length == 1 && sender instanceof Player) {
				Player player = (Player) sender;
				UUID uuid = player.getUniqueId();

				if (!lastData.deaths.containsKey((uuid))) {
					sender.sendMessage(ChatColor.RED + "No death recorded");
					return true;
				}

				Location lastDeath = lastData.deaths.get(uuid);
				sender.sendMessage(ChatColor.YELLOW + "You died at X:" + ChatColor.GREEN + lastDeath.getBlockX() + ChatColor.YELLOW + " Y:" + ChatColor.GREEN + lastDeath.getBlockY() + ChatColor.YELLOW + " Z:" + ChatColor.GREEN + lastDeath.getBlockZ());

				return true;
			}
		}
		return true;
	}

	private void sendHelp(CommandSender sender) {

	}

	protected void logInfo(String msg) {
		log.info(msg);
	}

	protected void logWarning(String msg) {
		log.warning(msg);
	}

	public LastDataProvider getLastDataProvider() {
		return this.lastData;
	}
}
