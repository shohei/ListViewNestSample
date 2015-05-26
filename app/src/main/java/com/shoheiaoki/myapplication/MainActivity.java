package com.shoheiaoki.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {
    ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainListView = (ListView) findViewById(R.id.myListView);

        HashMap<String,ArrayList<Product>> cp = createSampleData();

        ArrayList<Category> categories = new ArrayList<>();
        for(Map.Entry<String,ArrayList<Product>> e: cp.entrySet()){
            String category = e.getKey();
            ArrayList<Product> products = e.getValue();
            Category c = new Category();
            c.setName(category);
            c.setProducts(products);
            categories.add(c);
        }

        CategoryAdapter cAdapter = new CategoryAdapter(this,0,categories);
        mainListView.setAdapter(cAdapter);
    }

    public class CategoryAdapter extends ArrayAdapter<Category>{
        private LayoutInflater layoutInflater;

        public CategoryAdapter(Context context, int resource, ArrayList<Category> categories) {
            super(context, resource, categories);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = layoutInflater.inflate(
                       R.layout.row_category,
                       parent,
                       false
                );
            }
            Category category = (Category) getItem(position);
            TextView cTextView = (TextView) convertView.findViewById(R.id.cTextView);
            cTextView.setText(category.getName());
            ArrayList<Product> products = category.getProducts();
            ListView subListView =  (ListView) convertView.findViewById(R.id.productListView);

            ProductAdapter pAdapter = new ProductAdapter(MainActivity.this,0,products);
            subListView.setAdapter(pAdapter);

            return convertView;
        }
    }

    public class ProductAdapter extends ArrayAdapter<Product>{
        LayoutInflater layoutInflater;

        public ProductAdapter(Context context, int resource, ArrayList<Product> products) {
            super(context, resource, products);
            this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        private int lastFocussedPosition = -1;
        private Handler handler = new Handler();
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = layoutInflater.inflate(
                        R.layout.row_product,
                        parent,
                        false
                );
            }
            TextView pdTextView = (TextView) convertView.findViewById(R.id.pdTextView);
            TextView priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
            final EditText amtEditText = (EditText) convertView.findViewById(R.id.amtEditText);
            Product p = (Product) getItem(position);
            pdTextView.setText(p.getName());
            priceTextView.setText(p.getPrice());
//            amtEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        handler.postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                if (lastFocussedPosition == -1 || lastFocussedPosition == position) {
//                                    lastFocussedPosition = position;
//                                    amtEditText.requestFocus();
//                                }
//                            }
//                        }, 200);
//                    } else {
//                        lastFocussedPosition = -1;
//                    }
//                }
//            });

            return convertView;
        }
    }

    public class Product {
        private String name;
        private String price;

        public String getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public class Category {
        private String name;
        private ArrayList<Product> products;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Product> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<Product> products) {
            this.products = products;
        }
    }

    protected HashMap<String,ArrayList<Product>> createSampleData(){
        HashMap<String,ArrayList<Product>> cp = new HashMap<>();
         ArrayList<Product> products = new ArrayList<>();
          Product p = new Product();
          p.setName("cookie1");
          p.setPrice("100");
          products.add(p);
          Product p2 = new Product();
          p2.setName("cookie1");
          p2.setPrice("100");
         products.add(p2);
        cp.put("cookie",products);
            ArrayList<Product> products2 = new ArrayList<>();
            Product p3 = new Product();
            p3.setName("milk1");
            p3.setPrice("100");
            products2.add(p3);
            Product p4 = new Product();
            p4.setName("milk2");
            p4.setPrice("100");
            products2.add(p4);
        cp.put("milk",products2);

        return cp;
    }


}
