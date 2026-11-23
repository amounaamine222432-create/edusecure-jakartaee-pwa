package tn.supcom.edusecure.crypto;

import java.util.Objects;

/**
 * Enveloppe persistable AES-GCM. Elle ne contient jamais la cle secrete.
 * Le tag GCM de 128 bits est inclus a la fin de {@code ciphertextAndTag}.
 */
public record EncryptedDocument(
        int envelopeVersion,
        String algorithm,
        String keyId,
        String documentId,
        int documentVersion,
        byte[] nonce,
        byte[] ciphertextAndTag
) {
    public static final int CURRENT_VERSION = 1;
    public static final String ALGORITHM = "AES-256-GCM";
    public static final int NONCE_LENGTH_BYTES = 12;
    public static final int TAG_LENGTH_BYTES = 16;

    public EncryptedDocument {
        if (envelopeVersion != CURRENT_VERSION) {
            throw new IllegalArgumentException("Version d'enveloppe inconnue");
        }
        if (!ALGORITHM.equals(algorithm)) {
            throw new IllegalArgumentException("Algorithme d'enveloppe invalide");
        }
        KeyIds.requireValid(keyId);
        if (documentId == null || documentId.isBlank()
                || documentId.length() > 255 || documentId.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("Identifiant de document invalide");
        }
        if (documentVersion < 1) {
            throw new IllegalArgumentException("Version de document invalide");
        }
        Objects.requireNonNull(nonce, "nonce");
        Objects.requireNonNull(ciphertextAndTag, "ciphertextAndTag");
        if (nonce.length != NONCE_LENGTH_BYTES) {
            throw new IllegalArgumentException("Nonce GCM invalide");
        }
        if (ciphertextAndTag.length < TAG_LENGTH_BYTES) {
            throw new IllegalArgumentException("Chiffre GCM malforme");
        }
        nonce = nonce.clone();
        ciphertextAndTag = ciphertextAndTag.clone();
    }

    @Override
    public byte[] nonce() {
        return nonce.clone();
    }

    @Override
    public byte[] ciphertextAndTag() {
        return ciphertextAndTag.clone();
    }
}
