package hotelapp;

import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The thread-safe class of ReviewSearcher
 */
public class ThreadSafeReviewSearcher extends ReviewSearcher{
    private ReentrantReadWriteLock reviewsLock;
    private ReentrantReadWriteLock keywordsLock;

    private ExecutorService poolManager;
    private Phaser phaser = new Phaser();
//    private Logger logger = LogManager.getLogger();

    /**
     * Constructor of ThreadSafeReviewSearcher
     * @param nThreads number of threads
     * @param path the review directory
     */
    public ThreadSafeReviewSearcher(String nThreads, String path) {
        super();
        this.reviewsLock = new ReentrantReadWriteLock();
        this.keywordsLock = new ReentrantReadWriteLock();
        if (nThreads == null || Integer.parseInt(nThreads) <= 0) {
            this.poolManager = Executors.newFixedThreadPool(1);
        } else {
            this.poolManager = Executors.newFixedThreadPool(Integer.parseInt(nThreads));
        }
        if (path != null) {
            this.parseDirectoryMultiThread(path);
            phaser.awaitAdvance(0);
            poolManager.shutdown();
            try {
                poolManager.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Parse the reviews with a given file path
     * Read the json file with the given file path, load the review information into review class
     * @param filePath the file path of the review file
     */
    protected void parseReview(String filePath) {
        try {
            this.reviewsLock.writeLock().lock();
            super.parseReview(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.reviewsLock.writeLock().unlock();
        }
    }

    /**
     * Return a set of Review objects with a given hotel id
     * @param hotelId the hotel id
     * @return the set of Review objects with the given hotel id
     */
    public Set<Review> findReview(String hotelId) {
        try {
            this.reviewsLock.readLock().lock();
            if (this.reviews.get(hotelId) == null) {
                System.out.println("No review for this hotel id: " + hotelId);
                return null;
            }
            return super.findReview(hotelId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.reviewsLock.readLock().unlock();
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
}
