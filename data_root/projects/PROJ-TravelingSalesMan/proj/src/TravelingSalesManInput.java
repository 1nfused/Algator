import si.fri.algotest.execute.AbstractInput;

/**
 * @author ...
 */
public class TravelingSalesManInput extends AbstractInput {

  public int maxGenerations;
  public int numberOfCities;
  public int shortestDistance;

  public TravelingSalesManInput(int maxGenerations, int numberOfCities, int shortestDistance) {
    this.maxGenerations = maxGenerations;
    this.numberOfCities = numberOfCities;
    this.shortestDistance = shortestDistance;
  }

  @Override
  public String toString() {
    return super.toString() + ", Input parameters - " + this.numberOfCities
    + " number of cities, " + this.maxGenerations + " max generations ";
  }
}