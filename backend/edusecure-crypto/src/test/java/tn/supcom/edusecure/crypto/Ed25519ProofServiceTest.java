package tn.supcom.edusecure.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class Ed25519ProofServiceTest {

    private static final String KEY_ID = "signing-2026-01";
    private Ed25519ProofService service;
    private ProofMetadata proof;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("Ed25519");
        KeyPair keyPair = generator.generateKeyPair();
        DevelopmentKeyResolver<PrivateKey> privateKeys = new DevelopmentKeyResolver<>();
        DevelopmentKeyResolver<PublicKey> publicKeys = new DevelopmentKeyResolver<>();
        privateKeys.put(KEY_ID, keyPair.getPrivate());
        publicKeys.put(KEY_ID, keyPair.getPublic());
        service = new Ed25519ProofService(
                privateKeys, publicKeys, KEY_ID, new JcsCanonicalization());
        proof = proof("issuer-supcom");
    }

    @Test
    void shouldAcceptValidSignature() {
        SignedProof signed = service.sign(proof);

        assertTrue(service.verify(signed));
        assertEquals(KEY_ID, signed.keyId());
    }

    @Test
    void shouldRejectAlteredSignature() {
        SignedProof signed = service.sign(proof);
        byte[] altered = signed.signature();
        altered[0] ^= 1;
        SignedProof tampered = new SignedProof(
                signed.proof(), signed.algorithm(), signed.keyId(), altered);

        assertFalse(service.verify(tampered));
    }

    @Test
    void shouldDetectModifiedProofField() {
        SignedProof signed = service.sign(proof);
        SignedProof tampered = new SignedProof(
                proof("another-issuer"), signed.algorithm(), signed.keyId(),
                signed.signature());

        assertFalse(service.verify(tampered));
    }

    @Test
    void shouldRejectWrongPublicKey() throws Exception {
        SignedProof signed = service.sign(proof);
        KeyPair wrongPair = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
        Ed25519ProofService wrongVerifier = new Ed25519ProofService(
                keyId -> wrongPair.getPrivate(), keyId -> wrongPair.getPublic(),
                KEY_ID, new JcsCanonicalization());

        assertFalse(wrongVerifier.verify(signed));
    }

    private static ProofMetadata proof(String issuer) {
        return new ProofMetadata(
                "document-123",
                2,
                issuer,
                Instant.parse("2026-11-23T10:15:30Z"),
                "0123456789abcdef0123456789abcdef"
                        + "0123456789abcdef0123456789abcdef");
    }
}
