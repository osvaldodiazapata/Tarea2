package d.osvaldo.tarea2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Switch spreferences;
    EditText email, password;
    Button btnlogin, btnLoginContraseña;
    String passprevio="";
    private SharedPreferences pref;

    private FirebaseAuth firebaseauth; //componente para manejar la autenticacion
    private FirebaseAuth.AuthStateListener authStateListener; //componente listen

    /**
     para el login con google
     */

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindOn();//refenciamos los objetos del xml



        pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        inicializar();
        setCredentialsExis();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = email.getText().toString();
                String pass = password.getText().toString();

                firebaseauth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            gotoMain();
                        }else{
                            Toast.makeText(LoginActivity.this, "Error al iniciar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                /*String correo = email.getText().toString();
                String pass = password.getText().toString();
                if (TextUtils.equals(pass, passprevio) && !TextUtils.isEmpty(pass)) {
                    if (login(correo, pass)) {
                        gotoMain();
                        saveOnPreferences(correo, pass);
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "olvidaste la contraseña?", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        btnLoginContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRegister();
            }
        });
    }


    private void setCredentialsExis(){
        String preEmail = getUserMailPref();
        String prefPassword = getPasswordPref();
        if(!TextUtils.isEmpty(preEmail) && !TextUtils.isEmpty(prefPassword)){
            email.setText(preEmail);
            passprevio = prefPassword;
        }

    }
    private void inicializar(){ //metodo para inicializar los componentes de firebase
        firebaseauth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.d("firebaseUser", "Ususario LOgeado" + firebaseUser.getEmail());
                }else{
                    Log.d("firebaseUser", "no hay Ususario LOgeado" );
                }
            }
        };
    }
    private String getUserMailPref(){

        return pref.getString("correo", "");
    }

    private String getPasswordPref(){
        return pref.getString("password", "");
    }

    private void bindOn (){
        email =  findViewById(R.id.eCorreo);
        password =  findViewById(R.id.ePassword);
        btnlogin =  findViewById(R.id.bLogin);
        btnLoginContraseña=  findViewById(R.id.bLoginContraseña);
        spreferences =  findViewById(R.id.sPreferences);

    }

    private boolean login(String correo,String password){
        if (!isValidEmail(correo)){
            Toast.makeText(this, "Correo Equivocado", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isValidPassword(password)){
            Toast.makeText(this, "Minimo 5 caracteres en el password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }

    }

    private boolean isValidEmail(String correo){
        return !TextUtils.isEmpty(correo) && Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }

    private boolean isValidPassword(String password){
        return password.length()>4;
    }

    private void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void gotoRegister(){
        Intent intent = new Intent(this, RegistroActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveOnPreferences(String correo, String password){
        if (spreferences.isChecked()){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("correo", correo);
            editor.putString("password", password);
            //editor.commit();//este metodo detiene la ejecucion del programa hasta que todos los datos se guarden en prefeences. se conoce como shared sincrono
            editor.apply();//esta linea permite que el almacenamiento de los datos, se haga por debajo de la ejecucion. conocido como shared asincrono

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}