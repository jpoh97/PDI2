package co.edu.udea.pdi2;

import okhttp3.MultipartBody;
import retrofit2.Call;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyApi {
    @Multipart
    @POST("/pdi2/")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}
