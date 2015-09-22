package net.escapecraft.component;

import net.escapecraft.escapeutils.EscapeUtils;

/**
 * Represents an abstract component of EscapePlug.

 * @author james
 */
public abstract class AbstractComponent {

    protected Log log = null;

    /**
     * Gets the component log.
     * @returns the log
     */
    public Log getLog() {
        return log;
    }

    /**
     * Sets up the component log.
     * @param log component log
     */
    public void setLog(Log log) {
        this.log = log;
    }

    /**
     * Called upon being enabled.
     * @param plugin instance of EscapePlug
     */
    public abstract boolean enable(EscapeUtils plugin);

    /**
     * Called during onDisable().
     */
    public abstract void disable();

    /**
     * Called to tell the plugin to re-check it's config.
     */
    public void reloadConfig() {};
}
