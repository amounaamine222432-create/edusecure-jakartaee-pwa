
package tn.supcom.edusecure.api;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.HttpMethod;
import org.junit.jupiter.api.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RestSecurityArchitectureTest {

    private static final String PACKAGE =
            "tn.supcom.edusecure.api";

    @Test
    void everyRestEndpointMustHaveSecurityAnnotation()
            throws Exception {

        List<String> unsecured = new ArrayList<>();
        int endpointCount = 0;

        java.nio.file.Path classDir = java.nio.file.Path.of(
                "target/classes/tn/supcom/edusecure/api"
        );

        assertTrue(Files.isDirectory(classDir),
                "Le dossier des classes REST est absent");

        try (Stream<java.nio.file.Path> files =
                     Files.walk(classDir)) {

            List<java.nio.file.Path> classFiles = files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".class"))
                    .toList();

            for (java.nio.file.Path file : classFiles) {

                String relative = classDir.relativize(file)
                        .toString()
                        .replace('\\', '/');

                String className = PACKAGE + "."
                        + relative.substring(
                                0, relative.length() - 6)
                          .replace('/', '.');

                Class<?> clazz = Class.forName(className);

                for (Method method :
                        clazz.getDeclaredMethods()) {

                    if (!isHttpEndpoint(method)) {
                        continue;
                    }

                    endpointCount++;

                    if (!hasSecurityAnnotation(method)
                            && !hasSecurityAnnotation(clazz)) {

                        unsecured.add(
                                clazz.getSimpleName()
                                + "." + method.getName()
                        );
                    }
                }
            }
        }

        assertTrue(endpointCount > 0,
                "Aucun endpoint REST détecté");

        assertTrue(unsecured.isEmpty(),
                "Endpoints REST sans annotation de sécurité : "
                        + unsecured);
    }

    private boolean isHttpEndpoint(Method method) {

        for (Annotation annotation :
                method.getAnnotations()) {

            if (annotation.annotationType()
                    .isAnnotationPresent(HttpMethod.class)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasSecurityAnnotation(
            java.lang.reflect.AnnotatedElement element) {

        return element.isAnnotationPresent(
                    RolesAllowed.class)
                || element.isAnnotationPresent(
                    PermitAll.class)
                || element.isAnnotationPresent(
                    DenyAll.class);
    }
}
