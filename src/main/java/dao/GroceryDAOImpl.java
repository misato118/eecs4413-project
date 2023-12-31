package dao;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Country;
import model.Grocery;
import model.Brand;
import model.Category;
import model.Country;
import model.Grocery;

public class GroceryDAOImpl implements GroceryDAO {

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}

	private Connection getConnection() throws SQLException {
		// Use this line below for running the project on your local machine
		return DriverManager.getConnection("jdbc:sqlite:src/main/webapp/databases/groceries.db");
		
		// Use this line below for running the project using Docker
		//return DriverManager.getConnection("jdbc:sqlite:webapps/eecs4413-project/databases/groceries.db");
	}
	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		} catch (SQLException ex) {
		}
	}

	public List<Grocery> findAllGroceries(String sort) {
		List<Grocery> result = new ArrayList<Grocery>();
		
		String sql = "SELECT grocery.id as grocery_id, grocery.name, country.id as country_id, country.name, category.id as category_id, category.category_description, brand.id as brand_id, brand.name as brand_name, grocery.price"
				+ " FROM grocery"
				+ " INNER JOIN country, category, brand ON grocery.country_id = country.id AND grocery.category_id = category.id AND grocery.brand_id = brand.id";
		if(sort != null) {
			if(sort.equals("Price: Lowest to Highest")) {
				sql += " ORDER BY grocery.price ASC";
			}else if(sort.equals("Price: Highest to Lowest")) {
				sql+= " ORDER BY grocery.price DESC";
			}else if(sort.equals("Alphabetically: A-Z")) {
				sql += " ORDER BY grocery.name ASC"; 
			}else if(sort.equals("Alphabetically: Z-A")){
				sql += " ORDER BY grocery.name DESC";
			}else {
				sql += " ORDER BY brand.name";
			}
		}
		
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet =  statement.executeQuery();
			while (resultSet.next()) {
				Grocery grocery = new Grocery();
				Country country = new Country();

				setCountryAndGrocery(resultSet, country, grocery);	 				
				result.add(grocery);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}

	@Override
	public List<Grocery> searchGroceriesByKeyword(String keyWord,String sort) {
		List<Grocery> result = new ArrayList<Grocery>();
		
		String sql = "SELECT grocery.id as grocery_id, grocery.name, country.id as country_id, country.name, category.id as category_id, category.category_description, brand.id as brand_id, brand.name as brand_name, grocery.price"
				+ " FROM grocery"
				+ " INNER JOIN country, category, brand ON grocery.country_id = country.id AND grocery.category_id = category.id AND grocery.brand_id = brand.id"
				+ " WHERE grocery.name like '%" + keyWord.trim() + "%'";
		if(sort != null) {
			if(sort.equals("Price: Lowest to Highest")) {
				sql += " ORDER BY grocery.price ASC";
			}else if(sort.equals("Price: Highest to Lowest")) {
				sql+= " ORDER BY grocery.price DESC";
			}else if(sort.equals("Alphabetically: A-Z")) {
				sql += " ORDER BY grocery.name ASC"; 
			}else if(sort.equals("Alphabetically: Z-A")){
				sql += " ORDER BY grocery.name DESC";
			}else {
				sql += " ORDER BY brand.name";
			}
		}
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);  
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Grocery grocery = new Grocery();
				Country country = new Country();
                
				setCountryAndGrocery(resultSet, country, grocery);
				result.add(grocery);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}

		return result;
	}

	public List<Category> findAllCategories() {
		List<Category> result = new ArrayList<>();
		String sql = "SELECT * FROM category";

		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Category category = new Category();
				
				// populate category bean with needed info
                category.setId(resultSet.getInt("id"));
                category.setCategoryDescription(resultSet.getString("category_description"));

				result.add(category);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}
	
	public List<Brand> findAllBrands() {
		List<Brand> result = new ArrayList<>();
		String sql = "SELECT * FROM brand";

		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Brand brand = new Brand();
				
				// populate category bean with needed info
				brand.setId(resultSet.getInt("id"));
				brand.setName(resultSet.getString("name"));

				result.add(brand);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}

	public List<Grocery> findGroceriesByCategory(String category, String sort) {
		List<Grocery> result = new ArrayList<Grocery>();
		String sql = "SELECT grocery.id as grocery_id, grocery.name, country.id as country_id, country.name, category.id as category_id, category.category_description, brand.id as brand_id, brand.name as brand_name, grocery.price"
				+ " FROM grocery"
				//+ " INNER JOIN country, category ON grocery.country_id = country.id AND grocery.category_id = category.id"
				+ " INNER JOIN country, category, brand ON grocery.country_id = country.id AND grocery.category_id = category.id AND grocery.brand_id = brand.id"
				+ " WHERE category.category_description = '" + category + "'";
		if(sort != null) {
			if(sort.equals("Price: Lowest to Highest")) {
				sql += " ORDER BY grocery.price ASC";
			}else if(sort.equals("Price: Highest to Lowest")) {
				sql+= " ORDER BY grocery.price DESC";
			}else if(sort.equals("Alphabetically: A-Z")) {
				sql += " ORDER BY grocery.name ASC"; 
			}else if(sort.equals("Alphabetically: Z-A")){
				sql += " ORDER BY grocery.name DESC";
			}else {
				sql += " ORDER BY brand.name";
			}
		}
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Grocery grocery = new Grocery();
				Country country = new Country();
				
				// populate book and author beans with needed info, and then set author to book
				setCountryAndGrocery(resultSet, country, grocery);		
				result.add(grocery);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}
	
	public List<Grocery> findGroceriesByBrand(String brand, String sort) {
		List<Grocery> result = new ArrayList<Grocery>();
		String sql = "SELECT grocery.id as grocery_id, grocery.name, country.id as country_id, country.name, category.id as category_id, category.category_description, brand.id as brand_id, brand.name as brand_name, grocery.price"
				+ " FROM grocery"
				+ " INNER JOIN country, category, brand ON grocery.country_id = country.id AND grocery.category_id = category.id AND grocery.brand_id = brand.id"
				+ " WHERE brand.name = '" + brand + "'";
		if(sort != null) {
			if(sort.equals("Price: Lowest to Highest")) {
				sql += " ORDER BY grocery.price ASC";
			}else if(sort.equals("Price: Highest to Lowest")) {
				sql+= " ORDER BY grocery.price DESC";
			}else if(sort.equals("Alphabetically: A-Z")) {
				sql += " ORDER BY grocery.name ASC"; 
			}else if(sort.equals("Alphabetically: Z-A")){
				sql += " ORDER BY grocery.name DESC";
			}
		}
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Grocery grocery = new Grocery();
				Country country = new Country();
				
				// populate book and author beans with needed info, and then set author to book
				setCountryAndGrocery(resultSet, country, grocery);
				result.add(grocery);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}	
	
	public void insert(Grocery grocery) {
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(
					"insert into groceries (name) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, grocery.getName());
			statement.execute();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				grocery.setId(generatedKeys.getInt(1));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
	}
	
	public void delete(int groceryId) {
		Connection connection = null;

		try {
			connection = getConnection();
			PreparedStatement statement = connection
					.prepareStatement("delete from grocery where id=?");
			statement.setInt(1, groceryId);
			statement.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
	}
	
	public Grocery searchByID(int id) {
		Connection connection = null;
		Grocery grocery = null;
		String name = null;
		float price = 0;
		try {

			connection = getConnection();
			PreparedStatement statement = connection
					.prepareStatement("select * from grocery where id=?");
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			name = rs.getString("name");
			price = rs.getFloat("price");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		grocery = new Grocery(id, name, price);
		return grocery;
	}

	public Grocery searchByName(String name) {
		Grocery grocery = new Grocery();

		String sql = "SELECT grocery.id as grocery_id, grocery.name, country.id as country_id, country.name, category.id as category_id, category.category_description, brand.id as brand_id, brand.name as brand_name, grocery.price"
				+ " FROM grocery"
				+ " INNER JOIN country, category, brand ON grocery.country_id = country.id AND grocery.category_id = category.id AND grocery.brand_id = brand.id"
				+ " WHERE grocery.name = '" + name + "'";

		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);  
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Country country = new Country();     
				setCountryAndGrocery(resultSet, country, grocery);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return grocery;
	}
	
	public void setCountryAndGrocery(ResultSet rs, Country country, Grocery grocery) {
		try {
			country.setId(rs.getInt("country_id"));
			country.setGroceryId(rs.getInt("grocery_id"));
			country.setName(rs.getString(4));

			grocery.setId(rs.getInt("grocery_id"));
			grocery.setCategoryId(rs.getInt("category_id"));
			grocery.setBrandId(rs.getInt("brand_id"));
			grocery.setName(rs.getString("name"));
			grocery.setCountry(country);
			grocery.setCategory(rs.getString("category_description"));
			grocery.setBrand(rs.getString("brand_name"));
			grocery.setPrice(rs.getFloat("price"));
			grocery.setImg("images/" + rs.getString("name").toLowerCase().replace(" ", "") + ".png");
			grocery.setBrand(rs.getString(8));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}