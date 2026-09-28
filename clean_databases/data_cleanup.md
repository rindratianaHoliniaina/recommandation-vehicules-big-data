# Suppression des données existantes

> _Note: veuillez lire attentivement_

- Supprimer les fichiers stockés dans HDFS

```bash
hadoop fs -rm -r /tpa_groupe_14
```

- Supprimer immatriculations et catalogues de MongoDB

```bash
# Accès à mongoshell
mongo

> use sourceCSV
> db.catalogue.drop()

# Quitter mongoshell
> exit
```

- Supprimer les tables de NoSQL

```bash
# Connexion à KVStore
java -jar $KVHOME/lib/kvstore.jar runadmin -port 5000 -host localhost

kv-> connect store -name kvstore
kv-> execute'drop table marketing'
kv-> execute'drop table client'

# Quitter KVStore
kv-> exit
```

- Supprimer les tables internes ou externes et les vues de Hive

```bash
beeline -u jdbc:hive2://localhost:10000 "" ""

0: jdbc:hive2://localhost:10000> drop table if exists catalogue;
0: jdbc:hive2://localhost:10000> drop table if exists catalogue_co2_ext;
0: jdbc:hive2://localhost:10000> drop table if exists immatriculation_ext;
0: jdbc:hive2://localhost:10000> drop table if exists client_ext;
0: jdbc:hive2://localhost:10000> drop table if exists marketing_ext;

0: jdbc:hive2://localhost:10000> drop view if exists catalogue_co2_view;
0: jdbc:hive2://localhost:10000> drop view if exists catalogue_co2_ext_aggregated;
0: jdbc:hive2://localhost:10000> drop view if exists immatriculation_view;
0: jdbc:hive2://localhost:10000> drop view if exists immatriculation_co2_view;
0: jdbc:hive2://localhost:10000> drop view if exists client_view;
0: jdbc:hive2://localhost:10000> drop view if exists marketing_view;

```

