package d.osvaldo.tarea2;

/**
 * Created by osval on 23/04/18.
 */
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Productos;

public class AdapterProductos extends RecyclerView.Adapter<AdapterProductos.ProductosViewHolder>{

    private ArrayList<Productos> prodcutoslist;
    private int resource;
    private Activity activity;

    public AdapterProductos(ArrayList<Productos> productoslist, int cardview_detalle, FragmentActivity activity) {
        this.resource = cardview_detalle;
        this.prodcutoslist = productoslist;
        this.activity = activity;
    }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ProductosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductosViewHolder holder, int position) {
        Productos productos = prodcutoslist.get(position);
        holder.bindProductos(productos, activity);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder{

        private TextView tNombre, tValor;
        private ImageView ifoto;
        public ProductosViewHolder(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tValor = itemView.findViewById(R.id.tPrecio);
            ifoto = itemView.findViewById(R.id.iFoto);
        }

        public void bindProductos(Productos productos, Activity activity) {
            tNombre.setText(productos.getNombre());
            tValor.setText(productos.getPrecio());
            Picasso.get().load(productos.getFoto()).into(ifoto);
        }
    }


}
