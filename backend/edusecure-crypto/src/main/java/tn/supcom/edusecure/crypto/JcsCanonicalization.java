package tn.supcom.edusecure.crypto;

import org.erdtman.jcs.JsonCanonicalizer;

import java.io.IOException;
import java.util.Objects;

/** Canonicalisation JSON conforme JCS RFC 8785. */
public final class JcsCanonicalization {

    public byte[] canonicalize(String json) {
        Objects.requireNonNull(json, "json");
        try {
            return new JsonCanonicalizer(json).getEncodedUTF8();
        } catch (IOException exception) {
            throw new IllegalArgumentException(
                    "JSON non canonisable selon JCS RFC 8785", exception);
        }
    }
}
