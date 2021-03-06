package com.example.merguez.easytrip.affichage;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.RelativeLayout;

import com.example.merguez.easytrip.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by merguez on 15/09/2016.
 */
public class Calendrier extends DialogFragment implements android.content.DialogInterface.OnClickListener {

    private static String date;
    private View calendrierVue;
    private CalendarView calendrier;
    private Calendar monCalendrier;
    private static String dateDepart = "";
    private static String dateArrivee = "";
    private static Date dateMinMax;
    private static DecimalFormat format;
    private static SimpleDateFormat simpleDateFormat;
    private static boolean estChange = false;


    public static Calendrier newInstance(int title){
        Calendrier fragment = new Calendrier();
        Bundle args = new Bundle();
        args.putInt("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        calendrierVue = inflater.inflate(R.layout.accueil_calendrier, null);
        builder.setView(calendrierVue);
        calendrier = (CalendarView) calendrierVue.findViewById(R.id.accueilCVcalendrier);
        format = new DecimalFormat("00");
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
        calendrier.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = format.format(dayOfMonth) + "/" + format.format(month+1) + "/" + year;
                estChange = true;
            }
        });

        calendrier.setMinDate(calendrier.getDate());

        if (Accueil.getEstDepart() && dateArrivee.length() > 0){
            try {
                dateMinMax = simpleDateFormat.parse(dateArrivee);
                long dateMax = dateMinMax.getTime();
                calendrier.setMaxDate(dateMax);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!Accueil.getEstDepart() && dateDepart.length() > 0){
            try {
                dateMinMax = simpleDateFormat.parse(dateDepart);
                long dateMin = dateMinMax.getTime();
                calendrier.setMinDate(dateMin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        AlertDialog dialog = builder.setPositiveButton("OK", this)
                .setNegativeButton("Annuler", this).create();
        dialog.show();
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        return dialog;

    }

    public static String getDate() {
        return date;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (Accueil.getEstDepart() && estChange){
                    Accueil.setAccueilETdateDepart(date);
                    dateDepart = date;
                    estChange = false;
                } else if (!Accueil.getEstDepart() && estChange) {
                    Accueil.setAccueilETdateArrivee(date);
                    dateArrivee = date;
                    estChange = false;
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }

    }


}
