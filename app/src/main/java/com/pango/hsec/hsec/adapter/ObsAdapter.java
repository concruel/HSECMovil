package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.model.ObservacionModel;

/**
 * Created by Andre on 28/12/2017.
 */

public class ObsAdapter extends BaseAdapter {
    private Context context;
    ObservacionModel observacionModel;
    String [] obsDetcab;
    String []obsDetIzq;
    public ObsAdapter(Context context, ObservacionModel observacionModel,String [] obsDetcab,String []obsDetIzq) {
        this.context = context;
        this.observacionModel=observacionModel;
        this.obsDetcab=obsDetcab;
        this.obsDetIzq=obsDetIzq;

    }


    @Override
    public int getCount() {
        return obsDetcab.length;
    }

    @Override
    public ObservacionModel getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.obslist, parent, false);

        TextView ladoIzquierdo=convertView.findViewById(R.id.txcab);
        String a=obsDetIzq[position];
        ladoIzquierdo.setText(obsDetIzq[position]);
        TextView ladoDerecho=convertView.findViewById(R.id.txdet);
        String b=Utils.getTicketProperty(observacionModel,obsDetcab[position]);

        ladoDerecho.setText(Utils.getTicketProperty(observacionModel,obsDetcab[position]));


        return convertView;
    }
}
