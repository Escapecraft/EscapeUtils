package net.escapecraft.escapeutil;

import net.serubin.PosComponent;
import org.bukkit.plugin.java.JavaPlugin;

import net.escapecraft.component.ComponentManager;
import net.escapecraft.component.Log;

public class EscapeUtil extends JavaPlugin {

    public static EscapeUtil self = null;
    private static final String logPrefix = "EscapeUtil";
    private Log log = new Log(logPrefix);
    private ComponentManager componentManager;

    /**
     * Gets the plugin log prefix.
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
        componentManager.addComponent(PosComponent.class);

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
     * Returns the component manager.
     * 
     * @return the component manager
     */
    public ComponentManager getComponentManager() {
        return componentManager;
    }
}
