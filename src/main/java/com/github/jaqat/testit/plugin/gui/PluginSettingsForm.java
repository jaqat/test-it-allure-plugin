package com.github.jaqat.testit.plugin.gui;

import com.github.jaqat.testit.api.client.TestItApiClientBuilder;
import com.github.jaqat.testit.plugin.settings.PluginSettings;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Objects;

public class PluginSettingsForm {
    private JPanel rootPanel;
    private PluginSettings settings;

    private JTextField url;
    private JTextField apiKey;
    private JTextField projectId;

    private JTextField statusField;
    private JButton testButton;

    public PluginSettingsForm() {

        testButton.addActionListener(e -> handleTestButton());
        statusField.setVisible(false);
        statusField.setText("");
    }

    public void createUI(Project project) {
        settings = PluginSettings.getInstance(project);
        apiKey.setText(Objects.requireNonNull(settings).getApiKey());
        url.setText(settings.getServerUrl());
        projectId.setText(String.valueOf(settings.getProjectId()));
    }

    private void handleTestButton() {
        statusField.setVisible(true);
        if (isEmpty(url)) {
            statusField.setText("Field \"Test-It server url\" is required !");
        }

        if (isEmpty(apiKey)) {
            statusField.setText("Field \"API key\" is required !");
        }

        try {
            TestItApiClientBuilder.buildTestItApiClient(url.getText(), apiKey.getText()).getProjects();
            statusField.setText("Connection successful!");
        } catch (Exception e) {
            statusField.setText("Connection failed: " + e.getMessage());
        }
    }

    private boolean isEmpty(final JTextField field) {
        return StringUtils.isBlank(field.getText());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void apply() {
        settings.setApiKey(apiKey.getText());
        settings.setServerUrl(url.getText());
        settings.setProjectId(Integer.parseInt(projectId.getText()));
    }

    public void reset() {
        apiKey.setText(settings.getApiKey());
        url.setText(settings.getServerUrl());
        projectId.setText(String.valueOf(settings.getProjectId()));
        statusField.setVisible(false);
        statusField.setText("");
    }

    public boolean isModified() {
        Integer projectId = Integer.parseInt(this.projectId.getText());
        boolean modified = !apiKey.getText().equals(settings.getApiKey());
        modified |= !url.getText().equals(settings.getServerUrl());
        modified |= !projectId.equals(settings.getProjectId());
        return modified;
    }
}
