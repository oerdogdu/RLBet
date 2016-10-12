package com.solstice.atoi.rlbet;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Atoi on 29.09.2016.
 */
public class RequestPriority  extends StringRequest{
    Priority priority = Priority.HIGH;

    public RequestPriority(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
