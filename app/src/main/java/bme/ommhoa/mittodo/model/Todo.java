package bme.ommhoa.mittodo.model;

import com.orm.SugarRecord;

import java.util.Date;

public class Todo extends SugarRecord{
    private String title;
    private String description;
    private int color;
    public String objectId;
    public Date created;
    public Date updated;

    public Todo(){
    }

    public Todo(String title, String description, int priorityColor) {
        this.title = title;
        this.description = description;

        this.color = priorityColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
