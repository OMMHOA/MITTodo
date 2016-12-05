package bme.ommhoa.mittodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class RowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
