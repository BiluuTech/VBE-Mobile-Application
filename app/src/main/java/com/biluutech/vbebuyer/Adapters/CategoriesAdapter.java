package com.biluutech.vbebuyer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biluutech.vbebuyer.Activities.CategoryProductActivity;
import com.biluutech.vbebuyer.Models.CategoriesModel;
import com.biluutech.vbebuyer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private Context context;
    private List<CategoriesModel> categoriesModelList;

    public CategoriesAdapter(Context context, List<CategoriesModel> categoriesModelList) {
        this.context = context;
        this.categoriesModelList = categoriesModelList;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, final int position) {


        holder.tvCategoryName.setText(categoriesModelList.get(position).getCategoryName());
        Glide.with(context)
                .load(categoriesModelList.get(position).getImageUrl())
                .placeholder(R.color.gray)
//                    .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.ivCategoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryProductActivity.class);
                intent.putExtra("cid", categoriesModelList.get(position).getCid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private ImageView ivCategoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            ivCategoryImage = itemView.findViewById(R.id.iv_category_image);
        }
    }
}
