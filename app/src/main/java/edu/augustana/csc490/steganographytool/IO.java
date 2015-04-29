package edu.augustana.csc490.steganographytool;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

/**
 * Set of useful methods borrowed from the pixelknot project
 * Check them out on guthub @ https://github.com/guardianproject/PixelKnot/
 */
public class IO {
    public final static String DUMP = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StegoTool";

    public static String pullPathFromUri(Context context, Uri uri, ContentResolver cr) {
        if(uri.getScheme() != null && uri.getScheme().equals("file"))
            return URLDecoder.decode(uri.toString().replace("file://", ""));
        else if(uri.getScheme() != null && uri.getScheme().equals("content")) {
            try {

                File tmp = new File(DUMP, System.currentTimeMillis() + ".jpg");
                Log.v("Stego Tool", ""+DUMP);
                InputStream is = cr.openInputStream(uri);
                byte[] file_data = new byte[is.available()];

                is.read(file_data, 0, file_data.length);
                is.close();

                FileOutputStream fos = new FileOutputStream(tmp);
                fos.write(file_data);
                fos.flush();
                fos.close();

                return tmp.getAbsolutePath();
            } catch(FileNotFoundException e) {
                //Log.e('', e.toString());
                e.printStackTrace();
            } catch (IOException e) {
               // Log.e('', e.toString());
                e.printStackTrace();
            }
        } else {
            String path = null;
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);

            if(c != null && c.moveToFirst()) {

                for(String s : c.getColumnNames()) {
                    Log.d("", "col: " + s);
                }

                path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                c.close();
                Log.d("", "path found: " + path);
            } else {
                Log.d("", "URI is null");
            }

            return path;
        }

        return null;
    }
    public static String downsampleImage(String cover_image_name, File dump) {
        int scale = 1;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(cover_image_name, opts);
        int x = Math.max(opts.outHeight, opts.outWidth);

        if(x > 1280)
            scale = (int) Math.ceil(x/1280.0);

        Log.d("", "FYI scale is " + scale + " on img of " + opts.outWidth + " x " + opts.outHeight);

        opts = new BitmapFactory.Options();
        opts.inSampleSize = scale;

        Bitmap b = BitmapFactory.decodeFile(cover_image_name, opts);
        try {
            File downsampled_image = new File(dump, System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(downsampled_image);
            b.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            b.recycle();
            return downsampled_image.getAbsolutePath();
        } catch (FileNotFoundException e) {
            Log.e("", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("", e.toString());
            e.printStackTrace();
        }

        return null;
    }

}
