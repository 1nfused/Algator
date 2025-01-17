package si.fri.algotest.global;

import java.io.File;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import org.apache.commons.io.FileUtils;
import si.fri.algotest.entities.ELocalConfig;
import si.fri.algotest.entities.MeasurementType;

/**
 * Definition of global values use in the AT project
 *
 * @author tomaz
 */
public class ATGlobal {

  private static String ALGatorRoot      = System.getenv("ALGATOR_ROOT");
  private static String ALGatorDataRoot  = System.getenv("ALGATOR_DATA_ROOT");
  private static String ALGatorDataLocal = System.getenv("ALGATOR_DATA_LOCAL");
  
  
  public static int logTarget = 1;    // stdout
  public static int verboseLevel = 1; // print some information
  
  public static final String DEFAULT_CSV_DELIMITER    = ";";
  
    
  // File extensions for AT entities
  public static final String AT_FILEEXT_project    = "atp";
  public static final String AT_FILEEXT_algorithm  = "atal";
  public static final String AT_FILEEXT_testset    = "atts";
  public static final String AT_FILEEXT_resultdesc = "atrd";
  public static final String AT_FILEEXT_query      = "atqd";
  public static final String AT_FILEEXT_presenter  = "atpd";

  // For the structure of the Project folder see ALGator.docx documentation
  public static final String ATDIR_data_local     = "data_local";
  public static final String ATDIR_data_root      = "data_root";
  public static final String ATDIR_projects       = "projects";  
  public static final String ATDIR_projRootDir    = "PROJ-%s";
  public static final String ATDIR_projConfDir    = "proj";
  public static final String ATDIR_srcDir         = "src";
  public static final String ATDIR_binDir         = "bin";
  public static final String ATDIR_testsDir       = "tests";
  public static final String ATDIR_resultsDir     = "results";
  public static final String ATDIR_algsDir        = "algs";
  public static final String ATDIR_algDir         = "ALG-%s";
  public static final String ATDIR_queryDir       = "queries";
  public static final String ATDIR_presenterDir   = "presenters";
  public static final String ATDIR_queryOutput    = "output";

  public static final String ATDIR_logDir         = "log";
  public static final String ATDIR_algatorLOGfile = "algator.log";
  public static final String ATDIR_taskLogDir     = "tasks";
  
  public static final String ATDIR_tmpDir         = "tmp";
  public static final String ATDIR_tmpFile        = "tmpF";

  public static final String ATDIR_docFolder      = "doc";
  
  public static final String LOCAL_CONFIG_FILENAME   = "algator_local.acfg";      
  public static final String GLOBAL_CONFIG_FILENAME  = "algator.acfg";  
  
  public static final String ATDIR_libDir         = "lib";
  
  public static final String COUNTER_CLASS_EXTENSION = "_COUNT"; 
  
  
  /**
   * Normally ALGator executes algorithms in a separate JVM which enables ALGator to control 
   * the execution time (and kill process in it last for too long). This functionality 
   * disables debuging (in Netbeans, for example), since the algorithm does not run in 
   * the current JVM.
   * By setting debugMode = true, the algorithm is executed in the same JVM and it can be debuged. 
   */
  public static boolean debugMode = false;
  
  
  
  public static String getALGatorRoot() {
    String result = ALGatorRoot;
    if (result == null || result.isEmpty())
      result = System.getProperty("user.dir", "");
    return result;
  }
  
  public static String getALGatorDataRoot() {
    String result = ALGatorDataRoot;
    if (result == null || result.isEmpty())
      result = getALGatorRoot() + File.separator + ATDIR_data_root;
    return result;
  }

  public static String getALGatorDataLocal() {
    String result = ALGatorDataLocal;
    if (result == null || result.isEmpty())
      result = getALGatorRoot() + File.separator + ATDIR_data_local;
    return result;
  }
  
  
  public static void setALGatorRoot(String algatorRoot) {
    ALGatorRoot = algatorRoot;
  }
  public static void setALGatorDataRoot(String dataRoot) {
    ALGatorDataRoot = dataRoot;
  }
  public static void setALGatorDataLocal(String dataLocal) {
    ALGatorDataLocal = dataLocal;
  }

  
  public static String getLogFolder() {
    String folderName = getALGatorDataRoot() + File.separator + ATDIR_logDir;
    File folder = new File(folderName);
    if (!folder.exists())
      folder.mkdir();
    return folderName;
  }

