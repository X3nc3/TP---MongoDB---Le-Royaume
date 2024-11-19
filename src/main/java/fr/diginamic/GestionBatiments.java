package fr.diginamic;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class GestionBatiments {
    private MongoCollection<Document> batiments;
    private GestionRessources gestionRessources;

    public GestionBatiments(MongoCollection<Document> batiments, GestionRessources gestionRessources) {
        this.batiments = batiments;
        this.gestionRessources = gestionRessources;
    }

    //Méthode pour afficher les batiments
    public void afficherBatiment() {
        for (Document batiment : batiments.find()) {
            System.out.println(batiment.toJson());
        }
    }

    //Méthode pour supprimer un batiment
    public void supprimerBatiment(String type) {
        batiments.deleteOne(new Document("type", type));
        System.out.println("Batiment supprimé : " + type);
    }

    //Méthode pour ajouter un batiment sans condition
    public void ajouterBatiment(String type, int niveau, String fonction) {
        Document batiment = new Document("type", type).append("niveau", niveau).append("fonction", fonction);
        batiments.insertOne(batiment);
        System.out.println("Batiment ajouté : " + type);
    }

    //Méthode pour construire un batiment avec vérification des ressources
    public void construireBatiment(String type, int coutBois, int coutPierre, String fonction) {
        if (gestionRessources.verifierRessource("Bois", coutBois) && gestionRessources.verifierRessource("Pierre", coutPierre)) {
            //Déduire les ressources pour la construction
            gestionRessources.mettreAJourRessource("Bois", - coutBois);
            gestionRessources.mettreAJourRessource("Pierre", - coutPierre);

            //ajouter le batiment
            ajouterBatiment(type, 1, fonction);
            System.out.println("Batiment constuit : " + type);
        } else {
            System.out.println("Ressource insuffisantes pour construire le batiment.");
        }
    }

    //Méthode pour mettre à jour le niveau d'un batiment
    public void mettreAJourBatiment(String type, int nouveauNiveau) {
        batiments.updateOne(new Document("type", type), new Document("$set", new Document("niveau", nouveauNiveau)));
        System.out.println("Niveau mis à jour pour : " + type);
    }

    //Méthode pour améliorer le niveau d'unbatiment
    public void ameliorerBatiment(String type) {
        //Récuper le batiment à partir de son type
        Document batiment = batiments.find(new Document("type", type)).first();

        if (batiment != null) {
            int niveauActuel = batiment.getInteger("niveau", 1);
            int niveauSuivant = niveauActuel + 1;

            //Définir les couts pour chaque niveau (exemple : cout augmente avec le niveau)
            int coutBois = niveauSuivant * 100;
            int coutPierre = niveauSuivant * 50;

            //Vérifier si les ressources sont suffisantes
            if (gestionRessources.verifierRessource("Bois", coutBois) && gestionRessources.verifierRessource("Pierre", coutPierre)) {

                //Déduire les ressources nécessaires
                gestionRessources.mettreAJourRessource("Bois", - coutBois);
                gestionRessources.mettreAJourRessource("Pierre", - coutPierre);

                //Mettre à jour le niveau du batiment
                mettreAJourBatiment(type, niveauSuivant);
                System.out.println("Batiment amélioré : " + type + " au niveau " + niveauSuivant);
            } else {
                System.out.println("Ressources insuffisantes pour améliorer le batiment : " + type);
            }
        } else {
            System.out.println("Batiment introuvable : " + type);
        }
    }
}
