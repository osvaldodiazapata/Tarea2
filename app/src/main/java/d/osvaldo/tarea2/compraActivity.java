package d.osvaldo.tarea2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Compras;
import d.osvaldo.tarea2.model.Pedidos;
import d.osvaldo.tarea2.model.Productos;
import d.osvaldo.tarea2.model.Usuarios;

public class compraActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btconfirmar;
    private TextView tvconfirmarcompra;
    private RecyclerView.Adapter adapterProductos;
    private RecyclerView.LayoutManager layoutManager;

    int SumaPrecios = 0;

    private ListView listView;
    private ArrayList<Compras> listasCompras;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refcompras = ref.child("compras");
    DatabaseReference refPedidos = ref.child("pedidos");
    DatabaseReference refPedido1 = refPedidos.child("pedido2");
    DatabaseReference refArticulo = refPedido1.child("articulos");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        listasCompras = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        adapterProductos = new AdapterCompras(listasCompras, R.layout.cardview_compra, this);


        recyclerView = findViewById(R.id.recyclerviewcompra);
        btconfirmar = findViewById(R.id.btnComprarcompra);
        tvconfirmarcompra = findViewById(R.id.tvconfirmarcompra);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterProductos);

        //databaseReference = FirebaseDatabase.getInstance().getReference();
        //databaseReference.keepSynced(true);
        ref.child("compras").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listasCompras.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Compras productos = snapshot.getValue(Compras.class);
                        if (productos.getCantidad().equals("0")){
                            //Toast.makeText(compraActivity.this, "USTED NO HA SELECCIONADO PRODUCTOS", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(compraActivity.this, "no fue seleccionada", Toast.LENGTH_SHORT).show();
                        }else{
                            //SumaPrecios = SumaPrecios+Integer.valueOf(productos.getPrecio().toString());
                            //tvconfirmarcompra.setText(String.valueOf(SumaPrecios));
                            listasCompras.add(productos);
                        }
                    }
                    adapterProductos.notifyDataSetChanged();
                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refArticulo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        Pedidos pedidos = snapshot.getValue(Pedidos.class);
                        try {
                            SumaPrecios = SumaPrecios+Integer.valueOf(pedidos.getPrecio_u());
                        }catch (NumberFormatException ex){}

                        tvconfirmarcompra.setText(String.valueOf(SumaPrecios));

                        /*
                        Toast.makeText(compraActivity.this, "cosa"+pedidos.getNombre()+
                                "cantidad"+ pedidos.getCantidad()+
                                "precio"+pedidos.getPrecio_u(), Toast.LENGTH_SHORT).show();*/
                    }
                    SumaPrecios = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotopedidos();
            }
        });



    }
    private void gotopedidos() {
        Intent intent = new Intent(this, ProductosActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    class CompraAdapter extends ArrayAdapter<Pedidos> {

        public CompraAdapter(@NonNull Context context, ArrayList<Pedidos> data){
            super(context, R.layout.list_compras, data);
        }

        public CompraAdapter(compraActivity context, ArrayList<Productos> listasProductos) {
            super(context, R.layout.list_compras);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_compras,  null);

            Pedidos pedidos = getItem(position);

            TextView nombre = item.findViewById(R.id.textlistCantidadCompra);
            nombre.setText(pedidos.getCantidad());
            TextView password = item.findViewById(R.id.textlistNombreCompra);
            password.setText(pedidos.getNombre());
            TextView privilegio = item.findViewById(R.id.textlistPrecio_uCompra);
            privilegio.setText(pedidos.getPrecio_u());

            //return super.getView(position, convertView, parent);
            return item;
        }
    }
}
