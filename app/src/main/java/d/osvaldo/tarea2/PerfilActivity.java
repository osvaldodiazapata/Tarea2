package d.osvaldo.tarea2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {

    TextView eusuarioperfil, ecorreoperfil;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        eusuarioperfil = (TextView) findViewById(R.id.eUsuarioperfil);
        ecorreoperfil = (TextView) findViewById(R.id.eCorreoperfil);

        pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        setCredentialsExis();
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
            case R.id.Principal:
                gotoPrincipal();
                return true;
            case R.id.CerrarSesion:
                removeSharePreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoPrincipal(){
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
}
