package com.example.InheritableThreadLocal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InheritableThreadLocalApplication implements CommandLineRunner {
    private static final InheritableThreadLocal<Data> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final ThreadLocal<Data> THREAD_LOCAL = new ThreadLocal<>();

    public static void main(String[] args) {
        Data data = new Data();
        data.setValue("set by main thread");
        INHERITABLE_THREAD_LOCAL.set(data);


        Data data1 = new Data();
        data1.setValue("set by main thread");
        THREAD_LOCAL.set(data1);

        SpringApplication.run(InheritableThreadLocalApplication.class, args);

        System.out.println(Thread.currentThread().getName() + "--main thread(InheritableThreadLocal), after modify: " + data.getValue());
        System.out.println(Thread.currentThread().getName() + "--main thread(ThreadLocal), after modify: " + data1.getValue());
    }

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(
            () -> {
                Data data = INHERITABLE_THREAD_LOCAL.get();
                System.out.println(
                    Thread.currentThread().getName() + "--child thread(InheritableThreadLocal), before: " + data
                        .getValue());
                data.setValue(data.getValue() + ", append by child thread");
                INHERITABLE_THREAD_LOCAL.set(data);

                Data data1 = THREAD_LOCAL.get();
                if (null == data1) {
                    System.out.println(Thread.currentThread().getName() + "--child thread(ThreadLocal), null");
                } else {
                    System.out.println(
                        Thread.currentThread().getName() + "--child thread(ThreadLocal), before: " + data1.getValue());
                    data.setValue(data1.getValue() + ", append by child thread");
                    INHERITABLE_THREAD_LOCAL.set(data1);
                }
            }
        );
        thread.start();
        thread.join();
    }

    @lombok.Data
    public static class Data {
        private String value;
    }
}
