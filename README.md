\# 🔐 EduSecure

\## Plateforme sécurisée de distribution et de traçabilité des documents pédagogiques



\*\*EduSecure\*\* est une application web progressive (PWA) conçue pour sécuriser la distribution des documents pédagogiques entre enseignants et étudiants.



La plateforme associe des techniques de chiffrement, de stéganographie, de tatouage numérique (\*watermarking\*) et d'empreinte numérique (\*fingerprinting\*) afin de garantir la confidentialité des documents, leur intégrité et leur traçabilité.



Ce projet est réalisé dans le cadre de l'atelier de développement d'applications PWA/SPA utilisant \*\*Jakarta EE 11\*\*, \*\*WildFly 42\*\* et \*\*Java 25\*\*.



> \*\*État du projet :\*\* phase de conception et de spécification des exigences. Le développement des fonctionnalités est prévu dans les prochaines phases.



\---



\## 1. Présentation du projet



\### 1.1 Contexte



Dans un environnement universitaire, les enseignants partagent régulièrement des supports de cours, des travaux dirigés, des examens et d'autres documents pédagogiques avec leurs étudiants.



Les solutions de partage traditionnelles présentent plusieurs limites :



\- Possibilité de redistribution non autorisée des documents.

\- Difficulté à identifier l'origine d'une fuite.

\- Absence de mécanismes avancés de traçabilité.

\- Risques d'accès non autorisé aux ressources pédagogiques.

\- Protection insuffisante des données sensibles.

\- Difficulté à vérifier l'authenticité et l'intégrité des documents.



EduSecure propose une solution intégrant plusieurs mécanismes de sécurité et de dissimulation de l'information.



\### 1.2 Objectif général



L'objectif principal est de concevoir une plateforme permettant aux enseignants de distribuer des documents pédagogiques protégés aux étudiants autorisés, tout en assurant la traçabilité des copies distribuées.



\### 1.3 Objectifs spécifiques



\- Centraliser la gestion des documents pédagogiques.

\- Sécuriser les échanges entre enseignants et étudiants.

\- Chiffrer les documents avant leur distribution.

\- Protéger les clés de chiffrement.

\- Intégrer des informations de propriété dans les documents.

\- Produire des copies individualisées pour chaque étudiant.

\- Identifier l'origine probable d'une diffusion non autorisée.

\- Garantir la confidentialité et l'intégrité des données.

\- Assurer la disponibilité et la fiabilité de la plateforme.

\- Intégrer les exigences de sécurité dès la conception.



\---



\## 2. Acteurs de la plateforme



\### 2.1 Enseignant



L'enseignant peut :



\- S'authentifier sur la plateforme.

\- Ajouter des documents pédagogiques.

\- Consulter les documents publiés.

\- Définir les étudiants autorisés à accéder aux documents.

\- Déclencher la préparation des copies protégées.

\- Consulter les informations de distribution.

\- Vérifier la provenance d'une copie lorsque cela est autorisé.



\### 2.2 Étudiant



L'étudiant peut :



\- S'authentifier sur la plateforme.

\- Consulter les documents auxquels il est autorisé à accéder.

\- Télécharger sa copie personnalisée et protégée.

\- Utiliser sa clé privée pour récupérer les informations nécessaires au déchiffrement.

\- Consulter l'historique de ses téléchargements.



\### 2.3 Administrateur



L'administrateur peut :



\- Gérer les comptes utilisateurs.

\- Administrer les rôles et les autorisations.

\- Superviser les services de la plateforme.

\- Consulter les journaux d'audit.

\- Détecter les comportements suspects.

\- Assurer la maintenance et le suivi opérationnel.



\---



\## 3. Fonctionnalités principales



\### 3.1 Gestion des utilisateurs



La plateforme prévoit :



\- L'authentification des utilisateurs.

\- La gestion des profils.

\- La séparation des rôles.

\- Le contrôle d'accès aux documents.

\- La gestion des autorisations selon les responsabilités.



\### 3.2 Gestion des documents pédagogiques



Les enseignants peuvent déposer des documents pédagogiques.



Le système assure leur préparation avant distribution, notamment :



1\. Vérification des autorisations.

2\. Validation du document.

3\. Application du tatouage numérique.

4\. Génération d'une copie individualisée.

5\. Chiffrement du contenu.

6\. Préparation des informations de récupération.

