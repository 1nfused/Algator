package si.fri.algotest.execute;

import algator.ExternalExecute;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.ArrayUtils;
import si.fri.algotest.entities.EVariable;
import si.fri.algotest.entities.EResult;
import si.fri.algotest.entities.ETestSet;
import si.fri.algotest.entities.MeasurementType;
import si.fri.algotest.entities.Variables;
import si.fri.algotest.entities.VariableType;
import si.fri.algotest.entities.Project;
import si.fri.algotest.entities.StatFunction;
import si.fri.algotest.global.ATGlobal;
import si.fri.algotest.global.ATLog;
import si.fri.algotest.global.ErrorStatus;
import si.fri.algotest.global.ExecutionStatus;
import si.fri.algotest.tools.UniqueIDGenerator;

/**
 *
 * @author tomaz
 */
public class ExternalExecutor {

  /**
   * The original algorithm that is passed to the executor is written to the
   * TEST file, the algorithm with the results (times and parameters) is written
   * to the RESUL file.
   */
  public static enum SER_ALG_TYPE {

    TEST {
      @Override
      public String toString() {
        return ".test";
      }
    },
    RESULT {
      @Override
      public String toString() {
        return ".result";
      }
    }
  }

  private final static String SERIALIZED_ALG_FILENAME = "alg.ser";

  private static final EVariable failedErr = EResult.getExecutionStatusIndicator(ExecutionStatus.FAILED);

  /**
   * This file is used as a communication chanell between main ALGator executor
   * and ExternalJVMExecutor. When an algorithm test is started, this file is
   * initialized with empty contents. For each execution of the algorithm,
   * ExternalJVMExecutor writes one byte to this file. ALGator's executor
   * regulary checks the content of this file and stops the execution is there
   * is no progress.
   */
  private final static String COMMUNICATION_FILENAME = "comm.data";

  /**
   * Iterates through testset and for each test runs an algorithm.
   *
   * @param project
   * @param algName
   * @param it
   * @param resultDesc
   * @param notificator
   * @param mType
   * @return
   */
  public static void iterateTestSetAndRunAlgorithm(Project project, String algName, AbstractTestSetIterator it, 
          Notificator notificator, MeasurementType mType, File resultFile, int whereToPrint) {
    
    String instanceID = "Test"; //UniqueIDGenerator.getNextID();

    ETestSet testSet = it.testSet;

    // get the number of times to execute one test (this is applicable only
    /// for the EM type of tests; all other tests are performed only once)
    int timesToExecute = 1;
    if (mType.equals(MeasurementType.EM)) {
      try {
        timesToExecute = testSet.getFieldAsInt(ETestSet.ID_TestRepeat, 1);
      } catch (Exception e) {
        // if ETestSet.ID_TestRepeat parameter is missing, timesToExecute is set to 1 and exception is ignored
      }
    }
    timesToExecute = Math.max(timesToExecute, 1); // to prevent negative number

    // Maximum time allowed (in seconds) for one execution of one test; if the algorithm 
    // does not  finish in this time, the execution is killed
    int timeLimit = 10;
    try {
      timeLimit = testSet.getFieldAsInt(ETestSet.ID_TimeLimit, 10);
    } catch (Exception e) {
      try {
        timeLimit = Integer.parseInt(testSet.getField(ETestSet.ID_TimeLimit).toString());
      } catch (Exception ex) {
        // if ETestSet.ID_TimeLimit parameter is missing or invalid, timelimit is set to 10 (sec) and exception is ignored
      }
    }

    EResult resultDesc = project.getResultDescriptions().get(mType); if (resultDesc == null) resultDesc = new EResult();
    
    int testID = 0; // 
    try {
      while (it.hasNext()) {
        it.readNext();
        testID++;

        AbstractTestCase testCase = it.getCurrent();
        String testSetName = it.testSet.getName();

        Variables resultVariables = runTestCase(project, algName, testCase, mType,
                testSetName, testID, timesToExecute, timeLimit, notificator, instanceID+"-"+testID);

        printVariables(resultVariables, resultFile, resultDesc.getVariableOrder(), whereToPrint);
      }
      it.close();
    } catch (Exception e) {
      ErrorStatus.setLastErrorMessage(ErrorStatus.ERROR_CANT_RUN, e.toString());
    }

    ErrorStatus.setLastErrorMessage(ErrorStatus.STATUS_OK, "");
  }

