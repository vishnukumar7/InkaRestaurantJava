package com.example.cartitem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements CustomOnClickListner {

    final int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    AppCompatButton viewCart;
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private CartItemAdapter cartItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerViewBookScreen);
        databaseHelper = new DatabaseHelper(this);
        viewCart = findViewById(R.id.view_cart_items);
        initInsert();
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyCartActivity.class));
            }
        });
    }

    void initInsert(){
        for(int i=1;i<=20;i++){
            CartItem cartItem=new CartItem();
            cartItem.setCount(0);
            cartItem.setDescription("description "+i);
            cartItem.setName("item "+i);
            cartItem.setPrice(5);
            databaseHelper.oneTimeInsert(cartItem);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(flags);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemAdapter = new CartItemAdapter(databaseHelper.getAllCardItem(), this, this,0);
        recyclerView.setAdapter(cartItemAdapter);
        recyclerView.setHasFixedSize(false);
        int count = databaseHelper.getTotalCount();
        if (count != 0)
            viewCart.setText("VIEW CART(" + count + " ITEMS )");
        else
            viewCart.setText("VIEW CART");
    }

    @Override
    public void addItemCount(String name) {
        CartItem cartItem = databaseHelper.getCartItem(name);
        if (cartItem != null) {
            cartItem.setCount(cartItem.getCount() + 1);
            databaseHelper.insertCart(cartItem);
        }
        int count = databaseHelper.getTotalCount();
        if (count != 0)
            viewCart.setText("VIEW CART(" + count + " ITEMS )");
        else
            viewCart.setText("VIEW CART");
    }

    @Override
    public void subItemCount(String name) {
        CartItem cartItem = databaseHelper.getCartItem(name);
        if (cartItem != null) {
            cartItem.setCount(cartItem.getCount() - 1);
            databaseHelper.insertCart(cartItem);
        }
        int count = databaseHelper.getTotalCount();
        if (count != 0)
            viewCart.setText("VIEW CART(" + count + " ITEMS )");
        else
            viewCart.setText("VIEW CART");
    }
}
