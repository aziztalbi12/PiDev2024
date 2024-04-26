package services;

import entities.Category;
import entities.Product;
import interfaces.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements IService<Product> {

    Connection cnx;
    public ProductService() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Product p) throws SQLException {
        String req = "INSERT INTO product (nom, description, category_id, prix) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, p.getNom());
        ps.setString(2, p.getDescription());
        ps.setInt(3, p.getId());
        ps.setDouble(4, p.getPrix());
        ps.executeUpdate();
    }

    @Override
    public void update(Product product) throws SQLException {
        String req = "UPDATE product SET nom = ?, description = ?, category_id = ?, prix = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, product.getNom());
        ps.setString(2, product.getDescription());
        ps.setInt(3, product.getCategory_id());
        ps.setDouble(4, product.getPrix());
        ps.setInt(5, product.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Product product) throws SQLException {
        String req = "DELETE FROM product WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, product.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Product> read() throws SQLException {
        List<Product> products = new ArrayList<>();
        String req = "SELECT * FROM product";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("id"),
                    rs.getInt("category_id"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getDouble("prix")
            );
            products.add(product);
        }
        return products;
    }
}
