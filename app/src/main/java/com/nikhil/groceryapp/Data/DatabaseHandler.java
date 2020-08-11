package com.nikhil.groceryapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nikhil.groceryapp.Model.Grocery;
import com.nikhil.groceryapp.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    Context ctx;

    public DatabaseHandler( Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+Constants.TABLE_NAME+"("+Constants.KEY_ID+" INTEGER PRIMARY KEY,"+Constants.KEY_GROCERY_ITEM+" TEXT,"+Constants.KEY_QTY_NUMBER+" TEXT,"+Constants.KEY_DATE_NAME+" LONG)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        onCreate(db);
    }
    // CRUD Operations : CREATE, READ, UPDATE, DELETE METHODS

    //Add grocery
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());//get system time

        //Insert the row
        db.insert(Constants.TABLE_NAME,null,values);

        Log.d("Saved! ","Saved to db");

    }

    //Get grocery
    public Grocery getGrocery(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,Constants.KEY_GROCERY_ITEM,Constants.KEY_QTY_NUMBER,Constants.KEY_DATE_NAME},Constants.KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor!=null)
            cursor.moveToFirst();

            Grocery grocery=new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

            //convert timestamp to something readable
            java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
            String formatedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

            grocery.setDateItemAdded(formatedDate);


        return grocery;
    }

    //get all grocery
    public List<Grocery> getAllGrocery(){
        SQLiteDatabase db=this.getReadableDatabase();

        List<Grocery> groceryList=new ArrayList<>();

        Cursor cursor=db.query(Constants.TABLE_NAME,new String[]{
                Constants.KEY_ID,Constants.KEY_GROCERY_ITEM,Constants.KEY_QTY_NUMBER,Constants.KEY_DATE_NAME},
                null,null,null,null,Constants.KEY_DATE_NAME+" DESC");

        if(cursor.moveToFirst()){
            do{
                Grocery grocery=new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
                String formatedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                grocery.setDateItemAdded(formatedDate);

                //Add to grocery list
                groceryList.add(grocery);
            }while(cursor.moveToNext());
        }

        return groceryList;
    }

    //updated grocery
    public int updateGrocery(Grocery grocery){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());//get system time

        //update row
        return db.update(Constants.TABLE_NAME,values,Constants.KEY_ID+"=?",new String[]{String.valueOf(grocery.getId())});
    }

    //delete grocery
    public void deleteGrocery(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();

    }

    //get count
    public int getGroceryCount(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+Constants.TABLE_NAME,null);

        return cursor.getCount();
    }
}
