# EXAPUNKS Clone - Projet L2 Informatique

Ce dépôt contient un clone du jeu **Exapunks**, une simulation de hacking basée sur des puzzles, réalisé dans le cadre d'un cours de développement logiciel en 2ème année de Licence Informatique à l'Université Sorbonne Paris Nord.

## Description du Projet

L'objectif était de reproduire les mécanismes de jeu d'Exapunks : contrôler des agents (EXAs) via un langage d'assemblage simplifié pour manipuler des fichiers, naviguer dans des réseaux et résoudre des énigmes. Le projet utilise Java Swing pour l'interface graphique.

## Installation et Utilisation

### Prérequis
- Java Development Kit (JDK) 17 ou supérieur.

### Compilation
Depuis la racine du projet, créez un dossier `bin` et compilez les fichiers source :
```bash
mkdir -p bin
javac -d bin -sourcepath src src/controller/Main.java
```

### Lancement du Jeu
Pour lancer l'application graphique :
```bash
java -cp bin controller.Main
```

Pour tester la partie textuelle (interpréteur de commandes seul) :
```bash
java -cp bin main.TestPartieTextuelle
```

## Comment Jouer

1. **Lancement** : Cliquez sur "Jouer" dans le menu principal, puis choisissez un niveau (Niveau 1, 2 ou 3).
2. **Interface** :
   - **Zone de Texte** : Saisissez vos instructions dans le champ de texte à gauche.
   - **Mémoire (X, T, M)** : Visualisez l'état des registres de votre robot.
   - **Grille** : Observez les déplacements du robot et la position des fichiers.
3. **Contrôles** :
   - Cliquez sur **"Avancer"** pour exécuter les instructions ligne par ligne.
   - Cliquez sur **"Stop"** pour réinitialiser le niveau.

### Instructions de Base (Langage EXA)

| Commande | Usage | Description |
| :--- | :--- | :--- |
| `LINK` | `LINK <dir/id>` | Déplace le robot (directions : left, right, up, down). |
| `COPY` | `COPY <src> <dest>` | Copie une valeur dans un registre (X, T) ou un fichier (F). |
| `GRAB` | `GRAB <id_fichier>` | Ramasse un fichier présent sur la case. |
| `DROP` | `DROP` | Dépose le fichier tenu. |
| `ADDI` | `ADDI <v1> <v2> <d>` | Additionne v1 et v2, stocke dans d. |
| `TEST` | `TEST X != 0` | Compare une valeur et met à jour le registre T (Vrai/Faux). |
| `JUMP` | `JUMP <ligne>` | Saute à une ligne spécifique du code. |
| `HALT` | `HALT` | Arrête l'exécution et retire le robot de la grille. |

### Registres
- **X** : Registre de calcul général.
- **T** : Utilisé pour les tests et les sauts conditionnels (`FJUMP`).
- **F** : Accès au contenu du fichier actuellement tenu (lecture/écriture).

## Contributions

Le projet a été réalisé par une équipe de **7 personnes**. En tant que coordinateur, j'ai veillé à la répartition des tâches, au respect des délais et à la fluidité de la communication.

## Organisation du Code

Le code suit le pattern **MVC (Modèle-Vue-Contrôleur)** :
- `src/model/` : Logique métier (Robot, Grille, Instructions, Niveaux).
- `src/view/` : Interface graphique (Swing, Panneaux, Fenêtres).
- `src/controller/` : Gestion des événements et exécution des commandes.

Le code source complet et l'historique sont disponibles sur le GitLab de l'université : [Lien GitLab](https://gitlab.sorbonne-paris-nord.fr/12209923/exapunks.git).

## Remerciements

Merci à toute l'équipe pour son dévouement et son travail acharné sur ce projet.
