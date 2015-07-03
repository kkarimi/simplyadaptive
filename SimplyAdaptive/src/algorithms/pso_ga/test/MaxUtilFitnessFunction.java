package algorithms.pso_ga.test;

import java.util.Random;

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
		
//debug
//		double[] d={0.4199034700657363,	-0.3413154951922479,	0.23878103474201584,	0.2732117335873615};
//		position=d;
//\debug
		double f = 0;
		double p = position[position.length - 1]; //trembling hand
		
		//for each transaction 
		for (int t = 0; t < variables.length; t+=1) {
			double maxUtility = -Double.MAX_VALUE;
			int maxj = -1;
			int choiceCount=variables[t].length;
			
			// Here, if p > 0, there should be a MonteCarlo Simulation, running the process a few times
			// and taking the average. 
			// the larger the value of p, the larger the number of runs in the Monte Carlo should be.
			if( this.r.nextDouble()> p){
				//for each choice
				for (int j = 0; j < choiceCount; j++) {
					double utility = 0;
					int attributeCount=variables[t][j].length;
					assert attributeCount==position.length - 1;
					//for each attribute
					for (int i = 0; i < attributeCount; i++) {
						utility +=  variables[t][j][i] * position[i];
					}
					if (utility > maxUtility) {
						maxUtility = utility;
						maxj = j;
					}
				}
			}else{
				//trembling hand = > random choice
				maxj=(int)(Math.random()*choiceCount);
			}
			if (maxj == this.choiceIndex[t]) {
				// Max utility prediction is correct.
				f += 1/(double) this.variables.length;
			} else {
				// Incorrect
				f += 0;  
			}
			
		}
		return f;
	}

}
