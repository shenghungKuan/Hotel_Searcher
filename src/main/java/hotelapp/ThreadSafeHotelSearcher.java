package hotelapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The thread-safe HotelSearcher class
 */
public class ThreadSafeHotelSearcher extends HotelSearcher{
    private ReentrantReadWriteLock lock;

    /**
     * Constructor of ThreadSafeHotelSearcher
     * @param path the hotel directory to be parsed
     */
    public ThreadSafeHotelSearcher(String path) {
        super();
        this.lock = new ReentrantReadWriteLock();
        this.parseHotel(path);
    }
    /**
     * Parse the hotels with a given file path
     * Read the json file with the given file path, load the hotel information into hotel class
     * @param filePath the file path of the hotel file
     */
    protected void parseHotel(String filePath) {
        try {
            lock.writeLock().lock();
            super.parseHotel(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Return the Hotel object with a given hotel id
     * @param hotelId the id of the hotel
     * @return the Hotel object with the given hotel id
     */
    public Hotel find(String hotelId) {
        try {
            lock.readLock().lock();
            return super.find(hotelId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns a list of hotel whose names contain the given name
     * @param hotelName the name of the hotels
     * @return a list of hotel whose names contain the given name
     */
    @Override
    public List<Hotel> search(String hotelName) {
        try {
            lock.readLock().lock();
            return super.search(hotelName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Return a collection of all the hotels
     * @return a collection of all the hotels
     */
    public Collection<Hotel> getHotels() {
        try {
            lock.readLock().lock();
            return super.getHotels();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }
}
