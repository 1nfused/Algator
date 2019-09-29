import si.fri.algotest.execute.AbstractAlgorithm;

/**
 *
 * @author ...
 */
public abstract class TravelingSalesManAbsAlgorithm extends AbstractAlgorithm {
 
  @Override
  public TravelingSalesManTestCase getCurrentTestCase() {
    return (TravelingSalesManTestCase) super.getCurrentTestCase(); 
  }

  protected abstract TravelingSalesManOutput execute(TravelingSalesManInput travelingSalesManInput);

  @Override
  public void run() {
  	throw new Exception("Haha");
    algorithmOutput = execute(getCurrentTestCase().getInput());
  }
}