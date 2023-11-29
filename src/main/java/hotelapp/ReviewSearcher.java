package hotelapp;

import com.google.gson.Gson;
import org.javatuples.Triplet;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * The main searching class containing review search methods and several data structure for storing data
 */
public abstract class ReviewSearcher {

    protected HashMap<String, TreeSet<Review>> reviews;

    /**
     * Constructor without calling parseDirectory
     */
    public ReviewSearcher() {
        this.reviews = new HashMap<>();
    }

    /**
     * Constructor with calling parseDirectory
     * @param path the review directory to be parsed
     */
    public ReviewSearcher(String path) {
        this.reviews = new HashMap<>();
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
            for (Review r: wrapper.reviewDetails.reviewCollection.review) {
                if (r.getUserNickname().equals("")) {
                    r.setUserNickname("Anonymous");
                }
                this.reviews.computeIfAbsent(r.getHotelId(), k -> new TreeSet<>());
                this.reviews.get(r.getHotelId()).add(r);
            }
        } catch (IOException e) {
            System.out.println("Could not read the file: " + e);
        }
    }

    /**
     * Return the set of reviews with a given hotel id
     * @param hotelId the hotel id
     * @return the set of reviews with the given hotel id
     */
    public Set<Review> findReview(String hotelId) {
        return this.reviews.get(hotelId);
    }

    public Review findSpecificReview (String hotelId, String username) {
        if (this.reviews.get(hotelId) == null) {
            return null;
        }
        for (Review review: this.reviews.get(hotelId)) {
            if (review.getUserNickname() != null && review.getUserNickname().equals(username)) {
                return review;
            }
        }
        return null;
    }

    public void addReview(String username, String hotelId, String title, String text) {
        Review review = new Review(hotelId, title, text, username, LocalDateTime.now().toString());
        if (this.reviews.get(hotelId) != null) {
            this.reviews.get(hotelId).add(review);
        } else {
            TreeSet<Review> reviewSet = new TreeSet<>();
            reviewSet.add(review);
            this.reviews.put(hotelId, reviewSet);
        }
    }

    public void deleteReview (String username, String hotelId) {
        for (Review review: this.reviews.get(hotelId)) {
            if (review.getUserNickname().equals(username)) {
                this.reviews.get(hotelId).remove(review);
                break;
            }
        }

    }
}
