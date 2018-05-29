package d.osvaldo.tarea2;

/**
 * Created by osval on 23/04/18.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import d.osvaldo.tarea2.model.Productos;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterProductos extends RecyclerView.Adapter<AdapterProductos.ProductosViewHolder>{

    private ArrayList<Productos> productoslist;
    private int resource;
    private Activity activity;
    public Productos productos1;


    public AdapterProductos(ArrayList<Productos> productoslist){
        this.productoslist = productoslist;
    }

    public AdapterProductos(ArrayList<Productos> productoslist, int cardview_detalle, FragmentActivity activity) {
        Log.d("oncreateviewholder", "pasando");
        this.resource = cardview_detalle;
        this.productoslist = productoslist;
        this.activity = activity;
    }

    @Override
    public ProductosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("dialog", "activado");
                final Dialog dialog = new Dialog(itemView.getContext()); //pilas preguntar
                dialog.setContentView(R.layout.descripcion_productos);
                ImageView imageView = dialog.findViewById(R.id.imgDescripcion);
                //Picasso.get().load(productos.getFoto()).into(imageView);

                dialog.show();

                //Toast.makeText(activity, "abre actividad con detalle", Toast.LENGTH_SHORT).show();
            }
        });
        return new ProductosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductosViewHolder holder, int position) {
        final Productos productos = productoslist.get(position);
        holder.bindProductos(productos, activity);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Producto: "+productos.getNombre()+"\n id: "
                       +productos.getId()+"\n Precio: "+productos.getPrecio(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return productoslist.size();
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder{

        private TextView tNombre, tValor, tInventario;
        private CircleImageView ifoto;
        private Button btnSumar, btnRestar;
        private EditText edCantidadProductos;
        private CheckBox checkBox;
        public int precioInicial;

        public ProductosViewHolder(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tValor = itemView.findViewById(R.id.tPrecio);
            tInventario = itemView.findViewById(R.id.tvInventario);
            ifoto = itemView.findViewById(R.id.iFoto);
            btnSumar = itemView.findViewById(R.id.btnSumarCantidad);
            btnRestar = itemView.findViewById(R.id.btnRestarCantidad);
            edCantidadProductos = itemView.findViewById(R.id.edCantidadProductos);
            checkBox = itemView.findViewById(R.id.chkProducto);
            btnSumar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     // int precioInicial = Integer.valueOf(tValor.getText().toString());
                      int valor = Integer.valueOf(edCantidadProductos.getText().toString());
                      valor +=1;
                      edCantidadProductos.setText(String.valueOf(valor));
                      tValor.setText(String.valueOf(valor*precioInicial));

                      productos1.setCantidad((String) tValor.getText().toString());

                      //Toast.makeText(activity, "proasdf"+ productos1.getCantidad(), Toast.LENGTH_SHORT).show();

                      /*
                    Toast.makeText(activity, "Producto: "+ tNombre.getText().toString()+
                            "\n Cantidad: "+edCantidadProductos.getText().toString()+
                            "\n Precio: "+tValor.getText().toString(), Toast.LENGTH_SHORT).show();*/

                }
            });
            btnRestar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //int precioInicial = Integer.valueOf(tValor.getText().toString());
                    int valor = Integer.valueOf(edCantidadProductos.getText().toString());
                    if (valor > 0){
                        valor-=1;
                    }
                    edCantidadProductos.setText(String.valueOf(valor));
                    if (valor == 0){
                        tValor.setText(String.valueOf(precioInicial));
                    }else {
                        tValor.setText(String.valueOf(valor * precioInicial));
                    }
                    /*
                    Toast.makeText(activity, "Producto: "+ tNombre.getText().toString()+
                            "\n Cantidad: "+edCantidadProductos.getText().toString()+
                            "\n Precio: "+tValor.getText().toString(), Toast.LENGTH_SHORT).show();*/

                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                        if (checkBox.isChecked()){
                            //Toast.makeText(activity, "chekeado " + itemView.getId() , Toast.LENGTH_SHORT).show();
                            Log.d("checkbox", "true");
                            btnRestar.setBackgroundColor(R.color.blueviolet);
                            btnRestar.setEnabled(Boolean.parseBoolean("true"));
                            btnSumar.setEnabled(Boolean.parseBoolean("true"));




                        }else{
                            Log.d("checkbox", "false");
                            btnRestar.setEnabled(Boolean.parseBoolean("false"));
                            btnSumar.setEnabled(Boolean.parseBoolean("false"));
                        }
                }
            });


        }

        public void bindProductos(Productos productos, Activity activity) {
            Log.d("oncreateviewholder", "pasando"+productos.getNombre());

            productos1 = productos;
            tNombre.setText(productos.getNombre());
            tValor.setText(productos.getPrecio());
            tInventario.setText(productos.getInventario());
            edCantidadProductos.setText(productos.getCantidad());
            precioInicial = Integer.valueOf(tValor.getText().toString());

            Picasso.get().load(productos.getFoto()).into(ifoto);
        }
    }


}
