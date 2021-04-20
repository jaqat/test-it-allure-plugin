package com.github.jaqat.testit.plugin.gui;

import com.github.jaqat.testit.plugin.settings.AutotestDialogSettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutotestSettingsDialog extends DialogWrapper {
    private JPanel contentPane;
    //private JButton buttonOK;
    //private JButton buttonCancel;
    private JRadioButton autotestExternalIdManualValueRadioButton;
    private JRadioButton autotestExternalIdRandomUUIDRadioButton;
    private JTextField autotestExternalIdField;
    private JTextField manualTestsIdsField;

    AutotestDialogSettings autotestDialogSettings;
    private PropertiesComponent properties;
    private Project project;

    public AutotestSettingsDialog(Project project, AutotestDialogSettings autotestDialogSettings) {
        super(project);
        init();
        setModal(true);
        initDialog();
        setTitle("Test-It autotest properties");
        this.autotestDialogSettings = autotestDialogSettings;

        autotestExternalIdManualValueRadioButton.addActionListener(actionEvent -> {
                    autotestExternalIdManualValueRadioButton.setSelected(true);
                    autotestExternalIdField.setEnabled(true);
                    autotestExternalIdRandomUUIDRadioButton.setSelected(false);
                }
        );

        autotestExternalIdRandomUUIDRadioButton.addActionListener(actionEvent -> {
                    autotestExternalIdManualValueRadioButton.setSelected(false);
                    autotestExternalIdField.setEnabled(false);
                    autotestExternalIdRandomUUIDRadioButton.setSelected(true);
                }
        );

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> doCancelAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    private void initDialog() {
        autotestExternalIdRandomUUIDRadioButton.setSelected(true);
        autotestExternalIdManualValueRadioButton.setSelected(false);
        autotestExternalIdField.setEnabled(false);
    }

    private void saveState() {
        if (autotestExternalIdRandomUUIDRadioButton.isSelected()) {
            autotestDialogSettings.setAutotestExternalId(UUID.randomUUID().toString());
        } else {
            autotestDialogSettings.setAutotestExternalId(autotestExternalIdField.getText());
        }
        autotestDialogSettings.setManualTestsIds(
                manualTestsIdsField.getText().trim().isEmpty() ?
                        Collections.EMPTY_LIST :
                        Stream.of(
                                manualTestsIdsField.getText().split(",")
                        )
                                .map(String::trim)
                                .collect(Collectors.toList())
        );
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }


    @Override
    public void doOKAction() {
        saveState();
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }
}
