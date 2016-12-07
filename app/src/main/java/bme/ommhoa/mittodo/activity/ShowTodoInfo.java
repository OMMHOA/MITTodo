package bme.ommhoa.mittodo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import bme.ommhoa.mittodo.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTodoInfo extends AppCompatActivity {
    @BindView(R.id.show_todo_title)
    TextView todoTitle;

    @BindView(R.id.show_todo_description)
    TextView todoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_todo_info);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra(NewTodoActivity.TITLE);
        String description = intent.getStringExtra(NewTodoActivity.DESCRIPTION);

        todoTitle.setText(title);
        todoDescription.setText(description);
    }
}
