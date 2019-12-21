package com.example.complaintsystembeta.constants;

import com.example.complaintsystembeta.interfaace.JsonApiHolder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
   public static JsonApiHolder getApi(){
       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(Constants.REST_API)
               .addConverterFactory(GsonConverterFactory.create())
               .build();
       JsonApiHolder service = retrofit.create(JsonApiHolder.class);
       return service;
   }
}
