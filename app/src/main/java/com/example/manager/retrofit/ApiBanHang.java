package com.example.manager.retrofit;

import com.example.manager.model.NewProductModel;
import com.example.manager.model.OrderModel;
import com.example.manager.model.ProductCategoryModel;
import com.example.manager.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
            @Field("mobile") String mobile
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
}
