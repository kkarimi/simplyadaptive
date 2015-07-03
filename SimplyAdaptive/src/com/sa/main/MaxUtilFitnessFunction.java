package com.sa.main;

import java.io.IOException;
import java.util.Random;

import org.apache.mahout.classifier.evaluation.Auc;

import algorithms.pso_ga.PSOFitnessFunction;


public class MaxUtilFitnessFunction extends PSOFitnessFunction {

	double[][][] variables;
	int choiceIndex[];
	Random r= new Random();

	public MaxUtilFitnessFunction(double[][][] variables, int[] choiceIndex) {
		super(true); // minimize this function
		this.variables=variables;
		this.choiceIndex=choiceIndex;
	}


	public double evaluate(double position[]) {
		// first normalize 
		//normalize(position, position.length - 1);
		// then evaluate
		return hitEvaluate(position);
	}

	private void normalize(double position[], int n) {
		double sum=0; 
		for (int i = 0; i < n; i++) {
			sum += Math.abs(position[i]);
		}
		for (int i = 0; i < n; i++) {
			position[i] /= sum;
		}
	}


	/*
	 * returns fitness computed using % correct predicitons
	 */
	public double hitEvaluate(double position[]) {

		//debug
		//		double[] d={0.4199034700657363,	-0.3413154951922479,	0.23878103474201584,	0.2732117335873615};
		//		position=d;
		//\debug
		double f = 0;
		double p = position[position.length - 1]; //trembling hand

		Auc auc = new Auc();
		auc.setMaxBufferSize(10000);

		//for each transaction 
		for (int t = 0; t < variables.length; t+=1) {
			double maxUtility = -Double.MAX_VALUE;
			int maxj = -1;
			int choiceCount=variables[t].length;

			double utility[] ={ 0,0};
			for (int j = 0; j < choiceCount; j++) {
				int attributeCount=variables[t][j].length;
				assert attributeCount==position.length - 1;
				//for each attribute
				for (int i = 0; i < attributeCount; i++) {
					utility[j] +=  variables[t][j][i] * position[i];
				}
				if (utility[j] > maxUtility) {
					maxUtility = utility[j];
					maxj = j;
				}
			}
			int other=0;
			if(maxj==0) 
				other=1;

			double u[]={0,0};
			if(utility[other]<0){
				u[maxj]=1;
				u[other]=0;
			}else{
				u[0]=utility[0]/(utility[0]+utility[1]);
				u[1]=utility[1]/(utility[0]+utility[1]);
			}
			
			auc.add(this.choiceIndex[t], u[0]);
			

			//auc.add(this.choiceIndex[t], arg1)
			//			if (maxj == this.choiceIndex[t]) {
			//				// Max utility prediction is correct.
			//				f += 1/(double) this.variables.length;
			//			} else {
			//				// Incorrect
			//				f += 0;  
			//			}

		}
		return auc.auc();
	}

}
