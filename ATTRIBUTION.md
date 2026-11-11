# 📑 Fiche d’attribution, d’intégrité académique et d’usage de l’IA

> **Projet : EduSecure — Plateforme sécurisée de distribution et de traçabilité des documents pédagogiques**  
> **Statut du document : bilan de la phase 1 ; phase 2 non commencée.**  
> **Dernière mise à jour : 11 novembre 2026**

Ce document accompagne les livrables de l’atelier PWA/SPA et Jakarta EE. Il identifie les membres du trinôme, formalise leur engagement d’intégrité académique et rend transparent l’usage éventuel des outils d’intelligence artificielle. Il doit être actualisé à chaque phase du projet.

---

## 👥 1. Identification de l’équipe et rôles

- **Établissement :** École Supérieure des Communications de Tunis (SUP’COM)
- **Année universitaire :** 2026–2027
- **Groupe :** Trinôme n° 6
- **Application métier :** EduSecure
- **Objet :** Distribution sécurisée de documents pédagogiques avec chiffrement hybride, stéganographie, watermarking et fingerprinting
- **Encadrant :** Mohamed-Bécha KAANICHE
- **Socle technique prévu :** Java 25, Jakarta EE 11, WildFly 42, SPA/PWA

### Membres de l’équipe

| Membre | Adresse électronique | Rôle principal |
| :--- | :--- | :--- |
| **OUNI Mouhamed Amine** | `mouhamedamine.ouni@supcom.tn` | Coordination du trinôme, architecture de sécurité et conception du chiffrement hybride |
| **BEN FADHEL Zayd** | `zayd.benfadhel@supcom.tn` | Analyse des besoins fonctionnels et SRE, objectifs de fiabilité et évaluation des risques |
| **EZZINE Montassar** | `montassar.ezzine@supcom.tn` | Étude des services de dissimulation, revue des flux et modélisation des menaces |

**Répartition de travail :** ces responsabilités structurent le travail du trinôme. Les membres contribuent collectivement aux décisions de conception et aux livrables.

---

## 📜 2. Déclaration d’intégrité et d’anti-plagiat

En validant individuellement la section « Signatures », chaque membre certifie sur l’honneur que :

1. Les décisions de conception, les choix techniques et l’implémentation métier d’EduSecure sont élaborés, compris et validés par le trinôme.
2. Aucun code, schéma, diagramme ou passage documentaire provenant d’un autre groupe de l’atelier n’est repris sans autorisation ni attribution appropriée.
3. Toute ressource externe exploitée (documentation Jakarta EE et WildFly, bibliothèques open source, spécifications cryptographiques, articles scientifiques ou autres références) est citée à l’endroit pertinent dans les documents ou dans le code.
4. Les outils d’intelligence artificielle éventuellement utilisés sont déclarés de manière transparente ci-dessous ; les résultats sont relus, vérifiés et adaptés au contexte du projet.
5. Chaque membre est en mesure d’expliquer les parties dont il assume la responsabilité lors des points de contrôle en classe.

**La validation de cette déclaration demeure en attente des engagements individuels indiqués en section 4.**

---

## 🤖 3. Déclaration de l’usage des intelligences artificielles

L’IA peut servir d’assistant à la réflexion, à la structuration, à la reformulation et au débogage. Elle ne remplace ni l’analyse des risques ni la responsabilité intellectuelle de l’équipe.

### 3.1 Tableau de transparence IA — Phase 1

Les chiffres ci-dessous sont des **estimations indicatives proposées par l'équipe de rédaction**. Ils doivent refléter la part réelle de contenu généré par l'IA, et non le temps passé à l'utiliser. Le modèle indiqué est ChatGPT — GPT-6. La phase 2 ne fait l'objet d'aucune déclaration d'usage à ce stade, car elle n'a pas encore commencé.

| Composant / phase | IA utilisée (nom/version) | Pourcentage estimé de contenu généré | Rôle précis de l’IA et prompts clés |
| :--- | :--- | :--- | :--- |
| **Phase 1A — Cahier des charges** | ChatGPT — **GPT-6** | **20 % (estimation)** | Questionnement sur le périmètre du projet, les cas d'utilisation et les choix cryptographiques ; identification de lacunes éventuelles, sans rédaction autonome des décisions métier. Voir **P1A**. |
| **Phase 1B — Recueil des besoins fonctionnels et SRE** | ChatGPT — **GPT-6** | **25 % (estimation)** | Aide à examiner la précision et la vérifiabilité des exigences, à interroger les hypothèses sur les SLI/SLO et à repérer des exigences de sécurité manquantes. Voir **P1B**. |

