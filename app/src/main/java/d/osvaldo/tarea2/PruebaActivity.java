package d.osvaldo.tarea2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PruebaActivity extends AppCompatActivity {

    private EditText eNombre, eTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        eNombre = (EditText) findViewById(R.id.edNombre);
        eNombre = (EditText) findViewById(R.id.edTelefono);
    }

    public void onClick(View view) {
    }
}
