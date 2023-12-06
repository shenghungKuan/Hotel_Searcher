package database;

import hotelapp.Hotel;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 *
 * Modified from the example of Prof. Engle
 */
public class DatabaseHandler {

    private static DatabaseHandler dbHandler = new DatabaseHandler("database.properties"); // singleton pattern
    private Properties config;
    private String uri;
    private Random random = new Random();

    /**
     * DataBaseHandler is a singleton, we want to prevent other classes
     * from creating objects of this class using the constructor
     */
    private DatabaseHandler(String propertiesFile){
        this.config = loadConfigFile(propertiesFile);
        this.uri = config.getProperty("uri");
    }

    /**
     * Returns the instance of the database handler.
     * @return instance of the database handler
     */
    public static DatabaseHandler getInstance() {
        return dbHandler;
    }


    // Load info from config file database.properties

    /**
     * load database configuration properties
     * @param propertyFile filename of the properties file
     * @return
     */
    public Properties loadConfigFile(String propertyFile) {
        Properties config = new Properties();
        try (FileReader fr = new FileReader(propertyFile)) {
            config.load(fr);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        return config;
    }

    /**
     * Creates a table in the designated database
     */
    public void createTables() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_TABLE_USERS);
            statement.executeUpdate(PreparedStatements.CREATE_TABLE_HOTELS);
            statement.executeUpdate(PreparedStatements.CREATE_TABLE_REVIEWS);
            statement.executeUpdate(PreparedStatements.CREATE_TABLE_USERLIKES);
            statement.executeUpdate(PreparedStatements.CREATE_TABLE_USERREVIEWS);
            statement.executeUpdate(PreparedStatements.CREATE_TABLE_EXPEDIAHISTORY);
        }
        catch (SQLException ex) {
             System.out.println(ex);
        }
    }


    /**
     * Returns the hex encoding of a byte array.
     *
     * @param bytes - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] bytes, int length) {
        BigInteger bigint = new BigInteger(1, bytes);
        String hex = String.format("%0" + length + "X", bigint);

        assert hex.length() == length;
        return hex;
    }

    /**
     * Calculates the hash of a password and salt using SHA-256.
     *
     * @param password - password to hash
     * @param salt - salt associated with user
     * @return hashed password
     */
    public static String getHash(String password, String salt) {
        String salted = salt + password;
        String hashed = salted;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());
            hashed = encodeHex(md.digest(), 64);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return hashed;
    }

    /**
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     *
     * @param newuser - username of new user
     * @param newpass - password of new user
     */
    public void registerUser(String newuser, String newpass) {
        // Generate salt
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt = encodeHex(saltBytes, 32); // salt
        String passhash = getHash(newpass, usersalt); // hashed password
        System.out.println(usersalt);

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Registration: dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.REGISTER_SQL);
                statement.setString(1, newuser);
                statement.setString(2, passhash);
                statement.setString(3, usersalt);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Authenticates users by checking if the entered password matches the one in the database
     * @param username username
     * @param password password
     * @return true if it matches, otherwise false
     */
    public Timestamp authenticateUser(String username, String password) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Authentication: dbConnection successful");
            statement = connection.prepareStatement(PreparedStatements.AUTH_SQL);
            String usersalt = getSalt(connection, username);
            String passhash = getHash(password, usersalt);

            statement.setString(1, username);
            statement.setString(2, passhash);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                statement = connection.prepareStatement(PreparedStatements.UPDATE_LASTLOGIN);
                statement.setString(1, username);
                statement.executeUpdate();
            }
            return results.getTimestamp("lastlogin");
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Checks if the username already exists in the databse
     * @param username username
     * @return true if the username already exists, otherwise false
     */
    public boolean checkUsername(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Checking: dbConnection successful");
            statement = connection.prepareStatement(PreparedStatements.CHECK_SQL);

            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            return results.next();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Gets the salt for a specific user.
     *
     * @param connection - active database connection
     * @param user - which user to retrieve salt for
     * @return salt for the specified user or null if user does not exist
     * @throws SQLException if any issues with database connection
     */
    private String getSalt(Connection connection, String user) {
        String salt = null;
        try (PreparedStatement statement = connection.prepareStatement(PreparedStatements.SALT_SQL)) {
            statement.setString(1, user);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                salt = results.getString("usersalt");
                return salt;
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return salt;
    }

    public void addHotel(Hotel hotel) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Add hotel: dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.ADD_HOTEL);
                statement.setString(1, hotel.getId());
                statement.setString(2, hotel.getName());
                statement.setString(3, hotel.getLat());
                statement.setString(4, hotel.getLng());
                statement.setString(5, hotel.getAddress());
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public Hotel getHotelWithId(String hotelid) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Get hotel with id: dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.GET_HOTELWITHID);
                statement.setString(1, hotelid);
                ResultSet results = statement.executeQuery();
                if (results.next()) {
                    Hotel hotel = new Hotel(results.getString("name"),
                            results.getString("hotelid"), results.getString("lat"),
                            results.getString("lng"), results.getString("address"));
                    statement.close();
                    return hotel;
                }
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public List<Hotel> getAllHotel() {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("Get all hotels: dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.GET_ALLHOTEL);
                ResultSet results = statement.executeQuery();
                List<Hotel> hotels = new ArrayList<>();
                while (results.next()) {
                    hotels.add(new Hotel(results.getString("name"),
                            results.getString("hotelid"), results.getString("lat"),
                            results.getString("lng"), results.getString("address")));
                }
                statement.close();
                return hotels;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static void main(String[] args) {
        DatabaseHandler dbhandler = DatabaseHandler.getInstance();
        dbhandler.createTables();
        System.out.println("created tables ");
    }
}

