package edu.augustana.csc490.steganographytool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import info.guardianproject.f5android.plugins.f5.Extract;
import info.guardianproject.f5android.plugins.PluginNotificationListener;
import info.guardianproject.f5android.plugins.f5.Extract.ExtractionListener;
import info.guardianproject.f5android.stego.StegoProcessThread;
import info.guardianproject.f5android.stego.StegoProcessor;
import info.guardianproject.f5android.stego.StegoProcessorListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;


public class DecodeActivity extends ActionBarActivity implements PluginNotificationListener, ExtractionListener, StegoProcessorListener {
    public final static String DUMP = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StegoTool";
    final private int SELECT_PHOTO = 1;
    public String path_to_decode_image;
    public Activity a;
    public ContentResolver cr;
    public File dump;
    public byte[] seed = new String("This is hopefully Temporary").getBytes();
    public StegoProcessor stego_processor;
    TextView messageTextView;
    String message;
    ProgressDialog ringProgressDialog;
    public ImageView selectedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        cr = getContentResolver();
        Intent i = getIntent();
        if(i.getClipData() != null){
            ClipData.Item item = i.getClipData().getItemAt(0);
            path_to_decode_image = IO.pullPathFromUri(a, item.getUri(), cr);

        }


        //Log.v("Uri", uri.toString());

        Button imageSelectorButton = (Button) findViewById(R.id.attachImageButton);
        imageSelectorButton.setOnClickListener(imageSelectorListener);

        Button decodeButton = (Button) findViewById(R.id.decodeButton);
        decodeButton.setOnClickListener(decodeButtonListener);

        selectedImageView = (ImageView) findViewById(R.id.selectedDecodeImagePreview);

        messageTextView = (TextView) findViewById(R.id.displayTextView);
        messageTextView.setMovementMethod(new ScrollingMovementMethod()); //code from http://stackoverflow.com/questions/1748977/making-textview-scrollable-in-android
        a = this;

        dump = new File(DUMP);
        if(!dump.exists())
            dump.mkdir();
        stego_processor = new StegoProcessor(a);
    }

    View.OnClickListener decodeButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            ringProgressDialog = ProgressDialog.show(a, "Please wait ...", "Working some magic ;)", true);

            Extract extract = new Extract(a, path_to_decode_image, seed);
            stego_processor.addThread((StegoProcessThread) extract, true);
        }
    };

    //Listener to select photo to decode message from
    View.OnClickListener imageSelectorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent imageSelectorIntent = new Intent(Intent.ACTION_PICK);
            imageSelectorIntent.setType("image/*");
            startActivityForResult(imageSelectorIntent, SELECT_PHOTO);
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_PHOTO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Uri cover_image_uri = data.getData();

                path_to_decode_image = IO.pullPathFromUri(a, cover_image_uri, cr);
                Bitmap selectedImagePreview = BitmapFactory.decodeFile(path_to_decode_image);
                selectedImageView.setImageBitmap(selectedImagePreview);
                //cover_image_file = new File(path_to_cover_image);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onExtractionResult(ByteArrayOutputStream baos){
        message = new String(baos.toByteArray());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageTextView.setText(message);
            }
        });
        File deletedImage = new File(path_to_decode_image);
        deletedImage.delete();
        ringProgressDialog.dismiss();


    }
    public void onFailure(){
        ringProgressDialog.dismiss();
        //maybe toss in a failure message
    }

    public void onUpdate(String with_message){
        String temp;
        if(with_message==null){
            temp="nothing here";
        }else{
            temp = with_message;
        }
        Log.v("update", temp);
    }
    public void onDestroy(){
        stego_processor.destroy();
        super.onDestroy();
    }
    public void onProcessorQueueAborted(){

    }





}
