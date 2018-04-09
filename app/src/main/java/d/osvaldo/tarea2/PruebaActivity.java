package d.osvaldo.tarea2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import d.osvaldo.tarea2.model.Usuarios;

public class PruebaActivity extends AppCompatActivity {

    private EditText eNombre, eTelefono;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        eNombre = (EditText) findViewById(R.id.edNombre);
        eTelefono = (EditText) findViewById(R.id.edTelefono);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void onClick(View view) {
        Usuarios usuarioo = new Usuarios(databaseReference.push().getKey(),
            eNombre.getText().toString(),
            eTelefono.getText().toString()
        );

        databaseReference.child("usuarios").child(usuarioo.getId()).setValue(usuarioo);



    }
}
