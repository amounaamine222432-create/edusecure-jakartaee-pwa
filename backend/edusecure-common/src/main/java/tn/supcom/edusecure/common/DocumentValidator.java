
package tn.supcom.edusecure.common;

public final class DocumentValidator {

    public static final long MAX_DOCUMENT_SIZE = 10L * 1024 * 1024;

    private DocumentValidator() {
    }

    public static boolean isValidSize(long size) {
        return size > 0 && size <= MAX_DOCUMENT_SIZE;
    }
}
