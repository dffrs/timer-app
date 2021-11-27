package com.dffrs.comp.time.modifier;

import javax.swing.JLabel;

import com.dffrs.comp.time.Time;

/**
 * Class that modifies the Time and represent it on the GUI. To do this, it sets a new JLabel's Text every time it
 * modifies a Time's instance. After that, it sleeps for 1s.
 *
 * @author dffrs-iscteiulpt.
 */
public class TimeModifier extends Thread {

    private static final long MAX_TIME_SPENT_SLEEPING = 1000;
    private static final int MAX_TIME_ADDED = 1;
    private final Time time;
    private final JLabel label;
    // First solution
    private boolean isPaused;

    public TimeModifier(Time time, JLabel label) {
        super();
        if (time == null) {
            throw new NullPointerException("Error: Creating a TimeModifier instance. Arguments is a NULL Reference.\n");
        }
        this.time = time;
        this.label = label;
        this.isPaused = false;
    }

    public Time getTime() {
        return time;
    }

    // Getter
    public synchronized boolean isPaused() {
        return isPaused;
    }

    // Setter
    public synchronized void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    @Override
    public void run() {
        boolean interrupted = false;

        toHere:
        while (!interrupted) {

            while (isPaused) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        isPaused = false;
                        interrupted = true;
                        break toHere;
                    }
                }
            }

            // Increment time.
            this.time.incrementTime(MAX_TIME_ADDED);

            //Put it on the Label.
            this.label.setText(this.time.toString());

            //Sleep
            try {
                sleep(MAX_TIME_SPENT_SLEEPING);
            } catch (InterruptedException e) {
                interrupted = true;
                break;
            }
        }
        if (interrupted) {
            System.out.println(Thread.currentThread().getName() + " was interrupted. Shutting down...\n");
            Thread.currentThread().interrupt();
        }
    }
}