  public static Variables runTestCase(
          Project project, String algName, AbstractTestCase testCase, MeasurementType mType,
          String testSetName, int testID, int timesToExecute, int timeLimit,
          Notificator notificator, String instanceID) {
    
    if (instanceID == null || instanceID.isEmpty())
      instanceID = UniqueIDGenerator.getNextID();

    AbstractAlgorithm algorithm      = New.algorithmInstance(project, algName, mType);
    AbstractInput     input          = testCase != null ? testCase.getInput() : null;
    AbstractOutput    expectedOutput = testCase != null ? testCase.getExpectedOutput() : null;

    EResult           resultDesc     = project.getResultDescriptions().get(mType); if (resultDesc == null) resultDesc = new EResult();
    
    // V testcase dodam podatke o indikatorjih, da se ohrani informacija, ki je bila definirana
    // v atrd datoteki; recimo, če je tam definiram indikator tipa double, se prej podatki o tem indikatorju (recimo 
    // meta - število decimalk) ni prenesel in se je zato vedno uporabila default vrednost. Po tej spremembi se 
    // podatki pravilno prenesejo.
    if (expectedOutput != null) {
      for (EVariable evar : resultDesc.getVariables()) {
        expectedOutput.addIndicator(evar, false);
      }      
    }

    // nastavim podatek o številu ponovitev
    if (algorithm != null) {
      algorithm.setTimesToExecute(timesToExecute);
    }

    // were instanceBundle and algorithm created?
    boolean executionOK = testCase != null && algorithm != null;

    // was algorithm properly initialized?
    executionOK = executionOK && algorithm.init(testCase) == ErrorStatus.STATUS_OK;

    String cFolderName = ATGlobal.getTMPDir(project.getName());
    Variables resultVariables = new Variables();

    try {
      ErrorStatus executionStatus = ErrorStatus.ERROR_CANT_PERFORM_TEST;

      if (executionOK) {
        saveAlgToFile(New.getClassPathsForAlgorithm(project, algName), algorithm, cFolderName, SER_ALG_TYPE.TEST);

        executionStatus = runWithLimitedTime(cFolderName, timesToExecute, timeLimit, mType, false);
      }

      EVariable executionStatusParameter;
      switch (executionStatus) {
        case STATUS_OK:
          if (notificator != null) notificator.notify(testID, ExecutionStatus.DONE);
          executionStatusParameter = EResult.getExecutionStatusIndicator(ExecutionStatus.DONE);
          break;
        case PROCESS_KILLED:
          if (notificator != null) notificator.notify(testID, ExecutionStatus.KILLED);
          executionStatusParameter = EResult.getExecutionStatusIndicator(ExecutionStatus.KILLED);
          break;
        default:
          if (notificator != null) notificator.notify(testID, ExecutionStatus.FAILED);
          executionStatusParameter = failedErr;
      }

      Variables algResultIndicators = new Variables();

      // algorithm instance obtained from file as a result of execution
      if (executionStatus == ErrorStatus.STATUS_OK) { // the execution passed normaly (not killed)
        AbstractAlgorithm resultAlg = getAlgorithmFromFile(cFolderName, SER_ALG_TYPE.RESULT);

        if (resultAlg != null) {
          algResultIndicators = resultAlg.done();

          switch (mType) {
            case EM:
              algResultIndicators.addVariables(getTimeParameters(resultDesc, resultAlg), true);
              break;
            case CNT:
              algResultIndicators.addVariables(getCounterParameters(resultDesc, resultAlg), true);
              break;
          }
        } else {
          algResultIndicators.addVariable(EResult.getErrorIndicator("Invalid test: " + input.toString()), true);
          executionStatusParameter = failedErr;
        }
      } else { // the execution did not perform succesfully          
        try {
          // if possible, obtain indicators from the algorithm 
          algResultIndicators = algorithm.getCurrentTestCase().getExpectedOutput().getIndicators();
        } catch (Exception e) {
          algResultIndicators = new Variables();
        }

        if (executionStatus == ErrorStatus.PROCESS_KILLED) {
          algResultIndicators.addVariable(EResult.getErrorIndicator(
                  String.format("Process killed after %d seconds.", timeLimit)), true);
        } else {
          algResultIndicators.addVariable(EResult.getErrorIndicator(ErrorStatus.getLastErrorMessage()), true);
        }
      }

      if (algorithm.getCurrentInput() != null) {
        resultVariables.addVariables(algorithm.getCurrentInput().getParameters());
      }
           
      resultVariables.addVariables(algResultIndicators, false);
      resultVariables.addVariable(EResult.getAlgorithmNameParameter(algName), true);
      resultVariables.addVariable(EResult.getTestsetNameParameter(testSetName), true);
      resultVariables.addVariable(EResult.getTimestampParameter(System.currentTimeMillis()), true);
      resultVariables.addVariable(EResult.getInstanceIDParameter(instanceID), true);
            
      resultVariables.addVariable(executionStatusParameter, true); 

    } catch (Exception e) {
    } finally {
      ATGlobal.deleteTMPDir(cFolderName, project.getName());
    }

    return resultVariables;

  }

