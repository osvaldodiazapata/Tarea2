package d.osvaldo.tarea2;

import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UbicacionActivity extends AppCompatActivity {

    private Geocoder geocoder;
    private List<android.location.Address> addresses;
    double Latitud, Longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        Latitud = 38.95;
        Longitud = -5.9667;
        geocoder = new Geocoder(this, Locale.getDefault());
        direccionamiento(Latitud, Longitud);
    }

    private void direccionamiento(double latitud, double longitud) {
        try {
            addresses = geocoder.getFromLocation(latitud, longitud, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getLocality();
        Toast.makeText(this, "Usted esta en: "+ address, Toast.LENGTH_SHORT).show();

    }
}
