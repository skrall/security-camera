package org.krall.security.commandline;

import ch.qos.logback.classic.Level;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class LogLevelHandler extends OneArgumentOptionHandler<Level> {

    public LogLevelHandler(CmdLineParser parser, OptionDef option, Setter<? super Level> setter) {
        super(parser, option, setter);
    }

    @Override
    protected Level parse(String argument) throws NumberFormatException, CmdLineException {
        try {
            return Level.toLevel(argument);
        } catch(Exception e) {
            throw new CmdLineException(owner, String.format("Level %s is not a valid logback level.", argument));
        }
    }

    @Override
    public String getDefaultMetaVariable() {
        return "LEVEL";
    }
}
