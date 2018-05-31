package d.osvaldo.tarea2;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Compras;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by osval on 30/05/18.
 */

public class AdapterCompras extends RecyclerView.Adapter<AdapterCompras.ComprasViewHolder> {

    private ArrayList<Compras> compraslist;
    private int resource;
    private Activity activity;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refPedidos = ref.child("pedidos");
    DatabaseReference refPedido1 = refPedidos.child("pedido2");
    DatabaseReference refArticulo = refPedido1.child("articulos");


    public AdapterCompras(ArrayList<Compras> compraslist){
        this.compraslist = compraslist;
    }

    public AdapterCompras(ArrayList<Compras> compraslist, int cardview_compra, FragmentActivity activity) {
        //Log.d("oncreateviewholder", "pasando");
        this.resource = cardview_compra;
        this.compraslist = compraslist;
        this.activity = activity;
    }

    @Override
    public ComprasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        
        return new ComprasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ComprasViewHolder holder, int position) {
        final Compras compras = compraslist.get(position);
        holder.bindcompras(compras, activity);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    String uid = compras.getId();
                    refArticulo.child(uid).child("cantidad").setValue(compras.getCantidad());
                    refArticulo.child(uid).child("precio_u").setValue(compras.getPrecio());
                    refArticulo.child(uid).child("nombre").setValue(compras.getNombre());
                    refPedido1.child("mesa").setValue("5");

                    //Toast.makeText(activity, "es"+ compras.getNombre()+"id"+compras.getId(), Toast.LENGTH_SHORT).show();
                }else{
                    String uid = compras.getId();
                    refArticulo.child(uid).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {return compraslist.size();
    }

    public class ComprasViewHolder extends RecyclerView.ViewHolder {

        private TextView tNombre, tValor, tInventario;
        private CircleImageView ifoto;
        private Button btnSumar, btnRestar;
        private EditText edCantidadProductos;
        private CheckBox checkBox;
        public int precioInicial;

        public ComprasViewHolder(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tValor = itemView.findViewById(R.id.tPrecio);
            tInventario = itemView.findViewById(R.id.tvInventario);
            ifoto = itemView.findViewById(R.id.iFoto);
            btnSumar = itemView.findViewById(R.id.btnSumarCantidad);
            btnRestar = itemView.findViewById(R.id.btnRestarCantidad);
            edCantidadProductos = itemView.findViewById(R.id.edCantidadProductos);
            checkBox = itemView.findViewById(R.id.chkProducto);
        }

        public void bindcompras(Compras compras, Activity activity) {
            //Log.d("oncreateviewholder", "pasando"+productos.getNombre());

            tNombre.setText(compras.getNombre());
            tValor.setText(compras.getPrecio());
            tInventario.setText(compras.getInventario());
            edCantidadProductos.setText(compras.getCantidad());
            try{
                precioInicial = Integer.valueOf(tValor.getText().toString());
            }catch (NumberFormatException ex){

            }

            Picasso.get().load(compras.getFoto()).into(ifoto);

        }
    }
}
