package com.example.manager.retrofit;

import com.example.manager.model.MessageModel;
import com.example.manager.model.NewProductModel;
import com.example.manager.model.OrderModel;
import com.example.manager.model.ProductCategoryModel;
import com.example.manager.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
    @GET("getProduct.php")
    Observable<ProductCategoryModel> getProductCategory();

    @GET("getNewProduct.php")
    Observable<NewProductModel> getNewProduct();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<NewProductModel> getProduct(
            @Field("page") int page,
            @Field("type") int type

    );

    @POST("signup.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("email") String email,
            @Field("password") String pass,
            @Field("username") String name,
            @Field("mobile") String mobile,
            @Field("uid") String uid

    );

    @POST("signin.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("password") String pass
    );

    @POST("send_link.php")
    @FormUrlEncoded
    Observable<UserModel> resetPass(
            @Field("email") String email
    );

    @POST("order.php")
    @FormUrlEncoded
    Observable<UserModel> createOrder(
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("total_price") String total_price,
            @Field("user_id") int user_id,
            @Field("address") String address,
            @Field("num") int num,
            @Field("detail") String detail
    );

    @POST("order_records.php")
    @FormUrlEncoded
    Observable<OrderModel> viewOrder(
            @Field("user_id") int user_id
    );

    @POST("search.php")
    @FormUrlEncoded
    Observable<NewProductModel> search(
            @Field("search") String search
    );

    @POST("delete.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanpham(
            @Field("id") int id
    );


    @POST("themsp.php")
    @FormUrlEncoded
    Observable<MessageModel> addProduct(
            @Field("name") String name,
            @Field("price") String price,
            @Field("picture") String picture,
            @Field("description") String description,
            @Field("type") int type
    );

    @POST("updateSp.php")
    @FormUrlEncoded
    Observable<MessageModel> updateProduct(
            @Field("name") String name,
            @Field("price") String price,
            @Field("picture") String picture,
            @Field("description") String description,
            @Field("type") int type,
            @Field("id") int id
    );

    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token
    );

    @POST("updateOrder.php")
    @FormUrlEncoded
    Observable<MessageModel> updateOrder(
            @Field("id") int id,
            @Field("status") int status
    );

    @Multipart
    @POST("uploadImage.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);
}


