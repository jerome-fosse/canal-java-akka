# Test Canal en Java

## Enoncé de l'exercice
Le test doit être écrit en scala/java en s’appuyant sur la bibliothèque Akka Stream.<p>

Les données à traiter sont réparties en cinq fichiers TSV (tab separated values) à télécharger sur IMDB via les liens ci-dessous.
- https://datasets.imdbws.com/name.basics.tsv.gz
- https://datasets.imdbws.com/title.episode.tsv.gz
- https://datasets.imdbws.com/title.ratings.tsv.gz
- https://datasets.imdbws.com/title.principals.tsv.gz
- https://datasets.imdbws.com/title.basics.tsv.gz

La structure de données utilisée dans les fichiers est décrite dans le lien ci-dessous :
- https://www.imdb.com/interfaces/

Le besoin formulé par le product owner est le suivant :
- En tant qu'utilisateur je souhaite pouvoir saisir le nom d'un titre et retourner  
  l'ensemble des membres de l’équipe de tournage.
- En tant qu'utilisateur je souhaite pouvoir retourner le titre des 10 séries avec le plus  
  grand nombre d'épisodes.
  
## Stack Technique

- Java 15 minimum (Java 14 en activant les preview features)
- Akka Stream et Akka Http
- Postgresql

## Creation de la base de données et import des fichiers

Création de la base de données :
```aidl
docker-compose up -d
```

Demarrage de la base de données :
```aidl
docker-compose start
```

Import des fichiers :
```aidl
./mvnw clean install exec:exec@import
```
Entre le téléchargement des fichiers et l'import, il faut compter un peu moins de 15 minutes.

## Démarrer l'application

Une fois l'import terminé, pour démarrer le serveur :
```aidl
./mvnw exec:exec@server
```

Le serveur écoute sur le port 8080.

## Utiliser les services
### Afficher les membres de l'équipe de tournage d'un film
```aidl
curl -X POST http://localhost:8080/titles/principals/search -H "Content-Type: application/json" -d "{\"titleName\":\"The Last of the Mohicans\"}" | jq
```

### Afficher la liste des 10 série avec le plus d'épisodes
```aidl
curl http://localhost:8080/tvseries/topten | jq
```
