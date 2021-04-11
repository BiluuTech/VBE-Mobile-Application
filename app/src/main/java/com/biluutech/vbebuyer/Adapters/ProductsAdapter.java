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

import com.biluutech.vbebuyer.Activities.ProductDescriptionActivity;
import com.biluutech.vbebuyer.Models.ProductsModel;
import com.biluutech.vbebuyer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private List<ProductsModel> productsModelList;

    public ProductsAdapter(Context context, List<ProductsModel> productsModelList) {
        this.context = context;
        this.productsModelList = productsModelList;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            holder.tvProductName.setText(productsModelList.get(position).getProductName());
            holder.tvProductPrice.setText("Rs " + productsModelList.get(position).getProductPrice() + " /-");
            Glide.with(context)
                    .load(productsModelList.get(position).getImageUrl())
                    .placeholder(R.color.gray)
//                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(holder.ivProductImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ProductDescriptionActivity.class);
//                    intent.putExtra("productName", productsModelList.get(position).getProductName());
//                    intent.putExtra("productPrice", productsModelList.get(position).getProductPrice());
//                    intent.putExtra("productDescription", productsModelList.get(position).getProductDescription());
                    intent.putExtra("pid", productsModelList.get(position).getPid());
//                    intent.putExtra("productCid", productsModelList.get(position).getCid());
//                    intent.putExtra("productSid", productsModelList.get(position).getSid());
//                    intent.putExtra("productImageUrl", productsModelList.get(position).getImageUrl());
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return productsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvProductPrice;
        private ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
