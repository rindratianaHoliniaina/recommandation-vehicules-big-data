# Étape 1 : MapReduce

---

> _Note : Lire attentivement le fichier `README.md` à la racine du projet_

---

2. **MapReduce avec Spark**

- Aller dans le dossier du fichier mapreduce `mapreduce.ipynb`

```bash
cd /vagrant/tpa_groupe_14/2_mapreduce/python/
```

- Lancer Jupyter dans le dossier du fichier mapreduce
```bash
jupyter lab --ip=0.0.0.0
```

- Accéder à Jupiter dans un navigateur
- Exécuter chaque cellule de `mapreduce.ipynb`.

3. **Hive table**

- Ouvrir un nouveau terminal et exécutez vagrant ssh

- Connexion à Hive
```bash
beeline -u jdbc:hive2://localhost:10000 "" ""
```

- Créez la table `catalogue_co2_ext`, une table externe depuis HDFS, résultat du processus MapReduce.

- Exécutez le code dans `sql/hive-table-create-2.sql`.

- Vérifiez que les tables sont bien créées : `catalogue`, `client_ext`, `immatriculation_ext`, `marketing_ext` et `catalogue_co2_ext`
```bash
0: jdbc:hive2://localhost:10000> show tables;
```
