package com.example.cartitem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String cart_item_table_name="cart_item_table";

    private static final String item_name="name";
    private static  final String item_description="description";
    private static final String item_price="price";
    private static final String item_count="count";

    private static final String cart_item_schema="CREATE TABLE IF NOT EXISTS "+cart_item_table_name+"(" +
            "" +item_name+" text,"+
            "" +item_description+" text,"+
            "" +item_price+" integer,"+
            "" +item_count+" integer"+
            ")";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "cart.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(cart_item_schema);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.delete(cart_item_table_name,null,null);
        onCreate(sqLiteDatabase);
    }

    public void insertCart(CartItem cartItem){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(item_count,cartItem.getCount());
        values.put(item_description,cartItem.getDescription());
        values.put(item_price,cartItem.getPrice());
        values.put(item_name,cartItem.getName());
        int result=database.update(cart_item_table_name,values,item_name+"='"+cartItem.getName()+"'",null);
        if(result==0)
            database.insert(cart_item_table_name,null,values);
    }

    public void oneTimeInsert(CartItem cartItem){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+cart_item_table_name+" WHERE "+item_name+"='"+cartItem.getName()+"'",null);
        if(cursor==null || cursor.getCount() ==0)
            insertCart(cartItem);
        if (cursor != null) {
            cursor.close();
        }
    }



    public List<CartItem> getAllCardItem(){
        List<CartItem> cartItemList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+cart_item_table_name,null);
        if(cursor!=null && cursor.getCount()>=0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                CartItem cartItem=new CartItem();
                cartItem.setCount(cursor.getInt(cursor.getColumnIndex(item_count)));
                cartItem.setDescription(cursor.getString(cursor.getColumnIndex(item_description)));
                cartItem.setName(cursor.getString(cursor.getColumnIndex(item_name)));
                cartItem.setPrice(cursor.getInt(cursor.getColumnIndex(item_price)));
                cartItemList.add(cartItem);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return cartItemList;
    }

    public List<CartItem> getAllCardItem(int index){
        List<CartItem> cartItemList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+cart_item_table_name,null);
        if(cursor!=null && cursor.getCount()>=0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast() && index!=0){
                CartItem cartItem=new CartItem();
                cartItem.setCount(cursor.getInt(cursor.getColumnIndex(item_count)));
                cartItem.setDescription(cursor.getString(cursor.getColumnIndex(item_description)));
                cartItem.setName(cursor.getString(cursor.getColumnIndex(item_name)));
                cartItem.setPrice(cursor.getInt(cursor.getColumnIndex(item_price)));
                cartItemList.add(cartItem);
                index--;
                cursor.moveToNext();
            }
            cursor.close();
        }
        return cartItemList;
    }


    public CartItem getCartItem(String name){
        CartItem cartItem=null;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+cart_item_table_name+" WHERE "+item_name+"='"+name+"'",null);
        if(cursor!=null && cursor.getCount()!=0){
            cartItem=new CartItem();
            cursor.moveToFirst();
            cartItem.setCount(cursor.getInt(cursor.getColumnIndex(item_count)));
            cartItem.setDescription(cursor.getString(cursor.getColumnIndex(item_description)));
            cartItem.setName(cursor.getString(cursor.getColumnIndex(item_name)));
            cartItem.setPrice(cursor.getInt(cursor.getColumnIndex(item_price)));
            cursor.close();
        }
        return cartItem;
    }

    public int getCartItemCount(String name){
        int count=0;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+cart_item_table_name+" WHERE "+item_name+"='"+name+"'",null);
        if(cursor!=null && cursor.getCount()!=0){
            cursor.moveToFirst();
            count=cursor.getInt(cursor.getColumnIndex(item_count));
            cursor.close();
        }
        return count;
    }


    public int getTotalCount(){
        int total=0;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+cart_item_table_name,null);
        if(cursor!=null && cursor.getCount()>=0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                total+=cursor.getInt(cursor.getColumnIndex(item_count));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return total;
    }

    public int getTotalCost(){
        int total=0;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+cart_item_table_name,null);
        if(cursor!=null && cursor.getCount()>=0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                int count=cursor.getInt(cursor.getColumnIndex(item_count));
                int price=cursor.getInt(cursor.getColumnIndex(item_price));
                total+=(count*price);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return total;
    }



}
