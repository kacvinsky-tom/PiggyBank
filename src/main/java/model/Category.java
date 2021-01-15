package model;

import java.awt.*;
import java.util.Objects;

public class Category {

    private Long id;
    private String name;
    private Color color;

    public Category(String name){
        setName(name);
        setColor(Color.BLACK);  // ToDo Remove
    }

    public Category(String name, Color color){
        setName(name);
        setColor(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    @Override
    public boolean equals(Object obj) {
        Category c = (Category) obj;
        return c.getName().equals(this.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
