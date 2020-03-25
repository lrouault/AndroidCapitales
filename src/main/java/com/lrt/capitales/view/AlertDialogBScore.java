package com.lrt.capitales.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lrt.capitales.controller.MainActivity;
import com.lrt.capitales.R;

import static android.content.Context.MODE_PRIVATE;

public class AlertDialogBScore extends AlertDialog {
    private Context mContext;
    private int mCapPos;

    private TextView tv11,tv12,tv13;
    private TextView tv21,tv22,tv23;
    private TextView tv31,tv32,tv33;
    private TextView tv41,tv42,tv43;
    private TextView tv51,tv52,tv53;

    private SharedPreferences mMeilleurScoreSVG;


    public AlertDialogBScore(Context context, int capPos) {
        super(context);
        mContext = context;
        mCapPos = capPos;
        //mMeilleurScoreSVG = context.getSharedPreferences(mContext.getApplicationContext(),MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View content = LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_bscore, null);
        setView(content);


        //mMeilleurScoreSVG = PreferenceManager.getDefaultSharedPreferences(mContext);
        mMeilleurScoreSVG = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        if (mCapPos == 0) {
            setTitle("Best Score Capitales");
        } else {
            setTitle("Best Score Position");
        }


        String ligne1 = "AfAmAsEu";
        String ligne2 = "Af";
        String ligne3 = "Am";
        String ligne4 = "As";
        String ligne5 = "Eu";
        String prefixe = "";
        String on11="/100", on12="/100", on13="/100";
        String on21="/100", on22="/100", on23="/100";
        String on31="/100", on32="/100", on33="/100";
        String on41="/100", on42="/100", on43="/100";
        String on51="/100", on52="/100", on53="/100";

        if (mCapPos == 1) {
            prefixe = "MAP";
        }else{
            on11="/80";
            on12="/159";
            on13="/237";
            on21="/20";
            on22="/39";
            on23="/58";
            on31="/17";
            on32="/34";
            on33="/51";
            on41="/24";
            on42="/48";
            on43="/71";
            on51="/19";
            on52="/38";
            on53="/57";
        }


        tv11 = (TextView) content.findViewById(R.id.dialogBScore11);
        String on11_1=mMeilleurScoreSVG.getInt(prefixe+"1"+ligne1,0)+on11;
        SpannableStringBuilder on11_2 = new SpannableStringBuilder(on11_1);
        on11_2.setSpan(new RelativeSizeSpan(0.5f), on11_1.indexOf("/"), on11_1.indexOf("/")+on11.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv11.setText(on11_2);
        tv12 = (TextView) content.findViewById(R.id.dialogBScore12);
        String on12_1=mMeilleurScoreSVG.getInt(prefixe+"2"+ligne1,0)+on12;
        SpannableStringBuilder on12_2 = new SpannableStringBuilder(on12_1);
        on12_2.setSpan(new RelativeSizeSpan(0.5f), on12_1.indexOf("/"), on12_1.indexOf("/")+on12.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv12.setText(on12_2);
        tv13 = (TextView) content.findViewById(R.id.dialogBScore13);
        String on13_1=mMeilleurScoreSVG.getInt(prefixe+"3"+ligne1,0)+on13;
        SpannableStringBuilder on13_2 = new SpannableStringBuilder(on13_1);
        on13_2.setSpan(new RelativeSizeSpan(0.5f), on13_1.indexOf("/"), on13_1.indexOf("/")+on13.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv13.setText(on13_2);

        tv21 = (TextView) content.findViewById(R.id.dialogBScore21);
        String on21_1=mMeilleurScoreSVG.getInt(prefixe+"1"+ligne2,0)+on21;
        SpannableStringBuilder on21_2 = new SpannableStringBuilder(on21_1);
        on21_2.setSpan(new RelativeSizeSpan(0.5f), on21_1.indexOf("/"), on21_1.indexOf("/")+on21.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv21.setText(on21_2);
        tv22 = (TextView) content.findViewById(R.id.dialogBScore22);
        String on22_1=mMeilleurScoreSVG.getInt(prefixe+"2"+ligne2,0)+on22;
        SpannableStringBuilder on22_2 = new SpannableStringBuilder(on22_1);
        on22_2.setSpan(new RelativeSizeSpan(0.5f), on22_1.indexOf("/"), on22_1.indexOf("/")+on22.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv22.setText(on22_2);
        tv23 = (TextView) content.findViewById(R.id.dialogBScore23);
        String on23_1=mMeilleurScoreSVG.getInt(prefixe+"3"+ligne3,0)+on23;
        SpannableStringBuilder on23_2 = new SpannableStringBuilder(on23_1);
        on23_2.setSpan(new RelativeSizeSpan(0.5f), on23_1.indexOf("/"), on23_1.indexOf("/")+on23.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv23.setText(on23_2);

        tv31 = (TextView) content.findViewById(R.id.dialogBScore31);
        String on31_1=mMeilleurScoreSVG.getInt(prefixe+"1"+ligne3,0)+on31;
        SpannableStringBuilder on31_2 = new SpannableStringBuilder(on31_1);
        on31_2.setSpan(new RelativeSizeSpan(0.5f), on31_1.indexOf("/"), on31_1.indexOf("/")+on31.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv31.setText(on31_2);
        tv32 = (TextView) content.findViewById(R.id.dialogBScore32);
        String on32_1=mMeilleurScoreSVG.getInt(prefixe+"2"+ligne3,0)+on32;
        SpannableStringBuilder on32_2 = new SpannableStringBuilder(on32_1);
        on32_2.setSpan(new RelativeSizeSpan(0.5f), on32_1.indexOf("/"), on32_1.indexOf("/")+on32.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv32.setText(on32_2);
        tv33 = (TextView) content.findViewById(R.id.dialogBScore33);
        String on33_1=mMeilleurScoreSVG.getInt(prefixe+"3"+ligne3,0)+on33;
        SpannableStringBuilder on33_2 = new SpannableStringBuilder(on33_1);
        on33_2.setSpan(new RelativeSizeSpan(0.5f), on33_1.indexOf("/"), on33_1.indexOf("/")+on33.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv33.setText(on33_2);

        tv41 = (TextView) content.findViewById(R.id.dialogBScore41);
        String on41_1=mMeilleurScoreSVG.getInt(prefixe+"1"+ligne4,0)+on41;
        SpannableStringBuilder on41_2 = new SpannableStringBuilder(on41_1);
        on41_2.setSpan(new RelativeSizeSpan(0.5f), on41_1.indexOf("/"), on41_1.indexOf("/")+on41.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv41.setText(on41_2);
        tv42 = (TextView) content.findViewById(R.id.dialogBScore42);
        String on42_1=mMeilleurScoreSVG.getInt(prefixe+"2"+ligne4,0)+on42;
        SpannableStringBuilder on42_2 = new SpannableStringBuilder(on42_1);
        on42_2.setSpan(new RelativeSizeSpan(0.5f), on42_1.indexOf("/"), on42_1.indexOf("/")+on42.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv42.setText(on42_2);
        tv43 = (TextView) content.findViewById(R.id.dialogBScore43);
        String on43_1=mMeilleurScoreSVG.getInt(prefixe+"3"+ligne4,0)+on43;
        SpannableStringBuilder on43_2 = new SpannableStringBuilder(on43_1);
        on43_2.setSpan(new RelativeSizeSpan(0.5f), on43_1.indexOf("/"), on43_1.indexOf("/")+on43.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv43.setText(on43_2);

        tv51 = (TextView) content.findViewById(R.id.dialogBScore51);
        String on51_1=mMeilleurScoreSVG.getInt(prefixe+"1"+ligne5,0)+on51;
        SpannableStringBuilder on51_2 = new SpannableStringBuilder(on51_1);
        on51_2.setSpan(new RelativeSizeSpan(0.5f), on51_1.indexOf("/"), on51_1.indexOf("/")+on51.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv51.setText(on51_2);
        tv52 = (TextView) content.findViewById(R.id.dialogBScore52);
        String on52_1=mMeilleurScoreSVG.getInt(prefixe+"2"+ligne5,0)+on52;
        SpannableStringBuilder on52_2 = new SpannableStringBuilder(on52_1);
        on52_2.setSpan(new RelativeSizeSpan(0.5f), on52_1.indexOf("/"), on52_1.indexOf("/")+on52.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv52.setText(on52_2);
        tv53 = (TextView) content.findViewById(R.id.dialogBScore53);
        String on53_1=mMeilleurScoreSVG.getInt(prefixe+"3"+ligne5,0)+on53;
        SpannableStringBuilder on53_2 = new SpannableStringBuilder(on53_1);
        on53_2.setSpan(new RelativeSizeSpan(0.5f), on53_1.indexOf("/"), on53_1.indexOf("/")+on53.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv53.setText(on53_2);

        super.onCreate(savedInstanceState);
    }
}
