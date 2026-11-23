package tn.supcom.edusecure.crypto;

import java.util.Objects;

/** Enveloppe RSA-OAEP d'une cle AES; aucune cle en clair n'y est conservee. */
public record WrappedAesKey(
        String algorithm,
        String recipientKeyId,
        byte[] wrappedKey
) {
    public static final String ALGORITHM = "RSA-OAEP-256";

    public WrappedAesKey {
        if (!ALGORITHM.equals(algorithm)) {
            throw new IllegalArgumentException("Algorithme d'encapsulation invalide");
        }
        KeyIds.requireValid(recipientKeyId);
        Objects.requireNonNull(wrappedKey, "wrappedKey");
        if (wrappedKey.length < 256 || wrappedKey.length > 1024) {
            throw new IllegalArgumentException("Cle encapsulee malformee");
        }
        wrappedKey = wrappedKey.clone();
    }

    @Override
    public byte[] wrappedKey() {
        return wrappedKey.clone();
    }
}
