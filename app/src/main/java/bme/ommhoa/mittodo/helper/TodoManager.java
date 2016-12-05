package bme.ommhoa.mittodo.helper;


import java.util.ArrayList;
import java.util.List;

import bme.ommhoa.mittodo.model.Todo;

public abstract class TodoManager {

    private static List<Todo> todos = new ArrayList<>();

    public static Todo removeTodoFromBaasTodoList(Todo todo) {
        int indexOfTodo = -1;
        for (int i = 0; i < todos.size(); i++) {
            if (todoEquals(todo, todos.get(i))) {
                indexOfTodo = i;
                break;
            }
        }
        if (indexOfTodo == -1)
            return null;
        return todos.remove(indexOfTodo);
    }

    private static boolean todoEquals(Todo todo1, Todo todo2) {
        return todo1.getTitle().equals(todo2.getTitle()) &&
                todo1.getDescription().equals(todo2.getDescription()) &&
                todo1.getColor() == todo2.getColor();
    }

    public static void addBaasTodo(Todo todo) {
        todos.add(todo);
    }

    public static List<Todo> getNewTodosAndUpdateList(List<Todo> data) {
        List<Todo> newTodos = new ArrayList<>();
        for (Todo todo : data) {
            if (!todosContains(todo)) {
                newTodos.add(todo);
                todos.add(todo);
            }
        }
        return newTodos;
    }

    private static boolean todosContains(Todo todo) {
        for (Todo t : todos) {
            if (todoEquals(t, todo)) {
                return true;
            }
        }
        return false;
    }
}
