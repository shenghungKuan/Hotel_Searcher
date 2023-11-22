package hotelapp;

import com.google.gson.annotations.SerializedName;

/**
 * A class used to store information of hotels
 */
public class Hotel {
    @SerializedName("f")
    private String name;
    private String id;

    @SerializedName("ci")
    private String city;

    @SerializedName("pr")
    private String state;
    class Ll{
        private String lat;
        private String lng;

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }
    Ll ll;
    @SerializedName("ad")
    private String address;

    /**
     * Getter of the city
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter for the state
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Getter for the hotel name
     * @return the hotel name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the hotel id
     * @return hotel id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for the latitude
     * @return the hotel latitude
     */
    public String getLat() {
        return this.ll.getLat();
    }

    /**
     * Getter for the longitude
     * @return the hotel longitude
     */
    public String getLng() {
        return this.ll.getLng();
    }

    /**
     * Getter for the hotel address
     * @return the hotel address
     */
    public String getAddress() {
        return address;
    }


    /**
     * toString method for the hotel information
     * @return A string of the hotel information
     */
    public String toString() {
        return "\n" +
                "********************\n" +
                this.getName() + ": " + this.id + "\n" +
                this.getAddress() + "\n" +
                this.getCity() + ", " + this.getState() + "\n";
    }
}
