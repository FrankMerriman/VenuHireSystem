package unsw.venues;

import java.time.LocalDate;

import org.json.JSONObject;

/**
 * A booking in the venue hire system.
 * @author Frank Merriman
 */
public class Booking {
    private String bookingID;
    private LocalDate start;
    private LocalDate end;

    /**
     * Constructor for Booking class
     * @param bookingID unique name associated with booking
     * @param start start date for room bookings
     * @param end end date for room bookings
     */
    public Booking (String bookingID, LocalDate start, LocalDate end) {
        this.bookingID = bookingID;
        this.start = start;
        this.end = end;
    }

    /**
     * Compares a given string ID to the bookings own ID
     * The comparison is case sensitive
     * @param ID unique bookingID of a reservation
     * @return true if variable ID is the same as the bookings ID otherwise false
     */
    public boolean compareBookingID(String ID) {
        if (bookingID.equals(ID)) {
            return true;
        }

        return false;
    }

    /**
     * Gets the start date of the booking
     * @return start date of booking
     */
    public LocalDate getStartDate() {
        return start;
    }

    /**
     * Gets the end date of the booking
     * @return end date of booking
     */
    public LocalDate getEndDate() {
        return end;
    }

    /**
     * Creates a JSONObject containing the bookingID,
     * start date and end date of current booking
     * @return JSONObject containting booking details
     */
    public JSONObject bookingDetails() {
        JSONObject details = new JSONObject();

        details.put("id", bookingID);
        details.put("start", start.toString());
        details.put("end", end.toString());

        return details;
    }

}