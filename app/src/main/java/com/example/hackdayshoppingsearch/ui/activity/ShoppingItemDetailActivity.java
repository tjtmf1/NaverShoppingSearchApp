package com.example.hackdayshoppingsearch.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.ui.fragment.ShoppingItemDetailFragment;
import com.example.hackdayshoppingsearch.container.ShoppingItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ShoppingItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_item_detail);
        ShoppingItem item = getItemToShow();
        if(item == null) {
            finish();
        }
        initView(item);
    }

    private void initView(ShoppingItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detail_activity_host_layout, ShoppingItemDetailFragment.newInstance(item)).commit();
    }

    public ShoppingItem getItemToShow() {
        Intent intent = getIntent();
        return (ShoppingItem) intent.getSerializableExtra("item");
    }


    private ShoppingItem getShoppingItem() {
        ShoppingItem item = new ShoppingItem();
        item.setBrand("그린하우스");
        item.setCategory1("디지털/가전");
        item.setCategory2("음향가전");
        item.setCategory3("이어폰");
        item.setCategory4("");
        item.setHprice("0");
        item.setImage("https://shopping-phinf.pstatic.net/main_2219112/22191127825.jpg");
        item.setLink("https://search.shopping.naver.com/gate.nhn?id=22191127825");
        item.setLprice("23100");
        item.setMaker("그린하우스");
        item.setMallName("겟업");
        item.setProductId("22191127825");
        item.setProductType("2");
        item.setTitle("GREEN HOUSE 이어폰 홀더 블루/라이트 블루/그린 GH-ERH-<b>QA</b>");
        return item;
    }

}
