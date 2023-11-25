package com.dragon.lucky.command18;

public class ContainBean {

    private String content;
    private int id;

    public ContainBean(String content, int id) {
        this.content = content;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof ContainBean) {
            return ((ContainBean) obj).content.equals(this.content);
        }
        return super.equals(obj);
    }
}
