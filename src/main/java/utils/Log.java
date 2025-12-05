package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

	public static Logger log = LogManager.getLogger(Log.class);

	public static void info(String messgae) {
		log.info(messgae);
	}

	public static void warning(String messgae) {
		log.warn(messgae);
	}

	public static void error(String messgae) {
		log.error(messgae);
	}

	public static void debug(String messgae) {
		log.debug(messgae);
	}

}
