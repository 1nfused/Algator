package algator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import si.fri.adeserver.ADEGlobal;
import si.fri.adeserver.ADELog;
import si.fri.algotest.global.ATGlobal;

/**
 *
 * @author tomaz
 */
public class TaskServer {
  
  private static String introMsg = "ALGator TaskServer, " + Version.getVersion();

  private static Options getOptions() {
    Options options = new Options();

    Option data_root = OptionBuilder.withArgName("data_root_folder")
            .withLongOpt("data_root")
            .hasArg(true)
            .withDescription("use this folder as data_root; default value in $ALGATOR_DATA_ROOT (if defined) or $ALGATOR_ROOT/data_root")
            .create("dr");

    Option algator_root = OptionBuilder.withArgName("algator_root_folder")
            .withLongOpt("algator_root")
            .hasArg(true)
            .withDescription("use this folder as algator_root; default value in $ALGATOR_ROOT")
            .create("r");

    options.addOption(data_root);
    options.addOption(algator_root);

    options.addOption("h", "help", false,
            "print this message");
    options.addOption("s", "status", false,
            "print status of TaskServer process");

    return options;
  }

  private static void printMsg(Options options) {
    System.out.println(introMsg + "\n");

    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("algator.TaskServer [options]", options);

    System.exit(0);
  }

  public static void main(String args[]) {
    Options options = getOptions();

    CommandLineParser parser = new BasicParser();
    try {
      CommandLine line = parser.parse(options, args);

      if (line.hasOption("h")) {
	printMsg(options);
      }
            
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
      
      // TaskServer uses only data_root folder
      ATGlobal.setALGatorDataLocal(ATGlobal.getALGatorDataRoot());      

      boolean statusOnly = false;
      if (line.hasOption("status")) {
	statusOnly = true;
      }
      
      boolean serverIsAlive = askServer(ADEGlobal.REQ_CHECK_Q).equals(ADEGlobal.REQ_CHECK_A);
      
      ADELog.doVerbose = true;
      
      if (statusOnly) {
        if (serverIsAlive) {
          System.out.println(askServer(ADEGlobal.REQ_STATUS));
        } else {
          System.out.println("Task server is not running.");
        }        
      } else {
        if (serverIsAlive) {
          System.out.println("Task server is already running.");
        } else {
          si.fri.adeserver.ADETaskServer.runServer();
        }
      }
    } catch (ParseException ex) {
      printMsg(options);
    }
  }
  
  private static String askServer(String question) {
    String hostName   = "localhost"; // it has to be local host, since this method only tries to communicate with the server installed on this machine
    int    portNumber = ADEGlobal.ADEPort;
    
    try (   Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter    toServer    = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader fromServer  = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        ) {

      toServer.println(question);
      String answer = fromServer.readLine();
      
      return answer;      
      
    } catch (UnknownHostException e) {
      return ADEGlobal.ERROR_PREFIX + "Unknown host " + hostName;
    } catch (IOException e) {
      return ADEGlobal.ERROR_PREFIX + "I/O error " + e;
    }
  }

}
