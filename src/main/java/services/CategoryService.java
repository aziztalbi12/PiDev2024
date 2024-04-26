package services;

import entities.Category;
import interfaces.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService implements IService<Category> {
    Connection cnx;
    public CategoryService() {
        cnx = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Category c) throws SQLException {
        String req = "INSERT INTO category(nom, description, quantite) VALUES(?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, c.getNom());
        ps.setString(2, c.getDescription());
        ps.setInt(3, c.getQuantite());
        ps.executeUpdate();
    }

    @Override
    public void update(Category c) throws SQLException {
        String req = "UPDATE category SET nom = ?, description = ?, quantite = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, c.getNom());
        ps.setString(2, c.getDescription());
        ps.setInt(3, c.getQuantite());
        ps.setInt(4, c.getId());
        ps.executeUpdate();

    }

    @Override
    public void delete(Category c) throws SQLException {
        String req = "DELETE FROM category WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, c.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Category> read() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM Category";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Category category = new Category(
                    rs.getInt("id"),
                    rs.getInt("quantite"),
                    rs.getString("nom"),
                    rs.getString("description")
            );
            categories.add(category);
        }
        return categories;
    }
}
