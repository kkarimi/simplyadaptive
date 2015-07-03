package algorithms.pso_ga.test;

import algorithms.pso_ga.PSOFitnessFunction;
import algorithms.pso_ga.Particle;
import algorithms.pso_ga.ParticleUpdateMixed;
import algorithms.pso_ga.SwarmRepulsive;


public class Example_Predikto {
	double[] bestPosition;
	double fitness;
	int daysAhead=3;
// Sample Data 
// 25 days
// 5 indicators/attributes
	double[][]attributes={
			 {1,0,0,1,1}
			,{0,0,0,1,1}
			,{0,0,0,0,0}
			,{1,0,0,0,1}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,1,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			,{0,0,1,0,0}
			,{0,0,0,0,0}
			,{0,0,0,0,0}
			};
	
	    int[] wasDown={0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1};
	int[] daysToEvent={5,4,3,2,1,0,4,3,2,1,0,7,6,5,4,3,2,1,0,5,4,3,2,1,0};
	
	
	public void train(int maxIterations) {
// Setup fitness function		
		PSOFitnessFunction fitnessFunction=new PrediktoFitnessFunction(this.attributes,this.daysToEvent, daysAhead);
		
//SETUP SWARM	
		int variableCount=attributes[0].length;
		SwarmRepulsive swarm = new SwarmRepulsive( 1000 
				 , new Particle(variableCount) 
				 , fitnessFunction  );
		swarm.setOtherParticleIncrement(0.01);
		swarm.setRandomIncrement(0.01);
		swarm.setInertia(0.999); // best with 0.99 apparently
		swarm.setGlobalIncrement(0.01);
		swarm.setParticleIncrement(0.01);
		swarm.setMaxPosition(1);
		swarm.setMinPosition(-1);
		swarm.setMaxMinVelocity(0.01);
		
		//set number of particles in the swarm 		
		swarm.setNumberOfParticles(10);   
		//set update strategy ( crossover, mutation, repulsive)
		swarm.setParticleUpdate(new ParticleUpdateMixed(new Particle(variableCount+1), 0.0,0.005,0.0));
		// when to perform GAs
		((ParticleUpdateMixed)swarm.getParticleUpdate()).setGenerationGap(5); 
		 	
//Estimate
		for( int n = 0; n < maxIterations; n++ ) {

			swarm.evolve();

			//debug - print results
			{
				String stats="";
				double[] bestPosition=swarm.getBestPosition();
				for( int i = 0; i < bestPosition.length; i++ )
					stats += String.format("%.4f", bestPosition[i]) + (i < (bestPosition.length - 1) ? ", " : "");
				String s=n+">\t "+stats+"\t f: "+String.format("%.4f",swarm.getBestFitness());
				System.out.println(s);
			}
		}
		
		this.bestPosition=swarm.getBestPosition();
		this.fitness=swarm.getBestFitness();
		double f=fitnessFunction.evaluate(swarm.getBestPosition());
		System.out.println("Best fitness:"+f);
	}
		
	
	
	
		
	
	public static void main(String[] args) {

		Example_Predikto example=new Example_Predikto();	
		//train model
		example.train(200); // 100 iterations in the PSO estimation  	

	}

}
