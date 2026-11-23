package tn.supcom.edusecure.crypto;

import java.util.Objects;

/** Preuve, identifiant de cle et signature Ed25519 detachee. */
public record SignedProof(
        ProofMetadata proof,
        String algorithm,
        String keyId,
        byte[] signature
) {
    public static final String ALGORITHM = "Ed25519";
    public static final int SIGNATURE_LENGTH_BYTES = 64;

    public SignedProof {
        Objects.requireNonNull(proof, "proof");
        if (!ALGORITHM.equals(algorithm)) {
            throw new IllegalArgumentException("Algorithme de signature invalide");
        }
        KeyIds.requireValid(keyId);
        Objects.requireNonNull(signature, "signature");
        if (signature.length != SIGNATURE_LENGTH_BYTES) {
            throw new IllegalArgumentException("Signature Ed25519 malformee");
        }
        signature = signature.clone();
    }

    @Override
    public byte[] signature() {
        return signature.clone();
    }
}
