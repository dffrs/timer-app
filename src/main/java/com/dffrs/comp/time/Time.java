package com.dffrs.comp.time;

/**
 * Class to represent the time show in the GUI. It's stored as seconds,
 * but easily manipulated and converted to any popular format.
 *
 * @author dffrs-iscteiulpt.
 */
public final class Time {

    private int seconds;

    public Time(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Error: Creating a Time instance. Can't be a negative number.\n");
        }
        this.seconds = seconds;
    }

    private static String convertToStandardTime(int seconds) {
        int hours = (seconds / 3600);
        int minutes = ((seconds % 3600) / 60);
        int second = (seconds % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, second);
    }

    public int getHour() {
        return Integer.parseInt(convertToStandardTime(this.seconds).split(":")[0]);
    }

    public int getMinutes() {
        return Integer.parseInt(convertToStandardTime(this.seconds).split(":")[1]);
    }

    public int getSeconds() {
        return Integer.parseInt(convertToStandardTime(this.seconds).split(":")[2]);
    }

    public synchronized void incrementTime(int amount) {
        this.seconds += amount;
    }

    @Override
    public String toString() {
        return convertToStandardTime(this.seconds);
    }
}
