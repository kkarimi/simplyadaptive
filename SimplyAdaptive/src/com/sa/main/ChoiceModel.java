package com.sa.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.mahout.classifier.evaluation.Auc;

public class ChoiceModel {
	AttributeSet trainSet;
	AttributeSet testSet;
	double[] weightVec={1,0,0,0, 0,0,0,0, 0,0,0};

//		double[] weightVec={0.13221617,  0.00932483,  0.20509023, -0.25926373,  0.09588875,  0.09036701,
//				0.16458657, -0.10099631,  0.33519405,  0.18792684, -0.06606009};

	public ChoiceModel(AttributeSet trainSet, AttributeSet testSet){
		this.trainSet=trainSet;
		this.testSet=testSet;
		//this.weightVec=new double[11];
	}

	public void learnRnd(){
		for (int i=0; i<11;i++){
			weightVec[i]=Math.random();
			//weightVec[i]=0;
		}
	}	

	public void learnPSO(){
		PSOEstimator pso = new PSOEstimator(trainSet.values, trainSet.choices);
		pso.train(10);
		this.weightVec=pso.bestPosition;

	}	
	public double test(){
		ArrayList<String[]> aocFile=new ArrayList<String[]>();

		Auc auc = new Auc();

		for(ArrayList<Double> compVec:trainSet.dataMat){
			double utility[] ={ 0,0};

			for (int i=0; i<11;i++){
				utility[0]+=weightVec[i]   *compVec.get(i+1);
				utility[1]+=weightVec[i]*compVec.get(i+1+11);
			}

			int maxj=1;
			int other=0;

			if(utility[0]>utility[1]){
				maxj=0;
				other=1;
			}

			double u[]={0,0};
			if(utility[other]<0){
				u[maxj]=1;
				u[other]=0;
			}else{
				u[0]=utility[0]/(utility[0]+utility[1]);
				u[1]=utility[1]/(utility[0]+utility[1]);
			}



			auc.add(compVec.get(0).intValue(), u[0]);
			String[] s={compVec.get(0).toString(),String.valueOf(u[maxj])};
			aocFile.add(s);


		}

		try {
			File file = new File("/Users/adi/Dropbox/SimplyAdaptive/auc.txt");

			if (!file.exists()) {			
				file.createNewFile();
			}

			FileWriter fw;
			fw = new FileWriter(file.getAbsoluteFile());

			BufferedWriter bw = new BufferedWriter(fw);
			for(int i=0;i<aocFile.size();i++){
				bw.write(aocFile.get(i)[0]+","+aocFile.get(i)[1]+"\n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("AUC: "+auc.auc());
		return auc.auc();
	}


	public ArrayList<Double> predict() throws IOException{
		ArrayList<Double> resultVec=new ArrayList<Double>();
		for(ArrayList<Double> compVec:testSet.dataMat){
			double utility[] ={ 0,0};
			for (int i=0; i<11;i++){
				utility[0]+=weightVec[i]   *compVec.get(i);
				utility[1]+=weightVec[i]*compVec.get(i+11);
			}
			int maxj=1;
			int other=0;

			if(utility[0]>utility[1]){
				maxj=0;
				other=1;
			}

			double u[]={0,0};
			if(utility[other]<0){
				u[maxj]=1;
				u[other]=0;
			}else{
				u[0]=utility[0]/(utility[0]+utility[1]);
				u[1]=utility[1]/(utility[0]+utility[1]);
			}
			resultVec.add(u[0]);

		}

		File file = new File("/Users/adi/Data/out.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0;i<resultVec.size();i++){
			bw.write(resultVec.get(i)+"\n");
		}
		bw.close();

		System.out.println("Done.");

		return resultVec;
	}





}
