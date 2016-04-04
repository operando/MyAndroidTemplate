package com.os.operando.myandroidtemplate;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.os.operando.myandroidtemplate.databinding.ActivityMainBinding;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.button.setEnabled(false);

        Observable.create(subscriber -> subscriber.onNext("test"))
                .map(text -> Log.d("test", text.toString()));

        Observable.from(new String[]{"test", "Android", "hogehoge", "aaa"})
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .filter(text -> text.length() > 4)
                .subscribe(s -> {
                    Log.d(s, s);
                });


//        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
        Observable.range(1, 10)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(integer -> (integer % 2) == 0)
                .map(integer1 -> integer1 * 10)
                .subscribe(integer2 -> Log.d("test", integer2.toString()));

        Observable<Boolean> nameWritten = RxTextView.textChangeEvents(binding.name)
                .map(textViewTextChangeEvent -> !TextUtils.isEmpty(textViewTextChangeEvent.text()));

        Observable<Boolean> passWritten = RxTextView.textChangeEvents(binding.pass)
                .map(textViewTextChangeEvent -> !TextUtils.isEmpty(textViewTextChangeEvent.text()));

        Observable.combineLatest(nameWritten, passWritten, (aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .subscribe(isValid -> binding.button.setEnabled(isValid));

    }
}
