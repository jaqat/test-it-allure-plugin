package com.github.jaqat.testit.plugin;

import com.github.jaqat.testit.api.TestItApiWrapper;
import com.github.jaqat.testit.plugin.settings.AutotestDialogSettings;
import com.github.jaqat.testit.plugin.utils.PsiUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.jaqat.testit.api.TestItApiWrapper.getTestItApiWrapper;
import static java.lang.String.format;

public class AnnotationProcessor {

    static final String ALLURE_LINK_ANNOTATION = "io.qameta.allure.Link";
    static final String ALLURE_LINKS_ANNOTATION = "io.qameta.allure.Links";
    static final String ALLURE_TMS_LINK_ANNOTATION = "io.qameta.allure.TmsLink";

    private static PsiAnnotation createTmsLinkAnnotation(PsiMethod method, String autotestUuid) {
        return PsiUtils.createAnnotation(
                format("@%s(\"%s\")", ALLURE_TMS_LINK_ANNOTATION, autotestUuid),
                method
        );
    }

    public static List<PsiAnnotation> createLinkAnnotations(PsiMethod method /*  */, List<String> manualTestsIds) {
        TestItApiWrapper testItApiWrapper = getTestItApiWrapper(method.getProject());

        return manualTestsIds
                .stream()
                .distinct()
                .map(testItApiWrapper::getWorkItem)
                .filter(Objects::nonNull)
                .distinct()
                .map(workItem -> PsiUtils.createAnnotation(
                        format("@%s(type=\"manual\", value=\"%s\" /* %s */)", ALLURE_LINK_ANNOTATION, workItem.getId(), workItem.getGlobalId()),
                        method
                ))
                .collect(Collectors.toList());
    }

    public static void createAutoTestAnnotations(PsiMethod testMethod, AutotestDialogSettings autotestSettings) {

        PsiAnnotation tmsLinkAnnotation = createTmsLinkAnnotation(
                testMethod,
                autotestSettings.getAutotestExternalId()
        );

        List<PsiAnnotation> linkAnnotations = createLinkAnnotations(
                testMethod,
                autotestSettings.getManualTestsIds()
        );

        //PsiAnnotation displayNameAnnotation = testMethod.getAnnotation(JUNIT_DISPLAY_NAME_ANNOTATION);

        CommandProcessor.getInstance().executeCommand(
                testMethod.getProject(),
                () -> ApplicationManager.getApplication().runWriteAction(() -> {
                    PsiUtils.addImport(testMethod.getContainingFile(), ALLURE_TMS_LINK_ANNOTATION);
                    PsiUtils.addImport(testMethod.getContainingFile(), ALLURE_LINK_ANNOTATION);
                    testMethod.getModifierList().add(tmsLinkAnnotation);
                    linkAnnotations.forEach(annotation ->testMethod.getModifierList().add(annotation));
                }),
                "Create Autotest and add @TmsLink annotation with created UUID",
                null
        );
    }
}
