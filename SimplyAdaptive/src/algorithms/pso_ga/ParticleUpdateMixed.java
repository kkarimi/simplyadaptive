package algorithms.pso_ga;

import java.util.Random;

/**
 * Particle update strategy using a mix of PSO, RepulsivePSO and GeneticAlgorithms
 * 
 * The GA's slow things down a little bit but significantly improve convergence in large search spaces
 * 
 * @author Adi Andrei
 * 
 * Still in testing. 
 * 
 */

class Roulete{
	double[] mark;
	double maxValue;
	Random rand ;
	
	public void init(Swarm swarm){
		 rand = new Random((int)(Math.random()*Integer.MAX_VALUE));
		double sum=0;
		if(mark==null) 
			mark=new double[swarm.getNumberOfParticles()];
		for(int i=0; i<swarm.getNumberOfParticles();i++){
			if(swarm.getFitnessFunction().isMaximize())
				sum+=swarm.getParticle(i).getFitness(); 
			else
				sum+=1/swarm.getParticle(i).getFitness();// for minimisation use (1/fitness)
			mark[i]=sum;
		}
		maxValue=sum;
	}
	
	public int draw(){
		int i=0;
		double r=rand.nextDouble()*this.maxValue;
		
		while((r>mark[i]) &&(i<mark.length)){
			i++;
		}
		
		return i;
	}
}


/**
 * Particle update strategy
 * 
 * Every Swarm.evolve() itereation the following methods are called
 * 		- begin(Swarm) : Once at the begining of each iteration
 * 		- update(Swarm,Particle) : Once for each particle
 * 		- end(Swarm) : Once at the end of each iteration
 * 
 * @author Adi Andrei
 */
/**
 * @author Adi
 *
 */
public class ParticleUpdateMixed extends ParticleUpdate {
	
	/** Random vector for local update */
	double rlocal[];
	/** Random vector for global update */
	double rglobal[];
	/** Random factor for random velocity update */
	double randRand;
	/** Genetic parameters */
	double mutationPct;
	double crossoverPct;
	double repulsivePct;
	long count;
	int crossoverCount;
	int mutationCount;
	int repulsiveCount;
	Roulete roulete;
	int generation;
	int generationGap;//apply genetic operators only every xx generations
	Random random;
	
	/**
	 * Constructor 
	 * @param particle : Sample of particles that will be updated later
	 * @param crossoverPct : % particles to undergo crossover
	 * @param mutationPct : % particles to undergo mutation
	 */
	public ParticleUpdateMixed(Particle particle, double crossoverPct, double mutationPct, double repulsivePct) {
		super(particle);
		rlocal = new double[particle.getDimension()];
		rglobal = new double[particle.getDimension()];
		this.mutationPct=mutationPct;
		this.crossoverPct=crossoverPct;
		this.repulsivePct=repulsivePct;
		this.roulete=new Roulete();
		this.generation=0;
		this.generationGap=1;
		
		this.random = new Random();
		//TODO: remove this when running for real:
		this.random.setSeed(0);
	}

	/** Mutate a random individual 
	 */
	double[] getMutatedRandomParticle(Swarm swarm){
		double position[] = swarm.getParticle( (int)Math.floor(Math.random()*swarm.getNumberOfParticles())).getPosition().clone();
		for( int i = 0; i < position.length; i++ ) {
			if(Math.random()> 0.9){
				position[i] = swarm.minPosition[i]+Math.random()*(swarm.maxPosition[i]-swarm.minPosition[i]);
			}
		}
		return position;
	}
	
	
	
	/** Mutate the last value/dimension/component of a Roulete drawn particle
	 */
	double[] getRuleteLastMutatedParticle(Swarm swarm){
		double position[];	
		if(this.roulete==null)
			position= swarm.getBestPosition().clone();
		else
			position=swarm.getParticle( this.roulete.draw()).getPosition().clone();
		
		if(this.generation < 1000){ 
			int i=position.length-1;
			position[i] = position[i] + (1000-this.generation)/1000.0 * Math.random()*(swarm.maxPosition[i]-swarm.minPosition[i]);
			if(position[i]>1) position[i]=1/position[i];
		}
		return position;
	}
	
