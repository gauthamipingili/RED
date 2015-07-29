package org.robotframework.ide.eclipse.main.plugin.tableeditor.settings;

import static com.google.common.collect.Lists.newArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.robotframework.ide.eclipse.main.plugin.model.RobotElement;
import org.robotframework.ide.eclipse.main.plugin.model.RobotSetting;
import org.robotframework.ide.eclipse.main.plugin.model.RobotSettingsSection;

import com.google.common.base.Optional;

class GeneralSettingsModel {

    private final Map<String, RobotElement> settings = new LinkedHashMap<>(); {
        settings.put("Suite Setup", null);
        settings.put("Suite Teardown", null);
        settings.put("Test Setup", null);
        settings.put("Test Teardown", null);
        settings.put("Test Template", null);
        settings.put("Test Timeout", null);
        settings.put("Force Tags", null);
        settings.put("Default Tags", null);
    }

    private RobotSettingsSection settingsSection = null;

    private RobotSetting documentation = null;
    
    RobotSettingsSection getSection() {
        return settingsSection;
    }

    boolean areSettingsExist() {
        return settingsSection != null;
    }

    void update(final Optional<RobotElement> settingsSection) {
        this.settingsSection = null;
        this.documentation = null;
        for (final String key : settings.keySet()) {
            settings.put(key, null);
        }

        if (settingsSection.isPresent()) {
            this.settingsSection = (RobotSettingsSection) settingsSection.get();
            for (final RobotElement setting : settingsSection.get().getChildren()) {
                final String settingName = setting.getName();
                if (settings.containsKey(settingName)) {
                    settings.put(settingName, setting);
                } else if ("Documentation".equals(settingName)) {
                    documentation = (RobotSetting) setting;
                }
            }
        }
    }

    RobotSetting getDocumentationSetting() {
        return documentation;
    }

    String getDocumentation() {
        return documentation != null && !documentation.getArguments().isEmpty() ? documentation.getArguments().get(0)
                : "";
    }

    Set<Entry<String, RobotElement>> getEntries() {
        return settings.entrySet();
    }

    boolean contains(final RobotSetting setting) {
        return settings.values().contains(setting);
    }

    public List<RobotElement> getSettings() {
        final List<RobotElement> collectedSettings = newArrayList();
        collectedSettings.add(documentation);
        for (final RobotElement element : settings.values()) {
            collectedSettings.add(element);
        }
        return collectedSettings;
    }
}
