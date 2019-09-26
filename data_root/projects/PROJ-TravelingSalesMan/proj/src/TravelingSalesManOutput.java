import si.fri.algotest.execute.AbstractOutput;
import si.fri.algotest.execute.AbstractTestCase;

/**
 * 
 * @author ...
 */
public class TravelingSalesManOutput extends AbstractOutput {

  // TODO: define fields to hold the output data of an algorithm
  // ...
  
  public TravelingSalesManOutput(/* TODO: define appropriate constructor parameters */) {    
    // this.parameter = parameter;
  }
  
  
  @Override
  public String toString() {
    // TODO: provide a handy TravelingSalesManOutput string representation (include only important data)
    return super.toString();
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