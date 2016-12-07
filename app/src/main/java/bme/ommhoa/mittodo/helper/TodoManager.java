package bme.ommhoa.mittodo.helper;


import java.util.ArrayList;
import java.util.List;

import bme.ommhoa.mittodo.model.Todo;

public abstract class TodoManager {

    private static List<Todo> todos = new ArrayList<>();

    public static Todo removeTodoFromBaasTodoList(Todo todo) {
        Todo equalsTodo = getEqualsTodo(todo, todos);
        if (equalsTodo == null)
            return null;
        todos.remove(equalsTodo);
        return equalsTodo;
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
            if (getEqualsTodo(todo, todos) == null) {
                newTodos.add(todo);
                todos.add(todo);
            }
        }
        return newTodos;
    }

    public static List<Todo> getDeletedTodosAndUpdateList(List<Todo> data) {
        List<Todo> deletedTodos = new ArrayList<>();
        for (Todo todo : todos) {
            if (getEqualsTodo(todo, data) == null) {
                deletedTodos.add(todo);
            }
        }
        todos.removeAll(deletedTodos);
        return deletedTodos;
    }

    public static Todo getEqualsTodo(Todo todo, List<Todo> todoList) {
        for (Todo t : todoList) {
            if (todoEquals(t, todo)) {
                return t;
            }
        }
        return null;
    }
}
