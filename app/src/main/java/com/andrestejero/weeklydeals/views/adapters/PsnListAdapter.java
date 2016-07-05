package com.andrestejero.weeklydeals.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrestejero.weeklydeals.R;
import com.andrestejero.weeklydeals.models.Category;
import com.andrestejero.weeklydeals.models.Price;
import com.andrestejero.weeklydeals.models.Product;
import com.andrestejero.weeklydeals.models.PsnContainer;
import com.andrestejero.weeklydeals.models.PsnViewType;
import com.andrestejero.weeklydeals.utils.CollectionUtils;
import com.andrestejero.weeklydeals.utils.StringUtils;

import java.util.Collections;
import java.util.List;

public class PsnListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = PsnListAdapter.class.getSimpleName();

    @NonNull
    private Context mContext;

    @Nullable
    private OnItemClickListener mItemClickListener;

    @NonNull
    private List<Category> mCategories;

    @NonNull
    private List<Product> mProducts;

    public PsnListAdapter(@NonNull Context context) {
        this.mContext = context;
        mCategories = Collections.emptyList();
        mProducts = Collections.emptyList();
    }

    public void updatePsnList(@NonNull PsnContainer psnContainer) {
        this.mCategories = CollectionUtils.safeList(psnContainer.getCategories());
        this.mProducts = CollectionUtils.safeList(psnContainer.getProducts());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PsnViewType.CATEGORY.ordinal()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.psn_category_item, parent, false);
            return new CategoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.psn_game_item, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isPositionCategory(position)) {
            CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
            final Category category = mCategories.get(position);
            PsnListAdapterHelper.showCategoryName(category, viewHolder.categoryName);
            PsnListAdapterHelper.showCategoryImage(mContext, category, viewHolder.categoryImage);
        } else {
            ProductViewHolder viewHolder = (ProductViewHolder) holder;
            position -= CollectionUtils.safeSize(mCategories);
            final Product product = mProducts.get(position);
            PsnListAdapterHelper.showProductName(product, viewHolder.productName);
            PsnListAdapterHelper.showProductImage(mContext, product, viewHolder.productImage);

            if (product.getPrice() != null) {
                Price price = product.getPrice();
                if (price.getNormal() != null) {
                    viewHolder.normalPrice.setText(StringUtils.gamePrice(price.getNormal()));
                }
                if (price.getDiscountPrice() != null) {
                    viewHolder.discountPrice.setText(StringUtils.gamePrice(price.getDiscountPrice()));
                }
                if (price.getDiscount() != null) {
                    viewHolder.discount.setText(StringUtils.gamePercent(price.getDiscount()));
                }
                if (price.getBonusDiscountPrice() != null) {
                    viewHolder.plusDiscountPrice.setVisibility(View.VISIBLE);
                    viewHolder.plusDiscountPrice.setText(StringUtils.gamePrice(price.getBonusDiscountPrice()));
                } else {
                    viewHolder.plusDiscountPrice.setVisibility(View.GONE);
                }
                if (price.getBonusDiscount() != null) {
                    viewHolder.plusSavingContainer.setVisibility(View.VISIBLE);
                    viewHolder.plusDiscount.setText(StringUtils.gamePercent(price.getBonusDiscount()));
                } else {
                    viewHolder.plusSavingContainer.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (CollectionUtils.safeSize(mCategories) > position) {
            return PsnViewType.CATEGORY.ordinal();
        }
        return PsnViewType.PRODUCT.ordinal();
    }

    private boolean isPositionCategory(int position) {
        return getItemViewType(position) == PsnViewType.CATEGORY.ordinal();
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.safeSize(mCategories) + CollectionUtils.safeSize(mProducts);
    }

    public interface OnItemClickListener {
        void onCategoryClick(int position);
        void onItemClick(int position);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView categoryImage;
        private TextView categoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            View actionableCover = itemView.findViewById(R.id.actionableCover);
            categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
            categoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            actionableCover.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onCategoryClick(getLayoutPosition());
            }
        }
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productName;
        private ImageView productImage;
        private TextView normalPrice;
        private TextView discountPrice;
        private TextView discount;
        private View plusSavingContainer;
        private TextView plusDiscountPrice;
        private TextView plusDiscount;

        public ProductViewHolder(View itemView) {
            super(itemView);
            View actionableCover = itemView.findViewById(R.id.actionableCover);
            productName = (TextView) itemView.findViewById(R.id.tvProductName);
            productImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
            normalPrice = (TextView) itemView.findViewById(R.id.tvNormalPrice);
            discountPrice = (TextView) itemView.findViewById(R.id.tvDiscountPrice);
            discount = (TextView) itemView.findViewById(R.id.tvDiscount);
            plusSavingContainer = itemView.findViewById(R.id.llPlusSavingContainer);
            plusDiscountPrice = (TextView) itemView.findViewById(R.id.tvPlusDiscountPrice);
            plusDiscount = (TextView) itemView.findViewById(R.id.tvPlusGameDiscount);
            actionableCover.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getLayoutPosition());
            }
        }
    }
}
