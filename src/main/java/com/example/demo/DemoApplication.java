package com.example.demo;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
//        influxDBTest();
	}

    private static void influxDBTest() {
        System.out.println("######################################## influxdb write and query begins ########################################");
        InfluxDB influxDB = InfluxDBFactory.connect("http://15.114.100.54:8086", "root", "root");
        String dbName = "mydb";
        String rpName = "30_days";

        influxDB.setDatabase(dbName);
        influxDB.setRetentionPolicy(rpName);
        influxDB.enableBatch(BatchOptions.DEFAULTS);

        influxDB.write(Point.measurement("cpu")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("host", "serverC")
                .tag("region", "us2")
                .field("value", 0.95)
                .build());
        influxDB.close();

        Query query = new Query("select * from cpu", dbName);
        QueryResult result = influxDB.query(query);
        influxDB.close();
        System.out.println("######################################## influxdb write and query ends ########################################");
    }
}
