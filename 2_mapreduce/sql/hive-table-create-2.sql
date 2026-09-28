-- HDFS
-- Resultat MapReduce
CREATE EXTERNAL TABLE catalogue_co2_ext (
    id string,
    nom string,
    puissance int,
    longueur string,
    nbplaces int,
    nbportes int,
    couleur string,
    occasion boolean,
    prix int,
    marque string, 
    bonusmalus float,
    rejetco2 int, 
    coutenergie float
)
 ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
 STORED AS TEXTFILE LOCATION 'hdfs:/tpa_groupe_14/mapreduce';