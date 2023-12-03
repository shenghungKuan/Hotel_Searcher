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
                    "name VARCHAR(32) NOT NULL, " +
                    "lat VARCHAR(32) NOT NULL, " +
                    "lng VARCHAR(32) NOT NULL, " +
                    "address VARCHAR(32) NOT NULL);";

    public static final String CREATE_TABLE_REVIEWS =
            "CREATE TABLE reviews (" +
                    "reviewid INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "hotelid INTEGER NOT NULL, " +
                    "FOREIGN KEY (hotelid) REFERENCES hotels(hotelid), " +
                    "username VARCHAR(32) NOT NULL, " +
                    "FOREIGN KEY (username) REFERENCES users(username), " +
                    "title VARCHAR(32) NOT NULL, " +
                    "text LONGTEXT, " +
                    "date DATETIME NOT NULL, " +
                    "rating INT NOT NULL, " +
                    "likes INT NOT NULL);";

    public static final String CREATE_TABLE_USERLIKES =
            "CREATE TABLE userlikes (" +
                    "username VARCHAR(32), " +
                    "reviewid INT, " +
                    "FOREIGN KEY (username) REFERENCES users(username), " +
                    "FOREIGN KEY (reviewid) REFERENCES reviews(reviewid));";

    public static final String CREATE_TABLE_USERREVIEWS =
            "CREATE TABLE userreviews (" +
                    "username VARCHAR(32), " +
                    "reviewid INT, " +
                    "FOREIGN KEY (username) REFERENCES users(username), " +
                    "FOREIGN KEY (reviewid) REFERENCES reviews(reviewid));";

    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " + "VALUES (?, ?, ?);";

    /** Used to retrieve the salt associated with a specific user. */
    public static final String SALT_SQL =
            "SELECT usersalt FROM users WHERE username = ?";

    /** Used to authenticate a user. */
    public static final String AUTH_SQL =
            "SELECT username FROM users " + "WHERE username = ? AND password = ?";

    /**
     * Used to check if the username is in used
     */
    public static final String CHECK_SQL =
            "SELECT username FROM users " + "WHERE username = ?";

}
