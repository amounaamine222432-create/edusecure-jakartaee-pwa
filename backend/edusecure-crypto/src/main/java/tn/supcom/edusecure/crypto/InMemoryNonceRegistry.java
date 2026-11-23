package tn.supcom.edusecure.crypto;

import java.util.Base64;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registre local adapte aux tests et au developpement mono-instance.
 * Il ne remplace pas un compteur/registre durable de production.
 */
public final class InMemoryNonceRegistry implements NonceRegistry {

    private final Set<String> reservations = ConcurrentHashMap.newKeySet();

    @Override
    public boolean reserve(String keyId, byte[] nonce) {
        KeyIds.requireValid(keyId);
        if (nonce == null || nonce.length != EncryptedDocument.NONCE_LENGTH_BYTES) {
            throw new IllegalArgumentException("Nonce invalide");
        }
        return reservations.add(keyId + ":"
                + Base64.getEncoder().encodeToString(nonce));
    }
}
