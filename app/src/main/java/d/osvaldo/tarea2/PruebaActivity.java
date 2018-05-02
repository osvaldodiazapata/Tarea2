package d.osvaldo.tarea2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Usuarios;

public class PruebaActivity extends AppCompatActivity {

    private EditText eNombre, ePassword, ePrivilegio;
    private ListView listView;
    private ArrayList<Usuarios> listUsuarios;
    String uid;
    //private ArrayAdapter usuarioAdapter;
    //ArrayList<Usuarios> nombrelist;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        eNombre = findViewById(R.id.edNombre);
        ePassword = findViewById(R.id.edPassword);
        ePrivilegio = findViewById(R.id.edPrivilegio);

        listView = findViewById(R.id.listView);

        //nombrelist = new ArrayList<>();
        //usuarioAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        //listView.setAdapter(usuarioAdapter);


        //FirebaseDatabase.getInstance().setPersistenceEnabled(true); //activa la persistencia de datos
        databaseReference = FirebaseDatabase.getInstance().getReference();
        listUsuarios = new ArrayList<Usuarios>();

        final UsuarioAdapter usuarioAdapter = new UsuarioAdapter(this, listUsuarios);
        listView.setAdapter(usuarioAdapter);

        /*listView.setOnItemLongClickListener(
                String uid = listUsuarios.get(position).getId();
                databaseReference.child("usuarios").child(uid).removeValue();
                listUsuarios.remove(position);
                return false;
        );*/
        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsuarios.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Log.d("data: ", snapshot.toString());
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        listUsuarios.add(usuarios);
                    }
                    usuarioAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("load: ", databaseError.getMessage());
            }
        });



    }

    public void onClick(View view) {
        Usuarios usuarioo = new Usuarios(databaseReference.push().getKey(), //comando para capturar el id de los datos
                eNombre.getText().toString(),
                ePassword.getText().toString(),
                ePrivilegio.getText().toString()
        );

        databaseReference.child("usuarios").child(usuarioo.getId()).setValue(usuarioo);
        Log.d("conexion", "exitosa");


    }

    class UsuarioAdapter extends ArrayAdapter<Usuarios>{

        public UsuarioAdapter(@NonNull Context context, ArrayList<Usuarios> data){
            super(context, R.layout.list_item, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_item,  null);

            Usuarios usuario = getItem(position);

            TextView nombre = item.findViewById(R.id.textlistNombre);
            nombre.setText(usuario.getNombre());
            TextView password = item.findViewById(R.id.textlistPassword);
            password.setText(usuario.getPassword());
            TextView privilegio = item.findViewById(R.id.textlistPrivilegio);
            privilegio.setText(usuario.getPrivilegio());

            //return super.getView(position, convertView, parent);
            return item;
        }
    }
}