7\. Mise à disposition auprès de l'étudiant autorisé.



\### 3.3 Chiffrement hybride



EduSecure repose sur une approche combinant deux mécanismes cryptographiques.



\*\*Chiffrement symétrique : AES-256-GCM\*\*



Il est utilisé pour protéger le contenu des documents.



Avantages :



\- Performances adaptées aux fichiers.

\- Confidentialité du contenu.

\- Vérification de l'intégrité du document chiffré.



\*\*Chiffrement asymétrique : RSA-OAEP\*\*



Il permet de protéger la clé symétrique destinée à l'étudiant.



La clé AES utilisée pour le document est chiffrée à l'aide de la clé publique du destinataire.



Seule la clé privée correspondante permet de récupérer cette clé AES.



\### 3.4 Stéganographie



La stéganographie consiste à dissimuler une information dans un support numérique.



Dans EduSecure, cette technique est envisagée pour dissimuler une information cryptographique protégée dans une image associée à l'étudiant.



Le mécanisme prévu comprend :



1\. Protection de la clé AES à l'aide de la clé publique de l'étudiant.

2\. Insertion de la clé AES chiffrée dans une image.

3\. Transmission sécurisée de l'image contenant cette information.

4\. Extraction de l'information par l'étudiant autorisé.

5\. Déchiffrement de la clé AES avec sa clé privée.



La plateforme devra conserver l'image de profil originale sans modification.



\*\*Important :\*\* la stéganographie complète le chiffrement, mais ne constitue pas à elle seule une protection cryptographique.



\### 3.5 Tatouage numérique (Watermarking)



Le tatouage numérique permet d'intégrer une information de propriété au document.



Il contribue notamment à :



\- Identifier l'origine du document.

\- Associer le document à son propriétaire.

\- Faciliter la vérification de sa provenance.

\- Renforcer la protection contre les utilisations non autorisées.



\### 3.6 Empreinte numérique (Fingerprinting)



Chaque étudiant reçoit une copie individualisée du document.



Un identifiant de traçabilité est associé à cette copie.



Cette fonctionnalité permet de :



\- Distinguer les copies distribuées.

\- Associer une copie à un destinataire.

\- Faciliter l'investigation en cas de diffusion non autorisée.

\- Vérifier la correspondance entre une copie et son enregistrement de distribution.



Le mécanisme devra prendre en compte la protection des données personnelles des étudiants.



\---



\## 4. Processus de distribution sécurisée



Le fonctionnement cible est le suivant :



\*\*Étape 1 — Dépôt\*\*



L'enseignant dépose un document pédagogique sur la plateforme.



\*\*Étape 2 — Vérification\*\*



Le système vérifie les droits de l'enseignant et valide le document.



\*\*Étape 3 — Tatouage numérique\*\*



Une information de propriété est intégrée au document.



\*\*Étape 4 — Personnalisation\*\*



Une copie spécifique est préparée pour chaque étudiant autorisé.



\*\*Étape 5 — Chiffrement\*\*



La copie est chiffrée à l'aide d'AES-256-GCM.



\*\*Étape 6 — Protection de la clé\*\*



La clé AES est protégée au moyen de RSA-OAEP en utilisant la clé publique de l'étudiant.



\*\*Étape 7 — Stéganographie\*\*



L'information cryptographique protégée est dissimulée dans une image dédiée.



\*\*Étape 8 — Distribution\*\*



Les éléments nécessaires à la récupération du document sont mis à disposition de l'étudiant autorisé.



\*\*Étape 9 — Récupération\*\*



L'étudiant récupère la clé AES au moyen de sa clé privée, puis déchiffre son document.



\*\*Étape 10 — Traçabilité\*\*



Les événements de distribution et de téléchargement sont enregistrés à des fins d'audit.



\---



\## 5. Architecture technique



EduSecure adopte une architecture séparant la présentation, la logique métier et le stockage des données.



\### 5.1 Interface utilisateur



L'interface repose sur les principes :



\- SPA (\*Single Page Application\*).

\- PWA (\*Progressive Web Application\*).

\- Interface responsive.

\- Navigation dynamique.

\- Communication avec le serveur via des API REST.



\### 5.2 Serveur applicatif



Le middleware est développé avec :



\- Java 25.

\- Jakarta EE 11.

\- WildFly 42.

\- API REST Jakarta.

