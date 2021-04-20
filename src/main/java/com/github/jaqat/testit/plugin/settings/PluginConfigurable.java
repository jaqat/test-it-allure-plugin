package com.github.jaqat.testit.plugin.settings;

import com.github.jaqat.testit.plugin.gui.PluginSettingsForm;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginConfigurable implements Configurable {

    private PluginSettingsForm exporterForm;

    @SuppressWarnings("FieldCanBeLocal")
    private final Project mProject;

    public PluginConfigurable(Project mProject) {
        this.mProject = mProject;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Test-It Plugin";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        exporterForm = new PluginSettingsForm();
        exporterForm.createUI(mProject);
        return exporterForm.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return exporterForm.isModified();
    }

    @Override
    public void apply() {
        exporterForm.apply();
    }

    @Override
    public void reset() {
        exporterForm.reset();
    }

    @Override
    public void disposeUIResources() {
        exporterForm = null;
    }
}
