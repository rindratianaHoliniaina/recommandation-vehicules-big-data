-- HDFS
-- Immatriculation
CREATE TABLE IF NOT EXISTS immatriculation_ext (
    immatriculation string,
    marque string,
    nom string,
    puissance int,
    longueur string,
    nbplaces int,
    nbportes int,
    couleur string,
    occasion boolean,
    prix int
)
 ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
 STORED AS TEXTFILE LOCATION 'hdfs:/tpa_groupe_14/data/immatriculation'
 TBLPROPERTIES ("skip.header.line.count"="1");

-- MongoDb
-- Catalogue
CREATE TABLE IF NOT EXISTS catalogue (
    id string,
    marque string,
    nom string,
    puissance int,
    longueur string,
    nbplaces int,
    nbportes int,
    couleur string,
    occasion boolean,
    prix int
)
COMMENT 'Catalogue details'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE;

-- Oracle NoSql
-- Client
CREATE EXTERNAL TABLE client_ext (
    id int,
    age int,
    sexe string,
    taux int,
    situationfamiliale string,
    nbenfantsacharge int,
    deuxiemevoiture boolean,
    immatriculation string
)
STORED BY 'oracle.kv.hadoop.hive.table.TableStorageHandler' 
TBLPROPERTIES (
"oracle.kv.kvstore" = "kvstore", 
"oracle.kv.hosts" =  "localhost:5000", 
"oracle.kv.hadoop.hosts" = "localhost/127.0.0.1", 
"oracle.kv.tableName" = "client");

-- Marketing
CREATE EXTERNAL TABLE marketing_ext (
    id int,
    age int,
    sexe string,
    taux int,
    situationfamiliale string,
    nbenfantsacharge int,
    deuxiemevoiture boolean
)
STORED BY 'oracle.kv.hadoop.hive.table.TableStorageHandler' 
TBLPROPERTIES (
"oracle.kv.kvstore" = "kvstore", 
"oracle.kv.hosts" =  "localhost:5000", 
"oracle.kv.hadoop.hosts" = "localhost/127.0.0.1", 
"oracle.kv.tableName" = "marketing");

