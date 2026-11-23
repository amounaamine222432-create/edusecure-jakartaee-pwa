package tn.supcom.edusecure.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class RsaOaepKeyWrapperTest {

    private static final String RECIPIENT_KEY_ID = "rsa-recipient-2026-01";
    private SecretKey aesKey;
    private RsaOaepKeyWrapper wrapper;

    @BeforeEach
    void setUp() throws Exception {
        KeyPair keyPair = newRsaKeyPair();
        DevelopmentKeyResolver<PublicKey> publicKeys = new DevelopmentKeyResolver<>();
        DevelopmentKeyResolver<PrivateKey> privateKeys = new DevelopmentKeyResolver<>();
        publicKeys.put(RECIPIENT_KEY_ID, keyPair.getPublic());
        privateKeys.put(RECIPIENT_KEY_ID, keyPair.getPrivate());
        wrapper = new RsaOaepKeyWrapper(publicKeys, privateKeys, RECIPIENT_KEY_ID);

        KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
        aesGenerator.init(256);
        aesKey = aesGenerator.generateKey();
    }

    @Test
    void shouldWrapThenUnwrapAesKey() {
        WrappedAesKey wrapped = wrapper.wrap(aesKey);

        assertArrayEquals(aesKey.getEncoded(), wrapper.unwrap(wrapped).getEncoded());
        assertEquals(WrappedAesKey.ALGORITHM, wrapped.algorithm());
    }

    @Test
    void shouldRejectWrongPrivateKey() throws Exception {
        KeyPair wrongPair = newRsaKeyPair();
        DevelopmentKeyResolver<PrivateKey> wrongPrivateKeys = new DevelopmentKeyResolver<>();
        wrongPrivateKeys.put(RECIPIENT_KEY_ID, wrongPair.getPrivate());
        RsaOaepKeyWrapper wrongWrapper = new RsaOaepKeyWrapper(
                keyId -> wrongPair.getPublic(), wrongPrivateKeys, RECIPIENT_KEY_ID);
        WrappedAesKey wrapped = wrapper.wrap(aesKey);

        assertThrows(CryptoOperationException.class,
                () -> wrongWrapper.unwrap(wrapped));
    }

    @Test
    void shouldRejectTamperedWrappedKey() {
        WrappedAesKey original = wrapper.wrap(aesKey);
        byte[] modified = original.wrappedKey();
        modified[modified.length / 2] ^= 1;
        WrappedAesKey tampered = new WrappedAesKey(
                original.algorithm(), original.recipientKeyId(), modified);

        assertThrows(CryptoOperationException.class,
                () -> wrapper.unwrap(tampered));
    }

    private static KeyPair newRsaKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
