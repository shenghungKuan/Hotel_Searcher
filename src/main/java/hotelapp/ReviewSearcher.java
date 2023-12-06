package hotelapp;

import com.google.gson.Gson;
import database.DatabaseHandler;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;


/**
 * The main searching class containing review search methods and several data structure for storing data
 */
public class ReviewSearcher {

    private ExecutorService poolManager = Executors.newFixedThreadPool(4);
    private Phaser phaser = new Phaser();


    /**
     * Constructor without calling parseDirectory
     */
    public ReviewSearcher() {}

    public ReviewSearcher (String path) {
        this.parseDirectoryMultiThread(path);
        phaser.awaitAdvance(0);
        poolManager.shutdown();
        try {
            poolManager.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse the reviews with a given file path
     * Read the json file with the given file path, load the review information into review class
     * @param filePath the file path of the review file
     */
    protected void parseReview(String filePath) {
        Gson gson = new Gson();

        try (FileReader fr = new FileReader(filePath)) {
            Wrapper wrapper = gson.fromJson(fr, Wrapper.class);
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            for (Review r: wrapper.reviewDetails.reviewCollection.review) {
                if (r.getUserNickname().equals("")) {
                    r.setUserNickname("Anonymous");
                }
                dbHandler.addReview(r);
            }
        } catch (IOException e) {
            System.out.println("Could not read the file: " + e);
        }
    }

    /**
     * Traverse the given directory recursively and parse the review json files
     * @param directory The directory to be traversed
     */
    protected void parseDirectoryMultiThread(String directory) {
        Path p = Paths.get(directory);
        try (DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)) {
            for (Path path : pathsInDir) {
                if (!Files.isDirectory(path) && (path.toString().endsWith(".json"))){
                    poolManager.execute(new ReviewParser(path.toString()));
                    phaser.register();
                }else {
                    this.parseDirectoryMultiThread(path.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Can not open directory: " + directory);
        }
    }

    /**
     * The inner class of ThreadSafeReviewSearcher that implements Runnable
     */
    public class ReviewParser implements Runnable {

        private String file;
        /**
         * Constructor of ReviewParser
         * @param file the json file to be parsed
         */
        public ReviewParser(String file) {
            this.file = file;
        }

        /**
         * The overridden run method that parse reviews
         */
        @Override
        public void run() {
            try {
                parseReview(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                phaser.arriveAndDeregister();
            }
        }

    }

    /**
     * Return the set of reviews with a given hotel id
     * @param hotelId the hotel id
     * @return the set of reviews with the given hotel id
     */
    public List<Review> findReview(String hotelId) {
        return DatabaseHandler.getInstance().getReviewWithId(hotelId);
    }

    /**
     * Finds the review of the user of the hotel with given hotel id
     * @param hotelId the id of the hotel
     * @param username username
     * @return true if the user already has a review of the hotel
     */
    public boolean findSpecificReview (String hotelId, String username) {
        return DatabaseHandler.getInstance().getReviewWithName(hotelId, username);
    }

    /**
     * Adds review
     * @param username username
     * @param hotelId hotel id
     * @param title title of the review
     * @param text review content
     */
    public void addReview(String username, String hotelId, String title, String text, int rating) {
        Review review = new Review(hotelId, title, text, username, new java.sql.Date(System.currentTimeMillis()), rating);
        DatabaseHandler.getInstance().addReview(review);
    }

    /**
     * Removes the review of the user of the hotel
     * @param username username
     * @param hotelId hotel id
     */
    public void deleteReview (String username, String hotelId) {
        /*for (Review review: this.reviews.get(hotelId)) {
            if (review.getUserNickname().equals(username)) {
                this.reviews.get(hotelId).remove(review);
                break;
            }
        }*/

    }
    public static void main (String[] args) {
        ReviewSearcher reviewSearcher = new ReviewSearcher("input/reviews");
    }
}
