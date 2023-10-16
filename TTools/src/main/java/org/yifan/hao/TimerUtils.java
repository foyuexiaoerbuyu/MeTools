package org.yifan.hao;

import org.yifan.hao.swing.JswCustomWight;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {
    public static Timer startTimer(JswCustomWight.IDragCallBack iDragCallBack, long delay) {
        return startTimer(iDragCallBack, delay, 0, false);
    }
    public static Timer startTimer(JswCustomWight.IDragCallBack iDragCallBack, long delay, long period) {
        return startTimer(iDragCallBack, delay, period, false);
    }

    public static Timer startTimer(final JswCustomWight.IDragCallBack iDragCallBack, long delay, long period, final boolean isStop) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isStop) {
                    timer.cancel();
                    return;
                }
                iDragCallBack.dragCallBack("");
            }
        }, delay, period);
        return timer;
    }

    public static Timer startTimer(final JswCustomWight.IDragCallBack iDragCallBack) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                iDragCallBack.dragCallBack("");
            }
        }, 500, 0);
        return timer;
    }


}
