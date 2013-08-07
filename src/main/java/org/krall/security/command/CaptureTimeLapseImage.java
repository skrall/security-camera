package org.krall.security.command;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.krall.security.commandline.AppOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaptureTimeLapseImage implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(CaptureTimeLapseImage.class);

    private DefaultExecutor executor = new DefaultExecutor();

    private CommandLine command;

    private CustomExecuteResultHandler resultHandler;

    public CaptureTimeLapseImage() {
        executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
        AppOptions options = AppOptions.getInstance();
        command = CommandLine.parse(String.format("raspistill -o %s/%%08d.jpg -t 3600000 -tl %s -n " +
                                                  "-w %s -h %s ",
                                                  options.getInputDirectory().getAbsolutePath(),
                                                  options.getMillisBetweenCapture(),
                                                  options.getImageWidth(),
                                                  options.getImageHeight()));
    }

    @Override
    public void run() {
        try {
            resultHandler = new CustomExecuteResultHandler();
            executor.execute(command, resultHandler);
        } catch (Exception e) {
            logger.error("Error while running command", e);
            throw new RuntimeException("Error while running command", e);
        }
    }

    private class CustomExecuteResultHandler extends DefaultExecuteResultHandler {

        private final Logger logger = LoggerFactory.getLogger(CustomExecuteResultHandler.class);

        @Override
        public void onProcessComplete(int exitValue) {
            super.onProcessComplete(exitValue);
            logger.info("raspistill exited with value: %s", exitValue);
            if(exitValue == 0) {
                CaptureTimeLapseImage.this.run();
            } else {
                throw new RuntimeException("Not restarting raspistill, it exited with a value of " + exitValue);
            }
        }
    }
}
