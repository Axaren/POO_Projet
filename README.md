Auteurs : DAYRE Florian, LESNE Nathan

Mode d’emploi : (si javafx est dans le path)
Compilation : javac game_basic.Game.java
Exécution : java game_basic.Game

Règles du jeu : 

Le jeu se joue à deux joueurs (une personne contre une IA mais on peut avoir plus de joueurs). Chaque joueur possède une planète de départ, avec une puissance donnée, depuis laquelle ils peuvent créer des vaisseaux, qui ont aussi une certaine puissance donnée. Le but du jeu est de contrôler toutes les planètes présentes dans le jeu. Pour cela il faut envoyer des vaisseaux sur les différentes planètes pour les conquérir. Lorsque que la puissance d’une planète arrive à 0, elle est conquise par le joueur ayant envoyé les vaisseaux. Il y a également des planètes neutres, qui ne produisent pas de vaisseaux mais qui peuvent être conquises. Les planètes contrôlées par les joueurs produisent constamment des vaisseaux et un joueur peut envoyer les vaisseaux présents dans le stock de la planète en faisant un glisser déposer de la planète source vers la planète à attaquer et en choisissant le nombre de vaisseau à envoyer avec un pourcentage.

Fonctionnalités implémentées : 

( Quand on lance le jeu, les planètes sont générées procéduralement, la première planète de la liste est allouée au joueur, la seconde a un joueur IA, l’IA n’est pas implémentée.
- Les planètes contrôlées par les joueurs produisent des vaisseaux au cours du temps.
- Les vaisseaux peuvent se déplacer sur la carte sans toucher les planètes grâce a un algorithme de Pathfinding.
L’algorithme implémenté est Theta* (L’implémentation est adaptée de ce dépot https://github.com/BlueWalker/Pathfinding/tree/master/src/main/java/walker/blue/path/lib)
- Plus d’informations sur l’algorithme sont disponibles ici : http://aigamedev.com/open/tutorial/theta-star-any-angle-paths/
- Présence d'un mode debug, affichant la grille contenant la carte sous forme de graphe et les événements de la souris et du clavier. Activation/désactivation à l'aide de la touche T.
- Sauvegarde (W) et Chargement (X) du jeu.


Améliorations/Fonctionalités manquantes:

Le décollage des vaisseaux n'est pas optimal/correct, des bugs persistent sur le game_basic.pathfinding (le vaisseau dévie du chemin).
Aucune IA n'est présente sur le jeu.
Plusieurs types de vaisseaux/planètes.