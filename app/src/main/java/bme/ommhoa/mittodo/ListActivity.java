package bme.ommhoa.mittodo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import bme.ommhoa.mittodo.adapter.TodoItemTouchHelperCallback;
import bme.ommhoa.mittodo.adapter.TodoRecyclerAdapter;
import bme.ommhoa.mittodo.model.Todo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bme.ommhoa.mittodo.helper.NetworkRelatedHelper.*;

public class ListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TodoRecyclerAdapter todoRecyclerAdapter;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        configureRecyclerView();
        todoRecyclerAdapter = new TodoRecyclerAdapter(getApplicationContext(),
                connectivityManager);
        recyclerView.setAdapter(todoRecyclerAdapter);

        ItemTouchHelper.Callback callback =
                new TodoItemTouchHelperCallback(todoRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        todoRecyclerAdapter.init();
    }

    private void configureRecyclerView() {
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            if (isNetworkOnline(connectivityManager)) {
                todoRecyclerAdapter.refresh();
            } else {
                notifyUserNoNetwork(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String title = intent.getStringExtra(NewTodoActivity.TITLE);
        String description = intent.getStringExtra(NewTodoActivity.DESCRIPTION);
        Integer priorityId = intent.getIntExtra(NewTodoActivity.PRIORITY, 0);
        int priorityColor = getPriorityColor(priorityId);
        if (title != null && !title.isEmpty()) {
            Todo todo = new Todo(title, description, priorityColor);
            todoRecyclerAdapter.addTodo(todo);
            intent.removeExtra(NewTodoActivity.TITLE);
        }
    }

    // TODO: 2016. 12. 05. legyen kozos a newtodoval es lehet nem colort akarunk tarolni todoban
    private int getPriorityColor(Integer priority_id) {
        int priority_color = 0xFFFFFFFF;
        if (priority_id == R.id.important_priority) {
            priority_color = R.color.red;
        } else if (priority_id == R.id.normal_priority) {
            priority_color = R.color.mit_yellow;
        } else if (priority_id == R.id.optional_priority) {
            priority_color = R.color.mit_blue;
        }
        return priority_color;
    }

    @OnClick(R.id.add_new)
    public void addNewTodo(View view) {
        if (isNetworkOnline(connectivityManager)) {
            Intent intent = new Intent(view.getContext(), NewTodoActivity.class);
            startActivity(intent);
        } else {
            notifyUserNoNetwork(this);
        }
    }


}
