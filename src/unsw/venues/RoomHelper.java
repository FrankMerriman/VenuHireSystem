package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

/**
 * A RoomHelper associated with a given Venue
 * @author Frank Merriman, z5257800@ad.unsw.edu.au
 */
public class RoomHelper {
    private List<Room> venueRooms;

    /**
     * Constructor for RoomHelper
     * Initially the list of rooms is empty
     */
    public RoomHelper() {
        venueRooms = new ArrayList<Room>();
    }
    
    /**
     * Checks if roomName is already in use. No name duplicates
     * are allowed in a venue regardless of size difference. If no
     * difference is found, calls the Room class constructor and appends
     * result to venueRooms
     * @param roomName name of the new room being created
     * @param size size of the new room being created
     */
    public void addRoom(String roomName, String size) {
        boolean nameAvaliable = true;

        for (Room r : venueRooms) {
            if (r.compareRoomName(roomName)) {
                nameAvaliable = false;
            }
        }
        
        if (nameAvaliable) {
            venueRooms.add(new Room(roomName, size));
        }
    }

    /**
     * Finds rooms free that match booking request and adds their names to the list
     * of rooms selected to satisfy the booking request
     * @param ID unique booking ID associated with booking, case sensitive
     * @param start LocalDate for beginning of booking window
     * @param end LocalDate for ending of booking window
     * @param small number of small rooms in booking request
     * @param medium number of medium rooms in booking request
     * @param large number of large rooms in booking request
     * @return JSONArray contating the names of all rooms used to fulfill request
     * sorted in order they were added to venue
     */
    public JSONArray addBooking(
        String ID, LocalDate start, LocalDate end,
        int small, int medium, int large
    ) {
        JSONArray bookedRooms = new JSONArray();
        int smallCount = 0;
        int mediumCount = 0;
        int largeCount = 0;

        for (Room r : venueRooms) {
            switch(r.getSize()) {
                case "small":
                    if (smallCount < small && r.avaliableSpace(ID, start, end)) {
                        r.addBooking(ID, start, end);
                        bookedRooms.put(r.getName());
                        smallCount = smallCount + 1;
                    }
                    break;
                
                case "medium":
                    if (mediumCount < medium && r.avaliableSpace(ID, start, end)) {
                        r.addBooking(ID, start, end);
                        bookedRooms.put(r.getName());
                        mediumCount = mediumCount + 1;
                    }
                    break;
                
                case "large":
                    if (largeCount < large && r.avaliableSpace(ID, start, end)) {
                        r.addBooking(ID, start, end);
                        bookedRooms.put(r.getName());
                        largeCount = largeCount + 1;
                    }
                    break;
                
            }
            
        }

        
        return bookedRooms;
    }

    /**
     * Removes all bookings from rooms in venueRooms with an ID matching the given ID
     * @param ID unique booking ID of booking to be cancelled/removed 
     */
    public void removeBooking(String ID) {
        for (Room r : venueRooms) {
            r.removeBooking(ID);
        }
    }

    /**
     * Checks if a given bookingID exists yet across all rooms in the current venue
     * @param ID unique bookingID of a reservation
     * @return false if ID is not unique across all rooms otherwise true
     */
    public boolean uniqueID(String ID) {
        boolean unique = true;
        for (Room r : venueRooms) {
            if (r.uniqueID(ID) == false) {
                unique = false;
            }
        }

        return unique;
    }

    /**
     * Looks through all rooms in the current venues RoomHelper to check if there
     * are enough rooms of the correct size free on the requested
     * dates.
     * @param ID unique bookingID of a reservation
     * @param start first day of the booking
     * @param end last day of the booking
     * @param small number of small rooms requested
     * @param medium number of medium rooms requested
     * @param large number of large rooms requested
     * @return true if venue contains all rooms of requested size, free on requested dates otherwise false
     */
    public boolean avaliableSpace(String ID, LocalDate start, LocalDate end,
    int small, int medium, int large) {
        List<Room> smallRooms = new ArrayList<Room>();
        List<Room> mediumRooms = new ArrayList<Room>();
        List<Room> largeRooms = new ArrayList<Room>();

        for (Room r : venueRooms) {
            switch(r.getSize()) {

                case "small":
                if (smallRooms.size() < small) {
                    if (r.avaliableSpace(ID, start, end)) {
                        smallRooms.add(r);
                    }
                }
                break;

                case "medium":
                if (mediumRooms.size() < medium) {
                    if (r.avaliableSpace(ID, start, end)) {
                        mediumRooms.add(r);
                    }
                }
                break;

                case "large":
                if (largeRooms.size() < large) {
                    if (r.avaliableSpace(ID, start, end)) {
                        largeRooms.add(r);
                    }
                }
                break;
            }
        }

        if (
            smallRooms.size() == small &&
            mediumRooms.size() == medium &&
            largeRooms.size() == large
        ) {
            return true;
        }

        return false;
    }

    /**
     * Generates a JSONArray of all room names in a given venue.
     * For each room a list of its bookings, sorted by date, are also
     * created and added.
     * @return JSONObject containing a list of a venues rooms and their associated info
     */
    public JSONArray listRooms() {
        JSONArray list = new JSONArray();

        for (Room r : venueRooms) {
            list.put(r.listRoomDetails());
        }

        return list;
    }
}