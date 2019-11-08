package com.example.complaintsystembeta.interfaace;

import com.example.complaintsystembeta.model.PostResponse;
import com.example.complaintsystembeta.model.Posts;
import com.example.complaintsystembeta.model.SignUpData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonApiHolder {
    @GET("retrieve.php")
    Call<List<SignUpData>> getPost();

    @Multipart
    @POST("insert.php")
    Call<PostResponse> postData(@Part MultipartBody.Part fileFront,
                                @Part MultipartBody.Part fileBack,
                                @Part MultipartBody.Part fileWasaBill,
                                @Part("cnic") RequestBody cnic,
                                @Part("name") RequestBody name,
                                @Part("email") RequestBody email,
                                @Part("pass") RequestBody password,
                                @Part("contact") RequestBody contact,
                                @Part("address") RequestBody address,
                                @Part("gender") RequestBody gender
    );
    @POST("/posts")
    @FormUrlEncoded
    Call<PostResponse> savePost(
            @Field("body") String cnic,
            @Field("userId") String password
    );


}
