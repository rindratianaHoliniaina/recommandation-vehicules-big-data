# Analyse de la Clientèle d'un Concessionnaire Automobile pour la Recommandation de Modèles de Véhicules

## Membres du projet

| Nom et prénoms                               | Numéro | Numéro étudiant |
|----------------------------------------------|--------|-----------------|
| Andriamahanintsoa Elodie                     | 01     | 22316334        |
| Rasatarivony Andriharimanga Felana Diamondra | 43     | 22316561        |
| Rasatarivony Andriamalala Sitraka            | 44     | 22316560        |
| Ratsimandavana Rindratiana Holiniaina        | 48     | 22316565        |

---

## Notice : Lire attentivement

Ce guide vous aidera à initialiser les différents composants nécessaires pour l'analyse des données de clientèle et la
recommandation de modèles de véhicules. Veuillez suivre les étapes ci-dessous pour configurer et lancer les différents
outils et services utilisés dans ce projet.

---

### 1. Initialiser le Système de Fichiers Distribué Hadoop (HDFS)

Le système HDFS est utilisé pour stocker les données massives nécessaires à notre analyse. Pour démarrer HDFS et YARN (
Yet Another Resource Negotiator), exécutez les commandes suivantes :

```bash
start-dfs.sh
start-yarn.sh
```

Ces commandes démarrent le système de fichiers distribué et le gestionnaire de ressources de Hadoop.

### 2. Démarrer les Services Hive

Hive est utilisé pour gérer et interroger notre lac de données. Pour démarrer les services Hive, exécutez les commandes
suivantes :

```bash
nohup hive --service metastore > /dev/null &
nohup hiveserver2 > /dev/null &
```

- `nohup hive --service metastore > /dev/null &` : Démarre le service Metastore de Hive en arrière-plan.
- `nohup hiveserver2 > /dev/null &` : Démarre le serveur HiveServer2 en arrière-plan, permettant l'interaction avec les
  clients Hive.

### 3. Lancer le KVStore

KVStore est utilisé pour le stockage de clé-valeur rapide et sécurisé. Pour démarrer le service KVStore, exécutez la
commande suivante :

```bash
nohup java -Xmx64m -Xms64m -jar $KVHOME/lib/kvstore.jar kvlite -secure-config disable -root $KVROOT &
```

Cette commande démarre le service KVStore avec une configuration de sécurité désactivée et définit le répertoire racine
du KVStore.

### Optionnel : Tester la Connexion au KVStore

Pour tester la connexion au KVStore et vérifier que tout fonctionne correctement, utilisez les commandes suivantes :

```bash
java -jar $KVHOME/lib/kvstore.jar runadmin -port 5000 -host localhost
```

Puis, dans l'interface de commande du KVStore :

```bash
kv-> connect store -name kvstore
kv-> exit
```

- `connect store -name kvstore` : Connecte le client au magasin de données nommé `kvstore`.
- `exit` : Quitte l'interface de commande du KVStore.

---

### Structure du Projet

1. **data** : Dossier contenant les sources de données (CSV)
2. **1_data_lake** : Dossier contenant les scripts et programmes de construction du lac de données.
3. **2_map_reduce** : Dossier contenant les scripts et programmes Hadoop Map Reduce.
4. **3_data_analysis** : Dossier contenant les scripts et programmes d’analyse de données.

---

### Remarques Supplémentaires

- Assurez-vous que tous les services démarrés continuent de fonctionner en arrière-plan avant de lancer les analyses.
- Les résultats des analyses seront stockés dans la base de résultats spécifiée dans le projet.

---

### Contacts

Pour toute question ou assistance, veuillez contacter les responsables du projet.

---

&copy; ITUniversity 2024
