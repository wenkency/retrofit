package com.lven.retrofitdemo.view;

import android.os.SystemClock;
import android.util.Log;

public class ThreadTest {
    private int x;
    private int y;

    // 1. 资源同步
    // 2. 访问互斥
    private volatile double a = 0f;

    private String name;

    public void print() {
        synchronized (this){
            while (name == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e("Runnable", name + "");
        }
    }

    public void setName(String name) {
        synchronized (this) {
            this.name = name;
            notifyAll();
        }
    }

    public void testPrint(){

        for (int i = 0; i <2 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(500);
                    print();
                }
            }).start();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                setName("Lven");
            }
        }).start();
    }

    public void count(int value) {
        synchronized (this) {
            this.x = value;
            this.y = value;
        }
    }

    public void minus(int value) {
        synchronized (this) {
            this.x -= value;
            this.y -= value;
        }
    }

    public void test() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    // 这里判断线程有没有中断标识
                    if (Thread.interrupted()) {
                        Log.e("Runnable", Thread.interrupted() + "");
                        return;
                    }
                    Log.e("Runnable", i + "");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        SystemClock.sleep(500);
        // 中断线程的一个标识
        thread.interrupt();
    }
}
