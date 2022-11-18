package ir.chaapaar.project.util;

import org.owasp.esapi.ESAPI;

public class LogUtils {

    public static String encode(String message) {
        message = message.replace('\n', '_').replace('\r', '_')
                .replace('\t', '_');
        message = ESAPI.encoder().canonicalize(message);
        return message;
    }

}
