import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class BaseGeneticAlgorithm extends BasicSortAbsAlgorithm {

  @Override
  protected TravellingSalesManOutput execute(TravellingSalesManInput input) {
    TravellingSalesManOutput result = new TravellingSalesManOutput();

    execute(input.maxGenerations, input.numberOfCities, input.shortestDistance);

    result.shortestDistance = input.shortestDistance;

    return result;
  }

  private int execute(int generations, int num_cities, int shortestDistance) {
  	// Create base genetic algorithm
    BaseGeneticAlgorithm bga = new BaseGeneticAlgorithm(generations, num_cities, shortestDistance);

    // Execute path finding
    returnb ga.calcShortestDistance();
}

class BaseGeneticAlgorithm  {

	int maxGenerations;
	int numberOfCities;

	public BaseGeneticAlgorithm(int maxGenerations, int numberOfCities, int shortestDistance) {
		this.maxGenerations = maxGenerations;
		this.numberOfCities = numberOfCities;
	}

	public int calcShortestDistance() {
		City cities[] = new City[this.numberOfCities];

		// Loop to create random cities
		for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
			// Generate x,y position
			int xPos = (int) (100 * Math.random());
			int yPos = (int) (100 * Math.random());
			// Add city
			cities[cityIndex] = new City(xPos, yPos);
		}

		// Initial GA
		GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.9, 2, 5);
		// Initialize population
		Population population = ga.initPopulation(cities.length);
		// Evaluate population
		//ga.evalPopulation(population, cities);
		Route startRoute = new Route(population.getFittest(0), cities);
		System.out.println("Start Distance: " + startRoute.getDistance());
		// Keep track of current generation
		int generation = 1;
		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, this.maxGenerations) == false) {
			// Print fittest individual from population
			Route route = new Route(population.getFittest(0), cities);
			//System.out.println("G"+generation+" Best distance:" + route.getDistance());
			// Apply crossover
			population = ga.crossoverPopulation(population);
			// Apply mutation
			population = ga.mutatePopulation(population);
			// Evaluate population
			ga.evalPopulation(population, cities);
			// Increment the current generation
			generation++;
		}

		// Display results
		System.out.println("Stopped after " + this.maxGenerations + " generations.");
		Route route = new Route(population.getFittest(0), cities);
		// Set shortest distance to input obj
		shortestDistance = route.getDistance();
		System.out.println("Best distance: " + route.getDistance());
	}
}


class City {

	private int x;
	private int y;

	public City(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double distanceFrom(City city) {
		// Give difference in x,y
		double deltaXSq = Math.pow((city.getX() - this.getX()), 2);
		double deltaYSq = Math.pow((city.getY() - this.getY()), 2);

		// Calculate shortest path
		double distance = Math.sqrt(Math.abs(deltaXSq + deltaYSq));
		return distance;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}

class Route {

	private City route[];
	private double distance = 0;

	public Route(Individual individual, City cities[]) {

		// Get individual's chromosome
		int chromosome[] = individual.getChromosome();
		// Create route
		this.route = new City[cities.length];
		for (int geneIndex = 0; geneIndex < chromosome.length; geneIndex++) {
			this.route[geneIndex] = cities[chromosome[geneIndex]];
		}
	}

	public double getDistance() {

		if (this.distance > 0) {
			return this.distance;
		}

		// Loop over cities in route and calculate route distance
		double totalDistance = 0;
		for (int cityIndex = 0; cityIndex + 1 < this.route.length; cityIndex++) {
			totalDistance += this.route[cityIndex].distanceFrom(this.route[cityIndex + 1]);
		}

		totalDistance += this.route[this.route.length - 1].distanceFrom(this.route[0]);
		this.distance = totalDistance;

		return totalDistance;
	}
}

class Individual {
	private int[] chromosome;
	private double fitness = -1;

	public Individual(int[] chromosome) {
		// Create individual chromosome
		this.chromosome = chromosome;
	}

	public Individual(int chromosomeLength) {
		// Create random individual
		int[] individual;
		individual = new int[chromosomeLength];
		for (int gene = 0; gene < chromosomeLength; gene++) {
			individual[gene] = gene;
		}
		this.chromosome = individual;
	}

	public int[] getChromosome() {
		return this.chromosome;
	}

	public int getChromosomeLength() {
		return this.chromosome.length;
	}

	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}

	public int getGene(int offset) {
		return this.chromosome[offset];
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return this.fitness;
	}

	public boolean containsGene(int gene) {
		for (int i = 0; i < this.chromosome.length; i++) {
			if (this.chromosome[i] == gene) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		String output = "";

		for (int gene = 0; gene < this.chromosome.length; gene++) {
			output += this.chromosome[gene];
		}

		return output;
	}
}


class Population {

	private Individual population[];
	private double populationFitness = -1;

	public Population(int populationSize) {
		this.population = new Individual[populationSize];
	}

	public Population(int populationSize, int chromosomeLength) {
		this.population = new Individual[populationSize];

		for (int individualCount = 0; individualCount <
				populationSize; individualCount++) {
			Individual individual = new Individual(chromosomeLength);
			this.population[individualCount] = individual;
		}
	}

	public Individual[] getIndividuals() {
		return this.population;
	}

	public Individual getFittest(int offset) {
		Arrays.sort(this.population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
				}
			});
		return this.population[offset];
	}

