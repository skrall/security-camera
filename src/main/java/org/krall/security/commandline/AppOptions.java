package org.krall.security.commandline;

import ch.qos.logback.classic.Level;
import org.kohsuke.args4j.Option;

import java.io.File;

@SuppressWarnings("FieldCanBeLocal")
public class AppOptions {

    private static AppOptions appOptions;

    @Option(name = "--width", aliases = {"-w"}, depends = "--height", usage = "Width of the image captured")
    private int imageWidth = 800;

    @Option(name = "--height", aliases = {"-h"}, depends = "--width", usage = "Height of the image captured")
    private int imageHeight = 600;

    @Option(name = "--inputDirectory", aliases = {"-i"}, usage = "Directory to read for image input.")
    private File inputDirectory = new File("/home/pi/images/input");

    @Option(name = "--outputDirectory", aliases = {"-o"}, usage = "Directory to read for image input.")
    private File outputDirectory = new File("/home/pi/images/output");

    @Option(name = "--verticalRegions", aliases = {"-vr"}, usage = "Number of vertical Regions")
    private int verticalRegions = 8;

    @Option(name = "--horizontalRegions", aliases = {"-hr"}, usage = "Number of horizontal Regions")
    private int horizontalRegions = 6;

    @Option(name = "--sensitivity", aliases = {"-s"}, usage = "Sensitivity level for motion detection")
    private int sensitivity = 5;

    @Option(name = "--stabilizer", aliases = {"-st"}, usage = "Stabilizer level for motion detection")
    private int stabilizer = 10;

    @Option(name = "--loglevel", aliases = {"-l"}, usage = "Log level", handler = LogLevelHandler.class)
    private Level logLevel = Level.INFO;

    @Option(name = "--useTimeLapse", aliases = {"-t"}, usage = "Use time lapse feature of raspistill")
    private boolean useTimeLapse = false;

    @Option(name = "--noWriteDifferences", aliases = {"-n"}, usage = "Don't write differences to disk")
    private boolean noWriteDifferences = false;

    @Option(name = "--noDrawDifferenceMarks", aliases = {"-a"}, usage = "Don't modify the images, " +
                                                                        "marking what area on the image changed")
    private boolean noDrawDifferenceMarks = false;

    @Option(name = "--startJetty", aliases = {"-j"}, usage = "Start jetty web server, to serve image differences.")
    private boolean startJetty = false;


    @Option(name = "--disableFreeSpaceChecker", aliases = {"-d"}, usage = "Disable background thread that checks for " +
                                                                          "free space.")
    private boolean disableFreeSpaceChecker = false;

    @Option(name = "--millisBetweenCapture", aliases = {"-m"}, usage = "Milliseconds between captures when in time " +
                                                                       "lapse mode.")
    private int millisBetweenCapture = 1000;

    @Option(name = "--maxPercentFree", usage = "Maximum percent used before a purge happens.")
    private int maxPercentFree = 80;


    @Option(name = "--keepPercentFree", usage = "Maximum percent used before a purge happens.")
    private int keepPercentFree = 50;

    @Option(name = "--raspistillOptions", aliases = {"-p"}, usage = "Options to pass directly to raspistill.")
    private String raspistillOptions = "";


    static {
        appOptions = new AppOptions();
    }

    private AppOptions() {
    }

    public static AppOptions getInstance() {
        return appOptions;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public File getInputDirectory() {
        return inputDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public int getVerticalRegions() {
        return verticalRegions;
    }

    public int getHorizontalRegions() {
        return horizontalRegions;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public int getStabilizer() {
        return stabilizer;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public boolean isUseTimeLapse() {
        return useTimeLapse;
    }

    public boolean isNoWriteDifferences() {
        return noWriteDifferences;
    }

    public boolean isNoDrawDifferenceMarks() {
        return noDrawDifferenceMarks;
    }

    public boolean isStartJetty() {
        return startJetty;
    }

    public boolean isDisableFreeSpaceChecker() {
        return disableFreeSpaceChecker;
    }

    public int getMillisBetweenCapture() {
        return millisBetweenCapture;
    }

    public int getMaxPercentFree() {
        return maxPercentFree;
    }

    public int getKeepPercentFree() {
        return keepPercentFree;
    }

    public String getRaspistillOptions() {
        return raspistillOptions;
    }
}
