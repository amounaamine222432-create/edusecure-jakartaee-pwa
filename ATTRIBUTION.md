# 📑 Fiche d’attribution, d’intégrité académique et d’usage de l’IA

> **Projet :** EduSecure — Plateforme sécurisée de distribution et de traçabilité des documents pédagogiques  
> **Statut :** Phases 1 et 2 — Livrables déposés  
> **Version :** 2.0  
> **Dernière mise à jour :** 15 novembre 2026

Ce document accompagne les livrables de l’atelier PWA/SPA et Jakarta EE. Il identifie les membres du trinôme, formalise leurs engagements d’intégrité académique, précise leurs responsabilités et assure la transparence concernant l’utilisation des outils d’intelligence artificielle.

Il est actualisé à chaque phase du cycle de développement du projet EduSecure.

---

## 👥 1. Identification de l’équipe et rôles

- **Établissement :** École Supérieure des Communications de Tunis (SUP’COM).
- **Année universitaire :** 2026–2027.
- **Groupe :** Trinôme n° 6.
- **Application métier :** EduSecure.
- **Encadrant :** Dr. Ing. Mohamed-Bécha KAANICHE.
- **Objet :** Distribution sécurisée de documents pédagogiques avec chiffrement hybride, stéganographie, watermarking et fingerprinting.
- **Socle technique :** Java 25, Jakarta EE 11, WildFly 42, SPA/PWA.

### 1.1 Membres de l’équipe

| Membre | Adresse électronique | Responsabilité principale |
|---|---|---|
| **OUNI Mouhamed Amine** | `mouhamedamine.ouni@supcom.tn` | Coordination, architecture du middleware Jakarta EE, API REST, conception AES/RSA et intégration. |
| **BEN FADHEL Zayd** | `zayd.benfadhel@supcom.tn` | Besoins fonctionnels/SRE, services watermarking et fingerprinting, vérification des marquages. |
| **EZZINE Montassar** | `montassar.ezzine@supcom.tn` | Stéganographie sur photo de profil, extraction côté navigateur, modélisation des menaces et tests. |

La validation des exigences, la revue des risques et les tests de sécurité relèvent d’une responsabilité collective.

### 1.2 Répartition des responsabilités — Phase 1

**OUNI Mouhamed Amine**

- Coordination du trinôme.
- Définition du périmètre et de l’architecture générale.
- Étude du middleware Jakarta EE.
- Conception du chiffrement hybride AES/RSA.
- Définition du processus de distribution sécurisée.

**BEN FADHEL Zayd**

- Identification des besoins fonctionnels.
- Ingénierie des exigences de sécurité.
- Application de la démarche SQUARE.
- Étude du watermarking et du fingerprinting.
- Définition des critères de vérification documentaire.

**EZZINE Montassar**

- Étude de la stéganographie sur les images PNG.
- Analyse des mécanismes d’insertion et d’extraction LSB.
- Identification des menaces et des cas d’abus.
- Étude des contraintes de sécurité.
- Préparation des scénarios de validation.

### 1.3 Répartition des responsabilités — Phase 2

**OUNI Mouhamed Amine**

- Revue de l’architecture applicative.
- Conception du middleware Jakarta EE et des API REST.
- Définition des frontières de confiance.
- Conception des mécanismes AES-256-GCM et RSA-OAEP.
- Gestion sécurisée des clés cryptographiques.
- Conception des contrôles d’authentification et d’autorisation.
- Coordination et intégration des décisions architecturales.

**BEN FADHEL Zayd**

- Traçabilité des besoins fonctionnels et des exigences SRE.
- Conception du moteur de watermarking.
- Conception du moteur de fingerprinting.
- Analyse des menaces contre les marquages documentaires.
- Définition des contre-mesures associées.
- Contribution à l’évaluation et à la priorisation des risques selon OWASP Risk Rating.

**EZZINE Montassar**

- Conception du moteur stéganographique LSB.
- Conception de l’extraction côté navigateur.
- Décomposition de l’application et analyse des surfaces d’attaque.
- Contribution à la modélisation STRIDE.
- Étude des risques de saturation des ressources Java.
- Préparation des scénarios et critères de tests de sécurité.

