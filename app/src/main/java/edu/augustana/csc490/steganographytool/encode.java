package edu.augustana.csc490.steganographytool;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;



public class encode extends ActionBarActivity {

    private final int SELECT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        ImageButton imageSelectorButton = (ImageButton) findViewById(R.id.selectImageButton);
        imageSelectorButton.setOnClickListener(imageSelectorListener);

        Button encodeButton = (Button) findViewById(R.id.encodeButton);
        encodeButton.setOnClickListener(encodeButtonListener);

        EditText messageTextView = (EditText) findViewById(R.id.messageEditText);

    }


    View.OnClickListener encodeButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){

        }
    };

    //Listener to select image to encode the message into
    View.OnClickListener imageSelectorListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent imageSelectorIntent = new Intent(Intent.ACTION_PICK);
            imageSelectorIntent.setType("image/*");
            startActivityForResult(imageSelectorIntent, SELECT_PHOTO);
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encode, menu);
        return true;
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
}
