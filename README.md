# EXAPUNKS Clone

> Clone du jeu de hacking **EXAPUNKS** — agents programmables, manipulation de fichiers, puzzles en assemblage.
> Projet L2 Informatique · Université Sorbonne Paris Nord · Java Swing · Architecture MVC

---

## Aperçu

EXAPUNKS Clone met le joueur aux commandes de robots (EXAs) qu'il programme dans un langage d'assemblage simplifié. Les robots naviguent sur une grille 5×5, ramassent et manipulent des fichiers, calculent des résultats et les déposent dans une zone cible (OUTBOX) pour valider chaque mission.

**7 niveaux** progressifs, de *Moyen* à *Ultime*, incluant des missions de calcul, de navigation, de logique conditionnelle et de coordination multi-robots.

---

## Prérequis

- **JDK 17** ou supérieur

---

## Installation et lancement

```bash
# 1. Compiler
mkdir -p bin
javac -d bin -sourcepath src src/controller/Main.java

# 2. Lancer le jeu
java -cp bin controller.Main
```

---

## Interface de jeu

```
┌─────────────────────────────────────────────────────────────────┐
│  ÉDITEUR DE CODE      │        GRILLE 5×5          │  FICHIERS  │
│  ┌──────────┬──────┐  │  ┌───┬───┬───┬───┬───┐    │  ┌─ F200   │
│  │ Code EXA │  X   │  │  │   │   │   │   │ R │    │  │  10     │
│  │          │  T   │  │  ├───┼───┼───┼───┼───┤    │  │  25     │
│  │          │  F   │  │  │   │░░░│   │░░░│   │    │  └──────── │
│  │          │  M   │  │  └───┴───┴───┴───┴───┘    │            │
│  └──────────┴──────┘  │                            │            │
│  [RESET][STEP][RUN][PAUSE][QUIT]   DÉLAI: 300ms    │            │
├───────────────────────────────────────────────────────────────  │
│  [ → ] STEP   [ ESPACE ] RUN   [ P ] PAUSE   [ R ] RESET        │
│  OBJECTIF : Lire F200 | Calculer 10 + 25 | Déposer en OUTBOX   │
└─────────────────────────────────────────────────────────────────┘
```

| Zone | Rôle |
|---|---|
| **Éditeur** | Saisir les instructions EXA du robot |
| **Registres X T F M** | Valeurs en temps réel |
| **Grille** | Visualisation des robots, fichiers, portails, OUTBOX |
| **Panneau FILES** | Contenu de chaque fichier mis à jour en live |
| **Barre du bas** | Raccourcis clavier + objectif de la mission |

---

## Raccourcis clavier

| Touche | Action |
|---|---|
| `Ctrl + →` | Exécuter une instruction (STEP) |
| `Ctrl + Entrée` | Lancer l'exécution automatique (RUN) |
| `Ctrl + P` | Mettre en pause |
| `Ctrl + R` | Réinitialiser le niveau |

---

## Langage EXA — Référence complète

### Déplacement

| Instruction | Syntaxe | Description |
|---|---|---|
| `LINK` | `LINK left/right/up/down` | Déplace le robot d'une case |

> Les portails (PORTE2↔PORTE5, PORTE3↔PORTE4) téléportent instantanément le robot au portail jumeau.

### Fichiers

| Instruction | Syntaxe | Description |
|---|---|---|
| `GRAB` | `GRAB <id>` | Ramasse le fichier `id` sur la case courante |
| `DROP` | `DROP` | Dépose le fichier tenu sur la case courante |

> `COPY F X` — lit et **supprime** la première valeur du fichier tenu → stocke dans X
> `COPY X F` — **ajoute** la valeur de X à la fin du fichier tenu

### Registres

| Registre | Rôle |
|---|---|
| `X` | Registre de calcul général |
| `T` | Résultat des tests (`1` = vrai, `0` = faux) |
| `F` | Accès au fichier tenu (lecture/écriture) |
| `M` | Registre partagé entre robots (communication) |

### Calcul

