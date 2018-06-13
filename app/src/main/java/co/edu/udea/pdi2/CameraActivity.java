package co.edu.udea.pdi2;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.MultipartEntity;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.pm.PackageManager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private FrameLayout frameLayout;
    private ShowCamera showCamera;
    private EditText resultsText;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        resultsText = (EditText) findViewById(R.id.resultsEditText);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        result = "";

        // Open the camera
        camera = Camera.open();

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    public void buttonPlayAudioAction(View view) {
        Toast.makeText(this, "Playing audio", Toast.LENGTH_SHORT).show();
    }

    public void sendImage(View view) {
        camera.takePicture(null, null, mPicture);
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, final Camera camera) {
            Bitmap pic = BitmapFactory.decodeByteArray(data, 0, data.length);

            //HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://6155bc15.ngrok.io/pdi2/");
            //org.apache.http.entity.mime.MultipartEntity mpEntity = new org.apache.http.entity.mime.MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), data);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", "my image.jpg", requestFile);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://6155bc15.ngrok.io/pdi2/").client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            MyApi api = retrofit.create(MyApi.class);
            retrofit2.Call<ResponseBody> call = api.uploadImage(body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //Log.d("...............", response.body().string());
                        //Log.d("...............", String.valueOf(response.body().string().contains("Imagen invalida")));
                        String res = response.body().string();
                        if(res.contains("Imagen invalida")) {
                            Log.d("..............", "alv1");
                            Toast.makeText(CameraActivity.this, "No se reconoce el gesto, vuelve a intentar", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("..................", "alv2");
                            Log.d("..................", res);
                            result += res;
                            Toast.makeText(CameraActivity.this, res, Toast.LENGTH_LONG).show();
                            updateView();
                            Log.d("..................", res);
                        }
                        updateView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(CameraActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("...............", t.getMessage());
                    Log.d("................", t.toString());
                    if(t instanceof SocketTimeoutException){
                        Toast.makeText(CameraActivity.this, "Problemas con el servidor", Toast.LENGTH_SHORT).show();
                    }
                    camera.startPreview();
                }
            });
            /*try {
                mpEntity.addPart("image", new StringBody(Base64.encodeToString(data, Base64.DEFAULT)));

                Toast.makeText(CameraActivity.this, "entre", Toast.LENGTH_SHORT).show();
                httppost.setEntity(mpEntity);
                Toast.makeText(CameraActivity.this, "entre2", Toast.LENGTH_SHORT).show();
                HttpResponse response = httpclient.execute(httppost);
                Toast.makeText(CameraActivity.this, "entre3", Toast.LENGTH_SHORT).show();
            } catch(Exception e){

                Toast.makeText(CameraActivity.this, "soy una excepcion", Toast.LENGTH_SHORT).show();
            }*/
        }
    };

    public void updateView() {
        camera.startPreview();
        //result = resultsText.getText().toString();
        resultsText.setText(result);
    }
}
