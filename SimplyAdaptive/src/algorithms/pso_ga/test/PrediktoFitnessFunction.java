package algorithms.pso_ga.test;

import java.util.Random;

import algorithms.pso_ga.PSOFitnessFunction;


public class PrediktoFitnessFunction extends PSOFitnessFunction {

	double[][] attributes;
	int daysToEvent[];
	Random r= new Random();
	int predictDaysAhead;

	public PrediktoFitnessFunction(double[][] attributes, int[] daysToEvent, int predictDaysAhead) {
		super(true); // minimize this function
		this.attributes=attributes;
		this.daysToEvent=daysToEvent;
		this.predictDaysAhead=predictDaysAhead;
	}


	public double evaluate(double position[]) {
		// first normalize 
		normalize(position, position.length - 1);
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
		/*  mcc = Matthews Correlation Coefficient
		 *  it returns a value between −1 and +1. 
		 *  A coefficient of +1 represents a perfect prediction,
		 *   0 no better than random prediction 
		 *   and −1 indicates total disagreement between prediction and observation
		 */
		double mcc = 0;
		double tp=0, tn=0,fp=0,fn=0;
		//for each transaction 
		double correct=0;
		for (int t = 0; t < attributes.length; t+=1) {
			double sum=0;
			for(int i=0; i<attributes[t].length; i++){
				sum+=attributes[t][i]*position[i];
			}
			boolean predictEvent=sum>0.0;
			if(predictEvent){
				if(daysToEvent[t] <= predictDaysAhead)
					tp++;
				else
					fp++;
			}else{
				if(daysToEvent[t] <= predictDaysAhead)
					fn++;
				else
					tn++;
			}
		}
		mcc=(tp+fp)*(tp+fn)*(tn+fp)*(tn+fn);
		if(mcc==0){
			mcc=1;
		}
		mcc=(tp*tn-fp*fn)/Math.sqrt(mcc);

		return mcc;//mcc>0 ? mcc: 0;
	}

}
