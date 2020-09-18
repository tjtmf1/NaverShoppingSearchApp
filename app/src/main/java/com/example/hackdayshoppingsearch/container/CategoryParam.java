package com.example.hackdayshoppingsearch.container;

import java.util.ArrayList;
import java.util.List;

public class CategoryParam {
    private String name;
    private List<String> param;
    public CategoryParam(String name, String code) {
        this.name = name;
        param = new ArrayList<>();
        param.add(code);
    }

    public String getName() {
        return name;
    }

    public List<String> getParam() {
        return param;
    }
}
