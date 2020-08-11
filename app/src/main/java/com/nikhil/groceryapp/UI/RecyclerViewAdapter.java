package com.nikhil.groceryapp.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nikhil.groceryapp.Activities.DetailsActivity;
import com.nikhil.groceryapp.Data.DatabaseHandler;
import com.nikhil.groceryapp.Model.Grocery;
import com.nikhil.groceryapp.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {
    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grocery grocery=groceryItems.get(position);
        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View view,Context ctx) {

            super(view);
            context=ctx;
            groceryItemName=(TextView) view.findViewById(R.id.name);
            quantity=(TextView) view.findViewById(R.id.qty);
            dateAdded=(TextView) view.findViewById(R.id.dateAdded);

            editButton=(Button) view.findViewById(R.id.editButton);
            deleteButton=(Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //goto next screen
                    int position=getAdapterPosition();

                    Grocery grocery=groceryItems.get(position);
                    Intent intent=new Intent(context, DetailsActivity.class);
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("name",grocery.getName());
                    intent.putExtra("quantity",grocery.getQuantity());
                    intent.putExtra("date",grocery.getDateItemAdded());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editButton:
                    int pos=getAdapterPosition();
                    Grocery grocery=groceryItems.get(pos);
                    editItem(grocery);

                    break;
                case R.id.deleteButton:
                    int position=getAdapterPosition();
                    Grocery grocery1=groceryItems.get(position);
                    deleteItem(grocery1.getId());

                    break;
            }
        }
        public void deleteItem(final int id){
            //create an alert dialog
            alertDialogBuilder=new AlertDialog.Builder(context);

            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.confirmation_dialog,null);

            Button noButton=(Button) view.findViewById(R.id.noButton);
            Button yesButton=(Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            alertDialog=alertDialogBuilder.create();
            alertDialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db=new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    alertDialog.dismiss();
                }
            });

        }
        public void editItem(final Grocery grocery){
            //create an alert dialog
            alertDialogBuilder=new AlertDialog.Builder(context);

            inflater=LayoutInflater.from(context);
            final View view=inflater.inflate(R.layout.popup,null);

            final EditText groceryItem=(EditText)view.findViewById(R.id.groceryItem);
            final EditText quantity=(EditText) view.findViewById(R.id.qty);
            Button saveButton=(Button) view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            alertDialog=alertDialogBuilder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db=new DatabaseHandler(context);

                    //update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty()&&!quantity.getText().toString().isEmpty()){
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }
                    else{
                        Snackbar.make(view,"Please Enter Grocery Item and Quantity",Snackbar.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();

                }
            });


        }


    }
}
