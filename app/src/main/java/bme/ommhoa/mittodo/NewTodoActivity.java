package bme.ommhoa.mittodo;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bme.ommhoa.mittodo.helper.NetworkRelatedHelper.*;

public class NewTodoActivity extends AppCompatActivity {
    public final static String TITLE = "title";
    public final static String DESCRIPTION = "description";
    public final static String PRIORITY = "priority";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.important_priority) ImageButton important;
    @BindView(R.id.normal_priority) ImageButton normal;
    @BindView(R.id.optional_priority) ImageButton optional;

    private ImageButton selectedButton;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        ButterKnife.bind(this);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        resetButtonColors();
        selectedButton = optional;
        selectedButton.getBackground().setColorFilter(
                0x01238938,PorterDuff.Mode.DARKEN);


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ListActivity.class);
        if (item.getItemId() == R.id.done_creating_new) {
            if (isNetworkOnline(connectivityManager)) {
                EditText title = (EditText) findViewById(R.id.new_title);
                String titleMessage = title.getText().toString();
                EditText description = (EditText) findViewById(R.id.new_description);
                String descriptionMessage = description.getText().toString();

                intent.putExtra(PRIORITY, selectedButton.getId());
                intent.putExtra(TITLE, titleMessage);
                intent.putExtra(DESCRIPTION, descriptionMessage);
            } else {
                notifyUserNoNetwork(this);
            }
        }

        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.important_priority, R.id.normal_priority, R.id.optional_priority})
    public void buttonSelected(View button) {
        resetButtonColors();
        button.getBackground().setColorFilter(0x01238938,PorterDuff.Mode.DARKEN);
        selectedButton = (ImageButton) button;
    }

    private void resetButtonColors() {
        important.getBackground().setColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.red),
                PorterDuff.Mode.DARKEN
        );

        normal.getBackground().setColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.mit_yellow),
                PorterDuff.Mode.DARKEN
        );

        optional.getBackground().setColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.mit_blue),
                PorterDuff.Mode.DARKEN
        );
    }

}
