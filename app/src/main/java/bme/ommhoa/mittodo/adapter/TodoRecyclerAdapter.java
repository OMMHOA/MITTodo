package bme.ommhoa.mittodo.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Collections;
import java.util.List;

import bme.ommhoa.mittodo.R;
import bme.ommhoa.mittodo.helper.TodoManager;
import bme.ommhoa.mittodo.model.Todo;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bme.ommhoa.mittodo.helper.NetworkRelatedHelper.isNetworkOnline;
import static bme.ommhoa.mittodo.helper.TodoManager.getNewTodosAndUpdateList;
import static bme.ommhoa.mittodo.helper.TodoManager.removeTodoFromBaasTodoList;

public class TodoRecyclerAdapter extends
        RecyclerView.Adapter<
                TodoRecyclerAdapter.ViewHolder> implements TodoTouchHelperAdapter {

    private static final String TAG = "MITTodo";

    private List<Todo> todos;
    private Context context;

    private ConnectivityManager connectivityManager;

    public TodoRecyclerAdapter(Context context, ConnectivityManager cm) {
        todos = Todo.listAll(Todo.class);
        this.context = context;
        connectivityManager = cm;
    }

    @Override
    public TodoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_row, parent, false);

        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final TodoRecyclerAdapter.ViewHolder holder,
                                 int position) {
        holder.tvTodoTitle.setText(todos.get(position).getTitle());
        holder.tvTodoDescription.setText(todos.get(position).getDescription());
        holder.rlActivityRow.setBackgroundColor(
                ContextCompat.getColor(
                        context,
                        todos.get(position).getColor()
                )
        );
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void addTodo(Todo todo) {
        todo.save();
        todos.add(todo);
        saveTodoInBaas(todo);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(todos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(todos, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        Todo todo = todos.remove(position);
        if (!isNetworkOnline(connectivityManager)) {
            notifyItemRemoved(position);
            return;
        }
        todo.delete();
        todo = removeTodoFromBaasTodoList(todo);
        if (todo == null) {
            Log.e(TAG, "No match in baas array");
            notifyItemRemoved(position);
            return;
        }
        Backendless.Persistence.of(Todo.class).remove(todo, new AsyncCallback<Long>() {
            @Override
            public void handleResponse(Long response) {
                // Contact has been deleted. The response is the
                // time in milliseconds when the object was deleted
                Log.i(TAG, "Todo successfully removed from Baas on " + response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                logError(fault, "delete");
            }
        });
        notifyItemRemoved(position);
    }

    private void saveTodoInBaas(Todo todo) {
        Backendless.Persistence.of(Todo.class).save(todo, new AsyncCallback<Todo>() {
            @Override
            public void handleResponse(Todo response) {
                TodoManager.addBaasTodo(response);
                Log.i(TAG, "New todo has been saved to Baas. ID: " + response.objectId);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                logError(fault, "save");
            }
        });
    }

    public void refresh() {
        Backendless.Persistence.of(Todo.class)
                .find(new AsyncCallback<BackendlessCollection<Todo>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<Todo> response) {
                        Log.i(TAG, "Baas response received");
                        List<Todo> newTodos =
                                TodoManager.getNewTodosAndUpdateList(response.getData());
                        addNewTodosLocally(newTodos);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        logError(fault, "listing");
                    }
                });
    }

    private void addNewTodosLocally(List<Todo> newTodos) {
        for (Todo todo : newTodos) {
            todo.save();
            todos.add(todo);
        }
        notifyDataSetChanged();
    }

    private void logError(BackendlessFault fault, String msg) {
        Log.e(TAG, "Baas " + msg + " error " + fault.getCode() + ": "
                + fault.getMessage());
        Log.e(TAG, "Baas error detail: " + fault.getDetail());
    }

    public void init() {
        getNewTodosAndUpdateList(todos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.todo_title) TextView tvTodoTitle;
        @BindView(R.id.description) TextView tvTodoDescription;
        @BindView(R.id.activity_row) RelativeLayout rlActivityRow;


        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
