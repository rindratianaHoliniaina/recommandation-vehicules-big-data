CREATE VIEW catalogue_co2_view AS
SELECT 
    id,
    nom,
    puissance,
    CASE 
        WHEN longueur = 'très longue' THEN 3
        WHEN longueur = 'longue' THEN 2
        WHEN longueur = 'moyenne' THEN 1
        WHEN longueur = 'courte' THEN 0
        ELSE NULL
    END AS longueur,
    nbplaces,
    nbportes,
    couleur,
    CASE 
        WHEN occasion = FALSE THEN 0
        WHEN occasion = TRUE THEN 1
        ELSE NULL
    END AS occasion,
    prix,
    marque,
    bonusmalus,
    rejetco2,
    coutenergie
FROM catalogue_co2_ext;


CREATE VIEW catalogue_co2_ext_aggregated AS
SELECT
    marque,
    AVG(bonusmalus) AS bonusmalus,
    AVG(rejetco2) AS rejetco2,
    AVG(coutenergie) AS coutenergie
FROM catalogue_co2_view
GROUP BY marque;

CREATE VIEW immatriculation_view AS
SELECT 
    immatriculation,
    marque,
    nom,
    puissance,
    CASE 
        WHEN longueur = 'très longue' THEN 3
        WHEN longueur = 'longue' THEN 2
        WHEN longueur = 'moyenne' THEN 1
        WHEN longueur = 'courte' THEN 0
        ELSE NULL
    END AS longueur,
    nbplaces,
    nbportes,
    couleur,
    CASE 
        WHEN occasion = FALSE THEN 0
        WHEN occasion = TRUE THEN 1
        ELSE NULL
    END AS occasion,
    prix
FROM immatriculation_ext;

CREATE VIEW immatriculation_co2_view AS
SELECT 
    i.immatriculation,
    i.marque,
    i.nom,
    i.puissance,
    i.longueur,
    i.nbplaces,
    i.nbportes,
    i.couleur,
    i.occasion,
    i.prix,
    c.bonusmalus,
    c.rejetco2,
    c.coutenergie
FROM 
    immatriculation_view i
INNER JOIN 
    catalogue_co2_ext_aggregated c
ON 
    i.marque = c.marque;

CREATE VIEW client_view AS
SELECT 
    id,
    CASE 
        WHEN age < 18 THEN 0
        ELSE age
    END AS age,
    CASE 
        WHEN sexe IN ('M', 'Homme', 'Masculin') THEN 'M'
        WHEN sexe IN ('F', 'Femme', 'Féminin') THEN 'F'
        ELSE '*'
    END AS sexe,
    CASE 
        WHEN taux <= 0 THEN 0
        ELSE taux
    END AS taux,
    CASE 
        WHEN situationfamiliale = 'Divorcée' THEN 'DIVORCE'
        WHEN situationfamiliale = 'Marié(e)' THEN 'MARIE'
        WHEN situationfamiliale = 'En Couple' THEN 'EN_COUPLE'
        WHEN situationfamiliale IN ('Seule', 'Seul', 'Célibataire') THEN 'CELIBATAIRE'
        ELSE '*'
    END AS situationfamiliale,
    CASE 
        WHEN nbenfantsacharge <= 0 THEN 0
        ELSE nbenfantsacharge
    END AS nbenfantsacharge,
    CASE 
        WHEN deuxiemevoiture = FALSE THEN 0
        WHEN deuxiemevoiture = TRUE THEN 1
        ELSE NULL
    END AS deuxiemevoiture,
    immatriculation
FROM client_ext;

CREATE VIEW marketing_view AS
SELECT 
    id,
    CASE 
        WHEN age < 18 THEN 0
        ELSE age
    END AS age,
    CASE 
        WHEN sexe IN ('M', 'Homme', 'Masculin') THEN 'M'
        WHEN sexe IN ('F', 'Femme', 'Féminin') THEN 'F'
        ELSE '*'
    END AS sexe,
    CASE 
        WHEN taux <= 0 THEN 0
        ELSE taux
    END AS taux,
    CASE
        WHEN situationfamiliale = 'Divorcée' THEN 'DIVORCE'
        WHEN situationfamiliale = 'Marié(e)' THEN 'MARIE'
        WHEN situationfamiliale = 'En Couple' THEN 'EN_COUPLE'
        WHEN situationfamiliale IN ('Seule', 'Seul', 'Célibataire') THEN 'CELIBATAIRE'
        ELSE '*'
    END AS situationfamiliale,
    CASE 
        WHEN nbenfantsacharge <= 0 THEN 0
        ELSE nbenfantsacharge
    END AS nbenfantsacharge,
    CASE 
        WHEN deuxiemevoiture = FALSE THEN 0
        WHEN deuxiemevoiture = TRUE THEN 1
        ELSE NULL
    END AS deuxiemevoiture
FROM marketing_ext;
