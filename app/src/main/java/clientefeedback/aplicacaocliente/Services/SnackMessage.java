package clientefeedback.aplicacaocliente.Services;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

import clientefeedback.aplicacaocliente.R;

/**
 * Created by Alexandre on 24/05/2016.
 */
public class SnackMessage {
    private SnackMessageInterface snackMessageInterface;
    private Object object;

    public SnackMessage(SnackMessageInterface s){
        this.snackMessageInterface = s;
    }

    public void snackShowError(View view){
        Snackbar snackbar = Snackbar
                .make(view, R.string.error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.btnretry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackMessageInterface.executeAfterMessage();
                    }
                });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void snackShowErrorWifi(View view){
        Snackbar snackbar = Snackbar
                .make(view, R.string.connection_swipe, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.connect, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackMessageInterface.executeAfterMessage();
                    }
                });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

}
