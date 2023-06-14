# Apanyan Movies & Series 

Nous avons essayé de déployer l'application, mais nous avons rencontré beaucoup de problèmes avec les dépendances, les fichiers manifest, les cycles de vie des servlets pour le serveur, et nous n'avons donc pas réussi à le déployer.

# Installation Serveur 

## Fichiers jar a ajouter : 
    java-json.jar 
    mongo-java-driver-3.12.13.jar 

Ces deux fichiers se trouvent dans le dossier JarExterne.


### Sur eclipse : 

Il faut ouvrir le dossier serveur dans Eclipse en tant qu'application web et le nommer "pc3r-projet". Il faut modifier le build path.

    Clique-droit sur le projet : 
        Build Path -> Configure Build Path 
        Java Build Path -> Librairies -> ClassPath -> Add External Jars 

Ajoutez les deux JAR du dossier JarExterne à la racine du projet.


Il faut également modifier le déploiement de l'assemblage.

    Build Path -> Configure Build Path 
    Deployment Assembly -> Add .. -> 
    Archives from File System 

Ajoutez les deux JAR du dossier JarExterne à la racine du projet.

Le chemin de déploiement est normalement le suivant :

    WEB-INF/lib/mongo-java-driver-3.12.13.jar 
    WEB-INF/lib/java-json.jar 

À partir de là, le serveur peut être exécuté sur Eclipse.
Nous avons utilisé Tomcat version 10.0.23.

Le serveur est sur le port 8080. 

# Installation Client 

    1. cd client
    2. npm install 
    3. npm install --save react-router-dom react-bootstrap
    4. npm run start 


# BDD 

Nous avons utilisé MongoDB pour la base de données. Nous avons attribué des noms aux collections ainsi qu'aux fichiers JSON correspondants à transférer sur MongoDB.

Le nom de la base de données est "Movie". Les noms des collections correspondent à leurs noms de dossier respectifs.
    