| Instruction | Syntaxe | Description |
|---|---|---|
| `ADDI` | `ADDI v1 v2 dest` | `dest = v1 + v2` |
| `SUBI` | `SUBI v1 v2 dest` | `dest = v1 - v2` |
| `MULI` | `MULI v1 v2 dest` | `dest = v1 × v2` |
| `DIVI` | `DIVI v1 v2 dest` | `dest = v1 ÷ v2` (entier) |
| `MODI` | `MODI v1 v2 dest` | `dest = v1 mod v2` |

> `v1`, `v2` peuvent être un registre (`X`, `T`, `M`, `F`) ou un entier littéral.

### Contrôle du flux

| Instruction | Syntaxe | Description |
|---|---|---|
| `TEST` | `TEST v1 op v2` | Compare v1 et v2 (`=`, `!=`, `>`, `<`, `>=`, `<=`), résultat dans T |
| `JUMP` | `JUMP label` | Saut inconditionnel |
| `FJUMP` | `FJUMP label` | Saut si T = 0 (faux) |
| `TJUMP` | `TJUMP label` | Saut si T ≠ 0 (vrai) |
| `MARK` | `MARK label` | Définit un label (ou `label:`) |
| `NOOP` | `NOOP` | Ne fait rien (attend un cycle) |
| `HALT` | `HALT` | Arrête le robot et le retire de la grille |

---

## Les 7 niveaux

| # | Difficulté | Mission | Mécanique principale |
|---|---|---|---|
| 1 | 🟡 MOYEN | Additionner 10 + 25 = **35** | `ADDI`, navigation simple |
| 2 | 🟠 RÉFLÉCHIR | Multiplier 6 × 4 = **24** | `MULI`, navigation corridors |
| 3 | 🔴 DIFFICILE | Si F[0] > F[1] → différence, sinon 0 | `TEST`, `FJUMP`, `SUBI` |
| 4 | 🟣 AVANCÉ | Produit 5 × 9 = **45** (deux fichiers) | Deux fichiers, `MULI X F X` |
| 5 | ⚫ EXPERT | Sommer 4 valeurs = **30** | Lectures multiples, `ADDI` |
| 6 | 🔵 MASTER | 72 ÷ 8 = **9** | `DIVI`, navigation labyrinthique |
| 7 | ⭐ ULTIME | Deux robots, coordination M, 11+7 = **18** | 2 robots, `NOOP`, registre `M` |

> Les solutions complètes sont disponibles dans [`SOLUTIONS.md`](SOLUTIONS.md).

---

## Architecture du code

```
src/
├── controller/
│   ├── Main.java              Point d'entrée
│   └── GameController.java    Exécution des instructions EXA
├── model/
│   ├── Grille.java            Grille de jeu, portails, GRAB/DROP
│   ├── Robot.java             Agent EXA (registres X, T, M, fichier)
│   ├── Fichier.java           Fichier FIFO
│   ├── Niveau.java            Classe abstraite des niveaux
│   ├── Niveau1..7.java        7 niveaux concrets
│   ├── AnalyseurSyntaxique.java  Parseur d'instructions
│   └── Instruction.java       Représentation d'une instruction
└── view/
    ├── GameWindow.java        Fenêtre principale, écrans boot/menu/jeu
    ├── GamePanel.java         Rendu graphique de la grille (dynamique)
    ├── TextZone.java          Éditeur de code + registres
    ├── Controller.java        Thread de jeu, synchronisation
    ├── WinScreen.java         Écran de victoire
    └── DefeatScreen.java      Écran d'échec
```

Le projet suit le pattern **MVC** :
- **Modèle** (`model/`) — logique métier pure, indépendante de l'affichage
- **Vue** (`view/`) — interface Swing, rendu dynamique adaptatif
- **Contrôleur** (`controller/`) — pont entre vue et modèle, thread séparé pour ne pas bloquer l'EDT

---

## Équipe

Projet réalisé par une équipe de **7 personnes** — L2 Informatique, Université Sorbonne Paris Nord.
