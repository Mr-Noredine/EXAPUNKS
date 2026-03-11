# Solutions — EXAPUNKS Clone

> Ce fichier contient les solutions complètes pour chacun des 7 niveaux.
> Chaque solution est expliquée étape par étape avec la carte du niveau et le code à saisir.

---

## Comment lire les solutions

- Les **coordonnées** sont notées `(X, Y)` où X = colonne (0→gauche, 4→droite) et Y = ligne (0→haut, 4→bas)
- `[MURE]` = case inaccessible (obstacle)
- `[OUTBOX]` = destination finale
- `R` = position de départ du robot
- `Fn` = fichier numéro n

---

## Niveau 1 — MOYEN · "Additionner"

**Objectif** : Lire les deux valeurs du fichier F200 (10 et 25), les additionner (= **35**), déposer F200 en OUTBOX.

### Carte
```
     X0     X1     X2     X3     X4
Y0:   R    LIBRE  LIBRE  LIBRE   F200
Y1:  LIBRE [MURE] LIBRE [MURE]  LIBRE
Y2:  LIBRE  LIBRE  LIBRE  LIBRE  LIBRE
Y3:  LIBRE [MURE] LIBRE [MURE]  LIBRE
Y4:  LIBRE  LIBRE [OUTBOX] LIBRE LIBRE
```

### Stratégie
1. Aller en (4,0) pour ramasser F200
2. Lire les deux valeurs dans X et T, calculer X+T
3. Écrire le résultat dans le fichier
4. Rejoindre l'OUTBOX en (2,4)

### Code (Robot 1)
```
LINK right
LINK right
LINK right
LINK right
GRAB 200
COPY F X
COPY F T
ADDI X T X
COPY X F
LINK down
LINK down
LINK down
LINK down
LINK left
LINK left
DROP
HALT
```

---

## Niveau 2 — RÉFLÉCHIR · "Quadrupler"

**Objectif** : Le robot part en (4,0), atteint F200 en (0,4) contenant **6**, le multiplie par 4 (= **24**), dépose en OUTBOX (4,4).

### Carte
```
     X0     X1     X2     X3     X4
Y0:  LIBRE  LIBRE  LIBRE  LIBRE   R
Y1: [MURE]  LIBRE [MURE]  LIBRE [MURE]
Y2:  LIBRE  LIBRE  LIBRE  LIBRE  LIBRE
Y3: [MURE]  LIBRE [MURE]  LIBRE [MURE]
Y4:  F200   LIBRE  LIBRE  LIBRE [OUTBOX]
```

### Stratégie
Le robot ne peut pas descendre directement sur les bords.
Il doit passer par la colonne X=1 pour descendre, puis aller chercher F200.

1. Aller à gauche jusqu'en (1,0)
2. Descendre par la colonne X=1 jusqu'en (1,4)
3. Aller à gauche en (0,4) → GRAB F200
4. Multiplier par 4 → écrire résultat
5. Aller à droite jusqu'en (4,4) → DROP

### Code (Robot 1)
```
LINK left
LINK left
LINK left
LINK down
LINK down
LINK down
LINK down
LINK left
GRAB 200
COPY F X
MULI X 4 X
COPY X F
LINK right
LINK right
LINK right
LINK right
DROP
HALT
```

---

## Niveau 3 — DIFFICILE · "Soustraction conditionnelle"

**Objectif** : Lire F200 contenant **[15, 8]**. Si la première valeur > la seconde, écrire leur différence (= **7**). Sinon écrire 0. Déposer en OUTBOX (2,4).

### Carte
```
     X0     X1     X2     X3     X4
Y0:   R    LIBRE  LIBRE  LIBRE   F200
Y1:  LIBRE [MURE] [MURE] [MURE]  LIBRE
Y2:  LIBRE  LIBRE [MURE]  LIBRE  LIBRE
Y3:  LIBRE [MURE] [MURE] [MURE]  LIBRE
Y4:  LIBRE  LIBRE [OUTBOX] LIBRE LIBRE
```

