package d.osvaldo.tarea2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private RecyclerView.Adapter adapterProductos;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Productos> productoslist;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ProductosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //activa la persistencia de datos
        databaseReference = FirebaseDatabase.getInstance().getReference();
        View itemView = inflater.inflate(R.layout.fragment_productos, container, false);
        recyclerView = itemView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        productoslist = new ArrayList<>();
        adapterProductos = new AdapterProductos(productoslist, R.layout.cardview_detalle, getActivity());
        recyclerView.setAdapter(adapterProductos);

        databaseReference.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productoslist.clear();
                if (dataSnapshot.exists()){
                    Log.d("data: ", "tenemosinfo");
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Productos productos = snapshot.getValue(Productos.class);
                        productoslist.add(productos);
                    }
                    adapterProductos.notifyDataSetChanged();
                }else{
                    Log.d("data: ", "no tenemos datos");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return itemView;
    }

}
