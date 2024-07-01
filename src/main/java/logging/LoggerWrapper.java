package logging;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerWrapper
{
    private final Logger logger;

    public LoggerWrapper(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    public void info(String message)
    {
        logger.info(message);
    }

    public void error(String message)
    {
        logger.error(message);
    }
}
