package com.aymanshehri.whenimthere.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;

import com.aymanshehri.whenimthere.Reconnect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class CheckConnectionAsync extends AsyncTask {

    private Context context;
    private RelativeLayout relativeLayout;

    public CheckConnectionAsync(Context context, RelativeLayout relativeLayout) {
        this.context = context;
        this.relativeLayout = relativeLayout;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        boolean isonline = isOnline();
        return isonline;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if ((boolean) o) {
            relativeLayout.setVisibility(View.GONE);
        }else{
            ((Activity)context).finish();
            Intent intent = new Intent(context, Reconnect.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    private boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }
}
