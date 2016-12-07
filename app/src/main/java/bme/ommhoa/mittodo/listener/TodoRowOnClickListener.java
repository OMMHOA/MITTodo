package bme.ommhoa.mittodo.listener;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import bme.ommhoa.mittodo.activity.ShowTodoInfo;
import bme.ommhoa.mittodo.model.Todo;

import static bme.ommhoa.mittodo.activity.NewTodoActivity.DESCRIPTION;
import static bme.ommhoa.mittodo.activity.NewTodoActivity.TITLE;

public class TodoRowOnClickListener implements View.OnClickListener {
    private Todo todo;
    private Context context;

    public TodoRowOnClickListener(Todo todo, Context context) {
        this.todo = todo;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ShowTodoInfo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(TITLE, todo.getTitle());
        intent.putExtra(DESCRIPTION, todo.getDescription());

        context.startActivity(intent);
    }
}
