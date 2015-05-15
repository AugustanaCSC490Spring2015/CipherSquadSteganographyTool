package edu.augustana.csc490.steganographytool;


import android.content.Intent;
import android.view.View;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button encodeButton = (Button) findViewById(R.id.encodeButton);
        encodeButton.setOnClickListener(encodeButtonListener);

        Button decodeButton = (Button) findViewById(R.id.decodeButton);
        decodeButton.setOnClickListener(decodeButtonListener);
    }

    View.OnClickListener encodeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent encodeIntent = new Intent(MainActivity.this, EncodeActivity.class);
            startActivity(encodeIntent);
        }
    };


    View.OnClickListener decodeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent decodeIntent = new Intent(MainActivity.this, DecodeActivity.class);
            startActivity(decodeIntent);
        }
    };
}
