package io.github.sunpm.linereference;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.util.io.FileUtil;

enum CopyFormat {
    LINE_REFERENCE("Line reference", "lineReference"),
    CLAUDE_CODE("Claude Code", "claudeCode");

    private static final String PREFIX = "io.github.sunpm.copylinereference.";
    final String label;
    private final String settingsKey;

    CopyFormat(String label, String key) {
        this.label = label;
        this.settingsKey = PREFIX + key;
    }

    boolean isEnabled() {
        return PropertiesComponent.getInstance().getBoolean(settingsKey + ".enabled", this == LINE_REFERENCE);
    }

    PathMode localPathMode() {
        boolean relative = PropertiesComponent.getInstance().getBoolean(settingsKey + ".relativePaths", this == CLAUDE_CODE);
        return relative ? PathMode.RELATIVE : PathMode.ABSOLUTE;
    }

    void save(boolean enabled, PathMode localPath) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue(settingsKey + ".enabled", enabled, this == LINE_REFERENCE);
        properties.setValue(settingsKey + ".relativePaths", localPath == PathMode.RELATIVE, this == CLAUDE_CODE);
    }

    boolean usesRelativePaths() {
        PathMode global = PathMode.global();
        return (global == PathMode.PER_FORMAT ? localPathMode() : global) == PathMode.RELATIVE;
    }

    String reference(String path, String lines) {
        return switch (this) {
            case LINE_REFERENCE -> FileUtil.toSystemDependentName(path) + ":" + lines;
            case CLAUDE_CODE -> {
                String target = path + "#L" + lines;
                yield path.chars().anyMatch(Character::isWhitespace) ? "@\"" + target + "\"" : "@" + target;
            }
        };
    }

    enum PathMode {
        ABSOLUTE("Absolute"),
        RELATIVE("Relative to project root"),
        PER_FORMAT("Set per format");

        private final String label;

        PathMode(String label) {
            this.label = label;
        }

        static PathMode global() {
            PropertiesComponent properties = PropertiesComponent.getInstance();
            if (properties.getBoolean(PREFIX + "perFormat")) return PER_FORMAT;
            return properties.getBoolean(PREFIX + "relativePaths") ? RELATIVE : ABSOLUTE;
        }

        void saveGlobal() {
            PropertiesComponent properties = PropertiesComponent.getInstance();
            properties.setValue(PREFIX + "perFormat", this == PER_FORMAT, false);
            if (this != PER_FORMAT) {
                properties.setValue(PREFIX + "relativePaths", this == RELATIVE, false);
            }
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
