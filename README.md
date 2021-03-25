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
