package com.dragon.lucky.command12;

import java.util.ArrayList;
import java.util.List;

public class NotExistsBean {

    private int line;
    private List<Integer> position;

    public NotExistsBean() {
        position = new ArrayList<>();
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPosition(int id) {
        return position.get(id);
    }

    public List<Integer> getPositions() {
        return position;
    }

    public void addPosition(int position) {
        this.position.add(position);
    }

}
