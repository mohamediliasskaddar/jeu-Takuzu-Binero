# Binairo / Takuzu – Mini Projet IA
## 1. Description du projet

Binairo, également connu sous le nom de Takuzu, est un jeu de réflexion basé sur une grille binaire. L’objectif est de remplir une grille carrée avec des 0 et des 1 en respectant les règles suivantes :

Chaque ligne et chaque colonne contient un nombre égal de 0 et de 1.

Pas plus de deux mêmes chiffres consécutifs sur une ligne ou une colonne.

Chaque ligne et chaque colonne est unique.

Cette application permet à l’utilisateur de jouer manuellement, de générer des grilles aléatoires et de résoudre automatiquement les grilles à l’aide de différentes heuristiques et techniques de résolution.

## 2. Fonctionnalités
###  2.1 Création et génération de grilles

- Création manuelle : l’utilisateur peut remplir chaque case directement.

- Grilles générées automatiquement avec différents niveaux :

- Débutant (6×6)

- Intermédiaire (8×8)

- Expert (10×10)

- Possibilité de créer une grille totalement vide.

### 2.2 Résolution et vérification

- Vérification de la validité et de la résolubilité de la grille initiale.

- Détection des violations de règles lors du jeu manuel.

- Suggestions automatiques pour aider l’utilisateur.

- Résolution automatique via plusieurs méthodes :

- Backtracking classique

- Optimisé : LCV (Least Constraining Value), Forward Checking (FC), AC-3 (Arc Consistency)

- Comparaison du temps de résolution pour chaque méthode.

### 2.3 Sauvegarde et reprise

- Sauvegarde des grilles dans un fichier local.

- Reprise de grilles existantes depuis un fichier.

- Indication claire de la structure du fichier requis (première ligne = taille de la grille, lignes suivantes = 0 et 1, séparés par espaces).

### 2.4 Interface utilisateur

- Grille affichée sous forme de boutons cliquables.

- Cycle rapide des valeurs d’une case : clic répété → 0 → 1 → vide.

- Messages informatifs et alertes lorsque des règles sont violées ou que la grille est irrésoluble.

- Barre de navigation stylisée avec boutons pour sauvegarder, charger, créer une grille, accéder aux règles, etc.

- Dialogue d’information à l’ouverture de l’application pour guider l’utilisateur.

## 3. Architecture du projet
```
   src/
   ├── controller/       # Gestion des interactions utilisateur
   ├── model/            # Représentation de la grille et logique de jeu
   ├── Solver/           # Algorithmes de résolution
   │   ├─ BacktrackingSolver.java
   │   ├─ AC3.java
   │   ├─ LCVHeuristic.java
   │   ├─ Position.java
   │   └─ SolverUtils.java
   ├── utils/            # Fonctions utilitaires (lecture/écriture de fichiers, vérifications)
   └── view/             # Interface graphique Swing
   └─ BinairoApp.java
```
## 4. Heuristiques et améliorations implémentées

- MVR (Most Constrained Variable) : choisir la variable avec le moins de valeurs possibles.

- Degree heuristic : choisir la variable qui contraint le plus les autres.

- LCV (Least Constraining Value) : essayer d’abord la valeur qui réduit le moins les options des voisins.

- Forward Checking (FC) : vérifier immédiatement les conflits après chaque assignation.

- AC-3 : propagation des contraintes pour réduire le domaine des variables.

## 5. Structure des fichiers de grille

Première ligne : taille de la grille (ex : 6 pour une 6×6)

Lignes suivantes : 0 ou 1 séparés par un espace pour chaque case.
````
Exemple 6×6 :

6
0 1 0 1 0 1
1 0 1 0 1 0
0 1 0 1 0 1
1 0 1 0 1 0
0 1 0 1 0 1
1 0 1 0 1 0

note: Cases vides peuvent être représentées par -1 .
````




## 6. Installation et exécution

1. Cloner le projet ou télécharger le ZIP.

2. Ouvrir le projet dans un IDE Java (IntelliJ préférable).

3. Compiler le projet et exécuter la classe view. BinairoApp.

4. À l’ouverture, suivre les instructions pour créer ou charger une grille.