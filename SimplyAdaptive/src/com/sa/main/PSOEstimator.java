package com.sa.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import algorithms.pso_ga.PSOFitnessFunction;
import algorithms.pso_ga.Particle;
import algorithms.pso_ga.ParticleUpdateMixed;
import algorithms.pso_ga.SwarmRepulsive;

import com.sa.util.CSVReader;


public class PSOEstimator {
	double[] bestPosition;
	double fitness;

	// Sample Data 
	// 10 transactions (purchases)
	// 4 choices at each transaction time	
	// attributes: {ideal point distance, promotions, price}
	// the dataset was created based on an utility model { 0.6, 0.1, 0.3 }, with trembling hand = 0.1
	double[] weightVec={0.13221617,  0.00932483,  0.20509023, -0.25926373,  0.09588875,  0.09036701,
			   0.16458657, -0.10099631,  0.33519405,  0.18792684, -0.06606009};
	double[] weightVec2={0.3526, 0.0879, 0.3720, -0.3956, 0.2216, -0.0030, 0.0056, -0.0002, 0.6596, 0.0004, 0.0024};
	
	double[][][] variables={
			{{0.55,0.98,0.13},{0.60,0.88,0.15}}
			,{{0.96,0.78,0.73},{0.10,0.99,0.82},{0.63,0.49,0.84},{0.41,0.75,0.53}}
			,{{0.90,0.72,0.00},{0.13,0.72,0.07},{0.08,0.14,0.35},{0.98,0.46,0.34}}
			,{{0.86,0.18,0.69},{0.97,0.22,0.77},{0.87,0.85,0.71},{0.61,0.01,0.21}}
			,{{0.78,0.79,0.17},{0.95,0.95,0.96},{0.01,0.02,0.72},{0.39,0.40,0.68}}
			,{{0.80,0.50,0.05},{0.44,0.99,0.07},{0.46,0.97,0.03},{0.85,0.35,0.48}}
			,{{0.97,0.26,0.01},{0.99,0.31,0.35},{0.77,0.85,0.09},{0.50,0.05,0.85}}
			,{{0.00,0.28,0.04},{0.31,0.87,0.65},{0.53,0.61,0.50},{0.92,0.91,0.51}}
			,{{0.52,0.32,0.61},{0.37,0.01,0.24},{0.48,0.70,0.39},{0.70,0.75,0.91}}
			,{{0.17,0.30,0.17},{0.00,0.43,0.01},{0.26,0.69,0.27},{0.89,0.41,0.90}}
			,{{0.58,0.97,0.63},{0.51,0.61,0.82},{0.31,0.03,0.74},{0.54,0.69,0.63}}
			,{{0.31,0.71,0.32},{0.75,0.93,0.75},{0.19,0.31,0.20},{0.12,0.53,0.13}}
			,{{0.59,0.33,0.26},{0.94,0.25,0.19},{0.53,0.74,0.30},{0.21,0.09,0.76}}
			,{{0.05,0.09,0.88},{0.09,0.53,0.35},{0.89,0.07,0.99},{0.53,0.18,0.58}}
			,{{0.60,0.22,0.67},{0.27,0.91,0.29},{0.51,0.02,0.06},{0.94,0.27,0.23}}
			,{{0.14,0.15,0.71},{0.60,0.60,0.43},{0.73,0.73,0.82},{0.30,0.30,0.60}}
			,{{0.67,0.75,0.69},{0.45,0.21,0.92},{0.34,0.15,0.04},{0.27,0.92,0.20}}
			,{{0.58,0.10,0.58},{0.96,0.82,0.97},{0.33,0.67,0.34},{0.14,0.43,0.14}}
			,{{0.60,0.71,0.80},{0.37,0.07,0.72},{0.00,0.50,0.34},{0.69,0.69,0.06}}
			,{{0.35,0.12,0.35},{0.52,0.51,0.70},{0.96,0.90,0.03},{0.69,0.17,0.26}}
			,{{0.46,0.58,0.54},{0.41,0.17,0.35},{0.19,0.37,0.31},{0.29,0.23,0.84}}
			,{{0.67,0.85,0.00},{0.11,0.39,0.03},{0.94,0.38,0.79},{0.88,0.53,0.27}}
			,{{0.72,0.13,0.84},{0.42,0.15,0.26},{0.29,0.34,0.62},{0.24,0.84,0.49}}
			,{{0.42,0.26,0.42},{0.85,0.84,0.86},{0.82,0.43,0.83},{0.05,0.74,0.06}}
			,{{0.77,0.77,0.12},{0.36,0.36,0.79},{0.16,0.16,0.96},{0.27,0.27,0.21}}
			,{{0.92,0.52,0.25},{0.05,0.01,0.22},{0.97,0.15,0.14},{0.41,0.85,0.81}}
			,{{0.32,0.37,0.91},{0.54,0.15,0.57},{0.52,0.22,0.88},{0.55,0.38,0.65}}
			,{{0.50,0.50,0.50},{0.42,0.42,0.42},{0.67,0.68,0.67},{0.02,0.02,0.02}}
			,{{0.93,0.94,0.52},{0.02,0.02,0.39},{0.33,0.34,0.67},{0.58,0.58,0.61}}
			,{{0.24,0.14,0.88},{0.39,0.44,0.50},{0.99,0.59,0.53},{0.29,0.09,0.70}}
	};

