package com.app.oktpus.constants;

import com.app.oktpus.model.SimpleListItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gyandeep on 14/3/18.
 */

public final class Countries {
    public List<SimpleListItemModel> getList() {
        List<SimpleListItemModel> list = new ArrayList<>();
        for(String item: Countries.list) {
            list.add(new SimpleListItemModel(item));
        }
        return list;
    }

    public static final List<String> list =
            Collections.unmodifiableList(Arrays
                    .asList(
                            "Canada",
                            "United States"
                    ));
}

