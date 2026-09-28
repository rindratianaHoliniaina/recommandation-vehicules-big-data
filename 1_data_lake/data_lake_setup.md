# Étape 1 : Création du lac de données

___

> _Note : Lire attentivement le fichier `README.md` à la racine du projet_

___

## A. Chargement des données

Définir une variable qui contenant le chemin vers les sources de données `csv`
```bash
DS_PATH="/vagrant/tpa_groupe_14/data"
```

### I. HDFS (Hadoop)

Téléverser les fichiers `CO2.csv` et `Immatriculations.csv` vers Hadoop HDFS

> Prérequis : Hadoop installé et configuré

#### Mettre le fichier CO2.csv dans Hadoop HDFS

- Définition des chemins des fichiers
```bash
CO2_FILE_PATH="$DS_PATH/CO2.csv"
IMMATRICULATIONS_FILE_PATH="$DS_PATH/Immatriculations.csv"
```

- Définition du jeu de caractères de `Immatriculations.csv` en UTF-8.
```bash
IMMATRICULATIONS_FILE_PATH_NEW="$DS_PATH/Immatriculations_UTF8.csv"

iconv -f ISO-8859-1 -t UTF-8 $IMMATRICULATIONS_FILE_PATH -o $IMMATRICULATIONS_FILE_PATH_NEW
```

- Création des dossiers respectifs aux 2 fichiers dans Hadoop dfs.
```bash
hadoop fs -mkdir -p /tpa_groupe_14/data/co2
hadoop fs -mkdir -p /tpa_groupe_14/data/immatriculation
```

- Copie des fichiers dans Hadoop HDFS.
```bash
hadoop fs -put -f $CO2_FILE_PATH /tpa_groupe_14/data/co2
hadoop fs -put -f $IMMATRICULATIONS_FILE_PATH_NEW /tpa_groupe_14/data/immatriculation
```

### II. MongoDB

#### Importer les données du fichier `Catalogue.csv` dans MongoDB.

- Définition du jeu de caractères de `Catalogue.csv` CSV en UTF-8 et modification des caractères `ï` en `i`.
```bash
CATALOGUES_FILE_PATH="$DS_PATH/Catalogue.csv"
CATALOGUES_FILE_PATH_NEW="$DS_PATH/Catalogue_UTF8.csv"

iconv -f ISO-8859-1 -t UTF-8 $CATALOGUES_FILE_PATH -o $CATALOGUES_FILE_PATH_NEW

sed -i 's/ï/i/g' $CATALOGUES_FILE_PATH_NEW
```

- Import des données de `Catalogue.csv` dans la collection `catalogues`.
```bash
mongoimport --db sourceCSV --collection catalogue --type csv --file $CATALOGUES_FILE_PATH_NEW --headerline;
```

### III. Oracle NoSQL

#### Importer les données des fichiers `Marketing.csv`, `Clients_4.csv` et `Clients_13.csv` dans KVStore.

1. **Néttoyage des données**
- Exporter la variable contenant le chemin du programme JAVA à éxecuter :
```bash
export TPT_HOME=/vagrant/tpa_groupe_14/1_data_lake/java
```

- Compiler ImportCSVtoNOSQL.java.
```bash
javac -g -cp $KVHOME/lib/kvclient.jar:$TPT_HOME $TPT_HOME/ImportCSVtoNOSQL.java
```

- Définition du jeu de caractères des fichiers CSV en UTF-8.
```bash
MARKETING_FILE="$DS_PATH/Marketing.csv"
CLIENTS_4_FILE="$DS_PATH/Clients_4.csv"
CLIENTS_13_FILE="$DS_PATH/Clients_13.csv"

MARKETING_FILE_NEW="$DS_PATH/Marketing_UTF8.csv"
CLIENTS_4_FILE_NEW="$DS_PATH/Clients_4_UTF8.csv"
CLIENTS_13_FILE_NEW="$DS_PATH/Clients_13_UTF8.csv"

iconv -f ISO-8859-1 -t UTF-8 $MARKETING_FILE -o $MARKETING_FILE_NEW
iconv -f ISO-8859-1 -t UTF-8 $CLIENTS_4_FILE -o $CLIENTS_4_FILE_NEW
iconv -f ISO-8859-1 -t UTF-8 $CLIENTS_13_FILE -o $CLIENTS_13_FILE_NEW
```

2. **Import des données dans Hive**

- Marketing.csv, avec le type "marketing".
```bash
java -Xmx256m -Xms256m -cp $KVHOME/lib/kvclient.jar:$TPT_HOME ImportCSVtoNOSQL $MARKETING_FILE_NEW marketing
```

- Clients_4.csv, avec le type "client".
```bash
java -Xmx256m -Xms256m -cp $KVHOME/lib/kvclient.jar:$TPT_HOME ImportCSVtoNOSQL $CLIENTS_4_FILE_NEW client
```

- Clients_13.csv, avec le type "client".
```bash
java -Xmx256m -Xms256m -cp $KVHOME/lib/kvclient.jar:$TPT_HOME ImportCSVtoNOSQL $CLIENTS_13_FILE_NEW client
```

- Vérifiez que les tables sont bien créé
```bash
java -jar $KVHOME/lib/kvstore.jar runadmin -port 5000 -host localhost

kv-> connect store -name kvstore
kv-> execute "show tables"
```

## B. Création du lac de données dans HIVE

1. **Mise en place des tables dans la base de données par défaut de Hive**

- Connexion à Hive
```bash
beeline -u jdbc:hive2://localhost:10000 "" ""
```

- Créer les tables :
    - Table locale : `catalogue`
    - Table externe depuis Oracle NoSQL : `immatriculation_ext`,`clients_ext`, `marketing_ext`

- Exécuter les commandes dans `sql/hive-table-create-1.sql`.

- Vérifier l'existence des tables nouvellement créées : `catalogue`, `client_ext`, `immatriculation_ext`, `marketing_ext`
```bash
0: jdbc:hive2://localhost:10000> show tables;
```

2. **Exécuter le processus ETL.**
- Ouvrir un nouveau terminal et exécuter `vagrant ssh`

- Aller au dossier contenant le script `dataLoader.py` (ETL)
```bash
cd /vagrant/tpa_groupe_14/1_data_lake/python/
```

- Installer les dépendances requises
```bash
pip install pymongo==3.12.0
```

- Lancez le programme python
```bash
python3 dataLoader.py
```
