package com.xadmin.NoteTaker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xadmin.NoteTaker.bean.userbean;

public class userdao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "rootpasswordgiven";
//	private String jdbcDriver="com.mysql.jdbc.Driver";
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES "
			+ " (?, ?, ?);";

	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
	
	public userdao()
	{
		
	}
	//separate method to get connection of jdbc and to load the driver and return connection
	protected Connection getConnection() {
		Connection connection = null;//connection object
		
		try {
			Class.forName("com.mysql.jdbc.Driver");//loading jdbc with driver name
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			//taking connection by using get connection method
			//driver manager is a class and get connection method to pass arguments
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;//return connection object
}
	
//crud operations
	
//insert user
	public  void insertUser(userbean user) throws SQLException {
		System.out.println(INSERT_USERS_SQL);//showing output
		// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();//calling get connection method to get
				//connection of jdbc and store it in connection object
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getName());//calling set string methd to set namee email countryto prepared staemtn
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();//to run the query in the databse
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	
	//select user by id
	
	public userbean selectUser(int id) {//fetch user id from db
		userbean user = null;
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new userbean(name, id, email, country);//paasing this to user bean class
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}
	
	//select all user method
	//return type will be list
	public List<userbean> selectAllUsers() {

		// using try-with-resources to avoid closing resources (boiler plate code)
		List<userbean> users = new ArrayList<>();//user store array list
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();

				// Step 2:Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new userbean(name, id, email, country));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}
	//update user
	public boolean updateUser(userbean user) throws SQLException {//type is boolean to check whether update method is caaling or not
		boolean rowUpdated;//variable
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			System.out.println("updated USer:"+statement);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());

			rowUpdated = statement.executeUpdate() > 0;//if it is >than 0 so return true
		}
		return rowUpdated;
	}
	//delete user
	
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
private void printSQLException(SQLException ex) 
{
	for (Throwable e : ex) {//running for each loop with ex object of sql exception
		if (e instanceof SQLException) {//check instance of exception
			e.printStackTrace(System.err);
			System.err.println("SQLState: " + ((SQLException) e).getSQLState());
			System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
			System.err.println("Message: " + e.getMessage());
			Throwable t = ex.getCause();//running get cause method to get cause of exception and storing it in t obj of throeable type
			while (t != null) {
				System.out.println("Cause: " + t);
				t = t.getCause();
			}
		}
	}
	
}
}
