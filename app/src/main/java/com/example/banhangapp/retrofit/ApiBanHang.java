package com.example.banhangapp.retrofit;


import com.example.banhangapp.model.DonHangModel;
import com.example.banhangapp.model.LoaiSPModel;
import com.example.banhangapp.model.SanPhamModel;
import com.example.banhangapp.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getData.php")
    Observable<LoaiSPModel> getLoaiSP();

    @GET("getDataMoi.php")
    Observable<SanPhamModel> getSanPham();

    @POST("getChiTiet.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getDienThoai(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangKiUser.php")
    @FormUrlEncoded
    Observable<UserModel> setDangKi(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile
    );

    @POST("dangNhapUser.php")
    @FormUrlEncoded
    Observable<UserModel> setDangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("resetEmail.php")
    @FormUrlEncoded
    Observable<UserModel> resetPass(
            @Field("email") String email
    );

    @POST("donHang.php")
    @FormUrlEncoded
    Observable<UserModel> createOrder(
            @Field("iduser") int iduser,
            @Field("diachi") String diachi,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("soluong") int soluong,
            @Field("tongtien") String tongtien,
            @Field("chitiet") String chitiet
    );

    @POST("purchaseHistory.php")
    @FormUrlEncoded
    Observable<DonHangModel> viewOrder(
            @Field("iduser") int iduser
    );

    @POST("searchProduct.php")
    @FormUrlEncoded
    Observable<SanPhamModel> search(
            @Field("search") String search
    );

}
