package com.bpapps.matrixjavav2.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DataListCat {
    @SerializedName("CatId")
    @Expose
    private Integer catId;

    @SerializedName("CTitle")
    @Expose
    private String categoryTitle;

    public Integer getCatId() {
        return catId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    @Override
    public String toString() {
        return "DataListCat{" +
                "catId=" + catId +
                ", categoryTitle='" + categoryTitle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataListCat that = (DataListCat) o;
        return catId.equals(that.catId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catId);
    }
}
