package fr.diginamic.entite;

import org.bson.Document;

public class Ressource extends Document {
    private String type;
    private int quantite;

    public Ressource(String type, int quantite) {
        this.type = type;
        this.quantite = quantite;
    }

    public void afficherDetailsRessource() {
        System.out.println("Ressource: " + type + ", Quantité: " + quantite);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ressource{");
        sb.append("type='").append(type).append('\'');
        sb.append(", quantite=").append(quantite);
        sb.append('}');
        return sb.toString();
    }
}
