package unimilk.dirtreeprinter.api.settings;

import java.util.List;

public interface ISettings {

    /**
     * Get the default expanded layers currently.
     * @return the default expanded layer counts
     */
    int getDefaultExpandedLayers();

    /**
     * Set the default expanded layers.
     * @param defaultExpandedLayers the default expanded layer counts to be set
     */
    void setDefaultExpandedLayers(int defaultExpandedLayers);

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
     * Add a filter rule to the rule list at a specific index.
     * @param rule the rule to be added
     * @param index the index where the rule should be added
     */
    void addRule(String rule, int index);

    /**
     * Remove a filter rule from the rules list.
     * @param rule  the rule to be removed
     * @return <code>true</code> if the rule is removed successfully; <code>false</code> otherwise.
     */
    boolean removeRule(String rule);

    /**
     * Remove a filter rule from the rules list by its index.
     * @param index the index of the rule to be moved
     * @return the moved rule
     */
    String removeRule(int index);

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
     * Mark that the settings have unsaved modification.
     */
    void markDirty();

    /**
     * Copy the settings to a new instance.
     * @return   a new Settings instance that contains the same settings as the old
     */
    ISettings copy();

}
