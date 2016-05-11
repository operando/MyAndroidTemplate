package com.os.operando.myandroidtemplate.model;

import com.rejasupotaro.android.kvs.annotations.Key;
import com.rejasupotaro.android.kvs.annotations.Table;

@Table(name = "user")
public class UserPrefsSchema {
    @Key(name = "user_id")
    int userId;
}
