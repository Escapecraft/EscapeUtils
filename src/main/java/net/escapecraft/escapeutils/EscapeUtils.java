package net.escapecraft.escapeutils;

import net.escapecraft.component.ComponentManager;
import net.escapecraft.component.Log;
import net.serubin.LastComponent.LastComponent;
import org.bukkit.plugin.java.JavaPlugin;

public class EscapeUtils extends JavaPlugin {

    public static EscapeUtils self = null;
    private static final String logPrefix = "EscapeUtil";
    private Log log = new Log(logPrefix);
    private ComponentManager componentManager;

    /**
     * Gets the plugin log prefix.
     *
     * @return the log prefix string
     */
    public String getLogPrefix() {
        return logPrefix;
    }

    /**
     * Runs plugin initialization.
     */
    @Override
    public void onEnable() {
        self = this;

        log.info("starting loading...");

        // load/creates/fixes config
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        // start the component manager
        componentManager = new ComponentManager(this, log);
        componentManager.addComponent(LastComponent.class);

        // start components
        componentManager.startupComponents();
        log.info("...loading complete");
    }

    /**
     * Runs plugin shutdown cleanup.
     */
    @Override
    public void onDisable() {
        log.info("unloading...");
        getComponentManager().shutdownComponents();
        self = null;
        log.info("unloaded");
    }

    /**
     * Returns the log
     *
     * @return the log
     */
    public Log getLog() {
        return this.log;
    }

    /**
     * Returns the component manager.
     *
     * @return the component manager
     */
    public ComponentManager getComponentManager() {
        return componentManager;
    }
}