### 1.4 Responsabilités collectives

Les trois membres contribuent collectivement aux activités suivantes :

- Validation des objectifs de sécurité.
- Revue des exigences fonctionnelles et de sécurité.
- Analyse des menaces STRIDE.
- Revue des cotations OWASP.
- Justification des contre-mesures techniques.
- Choix des principes et patrons de conception sécurisée.
- Relecture des livrables.
- Préparation et réalisation des futurs tests de sécurité.

---

## 📜 2. Déclaration d’intégrité académique et d’anti-plagiat

En validant individuellement la présente fiche, chaque membre s’engage à respecter les principes suivants :

1. Comprendre et justifier les décisions de conception, les choix techniques et les mécanismes de sécurité retenus pour EduSecure.

2. Ne reprendre aucun code, schéma, diagramme ou passage documentaire provenant d’un autre groupe de l’atelier sans autorisation ni attribution appropriée.

3. Citer les ressources externes exploitées, notamment les documentations techniques, les bibliothèques open source, les spécifications cryptographiques et les publications scientifiques.

4. Déclarer de manière transparente les contributions des outils d’intelligence artificielle aux analyses, aux documents et au code.

5. Relire, vérifier et adapter les résultats générés par l’intelligence artificielle au contexte du projet.

6. Être capable d’expliquer les parties dont il assume la responsabilité lors des points de contrôle et des présentations.

7. Ne présenter aucun résultat de test, de performance ou de sécurité comme validé sans preuve d’exécution.

8. Respecter les règles d’intégrité académique et de propriété intellectuelle applicables au projet.

La responsabilité scientifique, technique et académique des livrables demeure celle du trinôme.

---

## 🤖 3. Déclaration de l’usage des intelligences artificielles

L’intelligence artificielle est utilisée comme outil d’assistance à la réflexion, à l’analyse technique, à la structuration documentaire, à la rédaction et à la préparation de propositions de conception.

Son utilisation ne dispense pas les membres du trinôme de comprendre, vérifier et justifier les décisions retenues.

### 3.1 Tableau de transparence IA

| Composant / phase | IA utilisée | Pourcentage estimatif proposé | Rôle de l’IA |
|---|---|---:|---|
| **Phase 1A — Cahier des charges** | ChatGPT — GPT-6 | **20 %** | Assistance à la structuration du document, clarification du périmètre, reformulation, rédaction LaTeX et identification des incohérences. |
| **Phase 1B — Recueil des besoins fonctionnels et SRE** | ChatGPT — GPT-6 | **25 %** | Assistance à l’application de SQUARE, à la formulation des exigences SMART, à l’identification des risques et à la préparation des critères de validation. |
| **Phase 2 — Cahier conceptuel** | ChatGPT — GPT-6 | **30 %** | Assistance à la modélisation STRIDE, aux cotations OWASP, aux contre-mesures techniques, à l’architecture sécurisée et à la génération des documents et diagrammes LaTeX. |

**Convention de mesure :** les pourcentages de 20 %, 25 % et 30 % constituent des estimations déclaratives proposées. Ils ne résultent pas d’une mesure quantitative vérifiée du contenu généré.

Ils doivent être confrontés aux contributions humaines et aux générations effectives de l’IA, notamment à la rédaction substantielle des livrables, puis corrigés si nécessaire.

### 3.2 Assistance IA — Phase 1A : Cahier des charges

Les principaux domaines d’assistance sont :

- Clarification du périmètre fonctionnel.
- Structuration des objectifs du projet.
- Identification des acteurs et des cas d’utilisation.
- Identification des hypothèses et des contraintes techniques.
- Étude de la cohérence entre chiffrement et dissimulation.
- Assistance à la rédaction et à la mise en forme LaTeX.
- Correction et harmonisation documentaire.

**Pourcentage estimatif proposé : 20 %.**

### 3.3 Assistance IA — Phase 1B : Recueil des besoins fonctionnels et SRE

Dans le cadre de cet atelier, SRE signifie *Security Requirements Engineering*, soit l’ingénierie des exigences de sécurité.

Les principaux domaines d’assistance sont :

