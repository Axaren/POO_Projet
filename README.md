# POO_Projet

README
DAYRE Florian, LENSE Nathan

Mode d’emploi :

- Compilation : javac Game.java
- Exécution : java Game

Règles du jeu :

Le jeu se joue à deux joueurs (une personne contre une IA). Chaque joueur possède une planète de départ, avec une puissance 
donnée, depuis laquelle ils peuvent créer des vaisseaux, qui ont aussi une certaine puissance donnée. Le but du jeu est de 
contrôler toutes les planètes présentes dans le jeu. Pour cela il faut envoyer des vaisseaux sur les différentes planètes pour 
les conquérir. Lorsque que la puissance d’une planète arrive à 0, elle est conquise par le joueur ayant envoyé les vaisseaux. 
Il y a également des planètes neutres, qui ne produisent pas de vaisseaux mais qui peuvent être conquises. 
Les planètes contrôlées par les joueurs produisent constamment des vaisseaux et un joueur peut envoyer les vaisseaux présents 
dans le stock de la planète en faisant un glisser déposer de la planète source vers la planète à attaquer et en choisissant 
le nombre de vaisseau à envoyer avec un pourcentage.

Fonctionnalités implémentées :

- Quand on lance le jeux, les planètes sont générées procéduralement et une planète est attribuée aux joueurs et est de la couleur du joueur correspondant.
- Les planètes contrôlées par les joueurs produisent des vaisseaux au cours du temps.