  public static String getTaskLogFolder() {
    String folderName = getLogFolder() + File.separator + ATDIR_taskLogDir;
    File folder = new File(folderName);
    if (!folder.exists())
      folder.mkdir();
    return folderName;    
  }
  
  public static String getAlgatorLogFilename() {
    return getLogFolder() + File.separator + ATDIR_algatorLOGfile;
  }
  
  public static String getTaskStatusFilename(String project, String algorithm, String testset, String mtype) {
    return getTaskLogFolder() + File.separator + 
       String.format("%s-%s-%s-%s.status", project, algorithm, testset, mtype);
  }

  public static String getTaskHistoryFilename(String project, String algorithm, String testset, String mtype) {
    return getTaskLogFolder() + File.separator + 
       String.format("%s-%s-%s-%s.history", project, algorithm, testset, mtype);
  }

  /**
   * Extracts and returns the data root folder from the project root folder.
   * Example: /ALGATOR_ROOT/data_root/projects/PROJ-Sorting -> /ALGATOR_ROOT/data_root
   */
  public static String getDataRootFromProjectRoot(String projRoot) {
    //     /projects/PROJ-
    String middleStr = File.separator + ATDIR_projects + File.separator + String.format(ATDIR_projRootDir, "");
    int pos = projRoot.lastIndexOf(middleStr);
    return (pos != -1 ? projRoot.substring(0, pos) : projRoot);
  }
  
  /**
   * Returns the root of the project
   *
   * @param data_root root for all projects
   * @param projName project name
   * @return
   */
  public static String getPROJECTroot(String data_root, String projName) {
    return data_root + File.separator + ATDIR_projects + File.separator + String.format(ATDIR_projRootDir, projName);
  }

  /**
   * Returns folder path where all projects are located
   **/
  public static String getPROJECTSfolder(String data_root) {
    return data_root + File.separator + ATDIR_projects;
  }
  
  public static String getALGORITHMpath(String data_root, String projName) {
    return data_root + File.separator + ATDIR_projects + File.separator + String.format(ATDIR_projRootDir, projName)+ File.separator + ATDIR_algsDir;
  }
  public static String getTESTSETpath(String data_root, String projName) {
    return data_root + File.separator + ATDIR_projects + File.separator + String.format(ATDIR_projRootDir, projName)+ File.separator + ATDIR_testsDir;
  }
  
  
  /**
   * Return the name of the project's configuration folder 
   */
  public static String getPROJECTconfig(String data_root, String projName) {
    return getPROJECTroot(data_root, projName) + File.separator + ATDIR_projConfDir;
  }
  
  /**
   * Returns the name of the project configuration file
   *
   * @param data_root root for all projects
   * @param projName project name
   */
  public static String getPROJECTfilename(String data_root, String projName) {
    return getPROJECTconfig(data_root, projName)
            + File.separator + projName + "." + AT_FILEEXT_project;
  }
 
  public static boolean projectExists(String data_root, String projName) {
    File f = new File(getPROJECTfilename(data_root, projName));
    return f.exists();
  }
  
  /**
   * Returns the name of the folder with template file(s) and other java
   * sources.
   *
   * @param projectRoot
   * @return
   */
  public static String getPROJECTsrc(String projectRoot) {
    return projectRoot + File.separator + ATDIR_projConfDir + File.separator + ATDIR_srcDir;
  }

  /**
   * Returns the name of the folder with compiled project's java sources.   
   * This folder is subfolder of DATA_LOCAL folder
   */
  public static String getPROJECTbin(String projectName) {
    return getPROJECTroot(getALGatorDataLocal(), projectName) + File.separator 
            + ATDIR_projConfDir + File.separator + ATDIR_binDir;
  }

  
  public static String getPROJECTlib(String projectRoot) {
    return projectRoot + File.separator + ATDIR_projConfDir + File.separator + ATDIR_libDir;
  }

  
  public static String getPROJECTdoc(String data_root, String projName) {
    return getPROJECTconfig(data_root, projName) + File.separator + ATDIR_docFolder;
  }
  
  
  
