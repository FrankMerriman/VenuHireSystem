package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import java.util.Iterator;

/**
 * A bookingHelper associated with a Room
 * @author Frank Merriman
 */
public class BookingHelper {
    private List<Booking> bookings;

    /**
     * Constructor for BookingHelper
     * Initially bookings list is empty
     */
    public BookingHelper() {
        bookings = new ArrayList<Booking>();
    }


    /**
     * Insert a new Booking into BookingHelpers list of bookings.
     * Booking is inserted into an ordered list, which is sorted by order
     * of start date
     * @param ID unique bookingID of a reservation
     * @param start start date of booking
     * @param end end date of booking
     */
    public void addBooking(String ID, LocalDate start, LocalDate end) {
        Booking newBooking = new Booking(ID, start, end);
        Iterator<Booking> bookingItr = bookings.listIterator();

        //Empty List case
        if (bookings.size() == 0) {
            bookings.add(newBooking);
            return;
        }

        Booking curr = bookingItr.next();
        LocalDate currStart = curr.getStartDate();
        LocalDate currEnd = curr.getEndDate();
        int indexCurr = bookings.indexOf(curr);

        //case for only 1 item in list
        if (bookings.size() == 1) {
            if (end.isBefore(currStart)) {
                bookings.add(0, newBooking);
                return;
            } else if (start.isAfter(currEnd)) {
                bookings.add(newBooking);
                return;
            }
        }

        while (bookingItr.hasNext()) {
            Booking next = bookingItr.next();
            LocalDate nextStart = next.getStartDate();
            
            int indexNext = bookings.indexOf(next);

            if (indexCurr == 0) {
                if (end.isBefore(currStart)) {
                    bookings.add(0, newBooking);
                    return;
                }
            }
            
            if (indexCurr == (bookings.size() - 1)) {
                if (start.isAfter(currEnd)) {
                    bookings.add(newBooking);
                    return;
                }
            }

            //inserts between curr and next
            if (start.isAfter(currEnd) && end.isBefore(nextStart)) {
                bookings.add(indexNext, newBooking);
                return;
            }

            curr = next;
            currStart = nextStart;
            currEnd = next.getEndDate();
            indexCurr = bookings.indexOf(curr);
        }
        return;
    }

    /**
     * Remove all bookings with matching ID from booking helpers room
     * @param ID unique bookingID of a reservation
     */
    public void removeBooking(String ID){
        Iterator<Booking> bookingItr = bookings.iterator();

        while (bookingItr.hasNext()) {
            Booking curr = bookingItr.next();

            if (curr.compareBookingID(ID)) {
                bookingItr.remove();
                return;
            }
        }
    }

    /**
     * Checks if ID is unique amongst all bookingID's in bookings
     * @param ID unique bookingID of a reservation
     * @return false if any bookingID in bookings matches ID, else true
     */
    public boolean uniqueID(String ID) {
        for (Booking b : bookings) {
            if (b.compareBookingID(ID)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if there would be free time avaliable to make a new booking 
     * with requested start and end dates
     * Assumes dates are always correctly entered with start occuring before end
     * @param ID unique ID associated with a new booking
     * @param start
     * @param end
     * @return true if the requested period of time has no bookings already occuring, otherwise false
     */
    public boolean avaliableSpace(String ID, LocalDate start, LocalDate end) {
        List<Booking> bookings = removeBookingID(ID);
        Iterator<Booking> bookingItr = bookings.listIterator();
        
        if (bookings.size() == 0) {
            return true;
        }

        Booking curr = bookingItr.next();
        LocalDate currStart = curr.getStartDate();
        LocalDate currEnd = curr.getEndDate();
        int index = bookings.indexOf(curr);

        //case for only 1 item in list
        if (bookings.size() == 1) {
            if (end.isBefore(currStart) || start.isAfter(currEnd)) {
                return true;
            }
        }

        while (bookingItr.hasNext()) {
            Booking next = bookingItr.next();
            LocalDate nextStart = next.getStartDate();

            if (index == 0) {
                if (end.isBefore(currStart)) {
                    return true;
                }
            }
            
            if (index == (bookings.size() - 1)) {
                if (start.isAfter(currEnd)) {
                    return true;
                }
            }

            if (start.isAfter(currEnd) && end.isBefore(nextStart)) {
                return true;
            }

            curr = next;
            currStart = nextStart;
            currEnd = next.getEndDate();
            index = bookings.indexOf(curr);
        }

        return false;
    }

    /**
     * Makes a new list of all bookings not containing a given ID
     * @param ID unique booking ID
     * @return list of bookings associated with this room that don't have ID
     */
    public List<Booking> removeBookingID(String ID) {
        List<Booking> reducedList = new ArrayList<Booking>();

        for (Booking b : bookings) {
            if (!b.compareBookingID(ID)) {
                reducedList.add(b);
            }
        }

        return reducedList;
    }

    /**
     * Create a JSONArray containing the BookingHelpers bookings details
     * bookings are sorted by date
     * @return JSONArray containing booking details
     */
    public JSONArray listBookingDetails() {
        JSONArray bookingDetails = new JSONArray();
        
        for (Booking b : bookings) {
            bookingDetails.put(b.bookingDetails());
        }

        return bookingDetails;
    }

}