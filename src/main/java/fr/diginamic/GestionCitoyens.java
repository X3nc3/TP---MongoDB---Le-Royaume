package fr.diginamic;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

public class GestionCitoyens {
    private MongoCollection<Document> citoyens;

    public GestionCitoyens(MongoCollection<Document> citoyens) {
        this.citoyens = citoyens;
    }

    //Méthode pour afficher tout les citoyens
    public void afficherCitoyens() {
        for (Document citoyen : citoyens.find()) {
            System.out.println(citoyen.toJson());
        }
    }

    //Méthode pour ajouter un citoyen
    public void ajouterCitoyen(String type, int quantite, String role) {
        Document citoyen = citoyens.find(new Document("role", role)).first();
        if (citoyen != null) {
            mettreAJourCitoyen(role, citoyen.getInteger("quantite") +quantite);
        } else {
            Document nouveauCitoyen = new Document("type", type).append("role", role).append("quantite", quantite);
            citoyens.insertOne(nouveauCitoyen);
            System.out.println("Citoyen crée");
        }
    }

    //Méthode pour mettre a jour la quantité d'un citoyen (ajouter ou supprimer)
    public void mettreAJourCitoyen(String role, int changementQuantite) {
        citoyens.updateOne(new Document("role", role), new Document("$set", new Document("quantite", changementQuantite)));
        System.out.println("Quantité mise a jour pour le role : " + role);
    }

    //Méthode pour mettre à jour le role d'un citoyen
    public void mettreAJourCitoyen(String type, String ancienRole, String nouveauRole) {
        Document citoyenNouveauRole = citoyens.find(new Document("role", nouveauRole)).first();
        if (citoyenNouveauRole != null) {
            int qteNouveauRole = citoyenNouveauRole.getInteger("quantite") +1;
            mettreAJourCitoyen(nouveauRole, qteNouveauRole);
        } else {
            Document citoyen = new Document("type", type).append("quantite", 1).append("role", nouveauRole);
            citoyens.insertOne(citoyen);
        }
        Document citoyenAncienRole = citoyens.find(new Document("role", ancienRole)).first();
        int qteAncienRole = citoyenAncienRole.getInteger("quantite") -1;
        mettreAJourCitoyen(ancienRole, qteAncienRole);

        System.out.println("Role mis à jour pour le citoyen : " + type + " (" + ancienRole + " -> " + nouveauRole + ")");
    }

    //Méthode pour vérifier les ressources
    public boolean verifierCitoyens(String type, int quantiteNecessaire) {
        Document citoyen = citoyens.find(new Document("type", type)).sort(Sorts.descending("_id")).first(); //Récuper le document le plus récent
        int quantiteDisponible = citoyen.getInteger("quantite");
        if (citoyens != null) {
            System.out.println("Quantité disponible de " + type + ":" + quantiteDisponible);
            System.out.println("Quantité necessaire de " + type + ":" + quantiteNecessaire);
            return quantiteDisponible >= quantiteNecessaire;
        }
        return false;
    }
}