  /**
   * Prints varibles (parameters and indicators) in a given order to stdout and/or file.
   * Parameter whereToPrint: 0 ... none, 1 ... stdout, 2 ... file (note: 3 = both, 1 and 2).
   */
  public static void printVariables(Variables resultVariables, File resultFile, String [] order, int whereToPrint) {
    if (resultVariables != null) {
      // print to stdout
      if (((whereToPrint & ATLog.TARGET_STDOUT) == ATLog.TARGET_STDOUT))            
        resultVariables.printToFile(new PrintWriter(System.out), order);      
      
      // print to file
      if (((whereToPrint & ATLog.TARGET_FILE) == ATLog.TARGET_FILE) && (resultFile != null))
        resultVariables.printToFile(resultFile, order);          
    }  
  }
  
  
  // has the process finished?
  private static boolean processIsTerminated(Process process) {
    try {
      process.exitValue();
    } catch (IllegalThreadStateException itse) {
      return false;
    }
    return true;
  }

  /**
   * Method runs a given algorithm (algorithm's serialized file is written in
   * foldername) for n times (where n is one of the paramters in the algorithm's
   * testcase) and returns null if each execution finished in
   * timeForOneExecutionTime and errorMessage otherwise
   */
  static ErrorStatus runWithLimitedTime(String folderName, int timesToExecute,
          long timeForOneExecution, MeasurementType mType, boolean verbose) {

    Object result = ExternalExecute.runWithExternalJVM(folderName, mType, verbose);

    // during the process creation, an error occured
    if (result instanceof String) {
      return ErrorStatus.setLastErrorMessage(ErrorStatus.PROCESS_CANT_BE_CREATED, (String) result);
    }

    if (!(result instanceof Process)) {
      return ErrorStatus.setLastErrorMessage(ErrorStatus.ERROR.ERROR, "?unknown?");
    }

    Process externProcess = (Process) result;

    long milis = System.currentTimeMillis();
    whileloop:
    while (true) {
      // loop for one second
      for (int i = 0; i < 10; i++) {
        if (processIsTerminated(externProcess)) {
          break whileloop;
        }
        // wait for 0.1s
        try {
          Thread.sleep(100);
        } catch (Exception e) {
        }
      }

      long resultsCount = getCommunicationCount(folderName);
      long secondsPassed = (System.currentTimeMillis() - milis) / 1000;

      int expectedResults = (int) (secondsPassed / timeForOneExecution);
      if (resultsCount < expectedResults) {
        externProcess.destroy();
        return ErrorStatus.setLastErrorMessage(ErrorStatus.PROCESS_KILLED,
                String.format("(after %d sec.)", (int) secondsPassed));
      }
    }

    try {
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(externProcess.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(externProcess.getErrorStream()));

      String s;
      StringBuffer sb = new StringBuffer();
      while ((s = stdInput.readLine()) != null) {
        sb.append(s);
      }
      while ((s = stdError.readLine()) != null) {
        sb.append(s);
      }

      if (sb.length() != 0) {
        return ErrorStatus.setLastErrorMessage(ErrorStatus.PROCESS_CANT_BE_CREATED, sb.toString());
      } else {
        return ErrorStatus.STATUS_OK;
      }

    } catch (Exception e) {
      return ErrorStatus.setLastErrorMessage(ErrorStatus.PROCESS_CANT_BE_CREATED, e.toString());
    }
  }

  // pregledam resultDesc parametre in za vsak parameter tipa "timer" ustvarim
  // parameter v results s pravo vrednostj
  static Variables getTimeParameters(EResult resultDesc, AbstractAlgorithm algorithm) {
    Variables timeParameters = new Variables();
    long[][] times = algorithm.getExecutionTimes();

    if (resultDesc != null) {
      for (EVariable rdP : resultDesc.getVariables()) {
        if (VariableType.TIMER.equals(rdP.getType())) {
          String[] subtypeFields;
          try {
            int tID = rdP.getMeta("ID", 0);
            String statDesc = rdP.getMeta("STAT", "MIN");
            StatFunction fs = StatFunction.getStatFunction(statDesc);

            // times[tID] -> ArrayList<Long> (list)
            Long[] longObjects = ArrayUtils.toObject(times[tID]);
            ArrayList<Long> list = new ArrayList<>(java.util.Arrays.asList(longObjects));

            Long time = (Long) StatFunction.getFunctionValue(fs, list);
            EVariable timeP = new EVariable(
                    (String) rdP.getName(), null, VariableType.TIMER, time);
            timeParameters.addVariable(timeP, true);
          } catch (Exception e) {
            ErrorStatus.setLastErrorMessage(ErrorStatus.ERROR, "Meta parameter invalid (" + e.toString() + ")");
          }
        }
      }
    }
    return timeParameters;
  }

