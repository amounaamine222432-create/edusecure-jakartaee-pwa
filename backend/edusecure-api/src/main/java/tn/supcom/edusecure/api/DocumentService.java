
package tn.supcom.edusecure.api;

import tn.supcom.edusecure.common.DocumentValidator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class DocumentService {

    private final ConcurrentMap<UUID, DocumentRecord> documents =
            new ConcurrentHashMap<>();

    public record DocumentRecord(
            UUID id,
            String filename,
            String sha256,
            long size
    ) {}

    public enum VerificationStatus {
        VALID,
        INVALID,
        NOT_FOUND
    }

    public DocumentRecord issue(String filename, byte[] content) {

        if (filename == null
                || filename.isBlank()
                || filename.length() > 255
                || filename.contains("/")
                || filename.contains("\\")
                || filename.contains("\r")
                || filename.contains("\n")) {
            throw new IllegalArgumentException(
                    "Nom de fichier invalide");
        }

        if (content == null
                || !DocumentValidator.isValidSize(content.length)) {
            throw new IllegalArgumentException(
                    "Taille de document invalide");
        }

        DocumentRecord record = new DocumentRecord(
                UUID.randomUUID(),
                filename,
                sha256(content),
                content.length
        );

        documents.put(record.id(), record);

        return record;
    }

    public VerificationStatus verify(UUID id, byte[] content) {

        if (id == null
                || content == null
                || !DocumentValidator.isValidSize(content.length)) {
            throw new IllegalArgumentException(
                    "Document invalide");
        }

        DocumentRecord record = documents.get(id);

        if (record == null) {
            return VerificationStatus.NOT_FOUND;
        }

        byte[] expected = HexFormat.of()
                .parseHex(record.sha256());

        byte[] actual = sha256Bytes(content);

        return MessageDigest.isEqual(expected, actual)
                ? VerificationStatus.VALID
                : VerificationStatus.INVALID;
    }

    private static String sha256(byte[] content) {
        return HexFormat.of()
                .formatHex(sha256Bytes(content));
    }

    private static byte[] sha256Bytes(byte[] content) {
        try {
            return MessageDigest.getInstance("SHA-256")
                    .digest(content);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 indisponible", e);
        }
    }
}
