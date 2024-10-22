package com.example.exmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exmanager.R;
import com.example.exmanager.databinding.SampleCategoryItemBinding;
import com.example.exmanager.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    SampleCategoryItemBinding binding;
    Context context;
    ArrayList<Category> categories;

    //this interface use for select sepcific category on text view
    public interface CategoryClickListner{
        void onCategoryClicked(Category category);
    }


    CategoryClickListner categoryClickListner;

    public CategoryAdapter(Context context, ArrayList<Category> categories, CategoryClickListner categoryClickListner){
    this.context=context;
    this.categories=categories;
    this.categoryClickListner=categoryClickListner;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use ViewBinding to inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        SampleCategoryItemBinding binding = SampleCategoryItemBinding.inflate(inflater, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category= categories.get(position);
        holder.binding.categoryText.setText(category.getCategoryName());
        holder.binding.catIcon.setImageResource(category.getCategoryImage());

        holder.binding.catIcon.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));

        holder.itemView.setOnClickListener(c->{
            categoryClickListner.onCategoryClicked(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private final SampleCategoryItemBinding binding;

        public CategoryViewHolder(SampleCategoryItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
