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

    if (fields.length < 4) {
      return null;
    }

    // testName
    inputParameters.setVariable("Test", fields[0]);

    // propulation size
    int populationSize;
    try {
      populationSize = Integer.parseInt(fields[1]);
    } catch (Exception e) {
      return null;
    }
    inputParameters.setVariable("P", populationSize);

    // Max generations
    int maxGenerations;
    try {
      maxGenerations = Integer.parseInt(fields[2]);
    } catch (Exception e) {
      return null;
    }
    inputParameters.setVariable("G", maxGenerations);

    return generateTestCase(inputParameters);
  }

  @Override
  public TravelingSalesManTestCase generateTestCase(Variables inputParameters) {
    String path       = inputParameters.getVariable("Path",    "").getStringValue();

    // create a test case
    TravelingSalesManTestCase travelingSalesManTestCase = new TravelingSalesManTestCase();
    travelingSalesManTestCase.setInput(new TravelingSalesManInput(inputParameters));
    travelingSalesManTestCase.getInput().setParameters(inputParameters);

    int expectedResult = inputParameters.populationSize;

    travelingSalesManTestCase.setExpectedOutput(new TravelingSalesManOutput());

    return travelingSalesManTestCase;

  }

}