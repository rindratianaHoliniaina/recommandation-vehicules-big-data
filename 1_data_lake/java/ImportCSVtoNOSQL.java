import oracle.kv.KVStore;
import java.util.List;

import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.FaultException;
import oracle.kv.StatementResult;
import oracle.kv.table.TableAPI;
import oracle.kv.table.Table;
import oracle.kv.table.Row;
import oracle.kv.table.PrimaryKey;
import oracle.kv.ConsistencyException;
import oracle.kv.RequestTimeoutException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Integer;
import oracle.kv.table.TableIterator;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ImportCSVtoNOSQL {

    /**
     * @param args
     */
    private final KVStore store;

    public static void main(String[] args) {

        System.out.println("***************Arg Legnth : " + args.length);

        if (args.length == 0) {
            System.out.println("Add file path and the type in argument");
        }

        for (String string : args) {
            System.out.println(string);
        }

        String FILE_PATH = args[0];

        ImportCSVtoNOSQL importer = new ImportCSVtoNOSQL(args);

        Object object = null;

        if (args[1].equals("marketing")) {
            object = new Marketing();
        } else if (args[1].equals("client")) {
            object = new Client();
        }

        importer.createTable(object);

        importer.insertRow(object, FILE_PATH, importer.store);

    }

    ImportCSVtoNOSQL(String[] argv) {
        String storeName = "kvstore";
        String hostName = "localhost";
        String hostPort = "5000";
        store = KVStoreFactory.getStore(new KVStoreConfig(storeName, hostName + ":" + hostPort));

    }

    /*
     * Creation of a query to create the table
     */
    public String getStatementString(Object object) {
        List<String> attributsEtTypes = new ArrayList<>();
        Class<?> classe = object.getClass(); // Obtient la classe de l'objet
        Field[] fields = classe.getDeclaredFields();
        String className = classe.getName();

        String statement = "create table " + className + " ( ";

        for (Field field : fields) {
            String nomAttribut = field.getName();
            String typeAttribut = field.getType().getSimpleName();
            if (typeAttribut.equals("int")) {
                typeAttribut = "INTEGER";
            }
            statement = statement + nomAttribut + " " + typeAttribut + " , ";
            attributsEtTypes.add(nomAttribut + " : " + typeAttribut);
        }

        statement = statement + "PRIMARY KEY (" + fields[0].getName() + "))";
        System.out.println("============================================");
        System.out.println(statement);
        System.out.println("============================================");

        return statement;
    }

    /*
     * function that creates a table corresponding to a class
     */
    public void createTable(Object object) {

        StatementResult result = null;
        // String statement = null;
        System.out.println("****** Dans : create table ********");
        try {

            /*
             * Add table to the database.
             * Executethisstatementasynchronously.
             */

            String statement = this.getStatementString(object);

            // result = store.executeSync(statement);
            result = store.executeSync(statement);
            displayResult(result, statement);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalidstatement:\n" +
                    e.getMessage());
        } catch (FaultException e) {
            System.out.println("Statement couldn't be executed, pleaseretry: " + e);
            e.printStackTrace();
        }

    }

    private void displayResult(StatementResult result, String statement) {

        System.out.println("===========================");

        if (result.isSuccessful()) {

            System.out.println("Statement was successful:\n\t" + statement);
            System.out.println("Results:\n\t" + result.getInfo());

        } else if (result.isCancelled()) {

            System.out.println("Statement was cancelled:\n\t" + statement);

        } else {

            /**
             * statementwasnot successful: maybein error, or maystill
             * bein progress.
             */
            if (result.isDone()) {
                System.out.println("Statementfailed:\n\t" + statement);
                System.out.println("Problem:\n\t"
                        + result.getErrorMessage());
            } else {
                System.out.println("Statementin progress:\n\t" + statement);
                System.out.println("Status:\n\t" + result.getInfo());
            }

        }
    }

    public static List<String> getListeAttributs(Object object) {

        List<String> attributs = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            attributs.add(field.getName());
        }

        return attributs;

    }

    public static List<String> getListeTypesAttributs(Object object) {

        List<String> typesAttributs = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            typesAttributs.add(field.getType().getSimpleName());
        }

        return typesAttributs;

    }

    /**
     * insertAPiloteRow : Insère une nouvelle ligne dans la table pilote
     */
    private void insertRow(Object object, String csvFilePath, KVStore kvstore) {

        String line;
        String cvsSplitBy = ",";
        TableAPI tableAPI = kvstore.getTableAPI();
        List<String> listeAttributs = getListeAttributs(object);
        List<String> listeTupeAttributs = getListeTypesAttributs(object);
        int nblisteAttributs = listeAttributs.size() - 1;
        int id = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            br.readLine(); // Ignorer la première ligne

            while ((line = br.readLine()) != null) {

                System.out.println("nb attribut " + nblisteAttributs);
                String[] data = line.split(cvsSplitBy);
                Row row = tableAPI.getTable(object.getClass().getName()).createRow();

                row.put("id", id++);

                for (int i = 0; i < nblisteAttributs; i++) {

                    int u = i + 1;
                    String typeAttibut = listeTupeAttributs.get(u);
                    System.out.println("N° " + u + " " + listeAttributs.get(u));

                    try {

                        if (typeAttibut.equalsIgnoreCase("int")) {
                            row.put(listeAttributs.get(u), Integer.parseInt(data[i].trim()));
                        } else if (typeAttibut.equalsIgnoreCase("boolean")) {
                            row.put(listeAttributs.get(u), Boolean.parseBoolean(data[i].trim()));
                        } else {
                            row.put(listeAttributs.get(u), data[i].trim());
                        }

                    } catch (NumberFormatException e) {

                        System.err.println("Erreur de conversion pour l'attribut " + listeAttributs.get(i) + ": "
                                + e.getMessage());

                    }

                }

                tableAPI.put(row, null, null);
                System.out.println("Enregistrement inséré avec succès : " + line);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class Marketing {

    // age sexe taux situationFamiliale nbEnfantsAcharge deuxiemevoiture
    // 21 F 1396 Célibataire 0 false
    int id;
    int age;
    String sexe;
    int taux;
    String situationFamiliale;
    int nbEnfantsAcharge;
    boolean deuxiemevoiture;

    public Marketing() {

    }

    public Marketing(int age, String sexe, int taux, String situationFamiliale, int nbEnfantsAcharge,
            boolean deuxiemevoiture) {
        this.age = age;
        this.sexe = sexe;
        this.taux = taux;
        this.situationFamiliale = situationFamiliale;
        this.nbEnfantsAcharge = nbEnfantsAcharge;
        this.deuxiemevoiture = deuxiemevoiture;
    }

}

class Client {

    int id;
    int age;
    String sexe;
    int taux;
    String situationFamiliale;
    int nbEnfantsAcharge;
    boolean deuxiemevoiture;
    String immatriculation;

    public Client(int age, String sexe, int taux, String situationFamiliale, int nbEnfantsAcharge,
                  boolean deuxiemevoiture, String immatriculation) {
        this.age = age;
        this.sexe = sexe;
        this.taux = taux;
        this.situationFamiliale = situationFamiliale;
        this.nbEnfantsAcharge = nbEnfantsAcharge;
        this.deuxiemevoiture = deuxiemevoiture;
        this.immatriculation = immatriculation;
    }

    public Client() {

    }

}
