package android.util;

import android.os.DeadSystemException;

import java.io.Writer;
import java.net.UnknownHostException;

/**
 * Created by kkmike999 on 2017/06/07.
 */
public class Log {

    public static final int VERBOSE = 2;
    public static final int DEBUG   = 3;
    public static final int INFO    = 4;
    public static final int WARN    = 5;
    public static final int ERROR   = 6;
    public static final int ASSERT  = 7;

    private static class TerribleFailure extends Exception {
        TerribleFailure(String msg, Throwable cause) { super(msg, cause); }
    }

    public interface TerribleFailureHandler {
        void onTerribleFailure(String tag, TerribleFailure what, boolean system);
    }

    private static TerribleFailureHandler sWtfHandler = new TerribleFailureHandler() {
        public void onTerribleFailure(String tag, TerribleFailure what, boolean system) {
//            RuntimeInit.wtf(tag, what, system);
        }
    };

    private Log() {
    }

    public static int v(String tag, String msg) {
        return println_native(LOG_ID_MAIN, VERBOSE, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return printlns(LOG_ID_MAIN, VERBOSE, tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        return println_native(LOG_ID_MAIN, DEBUG, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return printlns(LOG_ID_MAIN, DEBUG, tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        return println_native(LOG_ID_MAIN, INFO, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return printlns(LOG_ID_MAIN, INFO, tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        return println_native(LOG_ID_MAIN, WARN, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return printlns(LOG_ID_MAIN, WARN, tag, msg, tr);
    }

    public static boolean isLoggable(String tag, int level) {
        return true;
    }

    public static int w(String tag, Throwable tr) {
        return printlns(LOG_ID_MAIN, WARN, tag, "", tr);
    }

    public static int e(String tag, String msg) {
        return println_native(LOG_ID_MAIN, ERROR, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return printlns(LOG_ID_MAIN, ERROR, tag, msg, tr);
    }

    public static int wtf(String tag, String msg) {
        return wtf(LOG_ID_MAIN, tag, msg, null, false, false);
    }

    public static int wtfStack(String tag, String msg) {
        return wtf(LOG_ID_MAIN, tag, msg, null, true, false);
    }

    public static int wtf(String tag, Throwable tr) {
        return wtf(LOG_ID_MAIN, tag, tr.getMessage(), tr, false, false);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        return wtf(LOG_ID_MAIN, tag, msg, tr, false, false);
    }

    static int wtf(int logId, String tag, String msg, Throwable tr, boolean localStack,
                   boolean system) {
        TerribleFailure what = new TerribleFailure(msg, tr);
        // Only mark this as ERROR, do not use ASSERT since that should be
        // reserved for cases where the system is guaranteed to abort.
        // The onTerribleFailure call does not always cause a crash.
        int bytes = printlns(logId, ERROR, tag, msg, localStack ? what : tr);
        sWtfHandler.onTerribleFailure(tag, what, system);
        return bytes;
    }

    static void wtfQuiet(int logId, String tag, String msg, boolean system) {
        TerribleFailure what = new TerribleFailure(msg, null);
        sWtfHandler.onTerribleFailure(tag, what, system);
    }

    public static TerribleFailureHandler setWtfHandler(TerribleFailureHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler == null");
        }
        TerribleFailureHandler oldHandler = sWtfHandler;
        sWtfHandler = handler;
        return oldHandler;
    }

//    /**
//     * Handy function to get a loggable stack trace from a Throwable
//     * @param tr An exception to log
//     */
//    public static String getStackTraceString(Throwable tr) {
//        if (tr == null) {
//            return "";
//        }
//
//        // This is to reduce the amount of log spew that apps do in the non-error
//        // condition of the network being unavailable.
//        Throwable t = tr;
//        while (t != null) {
//            if (t instanceof UnknownHostException) {
//                return "";
//            }
//            t = t.getCause();
//        }
//
//        StringWriter sw = new StringWriter();
//        PrintWriter  pw = new FastPrintWriter(sw, false, 256);
//        tr.printStackTrace(pw);
//        pw.flush();
//        return sw.toString();
//    }

    public static int println(int priority, String tag, String msg) {
        return println_native(LOG_ID_MAIN, priority, tag, msg);
    }

    /** @hide */
    public static final int LOG_ID_MAIN   = 0;
    /** @hide */
    public static final int LOG_ID_RADIO  = 1;
    /** @hide */
    public static final int LOG_ID_EVENTS = 2;
    /** @hide */
    public static final int LOG_ID_SYSTEM = 3;
    /** @hide */
    public static final int LOG_ID_CRASH  = 4;

    /** @hide */
    public static int println_native(int bufID,
                                     int priority, String tag, String msg) {
        return 0;
    }

    /**
     * Return the maximum payload the log daemon accepts without truncation.
     *
     * @return LOGGER_ENTRY_MAX_PAYLOAD.
     */
    private static int logger_entry_max_payload_native() {
        return 0;
    }

    /**
     * Helper function for long messages. Uses the LineBreakBufferedWriter to break
     * up long messages and stacktraces along newlines, but tries to write in large
     * chunks. This is to avoid truncation.
     *
     * @hide
     */
    public static int printlns(int bufID, int priority, String tag, String msg,
                               Throwable tr) {
        ImmediateLogWriter logWriter = new ImmediateLogWriter(bufID, priority, tag);
        // Acceptable buffer size. Get the native buffer size, subtract two zero terminators,
        // and the length of the tag.
        // Note: we implicitly accept possible truncation for Modified-UTF8 differences. It
        //       is too expensive to compute that ahead of time.
        int bufferSize = NoPreloadHolder.LOGGER_ENTRY_MAX_PAYLOAD  // Base.
                - 2                                                // Two terminators.
                - (tag != null ? tag.length() : 0)                 // Tag length.
                - 32;                                              // Some slack.
        // At least assume you can print *some* characters (tag is not too large).
        bufferSize = Math.max(bufferSize, 100);

//        LineBreakBufferedWriter lbbw = new LineBreakBufferedWriter(logWriter, bufferSize);
//
//        lbbw.println(msg);
        System.out.println(msg);

        if (tr != null) {
            // This is to reduce the amount of log spew that apps do in the non-error
            // condition of the network being unavailable.
            Throwable t = tr;
            while (t != null) {
                if (t instanceof UnknownHostException) {
                    break;
                }
                if (t instanceof DeadSystemException) {
//                    lbbw.println("DeadSystemException: The system died; "
//                            + "earlier logs will point to the root cause");
                    System.out.println("DeadSystemException: The system died; "
                            + "earlier logs will point to the root cause");
                    break;
                }
                t = t.getCause();
            }
            if (t == null) {
//                tr.printStackTrace(lbbw);
                tr.printStackTrace();
            }
        }

//        lbbw.flush();

        return logWriter.getWritten();
    }

    /**
     * NoPreloadHelper class. Caches the LOGGER_ENTRY_MAX_PAYLOAD value to avoid
     * a JNI call during logging.
     */
    static class NoPreloadHolder {
        public final static int LOGGER_ENTRY_MAX_PAYLOAD =
                logger_entry_max_payload_native();
    }

    /**
     * Helper class to write to the logcat. Different from LogWriter, this writes
     * the whole given buffer and does not break along newlines.
     */
    private static class ImmediateLogWriter extends Writer {

        private int    bufID;
        private int    priority;
        private String tag;

        private int written = 0;

        /**
         * Create a writer that immediately writes to the log, using the given
         * parameters.
         */
        public ImmediateLogWriter(int bufID, int priority, String tag) {
            this.bufID = bufID;
            this.priority = priority;
            this.tag = tag;
        }

        public int getWritten() {
            return written;
        }

        @Override
        public void write(char[] cbuf, int off, int len) {
            // Note: using String here has a bit of overhead as a Java object is created,
            //       but using the char[] directly is not easier, as it needs to be translated
            //       to a C char[] for logging.
            written += println_native(bufID, priority, tag, new String(cbuf, off, len));
        }

        @Override
        public void flush() {
            // Ignored.
        }

        @Override
        public void close() {
            // Ignored.
        }
    }
}
