package d.osvaldo.tarea2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import d.osvaldo.tarea2.model.Usuarios;

public class PerfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView eusuarioperfil, ecorreoperfil;

    private SharedPreferences pref;
    private FirebaseAuth firebaseauth; //componente para manejar la autenticacion
    private FirebaseAuth.AuthStateListener authStateListener; //componente listen
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        eusuarioperfil = (TextView) findViewById(R.id.eUsuarioperfil);
        ecorreoperfil = (TextView) findViewById(R.id.eCorreoperfil);

        pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        setCredentialsExis();
        inicializar();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        database = FirebaseDatabase.getInstance();
        databaseReference =database.getReference("usuarios");

        Toast.makeText(PerfilActivity.this, firebaseauth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        databaseReference.child(firebaseauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Usuarios usuario = dataSnapshot.getValue(Usuarios.class);

                    String nombre = usuario.getNombre();
                    Toast.makeText(PerfilActivity.this, nombre, Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setCredentialsExis(){
        String preEmail = getUserMailPref();
        String prefPassword = getPasswordPref();
        if(!TextUtils.isEmpty(preEmail) && !TextUtils.isEmpty(prefPassword)){
            ecorreoperfil.setText(preEmail);
            eusuarioperfil.setText(prefPassword);
        }

    }
    private String getUserMailPref(){

        return pref.getString("correo", "");
    }

    private String getPasswordPref(){

        return pref.getString("nombre", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuperfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.NuestraApp:
                gotoMain();
                return true;
            case R.id.miproduActivity:
                gotoproductos();
                return true;
            case R.id.CerrarSesion:
                firebaseauth.signOut();
                if (Auth.GoogleSignInApi != null){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()){
                                gotoLogin();
                            }else{
                                Toast.makeText(PerfilActivity.this, "Error cerrando Sesion con Google",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if (LoginManager.getInstance() != null){
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

    private void gotoproductos() {
        Intent intent = new Intent(this, ProductosActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
    private void removeSharePreferences(){
        //pref.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseauth.removeAuthStateListener(authStateListener);
        googleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
