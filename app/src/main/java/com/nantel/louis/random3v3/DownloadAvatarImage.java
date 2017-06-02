package com.nantel.louis.random3v3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Louis Nantel on 2017-05-05.
 *
 */

public class DownloadAvatarImage extends AsyncTask<String, Void, Bitmap>{

    private Context context;
    private ImageView avatarView;

    public DownloadAvatarImage(Context context, ImageView avatarView){
        this.context = context;
        this.avatarView = avatarView;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            InputStream is = (InputStream) new URL(params[0]).getContent();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avatar);
        }
    }

    @Override
    protected void onPostExecute(Bitmap result){
        int dimensions = 75*(context.getResources().getDisplayMetrics().densityDpi/160);
        avatarView.setImageBitmap(Bitmap.createScaledBitmap(result, dimensions, dimensions, true));
    }

}