  static Variables getCounterParameters(EResult resultDesc, AbstractAlgorithm algorithm) {
    Variables counterParameters = new Variables();
    HashMap<String, Integer> counters = algorithm.getCounters();
    if (resultDesc != null && counters != null) {
      for (EVariable evar : resultDesc.getVariables()) {
        if (VariableType.COUNTER.equals(evar.getType())) {
          String counterName = (String) evar.getName();
          int value = 0;
          if (counters.containsKey(counterName)) {
            value = counters.get(counterName);
          }
          counterParameters.addVariable(new EVariable(counterName, null, null, value), true);
        }
      }
    }
    return counterParameters;
  }

  /**
   * Saves the measurement type, classpath and algotihm instance to a file.
   */
  public static boolean saveAlgToFile(URL[] urls, AbstractAlgorithm curAlg,
          String folderName, SER_ALG_TYPE algType) {
    try (FileOutputStream fis = new FileOutputStream(new File(folderName + File.separator + SERIALIZED_ALG_FILENAME + algType));
            ObjectOutputStream dos = new ObjectOutputStream(fis);) {
      dos.writeObject(urls);
      dos.writeObject(curAlg);

      return true;
    } catch (Exception e) {
      ErrorStatus.setLastErrorMessage(ErrorStatus.CANT_WRITE_ALGORITHM_TO_FILE, e.toString());
      return false;
    }
  }

  //need to do add path to Classpath with reflection since the URLClassLoader.addURL(URL url) method is protected:
  static void addPath(URL s) throws Exception {
    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    Class<URLClassLoader> urlClass = URLClassLoader.class;
    Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
    method.setAccessible(true);
    method.invoke(urlClassLoader, new Object[]{s});
  }

  public static AbstractAlgorithm getAlgorithmFromFile(String folderName, SER_ALG_TYPE algType) {
    try (FileInputStream fis = new FileInputStream(new File(folderName + File.separator + SERIALIZED_ALG_FILENAME + algType));
            ObjectInputStream ois = new ObjectInputStream(fis);) {
      // get the URLs that were used to load algorithm ...
      Object o = ois.readObject();
      URL[] urls = (URL[]) o;
      // ... and add these URLS to URLClassLoader
      if (urls != null) {
        for (URL url : urls) {
          addPath(url);
        }
      }

      // Try to instantiate the algorithm
      o = ois.readObject();
      return (AbstractAlgorithm) o;
    } catch (Exception e) {
      ErrorStatus.setLastErrorMessage(ErrorStatus.CANT_READ_ALGORITHM_FROM_FILE, e.toString());
      return null;
    }
  }

  public static URL[] getURLsFromFile(String folderName, SER_ALG_TYPE algType) {
    try (FileInputStream fis = new FileInputStream(new File(folderName + File.separator + SERIALIZED_ALG_FILENAME + algType));
            ObjectInputStream ois = new ObjectInputStream(fis);) {
      // get the URLs that were used to load algorithm ...
      Object o = ois.readObject();
      return (URL[]) o;
    } catch (Exception e) {
      ErrorStatus.setLastErrorMessage(ErrorStatus.CANT_READ_ALGORITHM_FROM_FILE, e.toString());
      return null;
    }
  }

  /**
   * This method clears the contents of the communication file
   *
   * @param folderName
   */
  public static boolean initCommunicationFile(String folderName) {
    File f = new File(folderName + File.separator + COMMUNICATION_FILENAME);
    try (FileWriter fw = new FileWriter(f)) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static void addToCommunicationFile(String folderName) {
    File f = new File(folderName + File.separator + COMMUNICATION_FILENAME);

    try (FileWriter fw = new FileWriter(f, true)) {
      fw.write((byte) 0);
    } catch (Exception e) {
    }
  }

  public static long getCommunicationCount(String folderName) {
    File f = new File(folderName + File.separator + COMMUNICATION_FILENAME);
    try {
      return f.length();
    } catch (Exception e) {
      return 0;
    }    
  }
  
  void a(Object a) {}
  void a(String a) {}

}
