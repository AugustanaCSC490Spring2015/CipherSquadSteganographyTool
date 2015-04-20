package edu.augustana.csc490.steganographytool;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class decode extends ActionBarActivity {

    final private int SELECT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        ImageButton imageSelectorButton = (ImageButton) findViewById(R.id.attachImageButton);
        imageSelectorButton.setOnClickListener(imageSelectorListener);

        Button decodeButton = (Button) findViewById(R.id.decodeButton);
        decodeButton.setOnClickListener(decodeButtonListener);

        TextView messageTextView = (TextView) findViewById(R.id.displayTextView);
    }

    View.OnClickListener decodeButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decode, menu);
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
