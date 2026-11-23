package tn.supcom.edusecure.crypto;

import java.security.Key;

/**
 * Frontiere entre les primitives cryptographiques et le stockage des cles.
 * Une implementation de production doit deleguer a un KMS/HSM ou a un
 * coffre applicatif adapte au type de cle.
 */
@FunctionalInterface
public interface KeyResolver<K extends Key> {

    K resolve(String keyId);
}