\- Services métier dédiés à la sécurité des documents.



\### 5.3 Services métier



Les principaux modules envisagés sont :



| Module | Responsabilité |

|---|---|

| Authentification | Vérification de l'identité |

| Autorisation | Contrôle des droits d'accès |

| Documents | Gestion des documents pédagogiques |

| Chiffrement | Protection cryptographique des documents |

| Stéganographie | Dissimulation des informations protégées |

| Watermarking | Intégration des informations de propriété |

| Fingerprinting | Individualisation et traçabilité des copies |

| Audit | Journalisation des événements de sécurité |



\### 5.4 Stockage



La plateforme prévoit le stockage des informations nécessaires à son fonctionnement :



\- Utilisateurs et rôles.

\- Métadonnées des documents.

\- Autorisations d'accès.

\- Références des copies distribuées.

\- Informations de traçabilité.

\- Journaux d'audit.



Le choix définitif des technologies de stockage sera documenté pendant la conception détaillée.



\---



\## 6. Technologies



| Technologie | Utilisation |

|---|---|

| Java 25 | Développement de la logique métier |

| Jakarta EE 11 | Services du middleware |

| WildFly 42 | Serveur d'applications |

| SPA | Interface web dynamique |

| PWA | Fonctionnalités d'application web progressive |

| API REST | Communication entre frontend et backend |

| AES-256-GCM | Chiffrement et intégrité des documents |

| RSA-OAEP | Protection des clés symétriques |

| Stéganographie | Dissimulation d'informations |

| Watermarking | Identification de la propriété |

| Fingerprinting | Identification des copies distribuées |

| Git | Gestion des versions |

| GitHub | Collaboration et suivi du développement |



\---



\## 7. Sécurité de la plateforme



La sécurité est considérée dès les premières phases de conception, conformément à l'approche \*\*Shift-Left Security\*\*.



Les exigences seront analysées et affinées progressivement tout au long du projet.



\### 7.1 Confidentialité



\- Accès réservé aux utilisateurs autorisés.

\- Chiffrement des documents sensibles.

\- Protection des clés cryptographiques.

\- Sécurisation des communications.



\### 7.2 Intégrité



\- Vérification de l'intégrité des documents.

\- Détection des modifications non autorisées.

\- Traçabilité des opérations sensibles.



\### 7.3 Disponibilité



\- Gestion des erreurs et des incidents.

\- Surveillance de l'état des services.

\- Stratégie de sauvegarde et de restauration.

\- Préparation des mécanismes de reprise.



\### 7.4 Authentification et autorisation



\- Identification des utilisateurs.

\- Gestion des rôles.

\- Contrôle d'accès aux ressources.

\- Vérification des autorisations côté serveur.



\### 7.5 Journalisation et audit



\- Enregistrement des connexions et événements sensibles.

\- Suivi des téléchargements.

\- Traçabilité des accès aux documents.

\- Détection des comportements anormaux.

\- Protection des journaux contre les modifications non autorisées.



\### 7.6 Analyse des menaces



L'analyse de sécurité s'appuiera notamment sur :



\- L'identification des actifs critiques.

\- La modélisation des menaces.

\- Les cas de mésusage.

\- L'analyse des scénarios d'attaque.

\- La priorisation des risques.

\- La définition de critères de validation mesurables.



Les contrôles seront adaptés aux risques réels du projet.



\---



\## 8. Approche SRE



L'approche \*\*Site Reliability Engineering (SRE)\*\* vise à garantir la fiabilité, la disponibilité et la maintenabilité de la plateforme.



Elle est particulièrement importante pour EduSecure, car les étudiants doivent pouvoir récupérer leurs documents sans interruption prolongée, notamment pendant les périodes d'examen.



\### Objectifs



\- Mesurer la disponibilité des services.

\- Surveiller les performances des API.

\- Détecter les erreurs applicatives.

\- Suivre les opérations critiques de distribution.

\- Réduire le temps de résolution des incidents.

\- Préparer la restauration des services.



\### Indicateurs envisagés



\- Taux de disponibilité.

\- Temps de réponse des API.

\- Taux d'échec des téléchargements.

\- Taux d'erreur des opérations cryptographiques.

\- Temps de récupération après incident.



Les objectifs chiffrés, seuils et procédures opérationnelles seront définis et justifiés dans le recueil des besoins SRE.



