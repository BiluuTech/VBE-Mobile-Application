package com.biluutech.vbebuyer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biluutech.vbebuyer.Activities.HomeActivity;
import com.biluutech.vbebuyer.Models.ProductsModel;
import com.biluutech.vbebuyer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<ProductsModel> productModelList;

    public CartAdapter(Context context, List<ProductsModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String pid = productModelList.get(position).getPid();
        final DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pid);
        final DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        holder.tvCartProductName.setText(productModelList.get(position).getProductName());
        holder.tvCartProductPrice.setText("Rs " + productModelList.get(position).getProductPrice() + " /-");
        Glide.with(context)
                .load(productModelList.get(position).getImageUrl())
                .placeholder(R.color.gray)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.ivCartProductImage);

        holder.btnDeleteCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delRef.removeValue();
                productModelList.clear();
                checkRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            context.startActivity(new Intent(context, HomeActivity.class));
                            ((Activity) context).finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCartProductImage;
        private TextView tvCartProductName, tvCartProductPrice;
        private Button btnDeleteCartProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCartProductImage = itemView.findViewById(R.id.ivCartProductImage);
            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            btnDeleteCartProduct = itemView.findViewById(R.id.btnDeleteCartProduct);

        }
    }
}
