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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    EditText ercontrseña, econtraseña, ecorreo, enombre;
    Button bregistro;
    private SharedPreferences pref;
    private FirebaseAuth firebaseauth; //componente para manejar la autenticacion
    private FirebaseAuth.AuthStateListener authStateListener; //componente listener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        onbind();
        pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        inicializar();
        bregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = ecorreo.getText().toString();
                String pass = econtraseña.getText().toString();
                String rpass = ercontrseña.getText().toString();
                String nombre = enombre.getText().toString();
                if(login(correo, pass)) {
                    if (TextUtils.equals(pass, rpass)) {
                        //gotoLogin();
                        //saveOnPreferences(correo, pass, nombre);
                        firebaseauth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(RegistroActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistroActivity.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                                            gotoLogin();
                                        }else{
                                            Toast.makeText(RegistroActivity.this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(RegistroActivity.this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
    private void onbind(){
        ercontrseña = (EditText) findViewById(R.id.eRContraseña);
        econtraseña = (EditText) findViewById(R.id.eContraseña);
        ecorreo = (EditText) findViewById(R.id.eCorreo);
        enombre = (EditText) findViewById(R.id.eNombre);
        bregistro = (Button) findViewById(R.id.bRegistro);
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


    private void saveOnPreferences(String correo, String password, String nombre){
       // if (spreferences.isChecked()){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("correo", correo);
            editor.putString("password", password);
            editor.putString("nombre", nombre);
            //editor.commit();//este metodo detiene la ejecucion del programa hasta que todos los datos se guarden en prefeences. se conoce como shared sincrono
            editor.apply();//esta linea permite que el almacenamiento de los datos, se haga por debajo de la ejecucion. conocido como shared asincrono

        //}

    }
    private void gotoLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
