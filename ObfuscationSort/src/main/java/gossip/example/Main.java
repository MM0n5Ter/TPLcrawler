package gossip.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        CLIParser cliParser = new CLIParser(args);
        logger.info("SDK Path:  "+ cliParser.getAndroidJAR());

        if(cliParser.getAnalysisOption() == RunOptions.SINGLE) {
            logger.info("Input: " + cliParser.getTargetAPK());
            Executor executor = new Executor(cliParser.getTargetAPK(), cliParser.getAndroidJAR(), RunOptions.SINGLE);
            executor.run();
        } else if (cliParser.getAnalysisOption() == RunOptions.DIRECTORY) {
            logger.info("Input: " + cliParser.getTargetDir());
            Executor executor = new Executor(cliParser.getTargetDir(), cliParser.getAndroidJAR(), RunOptions.DIRECTORY);
            executor.run();
        }

    }
}
