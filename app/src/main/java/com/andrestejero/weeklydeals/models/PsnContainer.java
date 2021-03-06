package com.andrestejero.weeklydeals.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andrestejero.weeklydeals.utils.CollectionUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PsnContainer {

    @Nullable
    private String id;

    @Nullable
    private String name;

    @Nullable
    @SerializedName("products")
    private List<Product> products;

    @Nullable
    @SerializedName("lists")
    private List<Category> categories;

    @Nullable
    private Paging paging;

    @Nullable
    private List<Filter> filters;

    @Nullable
    private List<Sort> sorting;

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @NonNull
    public List<Product> getProducts() {
        return CollectionUtils.safeList(products);
    }

    @NonNull
    public List<Category> getCategories() {
        return CollectionUtils.safeList(categories);
    }

    @Nullable
    public Paging getPaging() {
        return paging;
    }

    @NonNull
    public List<Filter> getFilters() {
        return CollectionUtils.safeList(filters);
    }

    @NonNull
    public List<Sort> getSorting() {
        return CollectionUtils.safeList(sorting);
    }

    /**
     *
     * @return la cantidad de productos/categorias de la pagina
     */
    public int getItemsCount() {
        return CollectionUtils.safeSize(products) + CollectionUtils.safeSize(categories);
    }

    /**
     *
     * @return la cantidad total de productos/categorias
     */
    public int getPagingTotal() {
        if (paging != null && paging.getTotal() != null) {
            return paging.getTotal();
        }
        return getItemsCount();
    }

    public void setProducts(@Nullable List<Product> products) {
        this.products = products;
    }

    public void setCategories(@Nullable List<Category> categories) {
        this.categories = categories;
    }
}