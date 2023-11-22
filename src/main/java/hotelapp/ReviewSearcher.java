package hotelapp;

import com.google.gson.Gson;
import org.javatuples.Triplet;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;


/**
 * The main searching class containing review search methods and several data structure for storing data
 */
public abstract class ReviewSearcher {

    protected HashMap<String, TreeSet<Review>> reviews;
    protected HashMap<String, TreeSet<Triplet<Integer, String, Review>>> keywords;
    protected static HashSet<String> redundantWord;

    static {
        redundantWord = new HashSet<>();
        redundantWord.add("a");
        redundantWord.add("the");
        redundantWord.add("is");
        redundantWord.add("are");
        redundantWord.add("were");
        redundantWord.add("and");
    }

    /**
     * Constructor without calling parseDirectory
     */
    public ReviewSearcher() {
        this.reviews = new HashMap<>();
        this.keywords = new HashMap<>();
    }

    /**
     * Constructor with calling parseDirectory
     * @param path the review directory to be parsed
     */
    public ReviewSearcher(String path) {
        this.reviews = new HashMap<>();
        this.keywords = new HashMap<>();
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
                this.addWord(r);
            }
        } catch (IOException e) {
            System.out.println("Could not read the file: " + e);
        }
    }

    /**
     * Add the words from the review text in the reviews to the data structure
     * Parse and count the words in the review text, adding the word count into the data structure
     * @param review The review to be processed
     */
    protected void addWord(Review review) {
        String[] text = review.getReviewText().split(" ");
        HashMap<String, Integer> keywordCount = new HashMap<>();
        for (String word: text) {
            if (word.matches("[0-9]+")) {
                continue;
            }
            String w = word.toLowerCase();
            if (!redundantWord.contains(w)) {
                if (!keywordCount.containsKey(w)) {
                    keywordCount.put(w, 0);
                }
                keywordCount.put(w, keywordCount.get(w) + 1);
            }
        }
        for (String word: keywordCount.keySet()) {
            Triplet<Integer, String, Review> triplet = Triplet.with(keywordCount.get(word), review.getDatePosted(), review);
            this.keywords.computeIfAbsent(word, k -> new TreeSet<>(new Comparator<Triplet<Integer, String, Review>>() {
                @Override
                public int compare(Triplet<Integer, String, Review> o1, Triplet<Integer, String, Review> o2) {
                    return o2.compareTo(o1);
                }
            }));
            this.keywords.get(word).add(triplet);
        }
    }

    /**
     * Return the set of reviews with a given hotel id
     * @param hotelId the hotel id
     * @return the set of reviews with the given hotel id
     */
    public TreeSet<Review> findReview(String hotelId) {
        return this.reviews.get(hotelId);
    }

    /**
     * Return a set of Triplet of the frequency of the word in the review, the word, and the reviews containing the word
     * @param keyword the word to find
     * @return the set of Triplet of the frequency of the word in the review, the word, and the reviews containing the word
     */
    public TreeSet<Triplet<Integer, String, Review>> findWord(String keyword) {
        return this.keywords.get(keyword);
    }

    /**
     * Print the reviews
     */
    public void showReviews() {
        for (String hotelId: this.reviews.keySet()) {
            System.out.println(this.reviews.get(hotelId));
        }
    }

}