### Stratégie
1. Aller en (4,0) → GRAB F200
2. Lire les deux valeurs dans X et T, sauvegarder T dans M
3. TEST : si X > M → soustraire → écrire résultat
4. Sinon → écrire 0
5. Contourner par la droite → OUTBOX en (2,4)

> **Astuce** : après `TEST X > M`, le registre T est écrasé (0 ou 1). Il faut donc sauvegarder la valeur de T dans M **avant** le test.

### Code (Robot 1)
```
LINK right
LINK right
LINK right
LINK right
GRAB 200
COPY F X
COPY F T
COPY T M
TEST X > M
FJUMP ZERO
SUBI X M X
COPY X F
JUMP DONE
MARK ZERO
COPY 0 F
MARK DONE
LINK down
LINK down
LINK down
LINK down
LINK left
LINK left
DROP
HALT
```

---

## Niveau 4 — AVANCÉ · "Produit de deux fichiers"

**Objectif** : Lire F200 (= **5**) et F201 (= **9**), calculer leur produit (= **45**), déposer F201 en OUTBOX (0,4).

### Carte
```
     X0     X1     X2     X3     X4
Y0:   R    LIBRE  LIBRE  LIBRE   F200
Y1:  LIBRE [MURE]  LIBRE [MURE]  LIBRE
Y2:  LIBRE  LIBRE [MURE]  LIBRE  LIBRE
Y3:  LIBRE [MURE]  LIBRE [MURE]  LIBRE
Y4: [OUTBOX] LIBRE LIBRE LIBRE   F201
```

### Stratégie
Le robot ne peut tenir qu'un fichier à la fois. Il faut :
1. Lire la valeur de F200 dans X, puis reposer F200
2. Ramasser F201, utiliser `MULI X F X` (lit et dépile la valeur de F201 directement)
3. Écrire le résultat dans F201, naviguer jusqu'à l'OUTBOX

> **Astuce** : `MULI X F X` lit la valeur du fichier **et** calcule en une seule étape.

### Code (Robot 1)
```
LINK right
LINK right
LINK right
LINK right
GRAB 200
COPY F X
DROP
LINK down
LINK down
LINK down
LINK down
GRAB 201
MULI X F X
COPY X F
LINK left
LINK left
LINK left
LINK left
DROP
HALT
```

---

## Niveau 5 — EXPERT · "Sommation de 4 valeurs"

**Objectif** : Lire les 4 valeurs de F200 **[3, 7, 12, 8]**, les additionner (= **30**), déposer en OUTBOX (0,4).

### Carte
```
     X0     X1     X2     X3     X4
Y0:   R    LIBRE  LIBRE  LIBRE   F200
Y1: [MURE]  LIBRE [MURE]  LIBRE [MURE]
Y2:  LIBRE  LIBRE [MURE]  LIBRE  LIBRE
Y3: [MURE]  LIBRE [MURE]  LIBRE [MURE]
Y4: [OUTBOX] LIBRE LIBRE  LIBRE  LIBRE
```

### Stratégie
1. Aller en (4,0) → GRAB F200
2. Lire les 4 valeurs une à une avec `COPY F T`, les accumuler dans X avec `ADDI`
3. Écrire le résultat, naviguer jusqu'à l'OUTBOX via la colonne X=3

> Le chemin retour passe par (3,0)→(3,4) via la colonne X=3 (accessible), puis à gauche jusqu'en (0,4).

### Code (Robot 1)
```
LINK right
LINK right
LINK right
LINK right
GRAB 200
COPY F X
COPY F T
ADDI X T X
COPY F T
ADDI X T X
COPY F T
ADDI X T X
COPY X F
LINK left
LINK down
LINK down
LINK down
LINK down
LINK left
LINK left
LINK left
DROP
HALT
```

---

## Niveau 6 — MASTER · "Division entière"

**Objectif** : Lire F200 (= **72**), calculer 72 ÷ 8 (= **9**), déposer en OUTBOX (0,4).

### Carte
```
     X0     X1     X2     X3     X4
Y0:   R    LIBRE  LIBRE  LIBRE  LIBRE
Y1:  LIBRE [MURE] [MURE] [MURE]  LIBRE
Y2:  LIBRE  LIBRE  LIBRE  LIBRE  F200
Y3:  LIBRE [MURE] [MURE] [MURE]  LIBRE
Y4: [OUTBOX] LIBRE LIBRE LIBRE  LIBRE
```

