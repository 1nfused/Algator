package algator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import si.fri.algotest.analysis.Analysis;
import static si.fri.algotest.analysis.Analysis.FindLimitTestsetID;
import si.fri.algotest.entities.EResult;
import si.fri.algotest.entities.EVariable;
import si.fri.algotest.entities.MeasurementType;
import si.fri.algotest.entities.Project;
import si.fri.algotest.entities.Variables;
import si.fri.algotest.execute.Notificator;
import si.fri.algotest.global.ATGlobal;
import si.fri.algotest.global.ATLog;
import si.fri.algotest.tools.ATTools;
import si.fri.algotest.tools.UniqueIDGenerator;

/**
 *
 * @author tomaz
 */
public class Analyse {
  private static String introMsg = "ALGator Analyse, " + Version.getVersion();
          
  
  private static Options getOptions() {
    Options options = new Options();

    Option algorithm = OptionBuilder.withArgName("algorithm_name")
	    .withLongOpt("algorithm")
	    .hasArg(true)
	    .withDescription("the name of the algorithm to use; if the algorithm is not given, all the algorithms of a given project are used")
	    .create("a");
    
    Option data_root = OptionBuilder.withArgName("folder")
	    .withLongOpt("data_root")
	    .hasArg(true)
	    .withDescription("use this folder as data_root; default:  $ALGATOR_ROOT/data_root")
	    .create("dr");

    Option data_local = OptionBuilder.withArgName("folder")
            .withLongOpt("data_local")
            .hasArg(true)
            .withDescription("use this folder as data_local; default: $ALGATOR_ROOT/data_local")
            .create("dl");    
    
    Option algator_root = OptionBuilder.withArgName("folder")
            .withLongOpt("algator_root")
            .hasArg(true)
            .withDescription("use this folder as algator_root; default: $ALGATOR_ROOT")
            .create("r");
    
    Option verbose = OptionBuilder.withArgName("verbose_level")
            .withLongOpt("verbose")
            .hasArg(true)
            .withDescription("print additional information (0 = OFF (default), 1 = some, 2 = all")
            .create("v");

    Option logTarget = OptionBuilder.withArgName("log_target")
            .hasArg(true)
            .withDescription("where to print information (1 = stdout (default), 2 = file, 3 = both")
            .create("log");
    
    Option whereResults = OptionBuilder.withArgName("where_results")
            .withLongOpt("where_results")            
            .hasArg(true)
            .withDescription("where to print results (1 = stdout, 2 = file, 3 = both (default)")
            .create("w");
    
    
    Option param = OptionBuilder.withArgName("parameter_name")
    	    .withLongOpt("parameter")
            .hasArg(true)
            .withDescription("the name of the parameter to use")
            .create("p");    
    Option params = OptionBuilder.withArgName("parameters")
            .withLongOpt("parameters")
            .hasArg(true)
            .withDescription("the value of parameters (json string)")
            .create("s");
    Option timeLimit = OptionBuilder.withArgName("time_limit")
            .withLongOpt("timelimit")
            .hasArg(true)
            .withDescription("time limit before kill (in seconds); defulat: 1")
            .create("t");
    Option timesToExecute = OptionBuilder.withArgName("time_to_execute")
            .withLongOpt("timestoexecute")
            .hasArg(true)
            .withDescription("number of times to execute algorithm; defulat: 1")
            .create("te");
    Option measurement = OptionBuilder.withArgName("mtype_name")
	    .withLongOpt("mtype")
	    .hasArg(true)
	    .withDescription("the name of the measurement type to use (EM, CNT or JVM); if the measurement type is not given, the EM measurement type is used")
	    .create("m");

    Option instanceID = OptionBuilder.withArgName("instance_id")
	    .withLongOpt("instance_id")
	    .hasArg(true)
	    .withDescription("identifier of this test instance; default: unique random value")
	    .create("i");
    
    options.addOption(algorithm);
    options.addOption(data_root);
    options.addOption(data_local);    
    options.addOption(algator_root);
    options.addOption(param);
    options.addOption(params);
    options.addOption(timeLimit);
    options.addOption(timesToExecute);
    options.addOption(measurement);
    options.addOption(instanceID);
    
        
    options.addOption(verbose);
    options.addOption(logTarget);
    options.addOption(whereResults);
    
    options.addOption("h", "help", false,
	    "print this message");        
    options.addOption("u", "usage", false, "print usage guide");
    
    return options;
  }

