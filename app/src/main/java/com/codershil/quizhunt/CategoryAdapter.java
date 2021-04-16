package com.codershil.quizhunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context mContext;
    ArrayList<CategoryModel> mCategoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels){
        mContext = context;
        mCategoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category,null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel model = mCategoryModels.get(position);
        holder.categoryName.setText(model.getCategoryName());
        Glide.with(mContext).load(model.getCategoryImage()).into(holder.categoryImage);
    }

    @Override
    public int getItemCount() {
        return mCategoryModels.size();
    }



    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.imgCategory);
            categoryName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}
