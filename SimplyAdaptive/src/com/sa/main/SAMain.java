package com.sa.main;

public class SAMain {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		AttributeSet trainSet= new AttributeSet("train.csv");
		AttributeSet testSet= new AttributeSet("test.csv");
		
		//trainSet.saveDiffMatrix("/Users/adi/data/trainRatio.csv");
		trainSet.normalize();
//		trainSet.saveDiffMatrix("/Users/adi/data/trainDiff.csv");
		
		ChoiceModel model = new ChoiceModel(trainSet,testSet);
		//model.learnRnd();
		model.learnPSO();
		model.test();
		model.predict();
		
	}

}
