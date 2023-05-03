package unsw.venues;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * A venue helper for the hire system. It keeps track of all venues in the system.
 * @author Frank Merriman, z5257800@ad.unsw.edu.au
 */
public class VenueHelper {
    private List<Venue> venues;

    /**
     * Constructor for VenueHelper
     * Originally it contains no venues
     */
    public VenueHelper() {
        venues = new ArrayList<Venue>();
    }

    /**
     * Attempts to add a room to a given venue. If venue doesn't
     * yet exist it is created, the room then added.
     * @param venue name of the venue destination
     * @param room name of the room to be created
     * @param size size of the room to be created
     */
    public void addRoom(String venue, String room, String size) {
        Iterator<Venue> venueItr = venues.iterator();
        boolean exists = false;

        while (venueItr.hasNext() && !exists) {
            Venue curr = venueItr.next();

            if (curr.compareVenueName(venue)) {
                curr.addRoom(room, size);
                exists = true;
            }
        }

        if (!exists) {
            Venue newVenue = addVenue(venue);
            newVenue.addRoom(room, size);
        }
        
    }

    /**
     * Creates and appends a Venue to VenueHelpers list
     * of venues.
     * @param venueName name of the new venue
     * @return the newly created Venue
     */
    public Venue addVenue(String venueName) {
        venues.add(new Venue(venueName));
        return venues.get(venues.size() - 1);
    }

    /**
     * Attempts to fulfill a request to book some rooms for some given dates.
     * First checks that the booking ID is not a duplicate of any other in the system
     * as they MUST be unique. It then checks that there is a venue with avaliable space
     * before adding booking data to that venues rooms. If no venues have avaliable space
     * or the ID is duplicate then this method fails.
     * @param id unique bookingID of a request
     * @param start start date for room bookings
     * @param end end date for room bookings
     * @param small number of small rooms requested
     * @param medium number of medium rooms requested
     * @param large number of large rooms requested
     * @return On success: {"venue": venueName, "rooms": [roomNames], "status": "success"}
     * On failure: {"status": "rejected}
     */
    public JSONObject addBooking(String id, LocalDate start, LocalDate end,
    int small, int medium, int large) {
        JSONObject result = new JSONObject();

        //check ID is original across entire system
        for (Venue v : venues) {
            if (v.uniqueID(id) == false) {
                result.put("status", "rejected");
                return result;
            }
        }

        //Check there is enough rooms avaliable in a given venue
        for (Venue v : venues) {
            if (v.avaliableSpace(id, start, end, small, medium, large)) {
                return v.addBooking(id, start, end, small, medium, large);
            }
        }

        //Case runs if no venues able to process request
        result.put("status", "rejected");
        return result;
    }

    /**
     * Attempts to fulfill a request to change an existing booking.
     * Changes can include number of rooms and the dates they are wanted for. 
     * The new request cannot overlap with any existing requests EXCEPT for itself,
     * if the change cannot be processed then the old bookings remain and no change is made
     * @param id unique bookingID of a request
     * @param start start date for room bookings
     * @param end end date for room bookings
     * @param small number of small rooms requested
     * @param medium number of medium rooms requested
     * @param large number of large rooms requested
     * @return On success: {"venue": venueName, "rooms": [roomNames], "status": "success"}
     * On failure: {"status": "rejected}
     */
    public JSONObject changeBooking(String id, LocalDate start, LocalDate end,
    int small, int medium, int large) {
        JSONObject result = new JSONObject();

        
        for (Venue v : venues) {
            if (v.avaliableSpace(id, start, end, small, medium, large)) {
                removeBooking(id);
                return v.addBooking(id, start, end, small, medium, large);
            }
        }


        //Case runs if no venues able to process request
        result.put("status", "rejected");
        return result;
    }

    /**
     * Looks through all Venues in the system and removes any record
     * of a booking with id matching param id
     * @param id unique bookingID of a request
     */
    public void removeBooking(String id) {
        for (Venue v : venues) {
            v.removeBooking(id);
        }
    }

    /**
     * Generates a JSONArray of all room names in a given venue.
     * For each room a list of its bookings, sorted by date, are also
     * created and added.
     * @param venue name of the venue
     * @return JSONArray containing all rooms in a given venue and their data
     */
    public JSONArray listRooms(String venue) {
        JSONArray venueRoomsDetails = new JSONArray();

        for (Venue v : venues) {
            if (v.compareVenueName(venue)) { //should only trigger once
                return v.listRooms();
            }
        }

        return venueRoomsDetails;
    }
}