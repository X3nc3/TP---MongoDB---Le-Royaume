package fr.diginamic.entite;

public class Citoyen {
    private String type;
    private int quantite;
    private String role;

    public Citoyen(String type, int quantite, String role) {
        this.type = type;
        this.quantite = quantite;
        this.role = role;
    }

    public void afficherDetailsCitoyen() {
        System.out.println("Citoyen: " + type + ", Quantité: " + quantite + ", Role" + role);
    }
}
