package com.boot.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MachineLearning {

	// 학습된 모델 반환
	public LogisticRegressionModel MachineGenerator(SparkSession spark, JsonNode learningJson) {

		log.info("MachineGenerator ==> (1)");
		List<Row> learningDataSet = makeLearningData(learningJson);

		log.info("MachineGenerator ==> (2)");
		StructType schema = new StructType(
				new StructField[] { new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
						new StructField("features", new VectorUDT(), false, Metadata.empty()) });

		log.info("MachineGenerator ==> (3)");
		Dataset<Row> training = spark.createDataFrame(learningDataSet, schema);

		log.info("MachineGenerator ==> (4)");
		LogisticRegressionModel model = makeModel(training);

		log.info("MachineGenerator ==> (5)");
		return model;
	}

	// 학습데이터 세팅
	public List<Row> makeLearningData(JsonNode jNode) {
		log.info("@# Start makeLearningData()----");
		List<Row> learningData = new ArrayList<>();

		for (int i = 0; i < jNode.size(); i++) {
			JsonNode rowNode = jNode.get(i);
			double label = rowNode.get(0).asDouble();
			double[] features = new double[rowNode.size() - 1];

			for (int k = 1; k < rowNode.size(); k++) {
				features[k - 1] = rowNode.get(k).asDouble();
			}
			learningData.add(RowFactory.create(label, Vectors.dense(features)));
			log.info(i + "번째 노드 학습데이터 저장 완료");
		}

		log.info("Result : learningData" + learningData);
		return learningData;
	}

	// 입력데이터 세팅
	public List<Row> makeInputData(JsonNode jNode) {
		log.info("@# Start makeInputData()----");
		List<Row> inputData = new ArrayList<>();

		for (int i = 0; i < jNode.size(); i++) {
			JsonNode rowNode = jNode.get(i);
			double[] values = new double[rowNode.size()];

			for (int k = 0; k < rowNode.size(); k++) {
				values[k] = rowNode.get(k).asDouble(); // 더 안전
			}

			inputData.add(RowFactory.create(Vectors.dense(values)));
			log.info(i + "번째 노드 입력데이터 저장 완료");
		}

		log.info("Result : inputData" + inputData);
		return inputData;
	}

	// 모델 생성 및 학습
	public LogisticRegressionModel makeModel(Dataset<Row> training) {
		// 로지스틱 회귀 객체 세팅
		LogisticRegression lr = new LogisticRegression().setMaxIter(10000).setRegParam(0.01);
		LogisticRegressionModel model = lr.fit(training);
		log.info("학습데이터로 맞춤모델 생성 makeModel()");
		return model;
	}

	// 학습된 모델로 예상치 반환(새로운 거 기존 거 둘 다 가능)
	public Dataset<Row> ResultRow(LogisticRegressionModel model, SparkSession spark, JsonNode inputJson) {
		List<Row> inputDataSet = makeInputData(inputJson);

		Dataset<Row> test = spark.createDataFrame(inputDataSet, new StructType(
				new StructField[] { new StructField("features", new VectorUDT(), false, Metadata.empty()) }));

		Dataset<Row> predictions = model.transform(test);
		predictions.select("features", "prediction", "probability").show(20, false);

		log.info("@# 예상결과 반환 완료!!");
		return predictions;
	}

}
