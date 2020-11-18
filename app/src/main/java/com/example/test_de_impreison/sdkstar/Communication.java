package com.example.test_de_impreison.sdkstar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.util.Pair;

import com.example.test_de_impreison.MainActivity;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.stario.StarResultCode;
import com.starmicronics.starioextension.IPeripheralCommandParser;
import com.starmicronics.starioextension.StarIoExtManager;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnusedParameters", "UnusedAssignment", "WeakerAccess"})
public class Communication {





    public static class CommunicationResult {
        private Result mResult = Result.ErrorUnknown;
        private int    mCode   = StarResultCode.FAILURE;

        public CommunicationResult(Result result, int code) {
            mResult = result;
            mCode   = code;
        }

        public Result getResult() {
            return mResult;
        }

        public int getCode() {
            return mCode;
        }
    }

    public enum Result {
        Success,
        ErrorUnknown,
        ErrorOpenPort,
        ErrorBeginCheckedBlock,
        ErrorEndCheckedBlock,
        ErrorWritePort,
        ErrorReadPort,
    }


    public interface StatusCallback {
        void onStatus(StarPrinterStatus result);
    }


    public interface SendCallback {
        void onStatus(CommunicationResult communicationResult);
    }



    public static void sendCommands(Object lock, byte[] commands, String portName, String portSettings, int timeout, int endCheckedBlockTimeout, Context context, SendCallback callback) {
        SendCommandThread thread = new SendCommandThread(lock, commands, portName, portSettings, timeout, endCheckedBlockTimeout, context, callback);
        thread.start();
    }

    public static void retrieveStatus(Object lock, String portName, String portSettings, int timeout, Context context, StatusCallback callback) {
        RetrieveStatusThread thread = new RetrieveStatusThread(lock, portName, portSettings, timeout, context, callback);
        thread.start();
    }



    public static String getCommunicationResultMessage(CommunicationResult communicationResult) {
        StringBuilder builder = new StringBuilder();

        switch (communicationResult.getResult()) {
            case Success:
                builder.append("Success!");
                break;
            case ErrorOpenPort:
                builder.append("Fail to openPort");
                break;
            case ErrorBeginCheckedBlock:
                builder.append("Printer is offline (beginCheckedBlock)");
                break;
            case ErrorEndCheckedBlock:
                builder.append("Printer is offline (endCheckedBlock)");
                break;
            case ErrorReadPort:
                builder.append("Read port error (readPort)");
                break;
            case ErrorWritePort:
                builder.append("Write port error (writePort)");
                break;
            default:
                builder.append("Unknown error");
                break;
        }

        if (communicationResult.getResult() != Result.Success) {
            builder.append("\n\nError code: ");
            builder.append(communicationResult.getCode());

            if (communicationResult.getCode() == StarResultCode.FAILURE) {
                builder.append(" (Failed)");
            }
            else if (communicationResult.getCode() == StarResultCode.FAILURE_IN_USE) {
                builder.append(" (In use)");
            }
            else if (communicationResult.getCode() == StarResultCode.FAILURE_PAPER_PRESENT) {
                builder.append(" (Paper present)");
            }
        }

        return builder.toString();
    }


    static class SendCommandThread extends Thread {
        private final Object mLock;
        private Communication.SendCallback mCallback;
        private byte[] mCommands;

        private StarIOPort mPort;

        private String  mPortName = null;
        private String  mPortSettings;
        private int     mTimeout;
        private int     mEndCheckedBlockTimeout;
        private Context mContext;

        SendCommandThread(Object lock, byte[] commands, String portName, String portSettings, int timeout, int endCheckedBlockTimeout, Context context, Communication.SendCallback callback) {
            mCommands               = commands;
            mPortName               = portName;
            mPortSettings           = portSettings;
            mTimeout                = timeout;
            mEndCheckedBlockTimeout = endCheckedBlockTimeout;
            mContext                = context;
            mCallback               = callback;
            mLock                   = lock;
        }

