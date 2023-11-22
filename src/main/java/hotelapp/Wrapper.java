package hotelapp;

import java.util.List;

/**
 * A wrapper class to load nested components in a json file
 */
public class Wrapper {
    public class ReviewDetail {
        public String startIndex;
        public class ReviewCollection {
            List<Review> review;
        }
        public ReviewCollection reviewCollection;
    }
    ReviewDetail reviewDetails;
    public void showReview() {
        for (Review r: reviewDetails.reviewCollection.review) {
            System.out.println(r);
        }
    }
}
