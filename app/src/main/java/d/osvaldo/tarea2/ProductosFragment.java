package d.osvaldo.tarea2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Productos;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btcomprar;
    private RecyclerView.Adapter adapterProductos;
    private RecyclerView.LayoutManager layoutManager;


    private ArrayList<Productos> productoslist;

    //private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ProductosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_productos, container, false);

        productoslist = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        adapterProductos = new AdapterProductos(productoslist, R.layout.cardview_detalle, getActivity());


        recyclerView = itemView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterProductos);

        //firebaseDatabase.getInstance().setPersistenceEnabled(true); //activa la persistencia de datos
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        databaseReference.child("inventario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productoslist.clear();
                if (dataSnapshot.exists()){
                    //Log.d("data: ", "tenemos informacion");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Log.d("datossssss ", "tenemos"+ snapshot.getValue());
                        Productos productos = snapshot.getValue(Productos.class);
                        productoslist.add(productos);
                        //Log.d("lista ", "tenemos " + productoslist);
                    }
                    adapterProductos.notifyDataSetChanged();
                    Log.d("notificacion", "realizada");
                }else{
                    Log.d("data: ", "no tenemos datos");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btcomprar = itemView.findViewById(R.id.btnComprar);
        btcomprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i =0;i<productoslist.size();i++) {
                    String nombre = productoslist.get(i).getNombre();
                    String id = productoslist.get(i).getPrecio();
                    String cantidad = productoslist.get(i).getCantidad();
                    Toast.makeText(getActivity(), " " + nombre + id + cantidad, Toast.LENGTH_SHORT).show();



                    /*
                    if(nombre.equals("0")){
                        Toast.makeText(getActivity(), "cero" , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "no tenemos milo ", Toast.LENGTH_SHORT).show();
                    }
*/

                }

            }
        });

        return itemView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
