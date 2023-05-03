package unsw.venues;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A room in the venue hire system. Each corresponds to a specific venue
 * @author Frank Merriman
 */
public class Room {
    private String roomName;
    private String roomSize;
    private BookingHelper bookings;

    /**
     * Constructor for Room
     * Initialy BookingHelper contains no bookings
     * @param name name of the room
     * @param size size of the room
     */
    public Room(String name, String size) {
        roomName = name;
        roomSize = size;
        bookings = new BookingHelper();
    }

    /**
     * Get the size of the room
     * size is a string equal to small, medium, large
     * @return size of room 
     */
    public String getSize() {
        return roomSize;
    }

    /**
     * Get the name of the room
     * name is a string that is case sensitive
     * @return name of room
     */
    public String getName() {
        return roomName;
    }

    /**
     * Compare the name of a new room to the current room
     * @param name name of new room
     * @return true if name is the same, else false
     */
    public boolean compareRoomName(String name) {
        if (roomName.equals(name)) {
            return true;
        }

        return false;
    } 

    /**
     * Add a Booking to current room's bookings using given data
     * @param ID unique ID of a booking request
     * @param start start of requested dates
     * @param end end of requested dates
     */
    public void addBooking(String ID, LocalDate start, LocalDate end) {
        bookings.addBooking(ID, start, end);
    }

    /**
     * Passes ID down to BookingHelper remove method
     * @param ID unique ID of a booking request
     */
    public void removeBooking(String ID){
        bookings.removeBooking(ID);
    }

    /**
     * Passes ID down to BookingHelper unique method
     * @param ID unique bookingID of a reservation
     * @return false if ID is not unique across all bookings otherwise true
     */
    public boolean uniqueID(String ID) {
        return bookings.uniqueID(ID);
    }

    /**
     * Checks if roomhelper bookings have space for new booking with
     * given params
     * @param start first day of the booking
     * @param end last day of the booking
     * @return true if room has no bookings overlapping with request, otherwise false
     */
    public boolean avaliableSpace(String ID, LocalDate start, LocalDate end) {
        return bookings.avaliableSpace(ID, start, end);
    }

    /**
     * Creates a JSONObject containing a rooms name and a list
     * of all its bookings sorted by date
     * @return JSONObject containing roomname and all booking data
     */
    public JSONObject listRoomDetails() {
        JSONObject info = new JSONObject();

        JSONArray bookingData = bookings.listBookingDetails();

        info.put("room", roomName);
        info.put("reservations", bookingData);
        return info;
    }


}