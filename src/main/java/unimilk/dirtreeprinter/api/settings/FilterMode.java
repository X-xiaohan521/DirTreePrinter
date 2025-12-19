package unimilk.dirtreeprinter.api.settings;

public enum FilterMode {
    BLACKLIST("blacklist", true),
    WHITELIST("whitelist", false);

    private final String name;
    private final boolean isBlacklist;

    FilterMode(String name, boolean isBlacklist) {
        this.name = name;
        this.isBlacklist = isBlacklist;
    }

    public static FilterMode of(String name) {
        if (name.equals(BLACKLIST.getName())) {
            return BLACKLIST;
        } else if (name.equals(WHITELIST.getName())) {
            return WHITELIST;
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isBlacklist() {
        return isBlacklist;
    }
}
