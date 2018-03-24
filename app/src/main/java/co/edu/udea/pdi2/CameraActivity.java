package co.edu.udea.pdi2;

import android.Manifest;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.content.pm.PackageManager;

public class CameraActivity extends AppCompatActivity {

    private Camera camera;
    private FrameLayout frameLayout;
    private ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        // Open the camera
        camera = Camera.open();

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    public void buttonPlayAudioAction(View view) {
        Toast.makeText(this, "Play audio...", Toast.LENGTH_SHORT).show();
    }
}
