package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest implements Runnable{
    @Override
    public void run() {
        for(int i=1; i<=10; i++){
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 创建固定大小的线程池
        // ExecutorService threadPool = Executors.newFixedThreadPool(3);
        // 创建不固定大小的线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 创建单一线程
        // ExecutorService threadPool = Executors.newSingleThreadExecutor();
        ThreadPoolTest threadPoolTest = new ThreadPoolTest();
        for(int i=1; i<=3; i++){
            // 利用线程池中的线程，处理任务
            threadPool.execute(threadPoolTest);
            System.out.println("========================================");
        }
        // 所有任务执行完成之后关闭线程池
        threadPool.shutdown();

        // 创建定时任务线程池
//        ScheduledExecutorService scheduleThreadPool = Executors.newScheduledThreadPool(3);
//        scheduleThreadPool.scheduleWithFixedDelay(new Runnable(){
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName() + "Bombing!!!!!!!!!!!!!!!");
//            }
//        }, 10, 2, TimeUnit.SECONDS);
    }
}
