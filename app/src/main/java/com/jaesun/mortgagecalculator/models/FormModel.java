package com.jaesun.mortgagecalculator.models;

import java.util.ArrayList;

public class FormModel extends Object {
    int uiId;
    String title;
    String content;
    String value;
    String placeholder;
    ArrayList<String> options;

    public FormModel(int uiId, String title) {
        this.uiId = uiId;
        this.title = title;
    }

    public FormModel(int uiId, String title, String value, ArrayList<String> options) {
        this.uiId = uiId;
        this.title = title;
        this.value = value;
        this.options = options;
    }

    public FormModel(int uiId, String title, String placeholder) {
        this.uiId = uiId;
        this.title = title;
        this.placeholder = placeholder;
    }



    public int getUiId() {
        return uiId;
    }

    public void setUiId(int uiId) {
        this.uiId = uiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
