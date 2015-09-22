package net.serubin.LastComponent;

import net.escapecraft.escapeutils.EscapeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Serubin on 8/17/2015.
 */
public class LastPlayerListener implements Listener{
	private final LastComponent last;
	private final LastDataProvider lastData;
	private final EscapeUtils plugin;


	public LastPlayerListener(EscapeUtils plugin, LastComponent last){
		this.plugin = plugin;
		this.last = last;
		this.lastData = last.lastData;


		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getLog().info("loaded listener");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(PlayerDeathEvent e){
		if(! (e.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player) e.getEntity();
		this.lastData.deaths.put(player.getUniqueId(), player.getLocation());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerQuitEvent(PlayerQuitEvent e){
		Player player = e.getPlayer();
		this.lastData.logins.put(player.getUniqueId(), System.currentTimeMillis()/1000D);
	}
}


