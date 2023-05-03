/**
 *
 */
package unsw.venues;

import java.time.LocalDate;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Robert Clifton-Everest
 * @author Frank Merriman, z5257800@ad.unsw.edu.au
 *
 */
public class VenueHireSystem {
    private VenueHelper venues;

    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
    public VenueHireSystem() {
        venues = new VenueHelper();
    }

    /**
     * Reads a command from some JSONObject and sends parsed data to respective method
     * @param json contains command type and paramters for command to run with
     */
    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = addRequest(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;

        
        case "change":
            id = json.getString("id");
            start = LocalDate.parse(json.getString("start"));
            end = LocalDate.parse(json.getString("end"));
            small = json.getInt("small");
            medium = json.getInt("medium");
            large = json.getInt("large");

            result = changeRequest(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;

        case "cancel":
            id = json.getString("id");

            cancelRequest(id);
            break;

        case "list":
            venue = json.getString("venue");

            JSONArray list = listRooms(venue);

            System.out.println(list.toString(2));
            break;
        }
    }

    /**
     * Adds a room of a given name to a specific venue.
     * A room can be of size: small, medium, large.
     * @param venue name of the venue room is to be added (Case sensitive)
     * @param room name of the room being added (Case sensitive)
     * @param size size of the room being added
     */
    private void addRoom(String venue, String room, String size) {
        venues.addRoom(venue, room, size);
    }

    /**
     * Attempts to process a request to book some rooms for given dates.
     * A request cannot overlap with any already existing request, all rooms
     * requested must be found in a singular venue and a request is denied if it cannot
     * be completed to its entirety.
     * @param id unique bookingID of a request
     * @param start start date for room bookings
     * @param end end date for room bookings
     * @param small number of small rooms requested
     * @param medium number of medium rooms requested
     * @param large number of large rooms requested
     * @return On success: {"venue": venueName, "rooms": [roomNames], "status": "success"}
     * On failure: {"status": "rejected}
     */
    public JSONObject addRequest(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
        return venues.addBooking(id, start, end, small, medium, large);
    }

    /**
     * Attempts to process a request to change an existing booking.
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
    public JSONObject changeRequest(String id, LocalDate start, LocalDate end,
    int small, int medium, int large) {
        return venues.changeBooking(id, start, end, small, medium, large);
    }

    /**
     * Cancels an existing booking request by removing records of
     * any bookings with bookingID matching the given parameter from all rooms
     * across all venues
     * @param id unique bookingID of a request
     */
    private void cancelRequest(String id) {
        venues.removeBooking(id);
    }

    /**
     * Generates a JSONArray of all room names in a given venue.
     * For each room a list of its bookings, sorted by date, are also
     * created and added.
     * @param venue name of the venue
     * @return JSONArray containing all rooms in a given venue and their data
     */
    private JSONArray listRooms(String venue) {
        return venues.listRooms(venue);
    }



    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
