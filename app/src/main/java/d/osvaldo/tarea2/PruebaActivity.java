package d.osvaldo.tarea2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Usuarios;

public class PruebaActivity extends AppCompatActivity {

    private EditText eNombre, eTelefono;
    private ListView listView;
    private ArrayAdapter usuarioAdapter;
    ArrayList<Usuarios> nombrelist;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        eNombre = findViewById(R.id.edNombre);
        eTelefono = findViewById(R.id.edTelefono);
        listView = findViewById(R.id.listView);

        nombrelist = new ArrayList<>();
        usuarioAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(usuarioAdapter);


        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //activa la persistencia de datos
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombrelist.clear();
                if (dataSnapshot.exists()){
                    Toast.makeText(PruebaActivity.this, "pilas aqui" +
                            "falta rellenar la lista", Toast.LENGTH_SHORT).show();
                    /*for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);

                    }*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombreList.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        nombreList.add(usuarios.getNombre());
                    }
                }
                usuarioAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String uid = nombreList.get(i).getId();
                nombreList.remove(i);
                nom
                return false;
            }
        });*/
    }

    public void onClick(View view) {
        Usuarios usuarioo = new Usuarios(databaseReference.push().getKey(), //comando para capturar el id de los datos
            eNombre.getText().toString(),
            eTelefono.getText().toString()
        );

        databaseReference.child("usuarios").child(usuarioo.getId()).setValue(usuarioo);
        Log.d("conexion", "exitosa");


    }
}
