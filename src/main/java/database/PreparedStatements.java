package database;

public class PreparedStatements {
    /** Prepared Statements  */
    /** For creating the users table */
    public static final String CREATE_TABLE_USERS =
            "CREATE TABLE users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL, " +
                    "lastlogin DATETIME NOT NULL);";

    public static final String CREATE_TABLE_HOTELS =
            "CREATE TABLE hotels (" +
                    "hotelid INTEGER PRIMARY KEY, " +
                    "name VARCHAR(64) NOT NULL, " +
                    "lat VARCHAR(32) NOT NULL, " +
                    "lng VARCHAR(32) NOT NULL, " +
                    "address VARCHAR(32) NOT NULL);";

    public static final String CREATE_TABLE_REVIEWS =
            "CREATE TABLE reviews (" +
                    "reviewid INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "hotelid INTEGER NOT NULL, " +
                    "FOREIGN KEY (hotelid) REFERENCES hotels(hotelid), " +
                    "username VARCHAR(32) NOT NULL, " +
                    "title VARCHAR(64) NOT NULL, " +
                    "text LONGTEXT, " +
                    "time TIMESTAMP, " +
                    "rating INT NOT NULL);";

    public static final String CREATE_TABLE_USERFAVORITES =
            "CREATE TABLE userfavorites (" +
                    "username VARCHAR(32) NOT NULL, " +
                    "hotelid INT NOT NULL, " +
                    "FOREIGN KEY (username) REFERENCES users(username), " +
                    "CONSTRAINT k PRIMARY KEY (username,hotelid), " +
                    "FOREIGN KEY (hotelid) REFERENCES hotels(hotelid));";

    public static final String CREATE_TABLE_EXPEDIAHISTORY =
            "CREATE TABLE expediahistory (" +
                    "username VARCHAR(32) NOT NULL, " +
                    "hotelid INT NOT NULL, " +
                    "CONSTRAINT con PRIMARY KEY (username, hotelid));";

    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt, lastlogin) " + "VALUES (?, ?, ?, NOW());";

    /** Used to retrieve the salt associated with a specific user. */
    public static final String SALT_SQL =
            "SELECT usersalt FROM users WHERE username = ?";

    /** Used to authenticate a user. */
    public static final String AUTH_SQL =
            "SELECT lastlogin FROM users " + "WHERE username = ? AND password = ?";

    public static final String UPDATE_LASTLOGIN =
            "UPDATE users " +
            "SET lastlogin = NOW() " +
            "WHERE username = ?;";

    /**
     * Used to check if the username is in used
     */
    public static final String CHECK_SQL =
            "SELECT username FROM users " + "WHERE username = ?";

    public static final String ADD_HOTEL =
            "INSERT INTO hotels (hotelid, name, lat, lng, address) " + "VALUES (?, ?, ?, ?, ?);";

    public static final String GET_HOTELWITHID =
            "SELECT * FROM hotels WHERE hotelid=?";

    public static final String GET_ALLHOTEL =
            "SELECT * FROM hotels";

    public static final String ADD_REVIEW =
            "INSERT INTO reviews (hotelid, username, title, text, time, rating) " + "VALUES (?, ?, ?, ?, ?, ?);";

    public static final String GET_REVIEWWITHID =
            "SELECT * FROM reviews WHERE hotelid=?";

    public static final String GET_REVIEWWITHNAME =
            "SELECT * FROM reviews WHERE hotelid=? AND username=?";

    public static final String DELETE_REVIEW =
            "DELETE FROM reviews WHERE hotelid=? AND username=?";

    public static final String ADD_EXPEDIAHISTORY =
            "INSERT INTO expediahistory (username, hotelid) " + "VALUES (?, ?);";

    public static final String GET_EXPEDIAHISTORY =
            "SELECT * FROM expediahistory WHERE username=?";

    public static final String CLEAR_HISTORY =
            "DELETE FROM expediahistory WHERE username=?";

    public static final String ADD_FAVORITE =
            "INSERT INTO userfavorites (username, hotelid) " + "VALUES (?, ?);";

    public static final String GET_FAVORITE =
            "SELECT * FROM userfavorites WHERE username=?";

    public static final String CLEAR_FAVORITE =
            "DELETE FROM userfavorites WHERE username=?";
}
