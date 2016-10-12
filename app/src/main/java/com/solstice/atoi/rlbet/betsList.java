package com.solstice.atoi.rlbet;

import java.util.ArrayList;

/**
 * Created by Atoi on 23.09.2016.
 */
public class betsList<T> {
    private ArrayList<T> list;

    public betsList(){
        list = new ArrayList<>();
    }

    public void add(T t) {
        list.add(t);
        FragmentSchedule fragment = new FragmentSchedule();
        fragment.refreshList();

    }
}
