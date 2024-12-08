package com.example.tp3.logger;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.reference.CtTypeReference;

public class LoggerInstrumenter {
    public static void main(String[] args) {
        // Initialiser Spoon
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java/com/example/tp3/controllers/ProductController.java");
        launcher.setSourceOutputDirectory("target/spooned"); // Où enregistrer le code transformé
        launcher.buildModel();

        // Parcourir toutes les classes
        for (CtType<?> ctClass : launcher.getFactory().Class().getAll()) {
            if (ctClass instanceof CtClass) {
                CtClass<?> clazz = (CtClass<?>) ctClass;

                // Pour chaque méthode de la classe
                for (CtMethod<?> method : clazz.getMethods()) {
                    if (method.getBody() != null) {
                        // Log unique en fonction du type d'opération
                        String logStatement;
                        if (method.getSimpleName().contains("get")) {
                            logStatement = "org.slf4j.LoggerFactory.getLogger(\"" + clazz.getQualifiedName() + "\").info(\"User: {} performed a READ operation on method: {}\", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), \"" + method.getSimpleName() + "\")";
                        } else if (method.getSimpleName().contains("create") || method.getSimpleName().contains("update") || method.getSimpleName().contains("delete")) {
                            logStatement = "org.slf4j.LoggerFactory.getLogger(\"" + clazz.getQualifiedName() + "\").info(\"User: {} performed a WRITE operation on method: {}\", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), \"" + method.getSimpleName() + "\")";
                        } else if (method.getSimpleName().contains("find")) {
                            logStatement = "org.slf4j.LoggerFactory.getLogger(\"" + clazz.getQualifiedName() + "\").info(\"User: {} performed a SEARCH operation on method: {}\", SecurityContextHolder.getContext().getAuthentication().getPrincipal(), \"" + method.getSimpleName() + "\")";
                        } else {
                            continue;
                        }

                        // Ajouter le log au début de la méthode
                        CtCodeSnippetStatement logSnippet = launcher.getFactory().Code().createCodeSnippetStatement(logStatement);
                        method.getBody().insertBegin(logSnippet);
                    }
                }
            }
        }

        // Générer le code modifié
        launcher.prettyprint();
    }
}