  private static void printMsg(Options options) {    
    HelpFormatter formatter = new HelpFormatter();
    String header = "functions: FindLimit | FindLimits | RunOne\n";
    formatter.printHelp("algator.Analyse [options] function project_name", header,  options, "");

    System.exit(0);
  }

  private static void printUsage() {
    Scanner sc = new Scanner((new Chart()).getClass().getResourceAsStream("/data/AnalyseUsage.txt")); 
    while (sc.hasNextLine())
      System.out.println(sc.nextLine());
    
    System.exit(0);
  }
  
  /**
   * Used to run the system. Parameters are given trought the arguments
   *
   * @param args
   */
  public static void main(String args[]) {  
    System.out.println(introMsg + "\n");
    
    Options options = getOptions();

    CommandLineParser parser = new BasicParser();
    try {
      CommandLine line = parser.parse(options, args);

      if (line.hasOption("h")) {
	printMsg(options);
      }

      if (line.hasOption("u")) {
        printUsage();
      }

      String[] curArgs = line.getArgs();
      if (curArgs.length != 2) {
	printMsg(options);
      }

      String function    = curArgs[0]; 
      String projectName = curArgs[1];
      
      String algatorRoot = ATGlobal.getALGatorRoot();
      if (line.hasOption("algator_root")) {
        algatorRoot = line.getOptionValue("algator_root");        
      }
      ATGlobal.setALGatorRoot(algatorRoot);

      String dataRoot = ATGlobal.getALGatorDataRoot();
      if (line.hasOption("data_root")) {
	dataRoot = line.getOptionValue("data_root");
      }
      ATGlobal.setALGatorDataRoot(dataRoot);
      
      String dataLocal = ATGlobal.getALGatorDataLocal();
      if (line.hasOption("data_local")) {
	dataLocal = line.getOptionValue("data_local");
      }
      ATGlobal.setALGatorDataLocal(dataLocal);      

      String algorithmName = "";
      if (line.hasOption("algorithm")) {
	algorithmName = line.getOptionValue("algorithm");
      }
      
      MeasurementType mType = MeasurementType.EM;
      if (line.hasOption("mtype")) {
	try {
          mType = MeasurementType.valueOf(line.getOptionValue("mtype").toUpperCase());
        } catch (Exception e) {}  
      }      
      
      ATGlobal.verboseLevel = 0;
      if (line.hasOption("verbose")) {
        if (line.getOptionValue("verbose").equals("1"))
          ATGlobal.verboseLevel = 1;
        if (line.getOptionValue("verbose").equals("2"))
          ATGlobal.verboseLevel = 2;
      }
      
      ATGlobal.logTarget = ATLog.TARGET_STDOUT;
      if (line.hasOption("log")) {
        if (line.getOptionValue("log").equals("0"))
          ATGlobal.logTarget = ATLog.TARGET_OFF;
        if (line.getOptionValue("log").equals("2"))
          ATGlobal.logTarget = ATLog.TARGET_FILE;
        if (line.getOptionValue("log").equals("3"))
          ATGlobal.logTarget = ATLog.TARGET_FILE + ATLog.TARGET_STDOUT;
      }     
      ATLog.setLogTarget(ATGlobal.logTarget);

      String instanceID = UniqueIDGenerator.getNextID();
      if (line.hasOption("instance_id"))
        instanceID = line.getOptionValue("instance_id");
        
      int whereToPrint = 3; // both, stdout and file
      if (line.hasOption("where_results")) try {
        whereToPrint = Integer.parseInt(line.getOptionValue("where_results"));
      } catch (Exception e) {}            
      
      int timeLimit = 1;
      if (line.hasOption("timelimit")) {
        try {
	  timeLimit = Integer.parseInt(line.getOptionValue("timelimit", "1"));
        } catch (Exception e) {}
      }
      int timesToExecute = 1;
      if (line.hasOption("timestoexecute")) {
        try {
	  timesToExecute = Integer.parseInt(line.getOptionValue("timestoexecute", "1"));
        } catch (Exception e) {}
      }
      
      
      String parameterName = "";
      if (line.hasOption("parameter")) {
	parameterName = line.getOptionValue("parameter");
      }
      
      String paramsJSON = "{}";
      if (line.hasOption("parameters")) {
	paramsJSON = line.getOptionValue("parameters");
      }
      Variables parameters = getParametersFromJSON(paramsJSON);
      
      // valid project?
      if (!ATGlobal.projectExists(dataRoot, projectName)) {
        ATGlobal.verboseLevel=1;
        ATLog.log("Project configuration file does not exist for " + projectName, 1);

        System.exit(0);      
      }
      
      Execute.syncTests(projectName);
      Project project = new Project(dataRoot, projectName);

      ArrayList<String> algorithms = new ArrayList<>();
      if (algorithmName.isEmpty()) {
        try {
          for (String eAlgName : project.getAlgorithms().keySet()) 
            algorithms.add(eAlgName);
        } catch (Exception e) {}        
      } else
        algorithms.add(algorithmName);
      
      //Notificator notificator = Notificator.getNotificator(projectName, "", FindLimitTestsetID, MeasurementType.EM);
      Notificator notificator = null;
      
      switch (function.toUpperCase()) {
        case "RUNONE":
          Analysis.runOne(dataRoot, project, algorithms, parameters, timeLimit, timesToExecute, mType, instanceID, whereToPrint);
          break;
        
        case "FINDLIMIT":
          if (parameterName.isEmpty()) {
            ATGlobal.verboseLevel=1;
            ATLog.log("Missing parameter (option -p).", 1);

           System.exit(0);       
          }
          ArrayList<Variables> results = 
            Analysis.getParameterLimit(dataRoot, project, algorithms, parameterName, parameters, timeLimit, instanceID, whereToPrint, notificator);
          break;
          
        case "FINDLIMITS":
          if (parameterName.isEmpty()) {
            ATGlobal.verboseLevel=1;
            ATLog.log("Missing parameter (option -p).", 1);

           System.exit(0);       
          }
          
          Analysis.getParameterLimits(dataRoot, project, algorithms, parameterName, parameters, timeLimit, instanceID, whereToPrint, notificator);
          break;          
          
        default:
          ATGlobal.verboseLevel=1;
          ATLog.log("Invalid function '" + projectName + "'.", 1);
          System.exit(0);                
      }           
    } catch (ParseException ex) {
      printMsg(options);
    }
  }
  
  private static Variables getParametersFromJSON(String paramsJSON) {
    HashMap<String, Object> params = new HashMap<>();
    try {
      params = ATTools.jSONObjectToMap(new JSONObject(paramsJSON));
    } catch (Exception e) {
    }
    Variables parameters = new Variables();
    for (String paramName : params.keySet()) {
      parameters.addVariable(new EVariable(paramName, params.get(paramName)));
    }
    return parameters;
  }
  
  private static void printResult(Project project, ArrayList<String> algorithms, ArrayList<Variables> results ) {
        EResult emResultDesc = project.getResultDescriptions().get(MeasurementType.EM);

    for (int i=0; i<results.size(); i++) {
      Variables result = results.get(i);
      if (result != null) {
        System.out.print(
          result.toString(emResultDesc.getVariableOrder(), false, ATGlobal.DEFAULT_CSV_DELIMITER)
        );
        System.out.println(ATGlobal.DEFAULT_CSV_DELIMITER + 
          result.getVariable(Analysis.MY_TIMER).getLongValue());
      }
    }
  }
}
