package hotelapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

/**
 * The main searching class containing hotel search methods and several data structure for storing data
 */
public abstract class HotelSearcher {

    protected TreeMap<String, Hotel> hotels;

    /**
     * Constructor without calling parseHotel
     */
    public HotelSearcher() {
        this.hotels = new TreeMap<>();
    }

    /**
     * Constructor with parseHotel
     * @param path the hotel file directory to be parsed
     */
    public HotelSearcher(String path) {
        this.hotels = new TreeMap<>();
        this.parseHotel(path);
    }

    /**
     * Parse the hotels with a given file path
     * Read the json file with the given file path, load the hotel information into hotel class
     * @param filePath the file path of the hotel file
     */
    protected void parseHotel(String filePath) {
        Gson gson = new Gson();

        try (FileReader fr = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(fr);
            JsonArray jsonArr = jo.getAsJsonArray("sr");

            Hotel[] hotels = gson.fromJson(jsonArr, Hotel[].class);
            for (Hotel hotel : hotels) {
                this.hotels.put(hotel.getId(), hotel);
            }
        } catch (IOException e) {
            System.out.println("Could not read the file: " + e);
        }
    }

    /**
     * Return the Hotel object with a given hotel id
     * @param hotelId the id of the hotel
     * @return the Hotel object with the given hotel id
     */
    public Hotel find(String hotelId) {
        if (hotelId == null) {
            return null;
        }
        return this.hotels.get(hotelId);
    }
}
