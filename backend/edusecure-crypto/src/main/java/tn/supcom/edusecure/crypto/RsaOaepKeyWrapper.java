package tn.supcom.edusecure.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;
import java.util.Objects;

/** Encapsulation de cles AES par RSA-OAEP SHA-256 / MGF1-SHA-256. */
public final class RsaOaepKeyWrapper {

    private static final String TRANSFORMATION = "RSA/ECB/OAEPPadding";
    private static final OAEPParameterSpec OAEP_SHA_256 = new OAEPParameterSpec(
            "SHA-256",
            "MGF1",
            MGF1ParameterSpec.SHA256,
            PSource.PSpecified.DEFAULT);
    private static final int MIN_RSA_BITS = 2048;

    private final KeyResolver<PublicKey> publicKeyResolver;
    private final KeyResolver<PrivateKey> privateKeyResolver;
    private final String activeRecipientKeyId;

    public RsaOaepKeyWrapper(KeyResolver<PublicKey> publicKeyResolver,
                             KeyResolver<PrivateKey> privateKeyResolver,
                             String activeRecipientKeyId) {
        this.publicKeyResolver = Objects.requireNonNull(
                publicKeyResolver, "publicKeyResolver");
        this.privateKeyResolver = Objects.requireNonNull(
                privateKeyResolver, "privateKeyResolver");
        this.activeRecipientKeyId = KeyIds.requireValid(activeRecipientKeyId);
    }

    public WrappedAesKey wrap(SecretKey aesKey) {
        requireAes256(aesKey);
        PublicKey publicKey = requireRsaKey(
                publicKeyResolver.resolve(activeRecipientKeyId), "publique");

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, OAEP_SHA_256);
            return new WrappedAesKey(
                    WrappedAesKey.ALGORITHM,
                    activeRecipientKeyId,
                    cipher.doFinal(aesKey.getEncoded()));
        } catch (GeneralSecurityException exception) {
            throw new CryptoOperationException(
                    "Echec de l'encapsulation RSA-OAEP", exception);
        }
    }

    public SecretKey unwrap(WrappedAesKey wrappedAesKey) {
        Objects.requireNonNull(wrappedAesKey, "wrappedAesKey");
        PrivateKey privateKey = requireRsaKey(
                privateKeyResolver.resolve(wrappedAesKey.recipientKeyId()),
                "privee");
        byte[] keyBytes = null;

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey, OAEP_SHA_256);
            keyBytes = cipher.doFinal(wrappedAesKey.wrappedKey());
            if (keyBytes.length != 32) {
                throw new CryptoOperationException(
                        "La cle recuperee n'est pas une cle AES-256");
            }
            return new SecretKeySpec(keyBytes, "AES");
        } catch (GeneralSecurityException exception) {
            throw new CryptoOperationException(
                    "Echec de la decapsulation RSA-OAEP", exception);
        } finally {
            if (keyBytes != null) {
                Arrays.fill(keyBytes, (byte) 0);
            }
        }
    }

    private static void requireAes256(SecretKey key) {
        if (key == null || !"AES".equalsIgnoreCase(key.getAlgorithm())
                || key.getEncoded() == null || key.getEncoded().length != 32) {
            throw new IllegalArgumentException("Une cle AES de 256 bits est requise");
        }
    }

    private static <K extends java.security.Key> K requireRsaKey(
            K key, String kind) {
        if (!(key instanceof RSAKey rsaKey)
                || rsaKey.getModulus().bitLength() < MIN_RSA_BITS) {
            throw new CryptoOperationException(
                    "Cle RSA " + kind + " indisponible ou trop courte");
        }
        return key;
    }
}
