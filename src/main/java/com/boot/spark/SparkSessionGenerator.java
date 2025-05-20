package com.boot.spark;

import org.apache.spark.sql.SparkSession;

public class SparkSessionGenerator {
	public SparkSession makeSparkSession(String appName, String master) {
		SparkSession spark = SparkSession.builder().appName(appName).master(master).getOrCreate();
		return spark;
	}
}
