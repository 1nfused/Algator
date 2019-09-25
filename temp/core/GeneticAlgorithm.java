package core;

public class GeneticAlgorithm {

	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	protected int tournamentSize;

	public GeneticAlgorithm(int populationSize, double mutationRate,
		double crossoverRate, int elitismCount, int tournamentSize) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

	public Population initPopulation(int chromosomeLength) {
		Population population = new Population(this.populationSize, chromosomeLength);
		return population;
	}

	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	public double calcFitness(Individual individual, City cities[]){
		// Get fitness
		Route route = new Route(individual, cities);
		double fitness = 1 / route.getDistance();
		// Store fitness
		individual.setFitness(fitness);
		return fitness;
	}

	public void evalPopulation(Population population, City cities[]){
		double populationFitness = 0;
		// Loop over population evaluating individuals and summing population fitness
		for (Individual individual : population.getIndividuals()) {
			populationFitness += this.calcFitness(individual, cities);
		}

		double avgFitness = populationFitness / population.size();
		population.setPopulationFitness(avgFitness);
	}
}