package tn.supcom.edusecure.crypto;

import java.time.Instant;
import java.util.Objects;
import java.util.regex.Pattern;

/** Champs metier couverts ensemble par la signature de preuve d'emission. */
public record ProofMetadata(
        String documentId,
        int documentVersion,
        String issuer,
        Instant issuedAt,
        String contentSha256
) {
    private static final Pattern SHA_256 = Pattern.compile("[0-9a-f]{64}");

    public ProofMetadata {
        if (documentId == null || documentId.isBlank() || documentId.length() > 255) {
            throw new IllegalArgumentException("Identifiant de document invalide");
        }
        if (documentVersion < 1) {
            throw new IllegalArgumentException("Version de document invalide");
        }
        if (issuer == null || issuer.isBlank() || issuer.length() > 255) {
            throw new IllegalArgumentException("Emetteur invalide");
        }
        Objects.requireNonNull(issuedAt, "issuedAt");
        if (contentSha256 == null || !SHA_256.matcher(contentSha256).matches()) {
            throw new IllegalArgumentException("Empreinte SHA-256 invalide");
        }
    }

    String toJson() {
        return "{" 
                + "\"documentId\":" + JsonStrings.quote(documentId) + ","
                + "\"documentVersion\":" + documentVersion + ","
                + "\"issuer\":" + JsonStrings.quote(issuer) + ","
                + "\"issuedAt\":" + JsonStrings.quote(issuedAt.toString()) + ","
                + "\"contentSha256\":" + JsonStrings.quote(contentSha256)
                + "}";
    }
}
