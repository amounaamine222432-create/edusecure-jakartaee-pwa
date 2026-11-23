package tn.supcom.edusecure.crypto;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Objects;

/** AES-256-GCM avec nonce de 96 bits, tag de 128 bits et AAD contextuelle. */
public final class AesGcmDocumentCipher {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BITS = 128;
    private static final int MAX_NONCE_ATTEMPTS = 16;
    private static final byte[] AAD_DOMAIN =
            "EDUSECURE-DOCUMENT-V1".getBytes(StandardCharsets.US_ASCII);

    private final KeyResolver<SecretKey> keyResolver;
    private final String activeKeyId;
    private final SecureRandom secureRandom;
    private final NonceRegistry nonceRegistry;

    public AesGcmDocumentCipher(KeyResolver<SecretKey> keyResolver,
                                String activeKeyId,
                                NonceRegistry nonceRegistry) {
        this(keyResolver, activeKeyId, nonceRegistry, new SecureRandom());
    }

    AesGcmDocumentCipher(KeyResolver<SecretKey> keyResolver,
                         String activeKeyId,
                         NonceRegistry nonceRegistry,
                         SecureRandom secureRandom) {
        this.keyResolver = Objects.requireNonNull(keyResolver, "keyResolver");
        this.activeKeyId = KeyIds.requireValid(activeKeyId);
        this.nonceRegistry = Objects.requireNonNull(nonceRegistry, "nonceRegistry");
        this.secureRandom = Objects.requireNonNull(secureRandom, "secureRandom");
    }

    public EncryptedDocument encrypt(byte[] plaintext,
                                     String documentId,
                                     int documentVersion) {
        Objects.requireNonNull(plaintext, "plaintext");
        validateDocumentContext(documentId, documentVersion);
        SecretKey key = resolveAes256Key(activeKeyId);
        byte[] nonce = newNonce();

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key,
                    new GCMParameterSpec(TAG_LENGTH_BITS, nonce));
            cipher.updateAAD(aad(documentId, documentVersion));
            byte[] ciphertextAndTag = cipher.doFinal(plaintext);
            return new EncryptedDocument(
                    EncryptedDocument.CURRENT_VERSION,
                    EncryptedDocument.ALGORITHM,
                    activeKeyId,
                    documentId,
                    documentVersion,
                    nonce,
                    ciphertextAndTag);
        } catch (GeneralSecurityException exception) {
            throw new CryptoOperationException("Echec du chiffrement AES-GCM", exception);
        }
    }

    public byte[] decrypt(EncryptedDocument document) {
        Objects.requireNonNull(document, "document");
        SecretKey key = resolveAes256Key(document.keyId());

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key,
                    new GCMParameterSpec(TAG_LENGTH_BITS, document.nonce()));
            cipher.updateAAD(aad(document.documentId(), document.documentVersion()));
            return cipher.doFinal(document.ciphertextAndTag());
        } catch (AEADBadTagException exception) {
            throw new CryptoOperationException(
                    "Authentification AES-GCM invalide", exception);
        } catch (GeneralSecurityException exception) {
            throw new CryptoOperationException("Echec du dechiffrement AES-GCM", exception);
        }
    }

    private byte[] newNonce() {
        for (int attempt = 0; attempt < MAX_NONCE_ATTEMPTS; attempt++) {
            byte[] nonce = new byte[EncryptedDocument.NONCE_LENGTH_BYTES];
            secureRandom.nextBytes(nonce);
            if (nonceRegistry.reserve(activeKeyId, nonce)) {
                return nonce;
            }
        }
        throw new CryptoOperationException("Impossible de reserver un nonce GCM unique");
    }

    private SecretKey resolveAes256Key(String keyId) {
        SecretKey key = keyResolver.resolve(KeyIds.requireValid(keyId));
        if (key == null || !"AES".equalsIgnoreCase(key.getAlgorithm())) {
            throw new CryptoOperationException("Cle AES indisponible ou invalide");
        }
        byte[] encoded = key.getEncoded();
        if (encoded == null || encoded.length != 32) {
            throw new CryptoOperationException("Une cle AES de 256 bits est requise");
        }
        return key;
    }

    private static byte[] aad(String documentId, int documentVersion) {
        validateDocumentContext(documentId, documentVersion);
        byte[] id = documentId.getBytes(StandardCharsets.UTF_8);
        return ByteBuffer.allocate(4 + AAD_DOMAIN.length + 4 + id.length + 4)
                .putInt(AAD_DOMAIN.length)
                .put(AAD_DOMAIN)
                .putInt(id.length)
                .put(id)
                .putInt(documentVersion)
                .array();
    }

    private static void validateDocumentContext(String documentId, int documentVersion) {
        if (documentId == null || documentId.isBlank()
                || documentId.length() > 255 || documentId.indexOf('\0') >= 0
                || documentVersion < 1) {
            throw new IllegalArgumentException("Contexte documentaire invalide");
        }
    }
}
