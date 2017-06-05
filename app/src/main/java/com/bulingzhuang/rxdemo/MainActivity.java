package com.bulingzhuang.rxdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func0;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        observableTest();
//        subjectTest();
        subjectTest2();
//        subjectTest3();
    }

    private void subjectTest3(){
        PublishSubject<String> publishSubject = PublishSubject.create();


        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

                Log.e("blz","subject:"+s); //接收到 as Bridge
            }
        });

        Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("as Bridge");
                subscriber.onCompleted();
            }
        }).subscribe(publishSubject);
    }

    private void subjectTest2(){
        PublishSubject<String> publishSubject = PublishSubject.create();

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e("blz","结束了");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("blz","打印："+s);
            }
        };
        publishSubject.subscribe(observer);
        Observable<String> observable = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("一条消息");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(publishSubject);
    }

    private void subjectTest() {
        Observable<String> observable = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("这是一条Observable");
                subscriber.onCompleted();
            }
        });

        PublishSubject<String> publishSubject = PublishSubject.create();
        //PublishSubject的Observer只会接收被订阅后发送的数据，所以这条是看不到的
        publishSubject.onNext("是一条PublishSubject");
        publishSubject.onCompleted();

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("结束");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        observable.subscribe(observer);
        publishSubject.subscribe(observer);

    }


    private void observableTest() {
        Observable<String> sender = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hi");
                subscriber.onNext("Hello");
                subscriber.onCompleted();
            }
        });

        Observable<String> justSender = Observable.just("just1", "just2", "just3");

        List<String> list = new ArrayList<>();
        list.add("From_0");
        list.add("From_1");
        list.add("From_2");
        Observable<String> fromSender = Observable.from(list);

        //延迟创建，等观察者订阅时才创建Observable
        Observable<String> deferSender = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just("just1", "just2", "just3");
            }
        });

        //定时器，根据每隔一段时间发送一次(执行一次相应代码)
        Observable<Long> intervalSender = Observable.interval(1, TimeUnit.SECONDS);

        //发送整数序列的Observable，10、11、12、13、14
        Observable<Integer> rangeSender = Observable.range(10, 5);

        //延迟5秒后发送
        Observable<Long> timerSender = Observable.timer(5, TimeUnit.SECONDS);

        //重复发送三次
        Observable<String> repeatSender = Observable.just("J_0", "J_1").repeat(3);

        Observer<String> receiver = new Observer<String>() {

            @Override
            public void onCompleted() {
                //数据接收完成时调用
            }

            @Override
            public void onError(Throwable e) {
                //异常时调用
            }

            @Override
            public void onNext(String s) {
                //正常过程中调用
                System.out.println(s);
            }
        };

//        sender.subscribe(receiver);
//        justSender.subscribe(receiver);
        fromSender.subscribe(receiver);
        deferSender.subscribe(receiver);
    }
}
