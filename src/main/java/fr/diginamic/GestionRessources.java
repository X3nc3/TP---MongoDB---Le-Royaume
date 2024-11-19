package fr.diginamic;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

public class GestionRessources {
    private MongoCollection<Document> ressources;

    public GestionRessources(MongoCollection<Document> ressources) {
        this.ressources = ressources;
    }

    // Méthode pour ajouter une ressource
    public void ajouterRessource(String type, int quantite) {
        Document ressource = ressources.find(new Document("type", type)).first();
        if (ressource != null) {
            int nouvelleQuantite = quantite + ressource.getInteger("quantite", 0);
            mettreAJourRessource(type, nouvelleQuantite);
        } else {
            ressources.insertOne(new Document("type", type).append("quantite", quantite));
        }
        System.out.println("Ressource ajoutée : "+ quantite + " " + type);
    }

    //Méthode pour afficher toutes les ressources
    public void afficherRessources() {
        for (Document ressources : ressources.find()) {
            System.out.println(ressources.toJson());
        }
    }

    //Méthode pour mettre à jour la quantité d'une ressource
    public void mettreAJourRessource(String type, int nouvelleQuantite) {
        ressources.updateOne(new Document("type", type), new Document("$set", new Document("quantite", nouvelleQuantite)));
        System.out.println("Quantité mise à jour pour : " + type);
    }

    //Méthode pour supprimer une ressource
    public void supprimerRessource(String type) {
        ressources.deleteOne(new Document("type", type));
        System.out.println("Ressource supprimée : " + type);
    }

    //Méthode pour vérifier les ressources
    public boolean verifierRessource(String type, int quantiteNecessaire) {
        Document ressource = ressources.find(new Document("type", type)).sort(Sorts.descending("_id")).first(); //Récuper le document le plus récent
        int quantiteDisponible = ressource.getInteger("quantite");
        if (ressource != null) {
            System.out.println("Quantité disponible de " + type + ":" + quantiteDisponible);
            System.out.println("Quantité necessaire de " + type + ":" + quantiteNecessaire);
            return quantiteDisponible >= quantiteNecessaire;
        }
        return false;
    }
}
