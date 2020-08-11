package com.nikhil.groceryapp.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.nikhil.groceryapp.Data.DatabaseHandler;
import com.nikhil.groceryapp.Model.Grocery;
import com.nikhil.groceryapp.R;
import com.nikhil.groceryapp.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this,MainActivity.class));
                finish();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        db=new DatabaseHandler(this);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList=new ArrayList<>();
        listItems=new ArrayList<>();

        //Get items from db
        groceryList=db.getAllGrocery();

        for(Grocery c: groceryList){
            Grocery grocery=new Grocery();
            grocery.setName(c.getName());
            grocery.setId(c.getId());
            grocery.setQuantity("qty: "+c.getQuantity());
            grocery.setDateItemAdded("Added on: "+c.getDateItemAdded());

            listItems.add(grocery);
        }
        recyclerViewAdapter=new RecyclerViewAdapter(this,listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}