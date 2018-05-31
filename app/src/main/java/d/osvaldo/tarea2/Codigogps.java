package d.osvaldo.tarea2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Codigogps extends AppCompatActivity implements ZBarScannerView.ResultHandler, GoogleApiClient.OnConnectionFailedListener {
    private final static String TAG="Scannerlog";
    private ZBarScannerView mScannerView;
    private String addressgps;


    //------VARIABLES DEL GPS --------
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE2 = 2;
    LocationManager locationManager;
    double longitudeNetwork, latitudeNetwork;
    private Geocoder geocoder;
    private List<Address> addresses;

    private FirebaseAuth firebaseauth; //componente para manejar la autenticacion
    private FirebaseAuth.AuthStateListener authStateListener; //componente listen

    private GoogleApiClient googleApiClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_forget:
                firebaseauth.signOut();
                Log.d("Manejador", "cosas : "+ LoginManager.getInstance());
                if (Auth.GoogleSignInApi != null){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()){
                                gotoLogin();
                            }else{
                                Toast.makeText(Codigogps.this, "Error cerrando Sesion con Google",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Log.d("CerrarSesion", "Google");
                }
                if (LoginManager.getInstance() != null){
                    Log.d("CerrarSesion", "facebook");
                    LoginManager.getInstance().logOut();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoLogin() {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        startActivity(intentLogin);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        firebaseauth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.d("firebaseUser", "Ususario Logeado" + firebaseUser.getDisplayName());
                    Log.d("firebaseUser", "Ususario Logeado" + firebaseUser.getEmail());
                }else{
                    Log.d("firebaseUser", "no hay Ususario LOgeado" );
                }
            }
        };

    //--------GPS---------------
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



//-----------------------------------------PRMISOS---------------------------------

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE2);

            }
        }


        geocoder = new Geocoder(this, Locale.getDefault());

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 10 * 1000, 10, locationListenerNetwork);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }
//--------------------------------FIN PERMISOS-----------------------------------------

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void toggleNetworkUpdates(View view) {
        if (!checkLocation())
            return;
        Button button = (Button) view;
        if (button.getText().equals(getResources().getString(R.string.pause))) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            locationManager.removeUpdates(locationListenerNetwork);
            button.setText(R.string.mostrar);
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 10 * 1000, 10, locationListenerNetwork);
            button.setText(R.string.pause);
        }
    }


    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();
            try {
                addresses=geocoder.getFromLocation(latitudeNetwork,longitudeNetwork,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final String address= addresses.get(0).getLocality();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("gpssss", address);
                    if (address.equals("Medellín")){
                        addressgps = "Medellin";
                    }else{
                        addressgps = address;
                    }

                    //Toast.makeText(Codigogps.this, "Usted esta en "+ address, Toast.LENGTH_SHORT).show();
                }
            });


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    //-----------------------------------<CODIGO QR>--------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }



    @Override
    public void handleResult(Result result) {
        String resultado = result.getContents().toString();
        //Toast.makeText(this, ""+resultado, Toast.LENGTH_SHORT).show();
    ///////////////////////////////////////////////////////////////////////////////////
        StringTokenizer stringTokenizer = new StringTokenizer(resultado,"#");
        int nDatos = stringTokenizer.countTokens();
        String[] Datos = new String[nDatos];
        int i = 0;
        while(stringTokenizer.hasMoreTokens()){
            String string = stringTokenizer.nextToken();
            Datos[i]=string;
            i++;
        }

        String ubicacion = Datos[0];
        String tienda = Datos[1];
        String mesa = Datos[2];
        Toast.makeText(this, "Ubicacion: " + ubicacion + " TIENDA: " + tienda+ " MESA: " + mesa, Toast.LENGTH_SHORT).show();
    ///////////////////////////////////////////////////////////////////////////////////
        //Log.d("TAGOS",result.getContents());//imprime el valor del codigo - para verlo en el logcat se filtra con V/Scannerlog:
        //Log.d("TAGOS", result.getBarcodeFormat().getName());// imprime el tipo de codigo

        String dirreccionGPS = addressgps;
        if (ubicacion.equals(dirreccionGPS)) {
            Intent intent = new Intent(Codigogps.this, ProductosActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(Codigogps.this, Codigogps.class);
            startActivity(intent);
            Toast.makeText(this, "USTED NO ESTA EN EL LUGAR INDICADO", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //------------------------------------<FIN CODIGO>---------------------------------------------
}