\---



\## 9. Organisation du dépôt



```text

edusecure-jakartaee-pwa/

│

├── README.md

├── .gitignore

│

├── docs/

│   ├── cahier\_des\_charges.pdf

│   └── recueil\_des\_besoins\_fonctionnels\_sre.pdf

│

├── backend/

│   └── README.md

│

├── frontend/

│   └── README.md

│

├── tests/

│   └── README.md

│

└── scripts/

&#x20;   └── README.md

```



\### Description des répertoires



| Répertoire | Description |

|---|---|

| `docs/` | Documents de conception et livrables |

| `backend/` | Développement du middleware Jakarta EE |

| `frontend/` | Développement de l'interface SPA/PWA |

| `tests/` | Tests fonctionnels, techniques et de sécurité |

| `scripts/` | Scripts d'automatisation et utilitaires |



Cette organisation correspond à la structure initiale. Elle évoluera avec l'avancement du développement.



\---



\## 10. Documentation du projet



\### Première séance — Analyse et spécification



Les deux premiers livrables sont disponibles au format PDF.



| Livrable | Description | Document |

|---|---|---|

| Cahier des charges | Présentation du projet, objectifs, périmètre et fonctionnalités | \[Consulter le cahier des charges](docs/cahier\_des\_charges.pdf) |

| Recueil des besoins fonctionnels et SRE | Exigences métier, sécurité, fiabilité et justification de l'approche SRE | \[Consulter le recueil des besoins](docs/recueil\_des\_besoins\_fonctionnels\_sre.pdf) |



Ces documents constituent la base de référence du projet et pourront être mis à jour conformément à la gestion des exigences.



\---



\## 11. Méthodologie de développement



Le projet sera réalisé progressivement selon les étapes suivantes.



\### Phase 1 — Analyse des besoins



\- Définition du périmètre.

\- Identification des acteurs.

\- Élaboration du cahier des charges.

\- Recueil des besoins fonctionnels.

\- Analyse initiale des exigences de sécurité et SRE.



\### Phase 2 — Conception



\- Définition de l'architecture.

\- Modélisation des données.

\- Conception des API REST.

\- Définition des interfaces utilisateur.

\- Conception des mécanismes cryptographiques.



\### Phase 3 — Développement



\- Implémentation du backend.

\- Implémentation du frontend.

\- Intégration des services de dissimulation de l'information.

\- Mise en place du contrôle d'accès.



\### Phase 4 — Vérification



\- Tests unitaires.

\- Tests d'intégration.

\- Tests fonctionnels.

\- Tests de sécurité.

\- Vérification des exigences de performance et de fiabilité.



\### Phase 5 — Intégration et validation



\- Déploiement sur WildFly.

\- Validation des scénarios métier.

\- Vérification de la traçabilité.

\- Contrôle de conformité avec les exigences spécifiées.

\- Préparation de la démonstration finale.



\---



\## 12. Équipe du projet



\*\*SUP'COM — Groupe 6\*\*



| Membre | Établissement |

|---|---|

| OUNI Mouhamed Amine | SUP'COM |

| BEN FADHEL Zayd | SUP'COM |

| EZZINE Montassar | SUP'COM |



\### Encadrant



\*\*Mohamed-Becha KAANICHE\*\*



\---



\## 13. Cadre académique



\- \*\*Établissement :\*\* École Supérieure des Communications de Tunis (SUP'COM).

\- \*\*Type de projet :\*\* Atelier de développement d'applications web.

\- \*\*Architecture :\*\* PWA/SPA avec middleware Jakarta EE.

\- \*\*Thématique :\*\* Sécurité et dissimulation de l'information.

\- \*\*Année universitaire :\*\* 2026–2027.



\---



\## 14. État d'avancement



| Élément | État |

|---|---|

| Définition du sujet | Terminé |

| Constitution de l'équipe | Terminé |

| Cahier des charges | Terminé |

| Recueil des besoins fonctionnels et SRE | Terminé |

| Initialisation du dépôt Git | Terminé |

| Conception détaillée | À réaliser |

| Développement du backend | À réaliser |

| Développement du frontend | À réaliser |

| Intégration cryptographique | À réaliser |

| Tests et validation | À réaliser |



\---



\*\*EduSecure — Sécuriser, personnaliser et tracer la distribution des documents pédagogiques.\*\*

