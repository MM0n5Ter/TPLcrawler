package gossip.example;

import org.apache.commons.cli.*;

enum RunOptions {
    DIRECTORY,
    SINGLE
}

public class CLIParser {
    private static Options options;
    private String targetAPK;
    private String androidJAR;
    private static String wordlistPath;
    private String targetDir;
    private RunOptions runOptions;

    public static class CLIArgs {
        static final String ANDROID_SDK_PATH = "s";
        static final String ANDROID_SDK_PATH_L = "android-sdk";

        static final String WORD_LIST_PATH = "w";
        static final String WORD_LIST_PATH_L = "wordlist";

        static final String ANALYSE_APK_DIR = "d";
        static final String ANALYSE_APK_SINGLE = "i";
        static final String ANALYSE_OUT_DIR = "o";
    }

    public CLIParser(String[] args) {
        setupOptions();
        run(args);
    }

    private void setupOptions() {
        options = new Options();
        Option sdkPath = Option.builder(CLIArgs.ANDROID_SDK_PATH)
                .argName("directory")
                .required(true)
                .longOpt(CLIArgs.ANDROID_SDK_PATH_L)
                .hasArg()
                .desc("path to android sdk")
                .build();

        Option wordlist = Option.builder(CLIArgs.WORD_LIST_PATH)
                .argName("file")
                .required(true)
                .longOpt(CLIArgs.WORD_LIST_PATH_L)
                .hasArg()
                .desc("path to android sdk")
                .build();

        Option analyseDir = Option.builder(CLIArgs.ANALYSE_APK_DIR)
                .argName("analyse_dir")
                .required(false)
                .hasArg()
                .desc("analyse a directory of apk")
                .build();

        Option analysePair = Option.builder(CLIArgs.ANALYSE_APK_SINGLE)
                .argName("analyse_single")
                .required(false)
                .hasArg()
                .desc("analyse a single apk")
                .build();

        Option resultDir = Option.builder(CLIArgs.ANALYSE_OUT_DIR)
                .argName("directory")
                .required(false)
                .hasArg().desc("path to output").build();

        options.addOption(sdkPath);
        options.addOption(wordlist);
        options.addOption(analysePair);
        options.addOption(analyseDir);
        options.addOption(resultDir);
    }


    private void die(String message) {
        System.out.println(message);
        System.exit(0);
    }

    private void usage() {
        System.out.println("usage: java -jar ObfuscationSort.jar -s apk_path");
        System.out.println("                                     -d apk_dir_path");
        System.exit(0);
    }


    private void run(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(CLIArgs.ANDROID_SDK_PATH)) {
                String sdkPath = cmd.getOptionValue(CLIArgs.ANDROID_SDK_PATH);
                if (!Utils.validateDirectory(sdkPath))
                    die("Android SDK path does not exist or it is not a directory: " + sdkPath);
                else
                    androidJAR = sdkPath;
            } else {
                usage();
            }

            if (cmd.hasOption(CLIArgs.WORD_LIST_PATH)) {
                String wordlistPath = cmd.getOptionValue(CLIArgs.WORD_LIST_PATH);
                if (!Utils.validateFile(wordlistPath))
                    die("Wordlist file not exists: " + wordlistPath);
                else
                    CLIParser.wordlistPath = wordlistPath;
            }

            if (cmd.hasOption(CLIArgs.ANALYSE_APK_DIR)) {
                String dir = cmd.getOptionValue(CLIArgs.ANALYSE_APK_DIR);
                if (!Utils.validateDirectory(dir))
                    die("dir path does not exist ot it is not a directory: " + dir);
                else
                    targetDir = dir;
                runOptions = RunOptions.DIRECTORY;
            } else if (cmd.hasOption(CLIArgs.ANALYSE_APK_SINGLE)) {
                runOptions = RunOptions.SINGLE;
                targetAPK = cmd.getOptionValues(CLIArgs.ANALYSE_APK_SINGLE)[0];
            } else {
                usage();
            }

            if (cmd.hasOption(CLIArgs.ANALYSE_OUT_DIR)) {
                String dir = cmd.getOptionValue(CLIArgs.ANALYSE_OUT_DIR);
                if (!Utils.validateDirectory(dir))
                    die("dir path does not exist ot it is not a directory: " + dir);
                else
                    config.outputDir = dir;
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTargetAPK() {
        return targetAPK;
    }

    public String getAndroidJAR() {
        return androidJAR;
    }

    public static String getWordlistPath() {
        return wordlistPath;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public RunOptions getAnalysisOption() {
        return runOptions;
    }
}