- Structuration de la méthodologie SQUARE.
- Identification des actifs et des objectifs de sécurité.
- Proposition et reformulation des exigences SMART.
- Identification des risques applicatifs.
- Formalisation des arbres d’attaque.
- Structuration des cas d’abus.
- Assistance à l’évaluation des risques.
- Préparation de la matrice de traçabilité.
- Définition des critères de validation.

**Pourcentage estimatif proposé : 25 %.**

### 3.4 Assistance IA — Phase 2 : Conception sécurisée

| Étape | Nature de l’assistance IA |
|---|---|
| **1. Objectifs de sécurité** | Structuration des objectifs opérationnels et de leur traçabilité avec les exigences SRE. |
| **2. Revue de l’application** | Organisation des acteurs, composants, technologies et cas d’utilisation. |
| **3. Décomposition de l’application** | Proposition de catalogues d’actifs, processus, magasins de données, points d’entrée et frontières de confiance. |
| **4. Modélisation STRIDE** | Proposition et classification des scénarios de menace contextualisés. |
| **5. Évaluation des risques** | Assistance à la définition des profils de cotation et au calcul des scores OWASP. |
| **6. Contre-mesures techniques** | Proposition de protections natives Jakarta EE/WildFly et de mécanismes applicatifs complémentaires. |
| **7. Architecture sécurisée** | Assistance à la formalisation des diagrammes, principes de sécurité et patrons de conception. |
| **8. Traçabilité et validation** | Structuration du plan de tests, des risques résiduels et des critères de vérification. |

L’intelligence artificielle a également contribué à :

- La génération du code source LaTeX du cahier conceptuel.
- La production des diagrammes TikZ.
- La formalisation des matrices STRIDE.
- La structuration des tableaux techniques.
- La réorganisation et l’amélioration de la mise en page.
- L’harmonisation avec les livrables de la phase 1.

**Pourcentage estimatif proposé : 30 %.**

Les scénarios, cotations et contre-mesures proposés constituent des éléments de conception. Leur efficacité pratique devra être vérifiée pendant les phases de développement et de validation.

### 3.5 Prompts d’accompagnement

Les formulations suivantes synthétisent les catégories d’interactions avec l’intelligence artificielle. Elles sont reformulées et ne constituent pas des transcriptions historiques exactes.

#### P1A — Cahier des charges

> Examiner le périmètre d’EduSecure, identifier les ambiguïtés fonctionnelles et techniques, vérifier la cohérence des mécanismes de chiffrement, de watermarking, de fingerprinting et de stéganographie, puis structurer le cahier des charges.

#### P1B — Ingénierie des exigences de sécurité

> Examiner les besoins fonctionnels et les exigences de sécurité d’EduSecure selon la démarche SQUARE. Identifier les exigences insuffisamment précises et proposer des critères SMART, des cas d’abus et des tests de validation.

#### P2A — Revue et décomposition de l’application

> Structurer la revue de l’application EduSecure et identifier ses composants, actifs, processus, points d’entrée, flux de données, dépendances externes et frontières de confiance.

#### P2B — Modélisation STRIDE

> Appliquer les six catégories STRIDE à l’architecture Jakarta EE 11, à WildFly 42 et aux moteurs de dissimulation. Identifier des menaces contextualisées, notamment la substitution de clés, l’effacement des marquages et la saturation de la Heap Java.

#### P2C — Évaluation et priorisation des risques

> Examiner les limites du modèle DREAD et la pertinence de CVSS et d’OWASP Risk Rating. Proposer une démarche de cotation adaptée au contexte d’EduSecure et expliciter les hypothèses de calcul.

#### P2D — Contre-mesures techniques

> Identifier les mécanismes de sécurité natifs de Jakarta EE, WildFly et Java, puis proposer des contre-mesures applicatives complémentaires adaptées aux menaces identifiées.

#### P2E — Architecture sécurisée

> Proposer une architecture modulaire intégrant les principes de défense en profondeur, de moindre privilège, de validation des entrées et les patrons de conception adaptés aux exigences de sécurité d’EduSecure.

#### P2F — Documentation et mise en page

> Structurer le cahier conceptuel en huit étapes de conception sécurisée. Générer et améliorer le code LaTeX, les tableaux, les diagrammes et la présentation, tout en maintenant la cohérence avec les livrables de la phase 1.

