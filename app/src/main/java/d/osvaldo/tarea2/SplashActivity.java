package d.osvaldo.tarea2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //firebaseDatabase.getInstance().setPersistenceEnabled(true); //activa la persistencia de datos
        pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentMain = new Intent(this, MainActivity.class);
        Intent intentProductos = new Intent(this, ProductosActivity.class);

        /*if(!TextUtils.isEmpty(getPasswordPref()) && !TextUtils.isEmpty(getUserMailPref())){
            startActivity(intentMain);
        }else{

        startActivity(intentLogin);
        }*/
        startActivity(intentLogin);
        finish();
    }
    private String getUserMailPref(){return pref.getString("correo", "");}

    private String getPasswordPref(){
        return pref.getString("password", "");
    }

}