	int[] choiceIndex={3,0,3,1,0,0,1,3,3,3,1,1,1,2,3,2,0,1,0,2,3,2,0,2,3,2,2,2,0,2};

	
	public PSOEstimator(){

	}
	
	public PSOEstimator(double[][][] variables,int[] choiceIndex){
		this.variables=variables;
		this.choiceIndex=choiceIndex;
	}

	public void train(int maxIterations) {

		// Setup fitness function		
		PSOFitnessFunction fitnessFunction=new MaxUtilFitnessFunction(this.variables,this.choiceIndex);

		int variableCount=variables[0][0].length;  

		//SETUP SWARM	
				//don't worry about these parameters
				SwarmRepulsive swarm = new SwarmRepulsive( 1000 
						 , new Particle(variableCount+1) // variable count + trembling hand
						 , fitnessFunction  );
				swarm.setOtherParticleIncrement(0.01);
				swarm.setRandomIncrement(0.01);
				swarm.setInertia(0.990); // best with 0.99 apparently
				swarm.setGlobalIncrement(0.01);
				swarm.setParticleIncrement(0.01);
				swarm.setMaxPosition(1);
				swarm.setMinPosition(-1);
				swarm.setMaxMinVelocity(0.01);
				
				//set number of particles in the swarm 		
				swarm.setNumberOfParticles(1000);  
				 
				//set update strategy ( crossover, mutation, repulsive)
				swarm.setParticleUpdate(new ParticleUpdateMixed(new Particle(variableCount+1), 0.0,0.01,0.5));
				
				((ParticleUpdateMixed)swarm.getParticleUpdate()).setGenerationGap(5); // when to perform GAs
				 
		//Estimate
		
		for( int n = 0; n < maxIterations; n++ ) {

			swarm.evolve();
//			if(n==100){
//				swarm.seedPosition(weightVec);
//			}
			
//			if(n==20){
//				swarm.seedPosition(weightVec2);
//			}

			//debug - print results
			{
				String stats="";
				double[] bestPosition=swarm.getBestPosition();
				for( int i = 0; i < bestPosition.length; i++ )
					stats += String.format("%.4f", bestPosition[i]) + (i < (bestPosition.length - 1) ? ", " : "");
				String s=n+">\t "+stats+"\t f: "+String.format("%.4f",swarm.getBestFitness())					
						;
				System.out.println(s);
				/*				swarm.setMaxPosition(max);
				swarm.setMinPosition(min);*/ // do we need this ?
			}
		}

		this.bestPosition=swarm.getBestPosition();
		this.fitness=swarm.getBestFitness();

	}






//	public static void main(String[] args) {
//
//		PSOEstimator example=new PSOEstimator();	
//		//train model
//		example.train(200); // 100 iterations in the PSO estimation  	
//
//	}

}
