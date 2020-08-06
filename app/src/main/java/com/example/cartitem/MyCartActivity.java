package com.example.cartitem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity implements View.OnClickListener, CustomOnClickListner {

    final int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    ImageView backArrow;
    int index;
    AppCompatTextView totalCost;
    RecyclerView recyclerView;
    CartItemAdapter adapter;
    DatabaseHelper databaseHelper;
    AppCompatTextView showmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        index=2;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setContentView(R.layout.activity_my_cart);
        recyclerView = findViewById(R.id.recyclerViewReviewOrder);
        showmore = findViewById(R.id.show_more);
        showmore.setOnClickListener(this);
        adapter = new CartItemAdapter(databaseHelper.getAllCardItem(index), this, this,1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        backArrow = findViewById(R.id.back_arror);
        totalCost = findViewById(R.id.total_text_cost);
        backArrow.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        totalCost.setText("Rs. " + databaseHelper.getTotalCost());
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_arror:
                onBackPressed();
                break;
            case R.id.show_more:
                index=index+2;
                int position=adapter.getPosition()-1;
                adapter.setCartItemList(databaseHelper.getAllCardItem(index));
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(position);
                if(index>=20)
                    showmore.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void addItemCount(String name) {
        CartItem cartItem = databaseHelper.getCartItem(name);
        if (cartItem != null) {
            cartItem.setCount(cartItem.getCount() + 1);
            databaseHelper.insertCart(cartItem);
            totalCost.setText("Rs. " + databaseHelper.getTotalCost());
        }
    }

    @Override
    public void subItemCount(String name) {
        CartItem cartItem = databaseHelper.getCartItem(name);
        if (cartItem != null) {
            cartItem.setCount(cartItem.getCount() - 1);
            databaseHelper.insertCart(cartItem);
            totalCost.setText("Rs. " + databaseHelper.getTotalCost());
        }
    }
}
