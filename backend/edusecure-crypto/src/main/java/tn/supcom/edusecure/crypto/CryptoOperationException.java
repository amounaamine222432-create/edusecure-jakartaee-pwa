package tn.supcom.edusecure.crypto;

/**
 * Echec ferme d'une operation cryptographique.
 *
 * <p>Le message ne contient volontairement ni cle, ni document, ni materiel
 * cryptographique.</p>
 */
public final class CryptoOperationException extends RuntimeException {

    public CryptoOperationException(String message) {
        super(message);
    }

    public CryptoOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
