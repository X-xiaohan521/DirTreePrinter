package unimilk.dirtreeprinter.api.settings;

import java.util.List;

public interface ISettings {
    /**
     * Get the filter mode used currently.
     * @return FilterMode: BLACKLIST / WHITELIST
     */
    FilterMode getFilterMode();

    /**
     * Set the filter mode.
     * @param filterMode  the new filter mode
     */
    void setFilterMode(FilterMode filterMode);

    /**
     * Get the filter rules used currently.
     * @return a List of String contains filter rules used currently.
     */
    List<String> getRules();

    /**
     * Add a filter rule to the rule list.
     * @param rule  a new rule to be added
     */
    void addRule(String rule);

    /**
     * Remove a filter rule from the rules list.
     * @param rule  the rule to be removed
     * @return <code>true</code> if the rule is removed successfully; <code>false</code> otherwise.
     */
    boolean removeRule(String rule);

    /**
     * Get the modification status of the settings.
     * @return  <code>true</code> if settings has been modified
     */
    boolean isModified();

    /**
     * Mark that the settings haven't been modified.
     */
    void markClean();

    /**
     * Copy the settings to a new instance.
     * @return   a new Settings instance that contains the same settings as the old
     */
    ISettings copy();
}
