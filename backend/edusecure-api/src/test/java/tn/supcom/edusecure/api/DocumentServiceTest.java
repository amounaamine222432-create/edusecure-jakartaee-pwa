
package tn.supcom.edusecure.api;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceTest {

    private final DocumentService service =
            new DocumentService();

    @Test
    void shouldIssueValidDocument() {
        byte[] content = "EduSecure test"
                .getBytes(StandardCharsets.UTF_8);

        var result = service.issue("cours.pdf", content);

        assertNotNull(result.id());
        assertEquals("cours.pdf", result.filename());
        assertEquals(content.length, result.size());
        assertEquals(64, result.sha256().length());
    }

    @Test
    void shouldRejectEmptyDocument() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.issue("cours.pdf", new byte[0])
        );
    }

    @Test
    void shouldRejectOversizedDocument() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.issue(
                        "cours.pdf",
                        new byte[10 * 1024 * 1024 + 1])
        );
    }

    @Test
    void shouldRejectInvalidFilename() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.issue("../secret.pdf", new byte[]{1})
        );
    }

    @Test
    void shouldVerifyValidDocument() {
        byte[] content = "Contenu initial"
                .getBytes(StandardCharsets.UTF_8);

        var issued = service.issue("cours.pdf", content);

        assertEquals(
                DocumentService.VerificationStatus.VALID,
                service.verify(issued.id(), content)
        );
    }

    @Test
    void shouldDetectModifiedDocument() {
        byte[] original = "Contenu initial"
                .getBytes(StandardCharsets.UTF_8);

        var issued = service.issue("cours.pdf", original);

        byte[] modified = "Contenu modifie"
                .getBytes(StandardCharsets.UTF_8);

        assertEquals(
                DocumentService.VerificationStatus.INVALID,
                service.verify(issued.id(), modified)
        );
    }

    @Test
    void shouldRejectUnknownDocument() {
        assertEquals(
                DocumentService.VerificationStatus.NOT_FOUND,
                service.verify(UUID.randomUUID(), new byte[]{1})
        );
    }
}
