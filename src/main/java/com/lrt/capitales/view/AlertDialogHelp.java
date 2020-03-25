package com.lrt.capitales.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;

public class AlertDialogHelp extends AlertDialog {

    public AlertDialogHelp(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_some_view, null);
        //setView(content);
        setTitle("Aide");
        setMessage(Html.fromHtml("Vous navez pas besoin d'aide pour ce jeu<br><br>"+
                "<b>Capitales:</b> Trouver la capitale (3 vies)<br><br>"+
                "<b>Position:</b> Placer 10 capitales sur la carte<br><br>"+
                "Créé par Laurent Rouault<br><br>"+
                "Contact: <br> lrouault40@gmail.com"));

        super.onCreate(savedInstanceState);
    }
}