### Stratégie
Le robot doit contourner les barrières horizontales.
1. Descendre par le bord gauche (X=0) jusqu'en (0,2)
2. Traverser la rangée Y=2 jusqu'en (4,2) → GRAB F200
3. Calculer 72÷8=9 avec `DIVI`
4. Descendre puis revenir à gauche jusqu'en (0,4)

> `DIVI X 8 X` divise X par le littéral 8 et stocke dans X.

### Code (Robot 1)
```
LINK down
LINK down
LINK right
LINK right
LINK right
LINK right
GRAB 200
COPY F X
DIVI X 8 X
COPY X F
LINK down
LINK down
LINK left
LINK left
LINK left
LINK left
DROP
HALT
```

---

## Niveau 7 — ULTIME · "Coordination deux robots"

**Objectif** : Robot 1 lit F200 (= **11**), l'envoie via le registre M. Robot 2 lit F201 (= **7**), reçoit M, calcule 7 + 11 = **18**, dépose F201 en OUTBOX (2,4).

### Carte
```
     X0     X1     X2     X3     X4
Y0:   R1   LIBRE  LIBRE  LIBRE   F200
Y1:  LIBRE [MURE]  LIBRE [MURE]  LIBRE
Y2:  LIBRE  LIBRE [OUTBOX] LIBRE  LIBRE
Y3:  LIBRE [MURE]  LIBRE [MURE]  LIBRE
Y4:   R2   LIBRE  LIBRE  LIBRE   F201
```

### Stratégie et synchronisation

Les deux robots s'exécutent **en alternance** (Robot 1 puis Robot 2 à chaque tick).
Robot 1 doit écrire dans M **avant** que Robot 2 ne le lise.

| Tick | Robot 1 | Robot 2 |
|------|---------|---------|
| 1–4  | LINK right ×4 → (4,0) | LINK right ×4 → (4,4) |
| 5    | GRAB 200 | GRAB 201 |
| 6    | COPY F X → X=11 | COPY F X → X=7 |
| 7    | DROP (repose F200) | **NOOP** (attente) |
| 8    | **COPY X M** → M=11 ✓ | **NOOP** (attente) |
| 9    | HALT | **COPY M T** → T=11 ✓ |
| 10   | — | ADDI X T X → X=18 |
| 11   | — | COPY X F |
| 12–13 | — | LINK left × 2 → (2,4) |
| 14   | — | DROP |
| 15   | — | HALT |

> **Clé** : les 2 `NOOP` dans Robot 2 laissent le temps à Robot 1 d'écrire M=11 au tick 8.
> Robot 2 lit M au tick 9, après que Robot 1 l'a écrit. ✓

### Code Robot 1 (éditeur du haut)
```
LINK right
LINK right
LINK right
LINK right
GRAB 200
COPY F X
DROP
COPY X M
HALT
```

### Code Robot 2 (éditeur du bas)
```
LINK right
LINK right
LINK right
LINK right
GRAB 201
COPY F X
NOOP
NOOP
COPY M T
ADDI X T X
COPY X F
LINK left
LINK left
DROP
HALT
```

---

## Résumé des solutions

| Niveau | Difficulté | Réponse | Instructions clés |
|--------|-----------|---------|-------------------|
| 1 | MOYEN | F200 → 35 à (2,4) | `ADDI X T X` |
| 2 | RÉFLÉCHIR | F200 → 24 à (4,4) | `MULI X 4 X` |
| 3 | DIFFICILE | F200 → 7 à (2,4) | `TEST`, `FJUMP`, `SUBI X M X` |
| 4 | AVANCÉ | F201 → 45 à (0,4) | `MULI X F X` |
| 5 | EXPERT | F200 → 30 à (0,4) | `ADDI` × 3 enchaînés |
| 6 | MASTER | F200 → 9 à (0,4) | `DIVI X 8 X` |
| 7 | ULTIME | F201 → 18 à (2,4) | 2 robots + `NOOP` + `COPY X M` |