  public static String getALGORITHMroot(String projectRoot, String algName) {
    return projectRoot + File.separator + ATDIR_algsDir + File.separator
            + String.format(ATDIR_algDir, algName);
  }

  public static String getALGORITHMfilename(String projectRoot, String algName) {
    return getALGORITHMroot(projectRoot, algName) + File.separator + algName + "." + AT_FILEEXT_algorithm;
  }

  public static String getALGORITHMsrc(String projectRoot, String algName) {
    return getALGORITHMroot(projectRoot, algName) + File.separator + ATDIR_srcDir;
  }

  public static String getALGORITHMdoc(String projectRoot, String algName) {
    return getALGORITHMroot(projectRoot, algName) + File.separator + ATDIR_docFolder;
  }
  
  
  // Algorithm's bin folder is a DATA_LOCAL subfolder
  public static String getALGORITHMbin(String projectName, String algName) {
    return getALGORITHMroot(getPROJECTroot(getALGatorDataLocal(), projectName), algName) + File.separator + ATDIR_binDir;
  }

  
  /************* TESTS *+++++++++++++++++++++*/
  public static String getTESTSroot(String data_root, String projectName) {
    String projectRoot = getPROJECTroot(data_root, projectName);
    return projectRoot + File.separator + ATDIR_testsDir;
  }
  
  public static String getTESTSdoc(String data_root, String projectName) {
    return getTESTSroot(data_root, projectName) + File.separator + ATDIR_docFolder;
  }
  
  /**
   * Returns the name of a test set configuration file. This file is placed in
   * the projects tests folder
   */
  public static String getTESTSETfilename(String data_root, String projectName, String testSetName) {
    return getTESTSroot(data_root, projectName) + File.separator + testSetName + "." + AT_FILEEXT_testset;
  }

  
  
  /************* RESULTS *+++++++++++++++++++++*/
  public static String getRESULTDESCfilename(String projectRoot, String projName, MeasurementType measurementType) {
    return projectRoot + File.separator + ATDIR_projConfDir + File.separator + projName + "-" + measurementType.getExtension() + "." + AT_FILEEXT_resultdesc;
  }

  public static String getRESULTSroot(String projectRoot, String computerID) {
    return projectRoot + File.separator + ATDIR_resultsDir + File.separator + computerID;
  }    

  /**
   * The name of a file containing results of the execution of the algorithm
   * {@code algName} on test set {@code testSetName}.
   */
  public static String getRESULTfilename(String projectRoot, String algName, String testSetName, MeasurementType measurementType, String computerID) {
    return getRESULTSroot(projectRoot, computerID) + File.separator + algName + "-" + testSetName + "." + measurementType.getExtension();
  }


  /**
   * The name of a file on tmpFolder to hold info of one test
   * @return 
   */
  public static String getJVMRESULTfilename(String tmpDir, String algName, String testSetName, int testNumber) {
    return tmpDir + File.separator + algName + "-" + testSetName + "-"+testNumber + "." + MeasurementType.JVM.getExtension();
  }


  public static String getQUERIESroot(String projectRoot) {
    return projectRoot + File.separator + ATDIR_queryDir;
  }  

  // the root for the algorithm's querier  
  public static String getALGQUERIESroot(String projectRoot, String algorthmName) {
    return projectRoot + File.separator + ATDIR_algsDir + File.separator + 
            String.format(ATDIR_algDir, algorthmName) + File.separator + ATDIR_queryDir;
  }  

  
  /**
    Queries are of two types: the queries belonging to the project and the queries belonging 
    to the algorithm. The first are located in the PROJ-name/query folder while the latest in
    the ALG-name/query folder. When requesting the query filename, the query could be eithen
    'name' (in this case this is a project's query) of algorithm_name/name (for algorithm's queries)
  */
  public static String getQUERYfilename(String projectRoot, String query) {
    String [] parts = query.split(":");
    if (parts.length == 1) // return projects's query filename
      return getQUERIESroot(projectRoot) + File.separator + query + "." + AT_FILEEXT_query;
    else { // return algorithm's query filename
      return getALGQUERIESroot(projectRoot, parts[0]) + File.separator + parts[1] + "." + AT_FILEEXT_query;
    }
      
  }
  
