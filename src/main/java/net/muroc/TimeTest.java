package net.muroc;

import org.joda.time.DateTime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Brendan on 4/22/2018.
 */
public class TimeTest {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void PrintCurrentTime()
    {
        final Runnable raspberripiInterogator = new Runnable()
        {
            @Override
            public void run() {
                System.out.println(DateTime.now().toString());

            }
        };
        final ScheduledFuture<?> i2 = scheduler.scheduleWithFixedDelay(raspberripiInterogator,0,5,SECONDS);
    }
}
