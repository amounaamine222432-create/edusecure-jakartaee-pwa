# EduSecure - Deploiement securise DEV

## Environnement
- Jakarta EE 11, WildFly 42, Java 25
- PWA : https://app.edusecure.local:8443/
- API : https://api.edusecure.local:8443/api/health
- TLS 1.3 uniquement sur le listener principal 8443
- Listener de test TLS 1.3 : 8543

## Certificat et secrets
- Certificat DEV PKCS12 : C:\edusecure\tls\edusecure-dev.p12
- Certificat public : C:\edusecure\tls\edusecure-dev.crt
- Domaines SAN : app.edusecure.local, api.edusecure.local
- Elytron Credential Store : edusecureCS
- Alias du certificat : edusecure-p12
- Key Store : edusecureKS
- Key Manager : edusecureKM
- Contexte SSL : edusecureCertTLS13
- Secret maitre fourni au processus WildFly par EDUSECURE_CS_MASTER
- IMPORTANT : ne jamais versionner les secrets, fichiers P12 ou credential stores.
- La variable utilisateur Windows constitue un mecanisme DEV, pas un coffre de secrets de production.

## Protections HTTP
- Content-Security-Policy stricte sur la PWA
- Referrer-Policy : strict-origin-when-cross-origin
- X-Content-Type-Options : nosniff
- HSTS DEV : max-age=300
- Cache-Control : no-cache sur /service-worker.js
- Cache-Control : no-store sur /api/documents et ses sous-chemins

## Tests verifies
- API et PWA : HTTP 200 avec verification explicite du certificat
- TLS 1.3 : valide ; TLS 1.2 : refuse
- Maven : 13 tests reussis, aucune erreur
- Tests documentaires : Cache-Control no-store confirme
- Enregistrement du Service Worker dans le navigateur : reste a confirmer

## Demarrage
Depuis la racine du depot, executer :
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\deployment\wildfly\scripts\start-wildfly-secure.ps1

## CI/CD
- GitHub Actions : tests, analyse des secrets, artefacts et attestation du WAR
- deploy-from-ci.ps1 : verification des artefacts, sauvegarde et rollback DEV
- Les artefacts du dernier run CI ne contiennent pas les modifications locales non commitees.
- Les parametres de production, la gestion centralisee des secrets et la restauration complete restent a industrialiser.
