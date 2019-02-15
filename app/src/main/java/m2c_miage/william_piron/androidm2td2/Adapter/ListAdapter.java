package m2c_miage.william_piron.androidm2td2.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import m2c_miage.william_piron.androidm2td2.Models.Movie;
import m2c_miage.william_piron.androidm2td2.R;

public class ListAdapter extends ArrayAdapter<Movie> {

    private LinearLayout item;
    private ImageView movieImage;
    private TextView movieName;
    private TextView movieDate;
    private TextView movieProd;
    private TextView movieReal;

    public ListAdapter(@NonNull Context context, @NonNull List<Movie> objects) {
        super(context, R.layout.activity_list_items, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Movie movie = (Movie) getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_items,parent, false);
        }

        item = (LinearLayout) view.findViewById(R.id.activity_list_items_item);
        movieImage = (ImageView) view.findViewById(R.id.imageViewMovie);
        movieName = (TextView) view.findViewById(R.id.textViewName);
        movieDate = (TextView) view.findViewById(R.id.textViewDate);
        movieProd = (TextView) view.findViewById(R.id.textViewProd);
        movieReal = (TextView) view.findViewById(R.id.textViewReal);

        movieName.setText(movie.getName());
        movieDate.setText(movie.getDate());
        movieProd.setText(movie.getProd());
        movieReal.setText(movie.getReal());

        if(movie.getImage() != null) {
            //movieImage.setImageBitmap(movie.getImage());
            /*byte[] arraytmp = movie.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(arraytmp, 0, arraytmp.length);
            movieImage.setImageBitmap(bmp);*/
            InputStream stream = new ByteArrayInputStream(movie.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            movieImage.setImageBitmap(bitmap);
        } else {
            movieImage.setImageBitmap(null);
        }

        if(position % 2 == 0) {
            item.setBackgroundColor(Color.WHITE);
        } else{
            item.setBackgroundColor(Color.GRAY);
        }

        return view;
    }

}