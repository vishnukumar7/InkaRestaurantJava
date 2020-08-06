package com.example.cartitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CustomViewHolder> {

    private List<CartItem> cartItemList;
    private CustomOnClickListner customOnClickListner;
    private DatabaseHelper databaseHelper;
    private int position;

    public CartItemAdapter(List<CartItem> cartItemList, CustomOnClickListner customOnClickListner, Context context) {
        this.cartItemList = cartItemList;
        databaseHelper=new DatabaseHelper(context);
        this.customOnClickListner = customOnClickListner;
    }

    public int getPosition() {
        return this.position;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        final CartItem cartItem = cartItemList.get(position);
        buttonView(holder,cartItem.getName());
        this.position=position;
       // holder.count.setText("" + cartItem.getCount());
        holder.description.setText(cartItem.getDescription());
        holder.price.setText("Rs." + cartItem.getPrice());
        holder.name.setText(cartItem.getName());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customOnClickListner.addItemCount(cartItem.getName());
                buttonView(holder,cartItem.getName());
            }
        });

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customOnClickListner.subItemCount(cartItem.getName());
                buttonView(holder,cartItem.getName());
            }
        });
    }

    private void buttonView(CustomViewHolder holder,String name) {
        int count= databaseHelper.getCartItemCount(name);
       holder.count.setText(""+count);
        if (count == 0) {
            holder.sub.setEnabled(false);
            holder.sub.setClickable(false);

        }
        else if (count == 20) {
            holder.add.setEnabled(false);
            holder.add.setClickable(false);

        }
        else{
            holder.sub.setEnabled(true);
            holder.sub.setClickable(true);
            holder.add.setEnabled(true);
            holder.add.setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView name, description, price, count;
        AppCompatButton add, sub;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.card_text_name);
            description = itemView.findViewById(R.id.card_text_description);
            price = itemView.findViewById(R.id.card_text_price);
            count = itemView.findViewById(R.id.card_text_count);

            add = itemView.findViewById(R.id.card_text_add);
            sub = itemView.findViewById(R.id.card_text_sub);
        }
    }
}
