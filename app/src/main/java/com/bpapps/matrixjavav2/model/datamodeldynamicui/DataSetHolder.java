package com.bpapps.matrixjavav2.model.datamodeldynamicui;

import com.bpapps.matrixjavav2.model.datamodel.DataListCat;
import com.bpapps.matrixjavav2.model.datamodel.DataListObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DataSetHolder {
    private Hashtable<DataListCat, ArrayList<DataListObject>> mDataSet;
    private List<DataListCat> mCategories;

    public DataSetHolder() {
        mDataSet = new Hashtable<>();
        mCategories = new ArrayList<>();
    }

    public DataSetHolder(List<DataListCat> categories, Hashtable<DataListCat, ArrayList<DataListObject>> dataSet) {
        this.mCategories = categories;
        this.mDataSet = dataSet;
    }

    public ArrayList<DataListObject> getItems(DataListCat category) {
        return mDataSet.get(category);
    }

    public List<DataListCat> getCategories() {
        return mCategories;
    }
}
