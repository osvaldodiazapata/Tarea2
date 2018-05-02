package d.osvaldo.tarea2;

/**
 * Created by osval on 23/04/18.
 */
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Productos;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterProductos
        extends RecyclerView.Adapter<AdapterProductos.ProductosViewHolder>{

    private ArrayList<Productos> productoslist;
    private int resource;
    private Activity activity;

    public AdapterProductos(ArrayList<Productos> productoslist){
        this.productoslist = productoslist;
    }

    public AdapterProductos(ArrayList<Productos> productoslist, int cardview_detalle, FragmentActivity activity) {

        this.resource = cardview_detalle;
        this.productoslist = productoslist;
        this.activity = activity;
    }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "abre actividad con detalle", Toast.LENGTH_SHORT).show();
            }
        });
        return new ProductosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductosViewHolder holder, int position) {
        Productos productos = productoslist.get(position);
        holder.bindProductos(productos, activity);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder{

        private TextView tNombre, tValor;
        private CircleImageView ifoto;

        public ProductosViewHolder(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tValor = itemView.findViewById(R.id.tPrecio);
            ifoto = itemView.findViewById(R.id.iFoto);
        }

        public void bindProductos(Productos productos, Activity activity) {
            Log.d("oncreateviewholder", "pasando"+productos.getNombre());
            tNombre.setText(productos.getNombre());
            tValor.setText(productos.getPrecio());
            Picasso.get().load(productos.getFoto()).into(ifoto);
        }
    }


}
/*
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
}*/