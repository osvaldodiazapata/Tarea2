package d.osvaldo.tarea2;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by osval on 19/03/18.
 */

public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
