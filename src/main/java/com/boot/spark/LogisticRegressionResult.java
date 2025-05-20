package com.boot.spark;

import java.io.File;
import java.io.IOException;

import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogisticRegressionResult {
	public static void main(String[] args) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		// 학습할 데이터가 담긴 json 파일은 여기서 변경
		String learningJsonStr = new LogisticRegressionResult().JsonToStringConverter("station_charge_data.json");

		String inputJsonStr = "[" + "[37.53701, 127.05056, 78, 9.45, 23]," + "[37.03232, 125.01324, 77, 8.41, 21]"
				+ "]";

		JsonNode learningJson = mapper.readTree(learningJsonStr);
		JsonNode inputJson = mapper.readTree(inputJsonStr);

		SparkSession spark = new SparkSessionGenerator().makeSparkSession("Reservatrion_analize", "local[*]");

		MachineLearning ML = new MachineLearning();

		LogisticRegressionModel model = ML.MachineGenerator(spark, learningJson);
		Dataset<Row> predictions = ML.ResultRow(model, spark, inputJson);

		predictions.show(false);
		log.info("@# LogisticRegressionResult predictions => " + predictions);

		spark.close();
	}

	public String JsonToStringConverter(String filename) throws IOException {
		// 1. JSON 파일 경로 (예: learning_data.json)
		File jsonFile = new File("src/main/resources/json/" + filename);

		// 2. ObjectMapper로 읽기
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonFile);

		// 3. JsonNode를 문자열로 변환 (줄바꿈 제거해서 한 줄로)
		String JsonStr = jsonNode.toString();

		return JsonStr;
	}
}