### 3.6 Contrôle humain et vérification

Avant d’intégrer une contribution issue de l’intelligence artificielle au projet, les membres s’engagent à :

- Vérifier sa conformité au cahier des charges.
- Vérifier sa cohérence avec les exigences SRE.
- Comprendre et justifier les mécanismes cryptographiques.
- Examiner la pertinence des scénarios STRIDE.
- Vérifier les hypothèses et les calculs OWASP.
- Contrôler les références techniques et bibliographiques.
- Vérifier la compatibilité des technologies retenues.
- Tester les propositions de code et de configuration.
- Documenter les limites et les risques résiduels.
- Assurer la traçabilité des modifications dans GitHub.

---

## ✍️ 4. Signatures et engagements des membres

*Cette section est destinée à être entérinée individuellement par les trois membres. Les mentions de confirmation ci-dessous prennent effet après leur accord réel.*

### Déclaration individuelle

> « Je certifie avoir lu, compris et approuvé la présente fiche d’attribution et la déclaration d’intégrité académique dans sa version 2.0. Je reconnais ma responsabilité dans les travaux du trinôme relatifs aux phases 1 et 2 du projet EduSecure. Je m’engage sur l’exactitude des informations déclarées concernant mes contributions et l’utilisation des outils d’intelligence artificielle. »

### 4.1 OUNI Mouhamed Amine

- **Adresse :** `mouhamedamine.ouni@supcom.tn`
- **Engagement initial :** 11/11/2026.
- **Date de validation de la version 2.0 :** 15/11/2026.
- **Engagement personnel :** ☑ Je souscris à la déclaration ci-dessus.
- **Validation des phases 1 et 2 :** ☑ Confirmée.
- **Signature électronique nominative :** `OUNI Mouhamed Amine`

### 4.2 BEN FADHEL Zayd

- **Adresse :** `zayd.benfadhel@supcom.tn`
- **Engagement initial :** 11/11/2026.
- **Date de validation de la version 2.0 :** 15/11/2026.
- **Engagement personnel :** ☑ Je souscris à la déclaration ci-dessus.
- **Validation des phases 1 et 2 :** ☑ Confirmée.
- **Signature électronique nominative :** `BEN FADHEL Zayd`

### 4.3 EZZINE Montassar

- **Adresse :** `montassar.ezzine@supcom.tn`
- **Engagement initial :** 11/11/2026.
- **Date de validation de la version 2.0 :** 15/11/2026.
- **Engagement personnel :** ☑ Je souscris à la déclaration ci-dessus.
- **Validation des phases 1 et 2 :** ☑ Confirmée.
- **Signature électronique nominative :** `EZZINE Montassar`

### 4.4 Validation collective

Sous réserve de leurs engagements individuels effectifs, les trois membres approuvent :

- ☑ La répartition harmonisée des responsabilités.
- ☑ Les livrables des phases 1 et 2.
- ☑ Les décisions de conception sécurisée.
- ☑ La déclaration relative à l’usage de l’intelligence artificielle.
- ☑ Les estimations déclaratives proposées de 20 %, 25 % et 30 %, à vérifier au regard des contributions réelles.
- ☑ Les engagements d’intégrité académique et d’anti-plagiat.

**Date de validation collective prévue :** 15 novembre 2026.

**Version du document :** 2.0.

---

## 📅 5. Suivi des mises à jour par phase

| Phase | Livrable / résultat attendu | Usage IA estimatif proposé | Statut |
|---|---|---:|---|
| **Phase 1A — Cahier des charges** | `docs/cahier_des_charges.pdf` — V1.1 | 20 % | ✅ Déposé |
| **Phase 1B — Recueil des besoins SRE** | `docs/recueil_des_besoins.pdf` — V2.0 | 25 % | ✅ Déposé |
| **Phase 2 — Conception sécurisée** | `docs/cahier_conceptuel.pdf` — V1.4 | 30 % | ✅ Déposé |
| **Phase 3 — Prototype métier** | Watermarking, fingerprinting, stéganographie et cryptographie | À déclarer | À réaliser |
| **Phase 4 — Intégration applicative** | API REST Jakarta EE, PostgreSQL, stockage privé et PWA | À déclarer | À réaliser |
| **Phase 5 — Tests et validation** | Tests fonctionnels, tests de sécurité et démonstration | À déclarer | À réaliser |

