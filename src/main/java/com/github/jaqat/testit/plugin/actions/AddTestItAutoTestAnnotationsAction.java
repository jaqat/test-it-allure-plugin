package com.github.jaqat.testit.plugin.actions;

import com.github.jaqat.testit.plugin.AnnotationProcessor;
import com.github.jaqat.testit.plugin.gui.AutotestSettingsDialog;
import com.github.jaqat.testit.plugin.settings.AutotestDialogSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public class AddTestItAutoTestAnnotationsAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final PsiElement element = event.getData(PlatformDataKeys.PSI_ELEMENT);
        if (element instanceof PsiMethod) {

            AutotestDialogSettings autoTestDialogSettings = new AutotestDialogSettings();
            AutotestSettingsDialog autotestSettingsDialog = new AutotestSettingsDialog(
                    event.getProject(),
                    autoTestDialogSettings
            );
            autotestSettingsDialog.show();

            if (autotestSettingsDialog.isOK()) {
                PsiMethod testMethod = (PsiMethod) element;
                AnnotationProcessor.createAutoTestAnnotations(testMethod, autoTestDialogSettings);
            }
        }
    }


}
