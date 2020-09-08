package ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Test {
    public static void main(String[] args) {
        // 传入1.线程池数 2.线程超时时间
        WeatherUtility.getAllCityWeather(100, 2000);
    }
}

public class WeatherUtility {
    public static void getAllCityWeather(int poolSize, int timeout) {
        // 构造线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, poolSize, timeout, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(poolSize));
        // 循环所有1000个city
        for(int i=0;i<1000;i++){
            // 假设city的名称为i
            GetWeatherTask getWeatherTask = new GetWeatherTask(i);
            executor.execute(getWeatherTask);
        }
        executor.shutdown();
    }
}

class GetWeatherTask implements Runnable {
    ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    private int city;

    public GetWeatherTask(int city) {
        this.city = city;
    }

    @Override
    public void run() {
        // 1. 调用天气的API（通过city获取weather）
//        System.out.println("city "+city+" 的查询任务执行完毕!!!返回值为weather");
        // 2. 每次任务执行完毕就把该city的weather放入map，直到全部完成
        map.put("city "+ city, "weather");
        System.out.println(map);
    }
}