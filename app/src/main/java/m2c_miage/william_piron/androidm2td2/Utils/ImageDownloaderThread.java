package m2c_miage.william_piron.androidm2td2.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import m2c_miage.william_piron.androidm2td2.MainActivity;
import m2c_miage.william_piron.androidm2td2.Models.Movie;

public class ImageDownloaderThread implements Runnable {

    private Movie movie;
    WeakReference<MainActivity> wrMainActivity;

    public ImageDownloaderThread(Movie movie, MainActivity mainActivity){
        this.movie = movie;
        this.wrMainActivity = new WeakReference<MainActivity>(mainActivity);
    }

    public void run(){
        URL url = null;
        Bitmap bitmap;
        try {
            url = new URL(movie.getImageUrl());
            URLConnection conn = url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            OutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            movie.setImage(((ByteArrayOutputStream) stream).toByteArray());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final MainActivity mainActivity = wrMainActivity.get();
        if (mainActivity != null) {
            mainActivity.handlerTui.post(new Runnable() {
                @Override
                public void run() {
                    mainActivity.adapter.notifyDataSetChanged();
                }
            });
        }

        /*mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.adapter.notifyDataSetChanged();
            }
        });*/
    }

    private byte[] BitmapToByte (Bitmap bitmap){
        byte [] bytearray;
        int size = bitmap.getRowBytes() * bitmap.getHeight();

        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        bytearray = byteBuffer.array();

        return bytearray;
    }

}