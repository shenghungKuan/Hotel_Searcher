package hotelapp;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The review class used to store the information in a review
 */
public class Review implements Comparable<Review>{
    private String hotelId;
    private String title;
    private String reviewText;
    @SerializedName(value = "user", alternate = {"userNickname"})
    private String userNickname;
    @SerializedName("reviewSubmissionTime")
    private String datePosted;

    private int ratingOverall;


    /**
     * The constructor of a Review
     * @param hotelId id of the hotel
     * @param title title of the hotel
     * @param reviewText review content
     * @param userNickname nickname of the user
     * @param datePosted posted date of the Review
     */
    public Review(String hotelId, String title, String reviewText, String userNickname, Timestamp datePosted, int rating) {
        this.hotelId = hotelId;
        this.title = title;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        this.datePosted = String.valueOf(datePosted);
        this.ratingOverall = rating;
    }

    public int getRatingOverall() {
        return ratingOverall;
    }

    public void setRatingOverall(int ratingOverall) {
        this.ratingOverall = ratingOverall;
    }

    /**
     * Getter of hotel id
     * @return hotelId the hotel id
     */
    public String getHotelId() {
        return hotelId;
    }

    /**
     * Setter for hotelId
     * @param hotelId the hotel id
     */
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    /**
     * Getter for title
     * @return title the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for review text
     * @return reviewText the review text
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Setter for review text
     * @param reviewText the review text
     */
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Getter for user nickname
     * @return userNickname the user's nickname
     */
    public String getUserNickname() {
        return userNickname;
    }

    /**
     * Setter for user nickname
     * @param userNickname user nickname of the review
     */
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    /**
     * Getter for posted date
     * @return datePosted the posted date
     */
    public Timestamp getDatePosted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[' ']['T']HH:mm:ss[.S][X]");
        return Timestamp.valueOf(LocalDateTime.parse(this.datePosted, formatter));
    }

    /**
     * Setter for datePosted
     * @param datePosted The posted date of the review
     */
    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    /**
     * The compareTo method for reviews
     * @param review the object to be compared.
     * @return an integer indicating the comparison between two reviews
     */
    @Override
    public int compareTo(Review review) {
        return this.getDatePosted().compareTo(review.getDatePosted());
    }

    /**
     * The toString method for reviews
     * @return an user-friendly string format
     */
    public String toString() {
        return "--------------------\n" +
                "Review by " + this.getUserNickname() + " on " +
                "\nReviewId: " +
                "\n" + this.title +
                "\n" + this.reviewText + "\n";
    }
}
