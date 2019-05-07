package com.example.aprojectktomkow.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aprojectktomkow.R;

import java.util.List;

public class RecipesListAdapter extends ArrayAdapter<Recipe>
{
    private static final String TAG = "RecipesListAdapter";

    private Context mContext;
    private int mResource;

    public RecipesListAdapter(Context context, int resource, List<Recipe> objects)
    {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String name = getItem(position).getName();
        String shortDescription = getItem(position).getShortDescription();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.recipe_adapter_title);
        TextView tvShortDescription = convertView.findViewById(R.id.recipe_adapter_short_description);

        tvName.setText(name);
        tvShortDescription.setText(shortDescription);

        return convertView;
    }
}