        @Override
        public void run() {
            Communication.Result result = Communication.Result.ErrorOpenPort;
            int code = StarResultCode.FAILURE;

            synchronized (mLock) {
                try {
                    if (mPort == null) {

                        if (mPortName == null) {
                            resultSendCallback(result, code, mCallback);
                            return;
                        } else {
                            mPort = StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext);
                        }
                    }
                    if (mPort == null) {
                        result = Communication.Result.ErrorOpenPort;
                        resultSendCallback(result, code, mCallback);
                        return;
                    }

                    StarPrinterStatus status;

                    result = Communication.Result.ErrorBeginCheckedBlock;

                    status = mPort.beginCheckedBlock();

                    if (status.offline) {
                        throw new StarIOPortException("A printer is offline.");
                    }

                    result = Communication.Result.ErrorWritePort;

                    mPort.writePort(mCommands, 0, mCommands.length);

                    result = Communication.Result.ErrorEndCheckedBlock;

                    mPort.setEndCheckedBlockTimeoutMillis(mEndCheckedBlockTimeout);

                    status = mPort.endCheckedBlock();

                    if (status.coverOpen) {
                        throw new StarIOPortException("Printer cover is open");
                    } else if (status.receiptPaperEmpty) {
                        throw new StarIOPortException("Receipt paper is empty");
                    } else if (status.offline) {
                        throw new StarIOPortException("Printer is offline");
                    }

                    result = Communication.Result.Success;
                    code = StarResultCode.SUCCESS;
                } catch (StarIOPortException e) {
                    code = e.getErrorCode();
                }

                if (mPort != null && mPortName != null) {
                    try {
                        StarIOPort.releasePort(mPort);
                    } catch (StarIOPortException e) {
                        // Nothing
                    }
                    mPort = null;
                }

                resultSendCallback(result, code, mCallback);
            }
        }

        private void resultSendCallback(final Communication.Result result, final int code, final Communication.SendCallback callback) {
            if (callback != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onStatus(new Communication.CommunicationResult(result, code));
                    }
                });
            }
        }
    }


    static class RetrieveStatusThread extends Thread {
        private final Object mLock;
        private Communication.StatusCallback mCallback;

        private StarIOPort mPort;

        private String  mPortName = null;
        private String  mPortSettings;
        private int     mTimeout;
        private Context mContext;

        @SuppressWarnings("unused")
        RetrieveStatusThread(Object lock, StarIOPort port, Communication.StatusCallback callback) {
            mPort     = port;
            mCallback = callback;
            mLock     = lock;
        }

        RetrieveStatusThread(Object lock, String portName, String portSettings, int timeout, Context context, Communication.StatusCallback callback) {
            mPortName     = portName;
            mPortSettings = portSettings;
            mTimeout      = timeout;
            mContext      = context;
            mCallback     = callback;
            mLock         = lock;
        }

        @Override
        public void run() {

            synchronized (mLock) {
                StarPrinterStatus status = null;

                try {
                    if (mPort == null) {

                        if (mPortName == null) {
                            resultSendCallback(null, mCallback);
                            return;
                        } else {
                            mPort = StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext);
                        }
                    }
                    if (mPort == null) {
                        resultSendCallback(null, mCallback);
                        return;
                    }

                    status = mPort.retreiveStatus();

                } catch (StarIOPortException e) {
                    // Nothing
                }

                if (mPort != null && mPortName != null) {
                    try {
                        StarIOPort.releasePort(mPort);
                    } catch (StarIOPortException e) {
                        // Nothing
                    }
                    mPort = null;
                }

                resultSendCallback(status, mCallback);
            }
        }

        private static void resultSendCallback(final StarPrinterStatus status, final Communication.StatusCallback callback) {
            if (callback != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onStatus(status);
                    }
                });
            }
        }
    }





}



