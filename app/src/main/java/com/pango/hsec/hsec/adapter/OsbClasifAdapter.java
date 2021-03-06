package com.pango.hsec.hsec.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.SubDetalleModel;

import java.util.List;

public class OsbClasifAdapter extends RecyclerView.Adapter<OsbClasifAdapter.ViewHolder> {



    private Activity activity;
    public static List<SubDetalleModel> items;

    public OsbClasifAdapter(Activity activity, List<SubDetalleModel> items) {
        this.activity = activity;
        this.items = items;

    }


    public void add(SubDetalleModel newdata) {
        items.add(newdata);
    }

    @Override
    public OsbClasifAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_obs_ischeck , parent, false);

        return new OsbClasifAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SubDetalleModel em = items.get(position);
    //    if(em.CodTipo.equals("OBSR")){
            viewHolder.det_gestion.setText(GlobalVariables.getDescripcion(GlobalVariables.Clasificacion_Obs, em.Descripcion));
            viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             items.get(position).Check = false;
                                                             //ListMetodologia.get(position).estado= false;
                                                             items.remove(position);
                                                             notifyItemRemoved(position);
                                                             notifyItemRangeChanged(position,items.size());
                                                             //notifyDataSetChanged();

                                                         }
                                                     }
            );


//        }


    }

    @Override
    public int getItemCount() {
        return items.size();
        //return 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView det_gestion;
        private ImageButton btn_Delete;

        public ViewHolder(View view) {
            super(view);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
            det_gestion = (TextView) view.findViewById(R.id.txt_gestion);
        }

    }





}
