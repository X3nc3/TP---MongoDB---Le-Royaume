package fr.diginamic;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("Royaume");
            System.out.println("Connexion réussie à la base de données : " + database.getName());

            // Initialisation des collections et gestionnaires

            MongoCollection<Document> ressourcesCollection = database.getCollection("Ressources");
            GestionRessources gestionRessources = new GestionRessources(ressourcesCollection);

            MongoCollection<Document> citoyensCollection = database.getCollection("Citoyens");
            GestionCitoyens gestionCitoyens = new GestionCitoyens(citoyensCollection);

            MongoCollection<Document> missionsCollection = database.getCollection("Missions");
            GestionMissions gestionMissions = new GestionMissions(gestionRessources, gestionCitoyens, missionsCollection);

            MongoCollection<Document> batimentCollection = database.getCollection("Batiments");
            GestionBatiments gestionBatiments = new GestionBatiments(batimentCollection, gestionRessources);

            // Scanner pour les entrées utilisateur
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            // Boucle du menu principal
            while (running) {
                System.out.println("\n=== Menu Principal ===");
                System.out.println("1. Ajouter des ressources");
                System.out.println("2. Afficher les ressources");
                System.out.println("3. Ajouter des citoyens");
                System.out.println("4. Afficher les citoyens");
                System.out.println("5. Préparer une mission");
                System.out.println("6. Envoyer une mission");
                System.out.println("7. Calculer les gains de mission");
                System.out.println("8. Quitter");
                System.out.print("Choisissez une option : ");
                int choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        System.out.print("Type de ressource : ");
                        String typeRessource = scanner.next();
                        System.out.print("Quantité : ");
                        int quantiteRessource = scanner.nextInt();
                        gestionRessources.ajouterRessource(typeRessource, quantiteRessource);
                        break;

                    case 2:
                        gestionRessources.afficherRessources();
                        break;

                    case 3:
                        System.out.print("Nom du citoyen : ");
                        String nomCitoyen = scanner.next();
                        System.out.print("Quantité : ");
                        int quantiteCitoyen = scanner.nextInt();
                        System.out.print("Rôle : ");
                        String roleCitoyen = scanner.next();
                        gestionCitoyens.ajouterCitoyen(nomCitoyen, quantiteCitoyen, roleCitoyen);
                        break;

                    case 4:
                        gestionCitoyens.afficherCitoyens();
                        break;

                    case 5:
                        System.out.print("Nom de la mission : ");
                        String nomMission = scanner.next();
                        System.out.print("Nombre de soldats nécessaires : ");
                        int soldatsNecessaires = scanner.nextInt();
                        System.out.print("Bois nécessaire : ");
                        int boisNecessaire = scanner.nextInt();
                        System.out.print("Nourriture nécessaire : ");
                        int nourritureNecessaire = scanner.nextInt();
                        boolean missionPrete = gestionMissions.preparerMission(nomMission, soldatsNecessaires, boisNecessaire, nourritureNecessaire);
                        if (missionPrete) {
                            System.out.println("Mission prête à être envoyée.");
                        } else {
                            System.out.println("Préparation de la mission échouée.");
                        }
                        break;

                    case 6:
                        System.out.print("Nom de la mission à envoyer : ");
                        String missionEnvoyee = scanner.next();
                        System.out.print("Nombre de soldats envoyés : ");
                        int soldatsEnvoyes = scanner.nextInt();
                        gestionMissions.envoyerEnMission(missionEnvoyee, soldatsEnvoyes);
                        break;

                    case 7:
                        System.out.print("Nom de la mission pour calculer les gains : ");
                        String missionPourGain = scanner.next();
                        System.out.print("Mission réussie ? (true/false) : ");
                        boolean missionReussie = scanner.nextBoolean();
                        System.out.print("Nombre de soldats envoyés : ");
                        int soldatsPourGain = scanner.nextInt();
                        gestionMissions.calculerGain(missionPourGain, missionReussie, soldatsPourGain);
                        break;

                    case 8:
                        running = false;
                        System.out.println("Au revoir !");
                        break;

                    default:
                        System.out.println("Option invalide. Veuillez réessayer.");
                }
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}