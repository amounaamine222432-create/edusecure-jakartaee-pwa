package tn.supcom.edusecure.crypto;

final class KeyIds {

    private static final int MAX_LENGTH = 128;

    private KeyIds() {
    }

    static String requireValid(String keyId) {
        if (keyId == null || keyId.isBlank() || keyId.length() > MAX_LENGTH
                || keyId.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("Identifiant de cle invalide");
        }
        return keyId;
    }
}
