package unsw.venues;

import java.time.LocalDate;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * A venue in the hire system
 * @author Frank Merriman, z5257800@ad.unsw.edu.au
 */
public class Venue {
    private String venueName;
    private RoomHelper rooms;

    /**
     * Constructor for Venue
     * Initially its RoomHelper has no rooms
     * @param name name of the venue
     */
    public Venue(String name) {
        venueName = name;
        rooms = new RoomHelper();
    }


    /**
     * Evaluates if a given venueName is the same as this.venueName
     * @param name name of venue
     * @return true if name is same, else false
     */
    public boolean compareVenueName(String name) {
        if (venueName.equals(name)) {
            return true;
        }

        return false;
    }

    /**
     * Adds a room to the current venue
     * @param name name of addRoom
     * @param size size of room (one of small, medium, large)
     */
    public void addRoom(String name, String size) {
        rooms.addRoom(name, size);
    }

    /**
     * Checks if a bookingID is unique across all rooms in this venue
     * @param ID unique bookingID of a reservation
     * @return false if ID is not unique across all rooms otherwise true
     */
    public boolean uniqueID(String id) {
        return rooms.uniqueID(id);
    }

    /**
     * Looks through all rooms in the current venue to check if there
     * are enough rooms of the correct size free on the requested
     * dates.
     * @param start first day of the booking
     * @param end last day of the booking
     * @param small number of small rooms requested
     * @param medium number of medium rooms requested
     * @param large number of large rooms requested
     * @return true if venue contains all rooms of requested size, free on requested dates otherwise false
     */
    public boolean avaliableSpace(String ID, LocalDate start, LocalDate end,
    int small, int medium, int large) {
        return rooms.avaliableSpace(ID, start, end, small, medium, large);
    }

    /**
     * Adds bookings to the correct rooms in the current venue.
     * Assumes there is going to be free rooms avaliable to meet booking
     * requirements.
     * @param id unique bookingID of a request
     * @param start start date for room bookings
     * @param end end date for room bookings
     * @param small number of small rooms requested
     * @param medium number of medium rooms requested
     * @param large number of large rooms requested
     * @return JSONObject containing venuename and a list of rooms used to 
     * serve the booking request
     */
    public JSONObject addBooking(String id, LocalDate start, LocalDate end,
    int small, int medium, int large) {
        JSONObject result = new JSONObject();

        JSONArray bookedRooms = rooms.addBooking(id, start, end, small, medium, large);

        result.put("status", "success");
        result.put("venue", venueName);
        result.put("rooms", bookedRooms);

        return result;
    }

    /**
     * Removes record of bookings from RoomHelper
     * @param id unique bookingID of a request
     */
    public void removeBooking(String ID) {
        rooms.removeBooking(ID);
    }

    /**
     * Generates a JSONArray of all room names in a given venue.
     * For each room a list of its bookings, sorted by date, are also
     * created and added.
     * @return JSONArray containg all rooms and their booking data
     * in this venue
     */
    public JSONArray listRooms() {
        return rooms.listRooms();
    }

}