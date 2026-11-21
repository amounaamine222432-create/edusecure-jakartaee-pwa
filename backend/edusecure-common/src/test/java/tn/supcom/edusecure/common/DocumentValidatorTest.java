
package tn.supcom.edusecure.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentValidatorTest {

    @Test
    void shouldAcceptValidDocument() {
        assertTrue(DocumentValidator.isValidSize(1024));
    }

    @Test
    void shouldAcceptMaximumSize() {
        assertTrue(DocumentValidator.isValidSize(
                DocumentValidator.MAX_DOCUMENT_SIZE));
    }

    @Test
    void shouldRejectEmptyDocument() {
        assertFalse(DocumentValidator.isValidSize(0));
    }

    @Test
    void shouldRejectOversizedDocument() {
        assertFalse(DocumentValidator.isValidSize(
                DocumentValidator.MAX_DOCUMENT_SIZE + 1));
    }

    @Test
    void shouldRejectNegativeSize() {
        assertFalse(DocumentValidator.isValidSize(-1));
    }
}
