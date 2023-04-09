package api;

import com.google.gson.JsonArray;

public class FarmModel {
    private JsonArray data;

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }
}