### 5.1 Bilan de la phase 1

Les livrables de la première phase définissent :

- Le périmètre fonctionnel d’EduSecure.
- Les acteurs et leurs responsabilités.
- L’architecture générale de l’application.
- Les 21 besoins fonctionnels.
- Les exigences de sécurité SMART.
- L’application de la démarche SQUARE.
- Les arbres d’attaque et les cas d’abus.
- Le registre initial des risques.
- Les contraintes cryptographiques et techniques.
- Les critères de recette et les scénarios de validation.

### 5.2 Bilan de la phase 2

Le cahier conceptuel de conception sécurisée comprend :

| Élément | Quantité |
|---|---:|
| Étapes de conception sécurisée | 8 |
| Objectifs de sécurité | 15 |
| Actifs identifiés | 20 |
| Processus logiques | 18 |
| Magasins de données | 10 |
| Points d’entrée | 30 |
| Frontières de confiance | 8 |
| Scénarios de menace STRIDE | 60 |
| Cotations initiales des risques | 60 |
| Principes de conception sécurisée | 25 |
| Tests de sécurité prévus | 33 |

Le cahier conceptuel comprend également :

- Des diagrammes d’architecture et de déploiement.
- Des diagrammes de flux de données (DFD).
- Des diagrammes d’activités.
- Des diagrammes de gestion des états.
- Les contre-mesures natives et applicatives.
- Les patrons de conception sécurisée.
- Les contrats de sécurité des moteurs de dissimulation.
- L’identification des risques résiduels.

Les cotations constituent des hypothèses de conception. Les tests de sécurité et les évaluations de performance restent à exécuter pendant la réalisation pratique.

### 5.3 Historique des révisions

| Date | Version | Modification |
|---|---|---|
| 11/11/2026 | 1.0 | Identification du trinôme, déclaration initiale d’intégrité académique et déclaration d’usage de l’IA pour la phase 1. |
| 15/11/2026 | 2.0 | Harmonisation des responsabilités, actualisation des livrables de la phase 1, intégration de la phase 2 et mise à jour des engagements. |

---

## 📚 6. Références principales

Les travaux du trinôme s’appuient notamment sur les ressources suivantes :

- Supports pédagogiques de sécurité applicative — SUP’COM.
- Documentation officielle Jakarta EE 11.
- Documentation officielle WildFly 42.
- Java Cryptography Architecture (JCA).
- Java Cryptography Extension (JCE).
- Web Crypto API — W3C.
- Security Quality Requirements Engineering (SQUARE) — Carnegie Mellon SEI.
- Modèle de menaces STRIDE.
- OWASP Risk Rating Methodology.
- OWASP Application Security Verification Standard (ASVS).
- Documentation Apache PDFBox.
- NIST SP 800-57 — Recommendation for Key Management.

Les références détaillées sont présentées dans les bibliographies des livrables correspondants.

---

## 🔗 7. Dépôt GitHub et traçabilité

**Dépôt officiel du projet :**

https://github.com/amounaamine222432-create/edusecure-jakartaee-pwa

**Organisation des livrables :**

- `ATTRIBUTION.md` — Fiche d’attribution et d’intégrité académique.
- `README.md` — Présentation générale du projet.
- `docs/cahier_des_charges.pdf` — Phase 1A.
- `docs/recueil_des_besoins.pdf` — Phase 1B.
- `docs/cahier_conceptuel.pdf` — Phase 2.

**Commit de publication des trois livrables :** `ab03ca8`

L’historique GitHub permet d’assurer la traçabilité des modifications et des contributions au projet.

---

**Engagement final**

Le trinôme n° 6 demeure responsable de l’exactitude des déclarations, de la compréhension des contributions techniques, de la vérification des contenus générés par l’intelligence artificielle et de la validation académique des livrables EduSecure.

**SUP’COM — Année universitaire 2026–2027**

**EduSecure — Trinôme n° 6**