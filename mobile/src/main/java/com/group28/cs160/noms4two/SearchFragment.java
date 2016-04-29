package com.group28.cs160.noms4two;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.group28.cs160.shared.NutritionFacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private Button searchButton;
    private List<NutritionFacts> data;
    private EditText searchText;
    private final FatSecretAPI api = new FatSecretAPI("6fa2832128934cbba364d29b7db8a557", "4291459ec6784ca0b35632ee3449a6ff");
    private TextView text;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, parent, false);

        searchText = (EditText) rootView.findViewById (R.id.searchText);
        searchButton = (Button) rootView.findViewById (R.id.searchButton);
        text = (TextView) rootView.findViewById (R.id.textView3);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mySearchText = searchText.getText().toString();
                if (mySearchText.length() > 0){
                    text.setText("Searching "+ mySearchText +" :");
                    String myFood = api.getFoodItems(mySearchText, 20);
                    data = new ArrayList<NutritionFacts>();
                    //System.out.print(myFood);
                    JSONObject food = null;   // { first
                    try {
                        food = new JSONObject(myFood);
                        food = food.getJSONObject("foods");
                        extract(food, data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    populateViewList(rootView, inflater);
                }
            }
        });

        return rootView;
    }


    private void populateViewList(View rootView, LayoutInflater inflater ){

        ArrayAdapter<NutritionFacts> adapter = new MyListAdapter(inflater, getContext());
        ListView list = (ListView) rootView.findViewById(R.id.listview_search);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                System.out.println("Im Clicking on List " + position);
                final NutritionFacts current = data.get(position);
                String foodId = current.fatSecretId;

                System.out.println(foodId);
                JSONObject k = null;
                try {
                    k = api.getFoodItem(foodId);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                System.out.print(k);
//                Intent current_Intent = new Intent(MainActivity.this, FoodInfo.class);
//                current_Intent.putExtra("Food", current);
//                startActivity(current_Intent);
            }
        });
    }

    private void extract(JSONObject object, List<NutritionFacts> data) throws JSONException {
        JSONArray FoodArray = object.getJSONArray("food");

        if (FoodArray != null) {
            for (int i = 0; i < FoodArray.length(); i++) {
                JSONObject food_entry = (JSONObject) FoodArray.get(i);
                String food_name = food_entry.get("food_name").toString();
                String food_description = food_entry.get("food_description").toString();
                String id = food_entry.get("food_id").toString();

                String food_type = food_entry.getString("food_type");
                String branch = null;
                if (food_type.equals("Generic")) {
                    branch = "Generic";
                }
                else if (food_type.equals("Brand")) {
                    branch = food_entry.getString("brand_name");
                }

                data.add(new NutritionFacts(food_name, branch , id, food_description));
            }
        }
    }

    private class MyListAdapter extends ArrayAdapter<NutritionFacts> {
        LayoutInflater inflator;
        public MyListAdapter(LayoutInflater i, Context c) {
            super(c, R.layout.search_result, data);
            this.inflator = i;
        }

        public View getView(final int position, final View convertView, final ViewGroup parent){
            View itemView = convertView;

            if (itemView == null) {
                itemView = inflator.inflate(R.layout.search_result, parent, false);
            }

            final NutritionFacts current = data.get(position);

            TextView name = (TextView) itemView.findViewById(R.id.textView);
            TextView dis = (TextView) itemView.findViewById(R.id.textView2);
            TextView branch = (TextView) itemView.findViewById(R.id.textView4);

            name.setText(current.name);
            dis.setText(current.dis);
            branch.setText("Branch: "+current.branch);

            return itemView;
        }
    }

}
