package edu.augustana.csc490.steganographytool;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import info.guardianproject.f5android.plugins.f5.Embed;
import info.guardianproject.f5android.stego.*;
import info.guardianproject.f5android.plugins.PluginNotificationListener;



import java.io.File;
//import info.guardianproject.f5android.plugins.f5.Embed;
//import info.guardianproject.f5android.plugins.f5.Embed.EmbedListener;
//import info.guardianproject.f5android.stego.StegoProcessThread;
//import info.guardianproject.f5android.stego.StegoProcessor;



public class EncodeActivity extends ActionBarActivity implements Embed.EmbedListener, PluginNotificationListener, MediaScannerConnection.MediaScannerConnectionClient, MediaScannerConnection.OnScanCompletedListener, StegoProcessorListener{
    public final static String DUMP = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StegoTool";
    private final int SELECT_PHOTO = 1;
    private Activity a;
    public ContentResolver cr;
    public String path_to_cover_image;
    public String secret_message;
    public EditText messageTextView;
    public byte[] seed = new String("This is hopefully Temporary").getBytes();
    public StegoProcessor stego_processor;
    public AlertDialog alertDialog;
    public File dump;
    public ProgressDialog ringProgressDialog;
    private String imageDeleted;
    public MediaScannerConnection conn;
    public File finalFile;
    public ImageButton imageSelectorButton;
    public PopupWindow error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        imageSelectorButton = (ImageButton) findViewById(R.id.selectImageButton);
        imageSelectorButton.setOnClickListener(imageSelectorListener);

        final Button encodeButton = (Button) findViewById(R.id.encodeButton);
        encodeButton.setOnClickListener(encodeButtonListener);

        Button shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(shareButtonListener);

        alertDialog = new AlertDialog.Builder(this).create();

        messageTextView = (EditText) findViewById(R.id.messageEditText);

        // To add once works
        messageTextView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.i("Clicked", "" + keyCode);
                    encodeButton.performClick();
                }
                return false; // very important
            }
        });


        cr = getContentResolver();
        dump = new File(DUMP);
        if(!dump.exists())
            dump.mkdir();
        a = this;
        stego_processor = new StegoProcessor(a);
    }


    View.OnClickListener encodeButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            secret_message=messageTextView.getText().toString();
            if(secret_message==null || secret_message.equals("") || path_to_cover_image==null){
                showErrorMessage();
            }else{
               // encodeThread = new EncodeThread();
                //encodeThread.start();
                ringProgressDialog = ProgressDialog.show(a, "Please wait ...", "Working some magic ;)", true);
                messageTextView.setText("");
                File dump = new File(DUMP);
                imageDeleted = path_to_cover_image;
                path_to_cover_image=IO.downsampleImage(path_to_cover_image, dump);
                File deletedImage = new File(imageDeleted);
                deletedImage.delete();
                imageDeleted=path_to_cover_image;
                Embed embed = new Embed(a, dump.getName(),path_to_cover_image , secret_message, seed) {
                    @Override
                    public void run() {

                        super.run();
                    }
                };
                stego_processor.addThread((StegoProcessThread) embed, true);


            }
        }
    };

    //Listener to select image to encode the message into
    View.OnClickListener imageSelectorListener = new View.OnClickListener(){
        //File f = new File(".jpeg");
        @Override
        public void onClick(View view){
            Intent imageSelectorIntent = new Intent(Intent.ACTION_PICK);
            imageSelectorIntent.setType("image/*");
            startActivityForResult(imageSelectorIntent, SELECT_PHOTO);
            //startActivityForResult(Intent.createChooser(imageSelectorIntent,
            //getResources().getString(R.string.selectImage)), SELECT_PHOTO);
        }
        //@Override

       /* public void OnActivityResult(){
            if(f.exists()) {
                Drawable d = Drawable.createFromPath(f);
                imageSelectorButton.setImageResource(d);
            }
        }*/

    };
    /*Method to handle the share button being clicked
    * The image Uri is loaded into an intent and shipped off to another app
    * */
    View.OnClickListener shareButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(finalFile != null){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(finalFile.toURI().toString()));
                startActivity(Intent.createChooser(intent, "Share Image"));
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_PHOTO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Uri cover_image_uri = data.getData();

                path_to_cover_image = IO.pullPathFromUri(a, cover_image_uri, cr);
                Log.i("file path", path_to_cover_image);
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

    /**
     * Saves the newly encoded image to the user's device and displays a popup to inform the user upon completion.
     * @param outFile
     */
    public void onEmbedded(final File outFile) {
        String extension = outFile.getName().substring(outFile.getName().lastIndexOf("_"));
        File tempFile = new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), dump.getName()), outFile.getName().replace(extension, ".jpg"));
        outFile.renameTo(tempFile);

        ringProgressDialog.dismiss();

        File deletedImage = new File(imageDeleted);
        deletedImage.delete();
        finalFile = tempFile;
        MediaScannerConnection.scanFile(a, new String[]{tempFile.getAbsolutePath()}, null, EncodeActivity.this);

        //code adapted from http://stackoverflow.com/questions/13082244/show-alertdialog-after-progressdialog-closes
        runOnUiThread(new Runnable() {
            public void run() {
                alertDialog.setTitle("Finished!");
                alertDialog.setMessage("Your encoded image has been saved.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }


    public void onFailure(){
        ringProgressDialog.dismiss();
        //maybe toss in an error message here
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

    public void onMediaScannerConnected(){


    }
    public void onScanCompleted(String path, Uri uri){

    }

    public void onDestroy(){
        stego_processor.destroy();
        super.onDestroy();
    }
    public void onProcessorQueueAborted(){

    }

    // method is the modified version of showSimplePopUp() from
    // http://www.androiddom.com/2011/06/displaying-android-pop-up-dialog.html
    private void showErrorMessage() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Error");
        if (secret_message.equals("") && path_to_cover_image == null) {
            helpBuilder.setMessage("You have not selected an image or typed in a secret message, please go back and make the necessary changes.");
        } else if (path_to_cover_image == null) {
            helpBuilder.setMessage("You have not selected an image, please go back and select an image you want to encode.");
        } else if (secret_message.equals("")) {
            helpBuilder.setMessage("You have not typed in a secret message, please go back and create a secret message.");
        }
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

}

