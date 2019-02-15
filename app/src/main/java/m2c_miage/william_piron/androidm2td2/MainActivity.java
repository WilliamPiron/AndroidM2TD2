package m2c_miage.william_piron.androidm2td2;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import m2c_miage.william_piron.androidm2td2.Adapter.ListAdapter;
import m2c_miage.william_piron.androidm2td2.Models.Movie;
import m2c_miage.william_piron.androidm2td2.Permissions.Permissions;
import m2c_miage.william_piron.androidm2td2.Utils.ImageDownloaderThread;
import m2c_miage.william_piron.androidm2td2.Utils.MyThreadFactory;

public class MainActivity extends AppCompatActivity {

    public ArrayAdapter<Movie> adapter;
    private Button refresh;
    private Button add;
    private Button askPerm;
    private Button save;
    private Button load;
    public static Handler handlerTui = new Handler(Looper.getMainLooper());
    public HandlerThread handlerThread = new HandlerThread("handlerThread");
    public Handler handler2;
    private ThreadPoolExecutor threadPoolExecutor;
    private BlockingQueue<Runnable> mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();
    private MyThreadFactory myThreadFactory = new MyThreadFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Movie> movies = new ArrayList<Movie>();
        movies.add(new Movie("https://image.tmdb.org/t/p/w1280/xjeYI75uMBtBjNlJ0cDJZDFg5Yv.jpg", "Silent Voice", "2016", "Naoko Yamada", "Reiko Yoshida"));
        movies.add(new Movie("https://image.tmdb.org/t/p/w1280/vpQxNHhS6BxmwKiWoUUPancE4mV.jpg", "Your Name", "2016", "Makoto Shinkai", "Makoto Shinkai"));
        movies.add(new Movie("https://image.tmdb.org/t/p/w1280/77Z0g5fc1qWJ7SfHyeiHsMkYx5O.jpg", "Spider-Man : Homecoming ", "2017", "Jon Watts", "Jon Watts"));
        ListView moviesView = (ListView) findViewById(R.id.listMovies);
        adapter = new ListAdapter(this, movies);
        moviesView.setAdapter(adapter);

        handlerThread.start();
        handler2 = new Handler(handlerThread.getLooper());

        threadPoolExecutor = new ThreadPoolExecutor(1, 3, 1, TimeUnit.SECONDS, mDecodeWorkQueue);
        threadPoolExecutor.setThreadFactory(myThreadFactory);
        refresh = findViewById(R.id.buttonRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* ThreadPool */
                for (Movie movie : movies) {
                    ImageDownloaderThread imageDownloaderThread = new ImageDownloaderThread(movie, MainActivity.this);
                    threadPoolExecutor.execute(imageDownloaderThread);
                }
            }
        });

        moviesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.remove(adapter.getItem(i));
                return true;
            }
        });

        askPerm = findViewById(R.id.buttonAskPerm);
        askPerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permissions perms = new Permissions(MainActivity.this);
                perms.askPerms();
            }
        });

        save = findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndCipher();
            }
        });

        load = findViewById(R.id.buttonLoad);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAndDecipher();
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);

        } else {
            // Permission has already been granted
        }



    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(this.getClass().getCanonicalName(), "OnRequestResult");
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];

            if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    MainActivity.this.finish();
                }

            }
            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    MainActivity.this.finish();
                }
            }
        }
    }

    public void saveAndCipher(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_saveandcipher);
        dialog.setTitle("Password");
        dialog.setCancelable(true);

        final EditText password = dialog.findViewById(R.id.passwordcipher);

        Button button = dialog.findViewById(R.id.buttonsaveandcipherdialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals("toto")){
                    // showToast
                    Toast.makeText(getApplicationContext(), "Good password", Toast.LENGTH_SHORT).show();
                } else{
                    dialog.dismiss();
                    // other stuff to do
                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void loadAndDecipher(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_loadanddecipher);
        dialog.setTitle("Password");
        dialog.setCancelable(true);

        final EditText password = dialog.findViewById(R.id.passworddecipher);

        Button button = dialog.findViewById(R.id.buttonloadanddecipherdialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals("toto")){
                    // showToast
                    Toast.makeText(getApplicationContext(), "Good password", Toast.LENGTH_SHORT).show();
                } else{
                    dialog.dismiss();
                    // other stuff to do
                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });

        dialog.show();
    }
}