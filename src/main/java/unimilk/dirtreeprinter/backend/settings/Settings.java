package unimilk.dirtreeprinter.backend.settings;

import unimilk.dirtreeprinter.api.settings.FilterMode;
import unimilk.dirtreeprinter.api.settings.ISettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Settings implements ISettings {

    private FilterMode filterMode;
    private final List<String> rules = new ArrayList<>();
    private boolean modified = false;

    Settings() {
        this.filterMode = FilterMode.BLACKLIST;
    }

    @Override
    public FilterMode getFilterMode() {
        return filterMode;
    }

    @Override
    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
        modified = true;
    }

    @Override
    public List<String> getRules() {
        return rules;
    }

    @Override
    public void addRule(String rule) {
        if (!rules.contains(rule)) {
            rules.add(rule);
            modified = true;
        }
    }

    @Override
    public boolean removeRule(String rule) {
        if (rules.remove(rule)) {
            modified = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void markClean() {
        modified = false;
    }

    @Override
    public void markDirty() {
        modified = true;
    }

    @Override
    public Settings copy() {
        Settings newSettings = new Settings();
        newSettings.setFilterMode(this.getFilterMode());
        this.rules.forEach(newSettings::addRule);
        if (this.isModified()) {
            newSettings.markDirty();
        } else {
            newSettings.markClean();
        }
        return newSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Settings)) return false;
        Settings settings = (Settings) o;
        return isModified() == settings.isModified() && getFilterMode() == settings.getFilterMode() && Objects.equals(getRules(), settings.getRules());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFilterMode(), getRules(), isModified());
    }
}
