package io.github.sunpm.linereference;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;

import javax.swing.JComponent;
import javax.swing.JPanel;

public final class PathFormatSettings implements Configurable {
    private static final String RELATIVE_PATHS = "io.github.sunpm.copylinereference.relativePaths";
    private ComboBox<String> format;

    static boolean useRelativePaths() {
        return PropertiesComponent.getInstance().getBoolean(RELATIVE_PATHS, false);
    }

    @Override
    public String getDisplayName() {
        return "Copy Line Reference";
    }

    @Override
    public JComponent createComponent() {
        format = new ComboBox<>(new String[]{"Absolute", "Relative to project root"});
        return FormBuilder.createFormBuilder()
                .addLabeledComponent("Path format:", format)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    @Override
    public boolean isModified() {
        return (format.getSelectedIndex() == 1) != useRelativePaths();
    }

    @Override
    public void apply() {
        PropertiesComponent.getInstance().setValue(RELATIVE_PATHS, format.getSelectedIndex() == 1, false);
    }

    @Override
    public void reset() {
        format.setSelectedIndex(useRelativePaths() ? 1 : 0);
    }

    @Override
    public void disposeUIResources() {
        format = null;
    }
}
