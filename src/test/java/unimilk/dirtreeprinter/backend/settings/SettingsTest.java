package unimilk.dirtreeprinter.backend.settings;

import org.junit.jupiter.api.Test;
import unimilk.dirtreeprinter.api.settings.ISettings;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    void removeRulesShouldWork() {
        ISettings actualSettings = new Settings();
        actualSettings.addRule(".idea");
        actualSettings.addRule(".vscode");
        actualSettings.removeRule(".idea");

        ISettings expectedSettings = new Settings();
        expectedSettings.addRule(".vscode");

        assertEquals(expectedSettings, actualSettings);
    }

    @Test
    void copiedSettingsShouldEqualOrigin() {
        ISettings originalSettings = new Settings();
        originalSettings.addRule("java");
        originalSettings.addRule("python");

        ISettings copiedSettings = originalSettings.copy();
        assertEquals(originalSettings, copiedSettings);
    }

    @Test
    void testHashCode() {
        Map<ISettings, Integer> settingsIndexMap = new HashMap<>();
        settingsIndexMap.put(new Settings(), 0);
        assertEquals(0, settingsIndexMap.get(new Settings()));
    }
}