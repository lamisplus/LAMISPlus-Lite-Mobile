package org.lamisplus.datafi.models;

import com.activeandroid.annotation.Column;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class Token {

    private Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Type tokenType = new TypeToken<List<Token>>(){}.getType();

    @SerializedName("id_token")
    @Expose
    private String id_token;

    @SerializedName("tokenList")
    private String tokenList;


    public Type getTokenType() {
        return tokenType;
    }

    public void setTokenType(Type tokenType) {
        this.tokenType = tokenType;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public void setTokenObject(String objectToken){
        this.tokenList = gson.fromJson(objectToken, tokenType);
    }



}
