package org.yifan.hao;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {

    public static Timer startTimer(Runnable runnable, long delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay);
        return timer;
    }

    public static Timer startTimer(Runnable runnable, long delay, long period) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay, period);
        return timer;
    }

    public static Timer startTimer(Runnable runnable, long delay, long period, int cyclesNum) {
        final int[] start = {0};
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ++start[0];
                if (start[0] == cyclesNum) {
                    timer.cancel();
                    return;
                }
                runnable.run();
            }
        }, delay, period);
        return timer;
    }

    public static Timer startTimer(Runnable runnable) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, 0, 500);
        return timer;
    }


}
