package tn.supcom.edusecure.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.EdECKey;
import java.util.Objects;

/** Signature et verification de preuves canonicalisees par JCS RFC 8785. */
public final class Ed25519ProofService {

    private final KeyResolver<PrivateKey> privateKeyResolver;
    private final KeyResolver<PublicKey> publicKeyResolver;
    private final String activeSigningKeyId;
    private final JcsCanonicalization canonicalization;

    public Ed25519ProofService(KeyResolver<PrivateKey> privateKeyResolver,
                               KeyResolver<PublicKey> publicKeyResolver,
                               String activeSigningKeyId,
                               JcsCanonicalization canonicalization) {
        this.privateKeyResolver = Objects.requireNonNull(
                privateKeyResolver, "privateKeyResolver");
        this.publicKeyResolver = Objects.requireNonNull(
                publicKeyResolver, "publicKeyResolver");
        this.activeSigningKeyId = KeyIds.requireValid(activeSigningKeyId);
        this.canonicalization = Objects.requireNonNull(
                canonicalization, "canonicalization");
    }

    public SignedProof sign(ProofMetadata proof) {
        Objects.requireNonNull(proof, "proof");
        PrivateKey privateKey = requireEd25519(
                privateKeyResolver.resolve(activeSigningKeyId), "privee");

        try {
            Signature signer = Signature.getInstance(SignedProof.ALGORITHM);
            signer.initSign(privateKey);
            signer.update(signingInput(proof, activeSigningKeyId));
            return new SignedProof(
                    proof,
                    SignedProof.ALGORITHM,
                    activeSigningKeyId,
                    signer.sign());
        } catch (GeneralSecurityException exception) {
            throw new CryptoOperationException(
                    "Echec de la signature Ed25519", exception);
        }
    }

    public boolean verify(SignedProof signedProof) {
        Objects.requireNonNull(signedProof, "signedProof");
        PublicKey publicKey = requireEd25519(
                publicKeyResolver.resolve(signedProof.keyId()), "publique");

        try {
            Signature verifier = Signature.getInstance(SignedProof.ALGORITHM);
            verifier.initVerify(publicKey);
            verifier.update(signingInput(signedProof.proof(), signedProof.keyId()));
            return verifier.verify(signedProof.signature());
        } catch (SignatureException exception) {
            return false;
        } catch (GeneralSecurityException exception) {
            throw new CryptoOperationException(
                    "Echec de la verification Ed25519", exception);
        }
    }

    byte[] signingInput(ProofMetadata proof, String keyId) {
        KeyIds.requireValid(keyId);
        String unsignedEnvelope = "{"
                + "\"proof\":" + proof.toJson() + ","
                + "\"alg\":" + JsonStrings.quote(SignedProof.ALGORITHM) + ","
                + "\"kid\":" + JsonStrings.quote(keyId)
                + "}";
        return canonicalization.canonicalize(unsignedEnvelope);
    }

    private static <K extends java.security.Key> K requireEd25519(
            K key, String kind) {
        if (!(key instanceof EdECKey edEcKey)
                || !SignedProof.ALGORITHM.equals(edEcKey.getParams().getName())) {
            throw new CryptoOperationException(
                    "Cle Ed25519 " + kind + " indisponible ou invalide");
        }
        return key;
    }
}
