package m2c_miage.william_piron.androidm2td2.Permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.ref.WeakReference;

import m2c_miage.william_piron.androidm2td2.MainActivity;

public class Permissions {
    private WeakReference<MainActivity> mainActivityWeakReference;

    public Permissions(MainActivity mainactivity) {
        this.mainActivityWeakReference = new WeakReference<MainActivity>(mainactivity);
    }

    public void askPerms(){
        // Here, thisActivity is the current activity
        final MainActivity thisActivity = mainActivityWeakReference.get();
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.CAMERA},
                        3);

        } else {
            // Permission has already been granted
        }
    }
}
