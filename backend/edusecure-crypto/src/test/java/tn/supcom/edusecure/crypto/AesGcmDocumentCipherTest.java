package tn.supcom.edusecure.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AesGcmDocumentCipherTest {

    private static final String KEY_ID = "aes-2026-01";
    private DevelopmentKeyResolver<SecretKey> keys;
    private AesGcmDocumentCipher cipher;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        keys = new DevelopmentKeyResolver<>();
        keys.put(KEY_ID, newAesKey());
        cipher = new AesGcmDocumentCipher(
                keys, KEY_ID, new InMemoryNonceRegistry());
    }

    @Test
    void shouldEncryptThenDecrypt() {
        byte[] plaintext = "Cours de securite".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        EncryptedDocument encrypted = cipher.encrypt(plaintext, "doc-42", 3);

        assertArrayEquals(plaintext, cipher.decrypt(encrypted));
        assertEquals(EncryptedDocument.NONCE_LENGTH_BYTES, encrypted.nonce().length);
        assertEquals(plaintext.length + EncryptedDocument.TAG_LENGTH_BYTES,
                encrypted.ciphertextAndTag().length);
    }

    @Test
    void shouldProduceOneHundredThousandDistinctNonces() {
        Set<String> nonces = new HashSet<>(140_000);

        for (int index = 0; index < 100_000; index++) {
            EncryptedDocument encrypted = cipher.encrypt(new byte[]{1}, "doc-42", 1);
            assertTrue(nonces.add(Base64.getEncoder().encodeToString(encrypted.nonce())),
                    "Nonce duplique a l'iteration " + index);
        }

        assertEquals(100_000, nonces.size());
    }

    @Test
    void shouldRejectModifiedCiphertext() {
        EncryptedDocument original = cipher.encrypt(new byte[]{1, 2, 3}, "doc-42", 1);
        byte[] modified = original.ciphertextAndTag();
        modified[0] ^= 1;

        EncryptedDocument tampered = copy(original, original.documentId(),
                original.nonce(), modified);

        assertThrows(CryptoOperationException.class, () -> cipher.decrypt(tampered));
    }

    @Test
    void shouldRejectIncorrectAssociatedData() {
        EncryptedDocument original = cipher.encrypt(new byte[]{1, 2, 3}, "doc-42", 1);
        EncryptedDocument wrongContext = copy(original, "doc-43",
                original.nonce(), original.ciphertextAndTag());

        assertThrows(CryptoOperationException.class,
                () -> cipher.decrypt(wrongContext));
    }

    @Test
    void shouldRejectMalformedNonceAndEnvelope() {
        assertThrows(IllegalArgumentException.class, () -> new EncryptedDocument(
                1, EncryptedDocument.ALGORITHM, KEY_ID, "doc-42", 1,
                new byte[11], new byte[16]));
        assertThrows(IllegalArgumentException.class, () -> new EncryptedDocument(
                1, EncryptedDocument.ALGORITHM, KEY_ID, "doc-42", 1,
                new byte[12], new byte[15]));
    }

    @Test
    void shouldRejectWrongKey() throws NoSuchAlgorithmException {
        EncryptedDocument encrypted = cipher.encrypt(new byte[]{1, 2, 3}, "doc-42", 1);
        DevelopmentKeyResolver<SecretKey> wrongKeys = new DevelopmentKeyResolver<>();
        wrongKeys.put(KEY_ID, newAesKey());
        AesGcmDocumentCipher wrongCipher = new AesGcmDocumentCipher(
                wrongKeys, KEY_ID, new InMemoryNonceRegistry());

        assertThrows(CryptoOperationException.class,
                () -> wrongCipher.decrypt(encrypted));
    }

    @Test
    void envelopeMustDefensivelyCopyBinaryValues() {
        EncryptedDocument encrypted = cipher.encrypt(new byte[]{1}, "doc-42", 1);
        byte[] exposedNonce = encrypted.nonce();
        byte original = exposedNonce[0];
        exposedNonce[0] ^= 1;

        assertEquals(original, encrypted.nonce()[0]);
    }

    private static EncryptedDocument copy(EncryptedDocument source,
                                           String documentId,
                                           byte[] nonce,
                                           byte[] ciphertext) {
        return new EncryptedDocument(source.envelopeVersion(), source.algorithm(),
                source.keyId(), documentId, source.documentVersion(), nonce, ciphertext);
    }

    private static SecretKey newAesKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey();
    }
}
