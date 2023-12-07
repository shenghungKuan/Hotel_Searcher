package hotelapp;

import com.google.gson.annotations.SerializedName;

/**
 * A class used to store information of hotels
 */
public class Hotel {
    @SerializedName("f")
    private String name;
    private String id;
    class Ll{
        private String lat;
        private String lng;

        public Ll(String lat, String lng) {
            this.lat = lat;
            this.lng = lng;
        }

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

    private String link;

    public Hotel(String name, String id, String lat, String lng, String address) {
        this.name = name;
        this.id = id;
        this.ll = new Ll(lat, lng);
        this.address = address;
        this.link = "https://www.expedia.com/"
                + this.name.replace(" ", "-")
                + ".h"
                + this.id
                + ".Hotel-Information";
    }

    /**
     * Generates the expedia link and return
     * @return the expedia link
     */
    public String getLink() {

        return this.link;
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
                this.getAddress() + "\n";
    }
}
