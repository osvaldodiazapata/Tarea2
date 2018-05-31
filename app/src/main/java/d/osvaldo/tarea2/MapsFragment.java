package d.osvaldo.tarea2;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Pedidos;
import d.osvaldo.tarea2.model.Usuarios;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {


    public MapsFragment() {
        // Required empty public constructor
    }

    private TextView tvProductos;
    private ListView listView;
    private ArrayList<Pedidos> listasPedidos;
    int suma=0;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refPedidos = ref.child("pedidos");
    DatabaseReference refPedido1 = refPedidos.child("pedido2");
    DatabaseReference refArticulo = refPedido1.child("articulos");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_maps, container, false);

        //return inflater.inflate(R.layout.fragment_maps, container, false);
        tvProductos = itemView.findViewById(R.id.tvPedido);

        listView = itemView.findViewById(R.id.listPedidos);
        listasPedidos = new ArrayList<Pedidos>();

        final PedidosAdapter pedidosAdapter = new PedidosAdapter(getContext(), listasPedidos);
        listView.setAdapter(pedidosAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String UID = listasPedidos.toString();
                //Toast.makeText(getContext(), "cosa "+ UID, Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        refArticulo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listasPedidos.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Log.d("datosIMportados: ", dataSnapshot.getChildren().toString());
                        Pedidos pedidos = snapshot.getValue(Pedidos.class);
                        listasPedidos.add(pedidos);
                        try{
                            suma = suma + Integer.valueOf(pedidos.getPrecio_u());
                        }catch (NumberFormatException ex){

                        }



                    }
                    if (listasPedidos.size() == 0){
                        tvProductos.setText("__");
                    }else{
                        tvProductos.setText(String.valueOf(suma));
                    }

                    suma=0;
                    pedidosAdapter.notifyDataSetChanged();
                }


                //Log.d("pedidos", ": "+dataSnapshot.getChildren().toString());


                //tvProductos.setText(dataSnapshot.getValue().toString()+"asdf"+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return itemView;
    }

    class PedidosAdapter extends ArrayAdapter<Pedidos>{


        public PedidosAdapter(@NonNull Context context, ArrayList<Pedidos> data) {
            super(context, R.layout.list_pedidos, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_pedidos, null);

            Pedidos pedidos = getItem(position);

            TextView cantidad = item.findViewById(R.id.textlistCantidadPedido);
            cantidad.setText(pedidos.getCantidad());
            TextView nombre = item.findViewById(R.id.textlistNombrePedido);
            nombre.setText(pedidos.getNombre());
            TextView precio_u = item.findViewById(R.id.textlistPrecio_uPedido);
            precio_u.setText(pedidos.getPrecio_u());


            return item;
        }
    }
}