	//TODO: Experiment with different types of crossover, mutation and 
	// other specialised operators: Michaelwicz - Genetic Algorithms + Data Structures)
	double[] getRuleteCrossoverParticle(Swarm swarm){
		double position1[] = swarm.getParticle( this.roulete.draw()).getPosition().clone();
		double position2[] = swarm.getParticle( this.roulete.draw()).getPosition();
		for( int i = 0; i < position1.length; i++ ) {
			if(Math.random()> 0.5){
				//position1[i]=position2[i];
				position1[i]=(position1[i]+position2[i])/2.0;
			}
		}
		return position1;
	}
	
	
	/** 
	 * This method is called at the beginning of each iteration
	 * Initialize random vectors use for local and global updates (rlocal[] and rother[])
	 */
	public void begin(Swarm swarm) {
		int i,dim = swarm.getSampleParticle().getDimension();
		randRand = Math.random();// Random factor for random velocity
		for( i=0 ; i < dim ; i++ ) {
			rlocal[i] = Math.random();
			rglobal[i] = Math.random();
		}
		
		//create roulete
		this.count=0;
		this.crossoverCount=(int)Math.floor(swarm.getNumberOfParticles()*this.crossoverPct)+this.mutationCount;
		this.repulsiveCount=(int)Math.floor(swarm.getNumberOfParticles()*this.repulsivePct)+this.crossoverCount;
		if(crossoverCount>0)
			this.roulete.init(swarm);
	}
	
	
	public void update(Swarm swarm, Particle particle) {
		double position[] = particle.getPosition();
		boolean applyGeneticOperators = (this.generation % this.generationGap == 0);
		if(this.count<this.crossoverCount){
			if(applyGeneticOperators){
				//GM: store the crossover of 2 individuals
				position=getRuleteCrossoverParticle(swarm);
				particle.setPosition(position);
			}else{
				// normal PSO
				double velocity[] = particle.getVelocity();
				double globalBestPosition[] = swarm.getBestPosition();
				double particleBestPosition[] = particle.getBestPosition();			
				// Update velocity and position
				for( int i = 0; i < position.length; i++ ) {
					// Update velocity
					velocity[i] = swarm.getInertia() * velocity[i] // Inertia
							+ rlocal[i] * swarm.getParticleIncrement() * (particleBestPosition[i] - position[i]) // Local best
							+ rglobal[i] * swarm.getGlobalIncrement() * (globalBestPosition[i] - position[i]); // Global best
					// Update position
					position[i] += velocity[i];
				}
			}
		}else if(this.count<this.repulsiveCount){
			//PSO: update Repulsive: moves away from some other (random) particle's best position
			double velocity[] = particle.getVelocity();
			double particleBestPosition[] = particle.getBestPosition();
			double maxVelocity[] = swarm.getMaxVelocity();
			double minVelocity[] = swarm.getMinVelocity();
			SwarmRepulsive swarmRepulsive = (SwarmRepulsive)swarm;

			// Randomly select other particle
			int randOtherParticle = (int) (Math.random() * swarm.size());
			double otherParticleBestPosition[] = swarm.getParticle(randOtherParticle).getBestPosition(); 

			// Update velocity and position
			for( int i = 0; i < position.length; i++ ) {

				// Create a random velocity (one on every dimension)
				double randVelocity = velocity[i] = (maxVelocity[i] - minVelocity[i]) * Math.random() + minVelocity[i];

/*				// Update velocity
				velocity[i] = swarmRepulsive.getInertia() * velocity[i] // Inertia
						+ rlocal[i] * swarmRepulsive.getParticleIncrement() * (particleBestPosition[i] - position[i]) // Local best
						+ rglobal[i] * swarmRepulsive.getOtherParticleIncrement() * (otherParticleBestPosition[i] - position[i]) // other Particle Best Position
						+ randRand * swarmRepulsive.getRandomIncrement() * randVelocity; // Random velocity
*/				
				// Update velocity away from global
				velocity[i] = swarmRepulsive.getInertia() * velocity[i] // Inertia
						+ rlocal[i] * swarmRepulsive.getParticleIncrement() * (particleBestPosition[i] - position[i]) // Local best
						+ rglobal[i] * swarmRepulsive.getOtherParticleIncrement() * (otherParticleBestPosition[i] - position[i]) // other Particle Best Position
						+ randRand * swarmRepulsive.getRandomIncrement() * randVelocity; // Random velocity
				position[i] += velocity[i];
				
			}
		}else{
			//PSO: the rest keep doing normal pso 
			double velocity[] = particle.getVelocity();
			double globalBestPosition[] = swarm.getBestPosition();
			double particleBestPosition[] = particle.getBestPosition();			
			// Update velocity and position
			for( int i = 0; i < position.length; i++ ) {
				// Update velocity
				velocity[i] = swarm.getInertia() * velocity[i] // Inertia
						+ rlocal[i] * swarm.getParticleIncrement() * (particleBestPosition[i] - position[i]) // Local best
						+ rglobal[i] * swarm.getGlobalIncrement() * (globalBestPosition[i] - position[i]); // Global best
				// Update position
				position[i] += velocity[i];
			}
		}
		
		if(this.random.nextDouble()<this.mutationPct){ //mutateLastValue(position);
			//if(this.generation < 100){ 
				for( int i = 0; i < position.length/*-1*/; i++ ) {
					//if(Math.random()> this.generation/100.0 ){
						position[i] = swarm.minPosition[i]+Math.random()*(swarm.maxPosition[i]-swarm.minPosition[i]);
					//}
				}
				particle.bestPosition=position;
			//}
			
		}
				
		this.count++;
	}
	
	
	
	/** This method is called at the end of each iteration */
	public void end(Swarm swarm) {
		this.generation++;
	}


	public double getMutationPct() {
		return mutationPct;
	}


	public void setMutationPct(double mutationPct) {
		this.mutationPct = mutationPct;
	}


	public double getCrossoverPct() {
		return crossoverPct;
	}


	public void setCrossoverPct(double crossoverPct) {
		this.crossoverPct = crossoverPct;
	}

	public int getIteration() {
		return generation;
	}

	public int getGenerationGap() {
		return generationGap;
	}

	public void setGenerationGap(int generationGap) {
		this.generationGap = generationGap;
	}




}
