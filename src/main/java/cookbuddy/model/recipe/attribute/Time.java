package cookbuddy.model.recipe.attribute;

import static cookbuddy.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;


/**
 * Represents a Recipe's time in the recipe book.
 * Guarantees: immutable; is valid as declared in {@link #isValidHour(int)}, (@Link #isValidMin(int)},
 * {@Link #isValidSec(int)}
 */
public class Time {

    public static final String MESSAGE_CONSTRAINTS_HOUR = "The recipe should be between 0 and 72 hours long, inclusive";
    public static final String MESSAGE_CONSTRAINTS_MIN = "Mins should be < 60";
    public static final String MESSAGE_CONSTRAINTS_SEC = "Secs should be < 60";



    private int hour;
    private int min;
    private int sec;

    /**
     * Constructs a {@code Time}.
     *
     * @param hour A valid hour.
     * @param min A valid minute.
     * @param sec A valid second.
     */
    public Time(int hour, int min, int sec) {
        requireNonNull(hour);
        requireNonNull(min);
        requireNonNull(sec);
        checkArgument(isValidHour(hour), MESSAGE_CONSTRAINTS_HOUR);
        checkArgument(isValidMin(min), MESSAGE_CONSTRAINTS_MIN);
        checkArgument(isValidSec(sec), MESSAGE_CONSTRAINTS_SEC);
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    /**
     * Returns true if a given string is a valid name.
     */

    public static boolean isValidHour(int test) {
        return (test >= 0 && test <= 72);
    }

    public static boolean isValidMin(int test) {
        return (test >= 0 && test < 60);
    }

    public static boolean isValidSec(int test) {
        return (test >= 0 && test < 60);
    }

    public void setTime(Time preptime) {
        hour = preptime.getHour();
        min = preptime.getMin();
        sec = preptime.getSec();
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    @Override
    public String toString() {
        String toReturn = "";
        if ((hour + min + sec) == 0) {
            toReturn += "-";
        } else {
            if (hour < 10) {
                toReturn += "0";
            }
            toReturn += String.valueOf(hour);
            toReturn += ":";
            if (min < 10) {
                toReturn += "0";
            }
            toReturn += String.valueOf(min);
            toReturn += ":";
            if (sec < 10) {
                toReturn += "0";
            }
            toReturn += String.valueOf(sec);
        }
        return toReturn;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Time // instanceof handles nulls
                    && hour == (((Time) other).hour))
                    && min == (((Time) other).min)
                    && sec == (((Time) other).sec); // state check
    }

    @Override
    public int hashCode() {
        return String.valueOf(hour + min + sec).hashCode();
    }

}
