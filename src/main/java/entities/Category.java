package entities;

public class Category {
    int id,quantite;
    String nom,description;


    public Category(int id, int quantite, String nom, String description) {
        this.id = id;
        this.quantite = quantite;
        this.nom = nom;
        this.description = description;
    }

    public Category(int quantite, String nom, String description) {
        this.quantite = quantite;
        this.nom = nom;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