  public static String getQUERYOutputFilename(String projectRoot, String query, String [] params) {
    String folderName = getQUERIESroot(projectRoot) + File.separator + ATDIR_queryOutput;
    File tmpFolder = new File(folderName);
    if (!tmpFolder.exists())
      tmpFolder.mkdirs();
    
    String fileName = query; 
    if (params != null) 
      for (String param : params) {
        param = param.replaceAll("[^a-zA-Z0-9.-]", "_");
        fileName += "_"+param;
      }
    
    return folderName + File.separator + fileName;
  }
  
  
  /************* presenters  *+++++++++++++++++++++*/
  public static String getPRESENTERSroot(String data_root, String projectName) {
    String projectRoot = getPROJECTroot(data_root, projectName);
    return getPRESENTERSroot(projectRoot);
  }    
  public static String getPRESENTERSroot(String projectRoot) {
    return projectRoot + File.separator + ATDIR_presenterDir;
  }

  
  /************* TMP folders   *+++++++++++++++++++++*/
  public static String getTMProot(String data_local, String prefix) {
    return data_local + File.separator + ATDIR_tmpDir + File.separator + prefix;
  }
  public static String getTMPDir(String data_local, String prefix) {
    String folderName = getTMProot(data_local, prefix) + File.separator + ATDIR_tmpFile + (new Random()).nextLong();
    File tmpFolder = new File(folderName);
    if (!tmpFolder.exists())
      tmpFolder.mkdirs();
    
    return folderName;
  }  
  public static String getTMPDir(String prefix) {
    return getTMPDir(getALGatorDataLocal(), prefix);
  }
  
  /**
   * Tests is tmpFolderName is an ALGator tmp dir. Method is used, for example, 
   * to prevent deleting non tmp folder.
   */
  public static boolean isTMPDir(String tmpFolderName, String data_local, String prefix) {
    return tmpFolderName != null && !tmpFolderName.isEmpty() && 
           data_local != null && !data_local.isEmpty() &&
           prefix   != null && !prefix.isEmpty()   &&
           tmpFolderName.contains(data_local) && tmpFolderName.contains(prefix) &&
           tmpFolderName.contains(File.separator + ATDIR_tmpDir + File.separator) && 
           tmpFolderName.contains(ATDIR_tmpFile);
  }
  
  public static void deleteTMPDir(String tmpFolderName, String data_local, String prefix) {
    try {
      // before deleting, test the "correctness" of the tmpFolderName
      if (ATGlobal.isTMPDir(tmpFolderName, data_local, prefix))
        FileUtils.deleteDirectory(new File(tmpFolderName));
    } catch (Exception e) {
      ErrorStatus.setLastErrorMessage(ErrorStatus.ERROR, "Folder can not be removed " + tmpFolderName + " for project " + prefix);
    }
  }
  public static void deleteTMPDir(String tmpFolderName, String prefix) {
    deleteTMPDir(tmpFolderName, getALGatorDataLocal(), prefix);
  }
  
  
  
  /************* Configurations (local and global) *+++++++++++++++++++++*/
  public static String getLocalConfigFilename() {
    return getALGatorDataLocal() + File.separator + LOCAL_CONFIG_FILENAME;
  }
  
  public static String getGlobalConfigFilename() {
    return getALGatorDataRoot() + File.separator +  GLOBAL_CONFIG_FILENAME;
  }
  
  public static String getThisComputerID() {
    try {
      ELocalConfig config = ELocalConfig.getConfig();
      String id = config.getComputerID();
      if (id == null || id.isEmpty())
        return "F0.C0";
      else
        return id;
    } catch (Exception e) {
      return "F0.C0";
    }
  }
  
  
  public static final String getBuildNumber() { 
   String msg; 
   try { 
     // resource bundle that provides the build number
     ResourceBundle versionRB = ResourceBundle.getBundle("version"); 
     msg = versionRB.getString("BUILD"); 
   } catch (MissingResourceException e) { 
     msg = e.toString();
   } 
   return msg; 
  } 


}
