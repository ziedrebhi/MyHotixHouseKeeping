package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class ImageViewActivity extends Activity {
    private static final String IMAGE_DIRECTORY_NAME = "MY_HOTIX_HOUSEKEEPING";
    ImageView image;
    String nameImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        image = (ImageView) findViewById(R.id.imageView1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameImage = extras.getString("name");
            //The key argument here must match that used in the other activity
        }
    }

    @Override
    protected void onResume() {
        image.setImageBitmap(getImageFromSD());
        super.onResume();
    }

    public Bitmap getImageFromSD() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        String photoPath = mediaStorageDir.getPath() + "/" + nameImage;
        Log.i("Camera ", "Path image " + photoPath);
        Bitmap icon = BitmapFactory.decodeFile(photoPath, options);
        return icon;
    }

}
