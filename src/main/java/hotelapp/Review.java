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
     * @param hotelId the hotel id
     * @param title the title of the review
     * @param reviewText the text of the review
     * @param userNickname the username of the review
     * @param datePosted the posted date of the review
     * @param rating the rating of the review
     */
    public Review(String hotelId, String title, String reviewText, String userNickname, Timestamp datePosted, int rating) {
        this.hotelId = hotelId;
        this.title = title;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        this.datePosted = String.valueOf(datePosted);
        this.ratingOverall = rating;
    }

    /**
     * Gets the overall rating of the review
     * @return the rating
     */
    public int getRatingOverall() {
        return ratingOverall;
    }

    /**
     * Getter of hotel id
     * @return hotelId the hotel id
     */
    public String getHotelId() {
        return hotelId;
    }

    /**
     * Getter for title
     * @return title the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for review text
     * @return reviewText the review text
     */
    public String getReviewText() {
        return reviewText;
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
