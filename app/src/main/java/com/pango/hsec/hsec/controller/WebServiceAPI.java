package com.pango.hsec.hsec.controller;

import com.pango.hsec.hsec.model.GetGaleriaModel;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by ICWB-01 on 04/04/2018.
 */

public interface WebServiceAPI {

    @GET
    Call<String> getToken(@Url String url);

    @GET("media/GetMultimedia/{CodNoticia}")
    Call<GetGaleriaModel> getFiles(@Header("Authorization") String token, @Path("CodNoticia") String CodNoticia);

    @Multipart
    @POST("Media/Upload")
    Call<String> uploadImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("Media/UploadMultimedia")
    Call<String> uploadFile(@Header("Authorization") String token, @Part MultipartBody.Part file, @Part("NroDocReferencia") RequestBody Nrodoc, @Part("CodTabla") RequestBody tabla, @Part("GrupoPertenece") RequestBody Grupo);

    @Multipart
    @POST("Media/UploadAllFiles")
    Call<String> uploadAllFile(@Header("Authorization") String token, @Part("NroDocReferencia") RequestBody Nrodoc, @Part("CodTabla") RequestBody tabla, @Part("GrupoPertenece") RequestBody Grupo, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Observaciones/Insertar")
    Call<String> insertarObservacion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody detalle, @Part("3") RequestBody planes, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Inspecciones/InsertarObservacion")
    Call<String> insertarInspeccionObs(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody planes, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Inspecciones/Insertar")
    Call<String> insertarInspeccion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody responsables,@Part("3") RequestBody atendidos, @Part("4") RequestBody observaciones,@Part("5") RequestBody planes,  @Part List<MultipartBody.Part> files);

}