# Phase 9 — Cryptographie et intégrité

## 1. Périmètre livré

Le module Maven `backend/edusecure-crypto` fournit des primitives séparées de
l'API REST et du stockage :

- chiffrement authentifié AES-256-GCM des documents ;
- encapsulation d'une clé AES par RSA-OAEP SHA-256 / MGF1-SHA-256 ;
- signature et vérification Ed25519 d'une preuve d'émission ;
- canonicalisation JSON JCS conforme à la RFC 8785 ;
- résolution des clés par identifiant versionné `kid` ;
- enveloppes persistables qui ne contiennent aucune clé secrète.

Le module est une dépendance de `edusecure-api` et est donc inclus dans le WAR.
Il n'est pas activé implicitement avec des clés locales : une activation sûre
exige un fournisseur de clés de production et un stockage des enveloppes.

## 2. AES-256-GCM

### Paramètres

| Paramètre | Valeur |
|---|---|
| Transformation JCA | `AES/GCM/NoPadding` |
| Taille de clé | 256 bits |
| Nonce | 96 bits aléatoires par `SecureRandom` |
| Tag d'authentification | 128 bits |
| Identifiant d'algorithme | `AES-256-GCM` |
| Version d'enveloppe | `1` |

`EncryptedDocument` contient :

```text
envelopeVersion | algorithm | keyId | documentId | documentVersion
nonce[12] | ciphertext || authenticationTag[16]
```

Le tag est placé par JCA à la fin de `ciphertextAndTag`. Les tableaux sont
copiés à la construction et à la lecture pour empêcher une modification par
référence. La clé AES n'est jamais incluse dans l'enveloppe.

### AAD

L'AAD est une structure binaire avec longueurs explicites :

```text
int32(domainLength) || "EDUSECURE-DOCUMENT-V1"
|| int32(documentIdUtf8Length) || UTF8(documentId)
|| int32(documentVersion)
```

Le domaine évite qu'un chiffré destiné à un autre protocole soit accepté.
Les longueurs rendent l'encodage non ambigu. Toute modification de
`documentId`, `documentVersion`, du nonce, du chiffré ou du tag provoque un
échec d'authentification et aucun clair n'est retourné.

### Unicité des nonces

`InMemoryNonceRegistry` réserve atomiquement le couple `(kid, nonce)` dans une
JVM et le générateur réessaie en cas de collision. Il convient aux tests et au
développement mono-instance uniquement.

En production, une des stratégies suivantes est obligatoire :

1. registre atomique durable partagé par toutes les instances ; ou
2. compteur de 96 bits partitionné par instance, avec allocation durable des
   plages et interdiction de remise à zéro ; ou
3. service KMS/HSM garantissant lui-même l'unicité des IV GCM.

Une clé doit être retirée du chiffrement avant épuisement de son espace de
nonces. Les anciennes clés restent accessibles en déchiffrement tant que les
documents associés existent.

## 3. RSA-OAEP

`RsaOaepKeyWrapper` chiffre uniquement les 32 octets d'une clé AES. Il ne doit
jamais recevoir un document complet.

La transformation JCA est `RSA/ECB/OAEPPadding`, accompagnée d'un
`OAEPParameterSpec` explicite :

- fonction de hachage OAEP : SHA-256 ;
- masque : MGF1 ;
- hachage MGF1 : SHA-256 ;
- label : valeur vide standard ;
- taille minimale de clé RSA : 2048 bits.

`WrappedAesKey` contient `RSA-OAEP-256`, le `recipientKeyId` et la clé
encapsulée. La clé privée est obtenue uniquement par le résolveur correspondant
au `kid`. Un échec OAEP ou une clé incorrecte produit une erreur fermée.

## 4. Ed25519 et preuve d'émission

Une preuve métier `ProofMetadata` couvre ensemble :

- `documentId` ;
- `documentVersion` ;
- `issuer` ;
- `issuedAt` au format `Instant` UTC ;
- `contentSha256` en hexadécimal minuscule.

L'entrée signée est l'enveloppe JSON non signée suivante :

```json
{
  "proof": { "...": "tous les champs métier" },
  "alg": "Ed25519",
  "kid": "signing-YYYY-NN"
}
```

Cette enveloppe entière est canonicalisée par JCS, encodée en UTF-8 puis
signée avec l'API JCA native `Signature.getInstance("Ed25519")`. Le `kid` et
l'algorithme sont donc couverts eux aussi. La vérification reconstruit les
mêmes octets ; changer un champ, le `kid` ou la signature invalide la preuve.

La preuve Ed25519 complète la vérification SHA-256 historique :

- SHA-256 seul détecte une différence par rapport à l'empreinte conservée ;
- Ed25519 authentifie aussi l'émetteur et le contexte de cette empreinte.

Ces deux notions ne doivent pas être présentées comme équivalentes.

## 5. Canonicalisation JCS RFC 8785

Le module utilise
`io.github.erdtman:java-json-canonicalization:1.1`, l'implémentation Java
référencée dans l'annexe G de la RFC 8785. Un simple tri des propriétés aurait
été insuffisant, notamment pour la sérialisation ECMAScript des nombres,
l'échappement des chaînes et l'ordre UTF-16.

Les tests reprennent :

