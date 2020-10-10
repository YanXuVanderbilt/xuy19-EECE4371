import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;

public class EmailProtocolMessage {

    private static final String PARAM_SEPERATOR = "&";
    private static final String PARAM_ASSIGNMENT = "=";

    public static final String TYPE_KEY = "type";

    public static final String LOGIN_COMMAND = "log_in";
    public static final String SENDEMAIL_COMMAND = "send_email";
    public static final String RETRIVE_EMAIL_COMMAND = "retrieve_emails";
    public static final String LOGOUT_COMMAND = "log_out";
    public static Hashtable<String, String> dict = new Hashtable<String, String>();

    public String toString() {
        for (Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            String seg = key + "=" + val;
        }
        // StringBuilder stringBuilder = new StringBuilder();
        // Set<String> keys = mParams.keySet();
        String seperator = "&";
        return "";
    }

    public String getParam(String key) {
        return "";
    }

    public void putParam(String key, String command) {
        dict.put(key, command);
    }

    public EmailProtocolMessage() {
    }

    public EmailProtocolMessage(String message) {

    }
}
