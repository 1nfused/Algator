import si.fri.algotest.entities.Variables;
import si.fri.algotest.execute.AbstractTestCase;

/**
 *
 * @author Luka
 */
public class TravelingSalesManTestCase extends AbstractTestCase {

  @Override
  public TravelingSalesManInput getInput() {
    return (TravelingSalesManInput) super.getInput();
  }

  @Override
  public TravelingSalesManOutput getExpectedOutput() {
    return (TravelingSalesManOutput) super.getExpectedOutput();
  }



  @Override
  public TravelingSalesManTestCase getTestCase(String testCaseDescriptionLine, String path) {
    // create a set of variables ...
    Variables inputParameters = new Variables();
    inputParameters.setVariable("Path", path);

    String[] fields = testCaseDescriptionLine.split(":");

    // ... and finally, create a test case determined by these parameters
    return generateTestCase(inputParameters);
  }

  @Override
  public TravelingSalesManTestCase generateTestCase(Variables inputParameters) {
    String path       = inputParameters.getVariable("Path",    "").getStringValue();

    // TODO:
    // ... read the values of the parameters and create a corresponding test case

    // ... Example: if test case is and array of integers of size "N"
    // int size = inputParameters.getVariable("N", 0).getIntValue();
    // int [] array = new int[size];



    // create a test case
    TravelingSalesManTestCase travelingSalesManTestCase = new TravelingSalesManTestCase();
    travelingSalesManTestCase.setInput(new TravelingSalesManInput(/* TODO: add parameters for constructor */));
    travelingSalesManTestCase.getInput().setParameters(inputParameters);
    travelingSalesManTestCase.setExpectedOutput(new TravelingSalesManOutput(/* TODO: add parameters for constructor */));

    return travelingSalesManTestCase;

  }

}