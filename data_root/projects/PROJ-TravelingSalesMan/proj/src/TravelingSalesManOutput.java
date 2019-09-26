import si.fri.algotest.execute.AbstractOutput;
import si.fri.algotest.execute.AbstractTestCase;

/**
 *
 * @author ...
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
      // TODO: for each indicator defined in the atrd file provide a "case" to determnine its value
      //case "indicator_name" :
      //  using the given test case travelingSalesManTestCase (which includes the input and the expected output)
      //    and the given travelingSalesManAlgorithmOutput (the actual output of the algorithm) calculate indicator_value
      //  return indicator_value;
      case "Check":
        return "nOK";
    }

    return null;
  }
}