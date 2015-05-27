package com.shoheiaoki.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends Activity {
    ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainListView = (ListView) findViewById(R.id.myListView);

        LinkedHashMap<String, ArrayList<Product>> cp = doJSONSerialize();

        ArrayList<Category> categories = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Product>> e : cp.entrySet()) {
            String category = e.getKey();
            ArrayList<Product> products = e.getValue();
            Category c = new Category();
            c.setName(category);
            c.setProducts(products);
            categories.add(c);
        }

        final CategoryAdapter cAdapter = new CategoryAdapter(this, 0, categories);
        mainListView.setAdapter(cAdapter);

        Button uploadBtn = (Button) findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < cAdapter.getCount(); i++) {
                    ArrayList<Product> ps = cAdapter.getItem(i).getProducts();
                    for (int j = 0; j < ps.size(); j++) {
                        Product p = ps.get(j);
                        String name = p.getName();
                        String amount = p.getAmount();
                        Log.e("hoge", name+" : "+amount);
                    }
                }
            }
        });
    }

    public class CategoryAdapter extends ArrayAdapter<Category> {
        private LayoutInflater layoutInflater;

        public CategoryAdapter(Context context, int resource, ArrayList<Category> categories) {
            super(context, resource, categories);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
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
            ListView subListView = (ListView) convertView.findViewById(R.id.productListView);

            ProductAdapter pAdapter = new ProductAdapter(MainActivity.this, 0, products);
            subListView.setAdapter(pAdapter);

            return convertView;
        }
    }

    public class ProductAdapter extends ArrayAdapter<Product> {
        LayoutInflater layoutInflater;

        public ProductAdapter(Context context, int resource, ArrayList<Product> products) {
            super(context, resource, products);
            this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.row_product,
                        parent,
                        false
                );
            }
            TextView pdTextView = (TextView) convertView.findViewById(R.id.pdTextView);
            TextView priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
            final Product p = (Product) getItem(position);
            pdTextView.setText(p.getName());
            priceTextView.setText(p.getPrice() + " UGX");
            final TextView amtTextView = (TextView) convertView.findViewById(R.id.amtTextView);
            Button cntUpBtn = (Button) convertView.findViewById(R.id.cntUpBtn);
            Button cntDownBtn = (Button) convertView.findViewById(R.id.cntDownBtn);
            cntUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   int cnt = Integer.valueOf(amtTextView.getText().toString());
                   cnt = cnt + 1;
                   amtTextView.setText(String.valueOf(cnt));
                   p.setAmount(String.valueOf(cnt));
                }
            });
            cntDownBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cnt = Integer.valueOf(amtTextView.getText().toString());
                    cnt = cnt - 1;
                    amtTextView.setText(String.valueOf(cnt));
                    p.setAmount(String.valueOf(cnt));
                }
            });


            return convertView;
        }
    }

    public class Product {
        private String name;
        private String price;
        private String amount;

        public Product() {
           amount = "0";
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAmount() {
            return amount;
        }


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


    protected LinkedHashMap<String, ArrayList<Product>> doJSONSerialize() {
        LinkedHashMap<String, ArrayList<Product>> cp = new LinkedHashMap<>();
        try {
            JSONObject rootJSONObject = new JSONObject(getJSONString());
            JSONArray rootJSONArray = rootJSONObject.getJSONArray("categories");
            for (int i = 0; i < rootJSONArray.length(); i++) {
                JSONObject categoryJSONObject = rootJSONArray.getJSONObject(i);
                String category_name = categoryJSONObject.getString("name");
                Log.e("hoge", category_name);
                ArrayList<Product> products = new ArrayList<>();
                try {
                    JSONArray productJSONArray = new JSONArray(categoryJSONObject.getString("products"));
                    for (int j = 0; j < productJSONArray.length(); j++) {
                        JSONObject productJSONObject = productJSONArray.getJSONObject(j);
                        String name = productJSONObject.getString("name");
                        String price = productJSONObject.getString("price");
                        Log.e("hoge", name);
                        Log.e("hoge", price);
                        Product p = new Product();
                        p.setName(name);
                        p.setPrice(price);
                        products.add(p);
                    }
                    cp.put(category_name, products);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cp;
    }


    public String getJSONString() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.product_categories);
            byte[] buffer = new byte[is.available()];
            while ((is.read(buffer)) != -1) {
            }
            json = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
