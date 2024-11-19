package fr.diginamic;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class GestionMissions {
    private GestionRessources gestionRessources;
    private GestionCitoyens gestionCitoyens;
    private MongoCollection<Document> missions;

    public GestionMissions(GestionRessources gestionRessources, GestionCitoyens gestionCitoyens, MongoCollection<Document> missions) {
        this.gestionRessources = gestionRessources;
        this.gestionCitoyens = gestionCitoyens;
        this.missions = missions;
    }

    //Méthode pour préparer une mission
    public boolean preparerMission(String nomMissions, int soldatsNecessaires, int boisNecessaires, int nourritureNecessaire) {
        System.out.println("Préparation de la mission : " + nomMissions);

        //Vérifier les ressources
        boolean boisSuffisant = gestionRessources.verifierRessource("Bois", boisNecessaires);
        boolean nourritureSuffisante = gestionRessources.verifierRessource("Nourriture", nourritureNecessaire);

        if (boisSuffisant && nourritureSuffisante) {
            //Vérifier le nombre de soldats disponibles
            boolean soldatDisponibles = gestionCitoyens.verifierCitoyens("défenseur", soldatsNecessaires);

            if (soldatDisponibles) {
                //Déduire les ressources
                gestionRessources.mettreAJourRessource("Bois", - boisNecessaires);
                gestionRessources.mettreAJourRessource("Nourriture", - nourritureNecessaire);

                //Recruter les soldats
                gestionCitoyens.mettreAJourCitoyen("Soldat", - soldatsNecessaires);

                System.out.println("Mission " + nomMissions + " prete avec " + soldatsNecessaires + " soldats.");
                return true;
            } else {
                System.out.println("Nombre insuffisant de soldats pour la mission.");
                return false;
            }
        } else {
            System.out.println("Ressources insuffisantes pour préparer la mission.");
            return false;
        }
    }

    //Méthode pour envoyer la mission (apres préparation réussie)
    public void envoyerEnMission(String nomMission, int soldatsNecessaires) {
        System.out.println("Envoi de la mission : " + nomMission);

        //Enregistrer la mission dans la collection des missions
        Document mission = new Document("nom", nomMission).append("soldatsEnvoyes", soldatsNecessaires).append("status", "En cours");
        missions.insertOne(mission);

        System.out.println("La mission " + nomMission + " a été envoyée avec " + soldatsNecessaires + " soldats.");
    }

    //Méthode pour calculer le gain en fonction de la mission
    public void calculerGain(String nomMission, boolean missionReussie, int soldatsEnvoyes) {
        if (missionReussie) {
            //Calcul des gains en fonction des soldats envoyés (exemple)
            int gainBois = soldatsEnvoyes * 20;
            int gainNourriture = soldatsEnvoyes * 10;

            //Mise à jour des ressources
            gestionRessources.mettreAJourRessource("Bois", gainBois);
            gestionRessources.mettreAJourRessource("Nourriture", gainNourriture);

            System.out.println("Mission réussie ! Gain : " + gainBois + " Bois, " + gainNourriture + " Nourriture.");
        } else {
            System.out.println("Mission échouée. Ancun gain obtenu.");
        }

        //Mettre à jour le statut de la mission en fonction du résultat
        missions.updateOne(new Document("nom", nomMission), new Document("$set", new Document("status", missionReussie ? "Réussie" : "Échouée")));
    }
}
