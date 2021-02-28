package com.bpapps.matrixjavav2.model.datamodeldynamicui;

import android.util.Log;

import com.bpapps.matrixjavav2.model.datamodel.DataListCat;
import com.bpapps.matrixjavav2.model.datamodel.DataListObject;
import com.bpapps.matrixjavav2.model.datamodel.Result;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DataModelDynamicUiUtils {
    private static final String TAG = "TAG.DataModelDynamicUiUtils";

    public static DataSetHolder getDataSet(Result result) {
        List<DataListCat> categorise = result.getDataObject().getDataListCat();
        List<DataListObject> allItems = result.getDataObject().getDataListObject();

        Hashtable<DataListCat, ArrayList<DataListObject>> retVal = new Hashtable<>(categorise.size());

        for (DataListCat category : categorise) {
            ArrayList<DataListObject> currCategoryItems = new ArrayList<>();

            for (DataListObject item : allItems) {
                if (item.getCatId() == category.getCatId()) {
                    currCategoryItems.add(item);
                }
            }

            retVal.put(category, currCategoryItems);
        }

        Log.d(TAG, retVal.toString());

        return new DataSetHolder(categorise, retVal);
    }
}
