package com.sinhvien.onlinefoodshop;

import com.google.gson.annotations.SerializedName;
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.Model.UserModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Các endpoint cho User
    @POST("api/users")
    Call<UserModel> addUser(@Body UserModel user);

    @POST("api/login")
    Call<UserModel> login(@Body LoginRequest loginRequest);

    @PUT("api/users/{email}")
    Call<UserModel> updateUserByEmail(@Path("email") String email, @Body UserModel user);

    @DELETE("api/users/{email}")
    Call<Void> deleteUserByEmail(@Path("email") String email);

    @GET("api/users")
    Call<List<UserModel>> getUsers();

    @GET("api/users/search")
    Call<List<UserModel>> searchUsersByEmail(@Query("email") String email);

    // Các endpoint cho Product
    @GET("api/products")
    Call<List<ProductModel>> getProducts();

    @POST("api/products")
    Call<ProductModel> addProduct(@Body ProductModel product);

    @PUT("api/products/{productID}")
    Call<ProductModel> updateProduct(@Path("productID") String productID, @Body ProductModel product);

    @DELETE("api/products/{productID}")
    Call<Void> deleteProduct(@Path("productID") String productID);

    @GET("api/products/search")
    Call<List<ProductModel>> searchProductsByName(@Query("name") String name);

    // Các endpoint cho Category
    @GET("api/categories")
    Call<List<CategoryModel>> getCategories();

    @POST("api/categories")
    Call<CategoryModel> addCategory(@Body CategoryModel category);

    @PUT("api/categories/{categoryId}")
    Call<CategoryModel> updateCategory(@Path("categoryId") String categoryId, @Body CategoryModel category);

    @DELETE("api/categories/{categoryId}")
    Call<Void> deleteCategory(@Path("categoryId") String categoryId);

    @GET("api/categories/search")
    Call<List<CategoryModel>> searchCategoriesByName(@Query("name") String name); // Endpoint tìm kiếm

    class LoginRequest {
        @SerializedName("email")
        String email;

        @SerializedName("password")
        String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}