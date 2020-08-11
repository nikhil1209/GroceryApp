package com.nikhil.groceryapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.nikhil.groceryapp.Data.DatabaseHandler;
import com.nikhil.groceryapp.Model.Grocery;
import com.nikhil.groceryapp.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogueBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText groceryQuantitiy;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new DatabaseHandler(this);

//        byPassActivity();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    createPopupDialogue();
                }
            });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    private void createPopupDialogue() {
        dialogueBuilder=new AlertDialog.Builder(this);
        View view= getLayoutInflater().inflate(R.layout.popup,null);
        groceryItem=(EditText) view.findViewById(R.id.groceryItem);
        groceryQuantitiy=(EditText)view.findViewById(R.id.qty);
        saveButton=(Button) view.findViewById(R.id.saveButton);

        dialogueBuilder.setView(view);
        dialog=dialogueBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:save to db
                //TODO:GO to next screen

                if(!groceryItem.getText().toString().isEmpty() && !groceryQuantitiy.getText().toString().isEmpty())
                    saveGroceryToDb(v);
            }
        });


    }

    private void saveGroceryToDb(View v) {
        Grocery grocery=new Grocery();

        String newGrocery=groceryItem.getText().toString();
        String newQuantity=groceryQuantitiy.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newQuantity);

        //Save to DB
        db.addGrocery(grocery);

        Snackbar.make(v,"Data Saved!", Snackbar.LENGTH_LONG).show();

        Log.d("Item Added ID: ",String.valueOf(db.getGroceryCount()));

        //delaying
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start next activity
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1000); //1 sec
    }

    public void byPassActivity(){
        if(db.getGroceryCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }
}