	public void setPopulationFitness(double fitness) {
		this.populationFitness = fitness;
	}

	public double getPopulationFitness() {
		return this.populationFitness;
	}

	public int size() {
		return this.population.length;
	}

	public Individual setIndividual(int offset, Individual individual) {
		return population[offset] = individual;
	}

	public Individual getIndividual(int offset) {
		return population[offset];
	}

	public void shuffle() {
		Random rnd = new Random();
		for (int i = population.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Individual a = population[index];
			population[index] = population[i];
			population[i] = a;
		}
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

class GeneticAlgorithm {

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

	public Individual selectParent(Population population) {
		// Create tournament
		Population tournament = new Population(this.tournamentSize);

		// Add random individuals to the tournament
		population.shuffle();
		for (int i = 0; i < this.tournamentSize; i++) {
			Individual tournamentIndividual = population.getIndividual(i);
			tournament.setIndividual(i, tournamentIndividual);
		}

		// Return the best
		return tournament.getFittest(0);
	}

	public Population crossoverPopulation(Population population) {
        // Create new population
        Population newPopulation = new Population(population.size());

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // Get parent1
            Individual parent1 = population.getFittest(populationIndex);

            // Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                // Find parent2 with tournament selection
                Individual parent2 = this.selectParent(population);

                // Create blank offspring chromosome
                int offspringChromosome[] = new int[parent1.getChromosomeLength()];
                Arrays.fill(offspringChromosome, -1);
                Individual offspring = new Individual(offspringChromosome);

                // Get subset of parent chromosomes
                int substrPos1 = (int) (Math.random() * parent1.getChromosomeLength());
                int substrPos2 = (int) (Math.random() * parent1.getChromosomeLength());

                // make the smaller the start and the larger the end
                final int startSubstr = Math.min(substrPos1, substrPos2);
                final int endSubstr = Math.max(substrPos1, substrPos2);

                // Loop and add the sub tour from parent1 to our child
                for (int i = startSubstr; i < endSubstr; i++) {
                    offspring.setGene(i, parent1.getGene(i));
                }

                // Loop through parent2's city tour
                for (int i = 0; i < parent2.getChromosomeLength(); i++) {
                    int parent2Gene = i + endSubstr;
                    if (parent2Gene >= parent2.getChromosomeLength()) {
                        parent2Gene -= parent2.getChromosomeLength();
                    }

                    // If offspring doesn't have the city add it
                    if (offspring.containsGene(parent2.getGene(parent2Gene)) == false) {
                        // Loop to find a spare position in the child's tour
                        for (int ii = 0; ii < offspring.getChromosomeLength(); ii++) {
                            // Spare position found, add city
                            if (offspring.getGene(ii) == -1) {
                                offspring.setGene(ii, parent2.getGene(parent2Gene));
                                break;
                            }
                        }
                    }
                }

                // Add child
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }

    public Population mutatePopulation(Population population){
        // Initialize new population
        Population newPopulation = new Population(this.populationSize);
        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex <
            population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);
            // Skip mutation if this is an elite individual
            if (populationIndex >= this.elitismCount) {
                //System.out.println("Mutating population member "+populationIndex);
                // Loop over individual's genes
                for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                    // Does this gene need mutation?
                    if (this.mutationRate > Math.random()) {
                        // Get new gene position
                        int newGenePos = (int) (Math.random() *
                        individual.getChromosomeLength());
                        // Get genes to swap
                        int gene1 = individual.getGene(newGenePos);
                        int gene2 = individual.getGene(geneIndex);
                        // Swap genes
                        individual.setGene(geneIndex, gene1);
                        individual.setGene(newGenePos, gene2);
                    }
                }
            }
            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual);
        }
        // Return mutated population
        return newPopulation;
    }
}