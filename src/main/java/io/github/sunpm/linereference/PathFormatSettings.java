package io.github.sunpm.linereference;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import io.github.sunpm.linereference.CopyFormat.PathMode;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.EnumMap;

public final class PathFormatSettings implements Configurable {
    private final EnumMap<CopyFormat, FormatRow> rows = new EnumMap<>(CopyFormat.class);
    private ComboBox<PathMode> globalPath;

    private record FormatRow(JBCheckBox enabled, ComboBox<PathMode> path) {}

    @Override
    public String getDisplayName() {
        return "Copy Line Reference";
    }

    @Override
    public JComponent createComponent() {
        int width = JBUI.scale(260);
        globalPath = new ComboBox<>(PathMode.values(), width);
        FormBuilder form = FormBuilder.createFormBuilder().addLabeledComponent("Path format:", globalPath);
        for (CopyFormat format : CopyFormat.values()) {
            JBCheckBox enabled = new JBCheckBox(format.label);
            ComboBox<PathMode> path = new ComboBox<>(new PathMode[]{PathMode.ABSOLUTE, PathMode.RELATIVE}, width);
            path.getAccessibleContext().setAccessibleName(format.label + " path format");
            rows.put(format, new FormatRow(enabled, path));
            form.addLabeledComponent(enabled, path);
            enabled.addItemListener(event -> updatePathControls());
        }
        globalPath.addActionListener(event -> updatePathControls());
        reset();
        return form.addComponentFillVertically(new JPanel(), 0).getPanel();
    }

    private void updatePathControls() {
        boolean perFormat = globalPath.getItem() == PathMode.PER_FORMAT;
        for (FormatRow row : rows.values()) {
            row.path.setEnabled(perFormat && row.enabled.isSelected());
        }
    }

    @Override
    public boolean isModified() {
        if (globalPath.getItem() != PathMode.global()) return true;
        for (var entry : rows.entrySet()) {
            CopyFormat format = entry.getKey();
            FormatRow row = entry.getValue();
            if (row.enabled.isSelected() != format.isEnabled() || row.path.getItem() != format.localPathMode()) return true;
        }
        return false;
    }

    @Override
    public void apply() {
        globalPath.getItem().saveGlobal();
        rows.forEach((format, row) -> format.save(row.enabled.isSelected(), row.path.getItem()));
    }

    @Override
    public void reset() {
        globalPath.setItem(PathMode.global());
        rows.forEach((format, row) -> {
            row.enabled.setSelected(format.isEnabled());
            row.path.setItem(format.localPathMode());
        });
        updatePathControls();
    }

    @Override
    public void disposeUIResources() {
        globalPath = null;
        rows.clear();
    }
}
