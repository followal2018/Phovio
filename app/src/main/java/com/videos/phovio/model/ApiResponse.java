package com.videos.phovio.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamim on 24/10/2017.
 */


public class ApiResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("values")
    @Expose
    private List<ApiValue> values = null;
    @SerializedName("randomUsers")
    @Expose
    private ArrayList<User> randomUsers;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApiValue> getValues() {
        return values;
    }

    public void setValues(List<ApiValue> values) {
        this.values = values;
    }

    public ArrayList<User> getRandomUsers() {
        return randomUsers;
    }

    public void setRandomUsers(ArrayList<User> randomUsers) {
        this.randomUsers = randomUsers;
    }
}
