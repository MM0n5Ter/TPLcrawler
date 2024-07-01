package gossip.example;

import gossip.example.analyse.AnalyseProfile;
import org.xmlpull.v1.XmlPullParserException;
import soot.Scene;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.options.Options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Executor {
    private final RunOptions option;
    private final String androidJar;
    private final String target;
    private static final Logger logger = LoggerFactory.getLogger(Executor.class);
    private AnalyseProfile profile;

    public Executor(String input, String androidJar, RunOptions option) {
        this.option = option;
        this.androidJar = androidJar;
        this.target = input;
    }

    public void run() {
        profile = new AnalyseProfile();
        switch (option) {
            case SINGLE:
                runSingle(target);
                break;
            case DIRECTORY:
                runDir(target);
                break;
        }
    }

    private void runSingle(String apkPath) {
        logger.info("Analysing: " + apkPath);
        addAPKProfile(apkPath, androidJar);
        writeResult(profile.getResult());
    }

    private void runDir(String dirPath) {
        logger.info("AnalysingPath: " + dirPath);
        List<String> apks = getApkList(dirPath);
        for(String apk : apks) {
            logger.info("Analysing: " + dirPath + "/" + apk);
            addAPKProfile(dirPath + "/" + apk, androidJar);
        }
        writeResult(profile.getResult());
    }

    private List<String> getApkList(String dirPath) {
        File directory = new File(dirPath);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.list())));
    }
    public void addAPKProfile(String apkPath, String androidJarPath) {
        setupSoot(apkPath, androidJarPath);
        try (ProcessManifest manifest = new ProcessManifest(apkPath)) {
            profile.AppProfile(apkPath, manifest);
        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeResult(List<String> packages) {
        String filename = config.outputDir + "/result.txt";
        try (PrintWriter out = new PrintWriter(filename)) {
            for(String pack:packages) {
                out.write(pack + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSoot(String apkPath, String androidJarPath) {
        soot.G.reset();
        Options sootOpt = Options.v();
        sootOpt.set_keep_line_number(false);
        sootOpt.set_prepend_classpath(true);
        sootOpt.set_allow_phantom_refs(true);
        sootOpt.set_whole_program(true);
        sootOpt.set_src_prec(Options.src_prec_apk);
        sootOpt.set_process_dir(Collections.singletonList(apkPath));
        sootOpt.set_android_jars(androidJarPath);
        sootOpt.set_process_multiple_dex(true);
//        sootOpt.set_num_threads(16);
        sootOpt.set_output_format(Options.output_format_dex);

        Scene.v().loadBasicClasses();
        Scene.v().loadNecessaryClasses();
        Scene.v().loadDynamicClasses();
    }
}
