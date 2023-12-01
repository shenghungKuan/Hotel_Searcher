package hotelapp;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The review class used to store the information in a review
 */
public class Review implements Comparable<Review>{
    private String hotelId;
    private String reviewId;
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
    public Review(String hotelId, String title, String reviewText, String userNickname, String datePosted) {
        this.hotelId = hotelId;
        this.title = title;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        this.datePosted = datePosted;
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
     * Getter for review id
     * @return the review id
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * Setter for review id
     * @param reviewId the review id
     */
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
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
    public String getDatePosted() {
        return datePosted;
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
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        int dateDiff = LocalDate.parse(review.getDatePosted(), formatter).compareTo(LocalDate.parse(this.getDatePosted(), formatter));
        return dateDiff;
    }

    /**
     * The toString method for reviews
     * @return an user-friendly string format
     */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return "--------------------\n" +
                "Review by " + this.getUserNickname() + " on " + LocalDate.parse(this.getDatePosted(), formatter) +
                "\nReviewId: " + this.reviewId +
                "\n" + this.title +
                "\n" + this.reviewText + "\n";
    }
}