- le vecteur nombres, littéraux et chaînes des sections 3.2.2/3.2.3 ;
- le vecteur d'ordre des noms de propriétés UTF-16 de la section 3.2.3 ;
- le rejet des noms de propriétés dupliqués ;
- l'égalité canonique de JSON ayant des ordres et espaces différents.

Références :

- <https://www.rfc-editor.org/rfc/rfc8785.html>
- <https://github.com/erdtman/java-json-canonicalization>

## 6. Gestion et rotation des clés

### Séparation obligatoire

Trois familles de clés et trois espaces de `kid` distincts sont requis :

| Usage | Type | Exemple de `kid` |
|---|---|---|
| Chiffrement documentaire | AES-256 | `aes-doc-2026-01` |
| Encapsulation destinataire | RSA | `rsa-recipient42-2026-01` |
| Signature d'émission | Ed25519 | `signing-2026-01` |

`KeyResolver<K>` constitue la frontière avec le coffre. Les services reçoivent
un `kid` actif pour les nouvelles opérations, alors que les enveloppes gardent
le `kid` historique nécessaire au déchiffrement ou à la vérification.

`DevelopmentKeyResolver` conserve les clés en mémoire non chiffrée. Il est
nommé et documenté comme fournisseur de développement uniquement ; il ne
possède ni audit, ni persistance sûre, ni contrôle d'accès. Il ne faut jamais
l'enregistrer comme bean de production.

### Procédure de rotation

1. Générer la nouvelle clé dans le KMS/HSM ou coffre approprié.
2. Publier un nouveau `kid` immuable et vérifier les droits du service.
3. Basculer le `kid` actif pour les nouvelles opérations.
4. Conserver l'ancienne clé en lecture/vérification seulement.
5. Réencapsuler les clés AES pour un changement de clé RSA ; éviter de
   rechiffrer le document lorsque seule la clé d'encapsulation change.
6. Rechiffrer progressivement avec une nouvelle clé AES si sa rotation est
   requise, avec de nouveaux nonces et une nouvelle version d'enveloppe.
7. Retirer une ancienne clé seulement après inventaire prouvant qu'aucun
   objet actif ne la référence et après expiration des obligations d'archive.
8. Journaliser uniquement `kid`, opération, résultat et identifiant métier ;
   ne jamais journaliser clé, clair, nonce avec clé, ou signature sensible.

Le Credential Store Elytron TLS existant n'est pas considéré par défaut comme
un coffre universel. En production, valider séparément le support des
`SecretKey`, clés privées RSA/Ed25519, politiques d'accès, sauvegardes,
rotation, audit et haute disponibilité. Un KMS/HSM dédié peut être nécessaire.

## 7. Intégration avec l'API existante

Les routes restent strictement compatibles :

- `POST /api/documents` émet toujours les métadonnées et l'empreinte SHA-256 ;
- `POST /api/documents/verify` compare toujours l'empreinte du contenu fourni.

Le service actuel ne persiste pas le contenu, seulement ses métadonnées dans
une `ConcurrentHashMap`. Il serait donc incorrect d'annoncer que les documents
sont stockés chiffrés. La phase 9 embarque les primitives dans le WAR mais ne
crée aucune clé privée/AES au démarrage et n'ajoute aucun secret aux réponses.

Le raccordement de production devra suivre ce pipeline :

```text
contenu -> transformations phase 10 -> AES-GCM -> stockage de l'enveloppe
       -> encapsulation RSA de la clé AES par destinataire
métadonnées + empreinte du contenu convenu -> JCS -> Ed25519 -> preuve stockée
```

Le choix de l'empreinte doit être explicite après les transformations de phase
10 : empreinte du document maître, du document personnalisé, ou des deux.
Les enveloppes immuables et les frontières `KeyResolver`, `NonceRegistry`
permettent ce branchement sans implémenter prématurément watermarking,
fingerprinting ou stéganographie.

## 8. Tests et vérification

Commande native demandée :

```powershell
mvn -f ".\backend\pom.xml" clean verify
```

Commande équivalente utilisée dans l'environnement de développement dépourvu
de Java/Maven local :

```bash
docker run --rm \
  -v /home/zayd/Desktop/Work/edusecure-jakartaee-pwa:/workspace \
  -v /tmp/edusecure-m2:/root/.m2 \
  -w /workspace maven:3.9.11-eclipse-temurin-25 \
  mvn --batch-mode --no-transfer-progress -f backend/pom.xml clean verify
```

Résultat vérifié le 23 novembre 2026 : `BUILD SUCCESS`, 31 tests, 0 échec,
0 erreur et 0 test ignoré. Ce total comprend 13 tests historiques et 18 tests
cryptographiques nouveaux.

## 9. Limites connues avant production

- aucun KMS/HSM/coffre applicatif n'est encore configuré ;
- le registre de nonces fourni n'est ni distribué ni persistant ;
- le contenu documentaire n'est pas encore persisté par l'application ;
- aucune migration/réencapsulation en masse n'est implémentée ;
- aucun endpoint de récupération d'une preuve signée n'est ajouté ;
- la supervision, les quotas cryptographiques et la révocation de destinataire
  restent à intégrer avec l'identité et le stockage futurs ;
- l'utilisation de clés AES non extractibles en HSM nécessitera un adaptateur
  de chiffrement côté fournisseur, car la validation actuelle exige une clé
  JCA exportable de 32 octets.

Ces limites sont intentionnellement visibles : le fournisseur mémoire n'est
pas présenté comme une solution de production.
