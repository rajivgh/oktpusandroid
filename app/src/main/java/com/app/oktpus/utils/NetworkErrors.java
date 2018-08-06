package com.app.oktpus.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.app.oktpus.R;

import java.util.IdentityHashMap;

/**
 * Created by Gyandeep on 29/8/17.
 */

public enum NetworkErrors {

    AuthFailureError(AuthFailureError.class),
    ServerError(ServerError.class),
    NetworkError(NetworkError.class),
    NoConnectionError(NoConnectionError.class),
    ParseError(ParseError.class),
    TimeOutError(TimeoutError.class),
    UNKNOWN(null);

    private static class Holder {
        public static final IdentityHashMap<Class<?>, NetworkErrors> map = new IdentityHashMap<>();
    }
    NetworkErrors(Class<?> targetClass) {
        Holder.map.put(targetClass, this);
    }
    public static NetworkErrors fromClass(Class<?> cls) {
        NetworkErrors c = Holder.map.get(cls);
        return c != null ? c : UNKNOWN;
    }


    public static String getErrorMessage(Context context, VolleyError error) {
        NetworkErrors errorType = NetworkError.fromClass(error.getClass());
        String msg = "";
        switch(errorType) {
            case AuthFailureError:
                msg = context.getResources().getString(R.string.auth_failure_error);
                break;
            case ServerError:
                msg = context.getResources().getString(R.string.server_error);
                break;
            case NetworkError:
                msg = context.getResources().getString(R.string.network_error);
                break;
            case TimeOutError:
                msg = context.getResources().getString(R.string.timeout_error);
                break;
            case NoConnectionError:
                msg = context.getResources().getString(R.string.no_connection_error);
                break;
            case ParseError:
                msg = context.getResources().getString(R.string.parse_error);
                break;
            case UNKNOWN:
                msg = context.getResources().getString(R.string.unknown);
                break;
        }
        return msg;
    }




    /*public void getErrorMessage(VolleyError error) {
        String className = error.getClass().getSimpleName();
        try {
            //this line searches a method named as className
            Method m = this.getClass().getDeclaredMethod(className);
            //this line execute the method
            m.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String AuthFailureError() {
        return "AuthFailureError";
    }*/



    /*public interface ErrorHandling{
        String getErrorMessage(VolleyError error);
    }

    class Error1 extends AuthFailureError implements ErrorHandling {
        @Override
        public String getErrorMessage(VolleyError error) {
            return "AuthFailure";
        }
    }

    class Error2 extends NetworkError implements ErrorHandling {
        @Override
        public String getErrorMessage(VolleyError error) {
            return null;
        }
    }

    class Error3 extends NoConnectionError implements ErrorHandling {
        @Override
        public String getErrorMessage(VolleyError error) {
            return null;
        }
    }

    class Error4 extends ParseError implements ErrorHandling {
        @Override
        public String getErrorMessage(VolleyError error) {
            return null;
        }
    }

    class Error5 extends ServerError implements ErrorHandling {
        @Override
        public String getErrorMessage(VolleyError error) {
            return "ServerError";
        }
    }

    class Error6 extends TimeoutError implements ErrorHandling {
        @Override
        public String getErrorMessage(VolleyError error) {
            return "SocketTimeout";
        }
    }*/


}
