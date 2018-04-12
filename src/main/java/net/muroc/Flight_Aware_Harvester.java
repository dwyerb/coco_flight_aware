package net.muroc;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Brendan on 4/11/2018.
 */
public class Flight_Aware_Harvester {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void GetAircraftData(SessionFactory sessionFactory)
    {
        final Runnable raspberripiInterogator = new Runnable()
        {
            @Override
            public void run() {
                //url to flight aware raspberri pi json output
                String url = "http://192.168.0.99:8080/data/aircraft.json";
                InputStream source = retrieveStream(url);

                Gson gson = new Gson();
                Reader reader = new InputStreamReader(source);
                Flight_Aware_Messages messages = gson.fromJson(reader, Flight_Aware_Messages.class);
                List<FlightAwarePointType> AntennaReturn = messages.aircraft;
                AntennaReturn.forEach((FlightAwarePointType FlightAwarePointType) ->{System.out.println(FlightAwarePointType.getFlight());});
                PostGRES_DataWriter pgDataWriter = new PostGRES_DataWriter();
                pgDataWriter.write_data_to_database(AntennaReturn,sessionFactory);
            }
        };
        final ScheduledFuture<?> i2 = scheduler.scheduleWithFixedDelay(raspberripiInterogator,0,5,SECONDS);
    }


    //method to get content back from url
    private static InputStream retrieveStream(String url)
    {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                //Log.w(getClass().getSimpleName(),
                //       "Error " + statusCode + " for URL " + url);
                return null;
            }
            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();
        }
        catch (IOException e) {
            getRequest.abort();
            //Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
    }
}
