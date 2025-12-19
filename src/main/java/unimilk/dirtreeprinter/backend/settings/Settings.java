package unimilk.dirtreeprinter.backend.settings;

import unimilk.dirtreeprinter.api.settings.FilterMode;
import unimilk.dirtreeprinter.api.settings.ISettings;

import java.util.ArrayList;
import java.util.List;

public class Settings implements ISettings {
    private FilterMode filterMode;
    private final List<String> rules = new ArrayList<>();

    public Settings() {
        this.filterMode = FilterMode.BLACKLIST;
    }

    @Override
    public FilterMode getFilterMode() {
        return filterMode;
    }

    @Override
    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    @Override
    public List<String> getRules() {
        return rules;
    }

    @Override
    public void addRule(String rule) {
        if (!rules.contains(rule)) {
            rules.add(rule);
        }
    }

    @Override
    public boolean removeRule(String rule) {
        return rules.remove(rule);
    }
}
