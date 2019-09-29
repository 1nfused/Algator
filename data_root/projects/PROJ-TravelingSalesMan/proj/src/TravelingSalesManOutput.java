import si.fri.algotest.execute.AbstractOutput;
import si.fri.algotest.execute.AbstractTestCase;

/**
 *
 * @author Luka
 */
public class TravelingSalesManOutput extends AbstractOutput {

  public int shortestDistance;

  public TravelingSalesManOutput() {} // TODO: Is this needed?
  public TravelingSalesManOutput(int shortestDistance) {
    this.shortestDistance = shortestDistance;
  }


  @Override
  public String toString() {
    return super.toString() + ", Shortest distance = " +
    Integer.toString(this.shortestDistance);
  }


  @Override
  protected Object getIndicatorValue(AbstractTestCase testCase, AbstractOutput algorithmOutput, String indicatorName) {
    TravelingSalesManTestCase travelingSalesManTestCase        = (TravelingSalesManTestCase) testCase;
    TravelingSalesManOutput   travelingSalesManAlgorithmOutput = (TravelingSalesManOutput) algorithmOutput;

    switch (indicatorName) {
      case "Check":
        boolean checkOK (travelingSalesManAlgorithmOutput.shortestDistance
                         instanceof Double);
        return checkOK ? "OK" : "NOK";
    }

    return null;
  }
}