### 3.2 Prompts d'accompagnement pour la phase 1

Les formulations ci-dessous constituent des **exemples de prompts d'accompagnement** : elles illustrent l'aide à la réflexion et à la relecture sans déléguer la réalisation des livrables. Elles ne sont pas présentées comme des transcriptions historiques ; seuls les prompts réellement utilisés doivent être déclarés comme tels.

**P1A — Relecture critique du cahier des charges (phase 1A)**

> « Nous avons rédigé le cahier des charges d'EduSecure, une application PWA/SPA basée sur Jakarta EE 11. Agis comme un relecteur exigeant : pose-nous des questions pour identifier les ambiguïtés de notre périmètre, les hypothèses non justifiées et les incohérences possibles entre chiffrement, stéganographie, watermarking et fingerprinting. Ne rédige pas le cahier des charges et ne prends aucune décision à notre place. »

**P1B — Auto-évaluation des besoins fonctionnels et SRE (phase 1B)**

> « Examine nos exigences fonctionnelles et SRE pour EduSecure uniquement sous l'angle de leur clarté, de leur testabilité et de leurs liens avec le triptyque confidentialité-intégrité-disponibilité. Signale les indicateurs SRE insuffisamment définis, pose-nous les questions nécessaires pour justifier nos objectifs, et suggère des critères de vérification. Ne crée pas d'exigences finales sans notre validation. »

### 3.3 Contrôle humain et vérification

Avant d’intégrer une suggestion issue d’un outil d’IA au dépôt, l’équipe s’engage à :

- Vérifier sa pertinence au regard du cahier des charges et des exigences SRE.
- Comprendre et justifier les choix cryptographiques proposés.
- Vérifier les hypothèses, les références et les modèles de menaces.
- Tester toute proposition de code ou de configuration avant de la considérer comme fonctionnelle.
- Corriger et attribuer les contributions de manière transparente.

---

## ✍️ 4. Signatures et engagements des membres

**Déclaration individuelle à signer :**

> « Je certifie avoir lu et compris cette fiche d'attribution et la déclaration d'intégrité académique. J'approuve les informations qui me concernent, je reconnais ma responsabilité dans le travail du trinôme et je m'engage sur l'exactitude des déclarations relatives à l'utilisation de l'intelligence artificielle. »

### 4.1 OUNI Mouhamed Amine

- **Adresse :** `mouhamedamine.ouni@supcom.tn`
- **Date de l'engagement déclaré :** 11/11/2026
- **Engagement personnel :** ☑ Je souscris à la déclaration ci-dessus.
- **Signature électronique nominative déclarée :** `OUNI Mouhamed Amine`
- **Commit de validation (identifiant facultatif) :** `____________________________`

### 4.2 BEN FADHEL Zayd

- **Adresse :** `zayd.benfadhel@supcom.tn`
- **Date prévue :** 11/11/2026
- **Engagement personnel :** ☑ Je souscris à la déclaration ci-dessus.
- **Signature électronique nominative déclarée :** `BEN FADHEL Zayd `
- **Commit de validation (identifiant facultatif) :** `____________________________`

### 4.3 EZZINE Montassar

- **Adresse :** `montassar.ezzine@supcom.tn`
- **Date prévue :** 11/11/2026
- **Engagement personnel :** ☑ Je souscris à la déclaration ci-dessus.
- **Signature électronique nominative déclarée :** `EZZINE Montassar`
- **Commit de validation (identifiant facultatif) :** `____________________________`


## 📅 5. Suivi des mises à jour par phase

| Phase | Contenu à maintenir à jour | Statut |
| :--- | :--- | :--- |
| **Phase 1A — Cahier des charges** | Périmètre métier, acteurs, cas d'utilisation, chiffrement et dissimulation, usage de l'IA | **Livrable déposé ; validation de l'attribution à terminer** |
| **Phase 1B — Recueil des besoins** | Besoins fonctionnels, exigences de sécurité, indicateurs SRE, usage de l'IA | **Livrable déposé ; validation de l'attribution à terminer** |
| **Phase 2 — Revue de l’application et modélisation des menaces** | Objectifs, architecture analysée, actifs et frontières de confiance, STRIDE, contre-mesures et risques, usage de l’IA | **Non commencée — fiche à actualiser au jalon 2** |

> **Pour la validation académique :** l'équipe reste responsable de l'exactitude des estimations (20 % et 25 %), de l'identification du modèle et de l'enregistrement des prompts effectivement utilisés. Chaque membre signe individuellement la déclaration.
