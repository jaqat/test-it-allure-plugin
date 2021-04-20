package com.github.jaqat.testit.plugin.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

public class PsiUtils {

	public static String getAnnotationAttribute(PsiAnnotation annotation, String value) {
		String valueRaw = Objects.requireNonNull(annotation.findDeclaredAttributeValue(value)).getText();
		if (valueRaw.startsWith("\"") && valueRaw.endsWith("\"")) {
			return StringUtils.replaceChars(
					valueRaw.substring(1, valueRaw.length() - 1),
					"\\\"",
					"\""
			);
		}
		return valueRaw;
	}

	public static PsiAnnotation createAnnotation(final String annotation, final PsiElement context) {
		final PsiElementFactory factory = PsiElementFactory.getInstance(context.getProject());
		return factory.createAnnotationFromText(annotation, context);
	}

	public static void addImport(final PsiFile file, final String qualifiedName) {
		if (file instanceof PsiJavaFile) {
			addImport((PsiJavaFile) file, qualifiedName);
		}
	}

	private static void addImport(final PsiJavaFile file, final String qualifiedName) {
		final Project project = file.getProject();
		Optional<PsiClass> possibleClass = Optional.ofNullable(JavaPsiFacade.getInstance(project)
				.findClass(qualifiedName, GlobalSearchScope.everythingScope(project)));
		possibleClass.ifPresent(psiClass -> JavaCodeStyleManager.getInstance(project).addImport(file, psiClass));
	}

}
