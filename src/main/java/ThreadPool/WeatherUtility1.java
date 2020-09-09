package ThreadPool;

import java.util.concurrent.*;

class Test1 {
    public static void main(String[] args) {
        // 传入1.线程数 2.整体超时时间（毫秒）
        Response response = WeatherUtility1.getAllCityWeather(100, 2);
        System.out.println(response);
    }
}

public class WeatherUtility1 implements Callable<Boolean> {
    static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    @Override
    public Boolean call() {
        for(int i=0;i<1000;i++){
            // 1. 调用天气的API（通过city获取weather）
//        System.out.println("city "+city+" 的查询任务执行完毕!!!返回值为weather");
            // 2. 每次任务执行完毕就把该city的weather放入map，直到全部完成
            map.put("city "+i, "weather");
        }
        return true;
    }

    /**
     * 批量获取城市的天气
     * @param poolSize 线程数
     * @param timeout 整体任务的超时时间（如果超时，就需要放弃整个任务）
     * @return responseCode 0：正常 1：异常 | exception: 错误信息 | map: 返回的城市天气
     */
    public static Response getAllCityWeather(int poolSize, long timeout) {
        // 返回码 0：正常 1：异常
        int responseCode = 0;
        Exception exception = null;
        // 构造线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, poolSize, 2000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(poolSize));
        WeatherUtility1 getWeatherTask = new WeatherUtility1();
        Future<Boolean> future = executor.submit(getWeatherTask);
        try {
            // 不管是否执行完成，future将在timeout毫秒之后取结果
            if(future.get(timeout, TimeUnit.MILLISECONDS)){
                System.out.println("future完成");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        } catch (ExecutionException e) {
            executor.shutdownNow();
        } catch (TimeoutException e) {
            System.out.println("future超时");
            future.cancel(true);
            responseCode = 1;
            exception = new TimeoutException("获取天气超过了"+timeout+"毫秒");
        } finally{
            executor.shutdownNow();
        }
        Response response = new Response();
        response.setMap(map);
        response.setResponseCode(responseCode);
        response.setException(exception);
        return response;
    }
}

class Response {
    int responseCode;
    Exception exception;
    ConcurrentHashMap<String, String> map;

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public ConcurrentHashMap<String, String> getMap() {
        return map;
    }

    public void setMap(ConcurrentHashMap<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", exception=" + exception +
                ", map=" + map +
                '}';
    }
}
