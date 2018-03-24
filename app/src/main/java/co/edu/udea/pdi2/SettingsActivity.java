package co.edu.udea.pdi2;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new SettingsFragment();
        fragmentTransaction.add(R.id.pref_container, fragment);
        //fragmentTransaction.replace(R.id.viewer, fragment);
        fragmentTransaction.commit();

    }
}
