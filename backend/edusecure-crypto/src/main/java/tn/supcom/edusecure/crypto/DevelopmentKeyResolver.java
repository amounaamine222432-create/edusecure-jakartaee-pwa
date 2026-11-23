package tn.supcom.edusecure.crypto;

import java.security.Key;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Fournisseur en memoire reserve aux tests et au developpement local.
 *
 * <p>Il ne chiffre pas les cles en memoire, ne fournit ni controle d'acces,
 * ni audit, ni persistance sure. Il ne doit donc jamais etre utilise comme
 * coffre de production. Creer une instance distincte par usage de cle.</p>
 */
public final class DevelopmentKeyResolver<K extends Key>
        implements KeyResolver<K> {

    private final ConcurrentMap<String, K> keys = new ConcurrentHashMap<>();

    public void put(String keyId, K key) {
        keys.put(KeyIds.requireValid(keyId), Objects.requireNonNull(key, "key"));
    }

    public void remove(String keyId) {
        keys.remove(KeyIds.requireValid(keyId));
    }

    @Override
    public K resolve(String keyId) {
        K key = keys.get(KeyIds.requireValid(keyId));
        if (key == null) {
            throw new CryptoOperationException("Cle inconnue: " + keyId);
        }
        return key;
    }
}
