package tn.supcom.edusecure.crypto;

/**
 * Reserve un nonce pour une cle donnee. Une implementation distribuee de
 * production doit rendre cette operation atomique entre toutes les instances.
 */
@FunctionalInterface
public interface NonceRegistry {

    boolean reserve(String keyId, byte[] nonce);
}
