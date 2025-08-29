package com.umeng.callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cty on 2016/12/21.
 */

public interface OnWxAuthListener {
    void onItemClick(int result, Map<String,String> data);
}
