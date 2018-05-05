package d.osvaldo.tarea2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import d.osvaldo.tarea2.model.Productos;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SharedPreferences pref;
    /**
     * para autenticacion con firebase
     */

    private FirebaseAuth firebaseauth; //componente para manejar la autenticacion
    private FirebaseAuth.AuthStateListener authStateListener; //componente listen

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        inicializar();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.miperfilactivity: //mi perfil
                gotoPerfil();
                return true;
            case R.id.mipruebaActivity:
                gotoprueba();
                return true;
            case R.id.miproductosActivity:
                gotoproductos();
                return true;
            case R.id.logout_forget:
                firebaseauth.signOut();
                Log.d("Manejador", "cosas : "+LoginManager.getInstance());
                if (Auth.GoogleSignInApi != null){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()){
                                gotoLogin();
                            }else{
                                Toast.makeText(MainActivity.this, "Error cerrando Sesion con Google",
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
                removeSharePreferences();
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

    private void gotoPerfil(){
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void gotoprueba() {
        Intent intent = new Intent(this, PruebaActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void gotoproductos() {
        Intent intent = new Intent(this, ProductosActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void removeSharePreferences(){
        //pref.edit().clear().apply();
        //saveOnPreferences();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveOnPreferences(){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("password", "");
            editor.apply();

    }

    private void inicializar(){ //metodo para inicializar los componentes de firebase
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
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseauth.addAuthStateListener(authStateListener);
        Log.d("estado", "onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("estado", "onstop");
        firebaseauth.removeAuthStateListener(authStateListener);
        googleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("estado", "onpause");
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("estado", "destroy");
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("estado", "onresume");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
