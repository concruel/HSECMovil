package com.pango.hsec.hsec.Noticias;

import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.Adap_Img;
import com.pango.hsec.hsec.adapter.ComentRecAdapter;
import com.pango.hsec.hsec.adapter.DocumentoAdapter;
import com.pango.hsec.hsec.adapter.GaleriaAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetComentModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.NoticiasModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentDetalle extends Fragment implements IActivity {

    private static View mView;
    String codNot="";
    String jsonNoticia="";
    String url="";
    TextView not_titulo,txfecha;
    WebView notDetalle;

    //galeria
    int contador=0;
    ArrayList<GaleriaModel> DataDocs=new ArrayList<GaleriaModel>();
    ArrayList<GaleriaModel> DataImg=new ArrayList<GaleriaModel>();
    GetGaleriaModel getImg;
    //private static View mView;
    String codObs="";
    String jsonGaleria="";
    String url2="";
    Adap_Img adaptador;
    DownloadManager downloadManager;
    private static final short REQUEST_CODE = 6545;

    boolean permiso=false;
    DocumentoAdapter documentoAdapter;
    GaleriaAdapter galeriaAdapter;

    TextView txGaleria,mensaje;
    //GridView grid_gal;
    ConstraintLayout cl_otros;
    FrameLayout frame_otros;
    //ListView list_docs;
    RelativeLayout rel_otros;
    LinearLayout galeria_foto;
    RecyclerView gridView,listView;
    //comentarios

    //private static View mView;
    //String codObs;
    String url3, url4;
    //String UrlObs;
    String jsonComentario="";
    ImageButton btn_send;
    EditText et_comentario;
    ComentRecAdapter comentAdapter;


    // TODO: Rename and change types and number of parameters
    public static FragmentDetalle newInstance(String sampleText) {

        FragmentDetalle f = new FragmentDetalle();

        Bundle b = new Bundle();
        b.putString("bString", sampleText);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_detalle, container, false);
        codNot=getArguments().getString("bString");

        GlobalVariables.view_fragment=mView;
        GlobalVariables.isFragment=true;

        ///galeria
        gridView = (RecyclerView) mView.findViewById(R.id.rec_galeria);
        listView = (RecyclerView) mView.findViewById(R.id.list_docs);

        txGaleria=(TextView) mView.findViewById(R.id.tx_gal);
        //grid_gal=(GridView) mView.findViewById(R.id.grid_gal);
        cl_otros=(ConstraintLayout) mView.findViewById(R.id.cl_otros);
        frame_otros=(FrameLayout) mView.findViewById(R.id.frame_otros);
        //list_docs=(ListView) mView.findViewById(R.id.list_docs);
        rel_otros=(RelativeLayout) mView.findViewById(R.id.rel_otros);
        galeria_foto=(LinearLayout) mView.findViewById(R.id.galeria_foto);
        mensaje=(TextView) mView.findViewById(R.id.mensaje);

        btn_send=(ImageButton) mView.findViewById(R.id.btn_send);
        et_comentario=(EditText) mView.findViewById(R.id.et_comentario);
        //GlobalVariables.count=1;
        GlobalVariables.view_fragment=mView;
        GlobalVariables.isFragment=true;

        ////------

        url= GlobalVariables.Url_base+"Noticia/Get/"+codNot;

        if(jsonNoticia.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url, FragmentDetalle.this,getActivity());
            obj.execute("1");
        }else {
            success(jsonNoticia,"1");
        }


//galeria
        url2= GlobalVariables.Url_base+"media/GetMultimedia/"+codNot;

        //https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/media/GetMultimedia/OBS00240578
        if(jsonGaleria.isEmpty()) {
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url2, FragmentDetalle.this,getActivity());
            obj.execute("2");

        }else{
            success(jsonGaleria,"2");
        }


        txGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gridView.getVisibility()==View.GONE){
                    gridView.setVisibility(View.VISIBLE);
                }else{
                    gridView.setVisibility(View.GONE);
                }
                //grid_gal.setVisibility(View.GONE);
            }
        });

        cl_otros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getVisibility()==View.GONE){
                    listView.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.GONE);
                }
            }
        });


/////comentarios

        url3= GlobalVariables.Url_base+"Comentario/getObs/"+codNot;


        if(jsonComentario.isEmpty()){
            GlobalVariables.istabs=true;
            final ActivityController obj = new ActivityController("get", url3, FragmentDetalle.this,getActivity());
            obj.execute("3");
        }else {
            success(jsonComentario,"3");
        }


        btn_send.setEnabled(false);
        et_comentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String a=s.toString().trim();
                if(a.equals("")) {
                    btn_send.setEnabled(false);
                }else{
                    btn_send.setEnabled(true);
                }
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comentario= String.valueOf(et_comentario.getText());

                String json = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.accumulate("CodComentario",codNot);
                    jsonObject.accumulate("Comentario",comentario);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                json += jsonObject.toString();

                url3= GlobalVariables.Url_base+"Comentario/insert";
                final ActivityController obj = new ActivityController("post", url, FragmentDetalle.this,getActivity());
                obj.execute(json);



            }
        });


        return mView;
    }

    private static boolean isDownloadManagerAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }


    public void checkSelfPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);

        } else {

            executeDownload();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    executeDownload();
                } else {
                    // permission denied!
                    Toast.makeText(getActivity(), "Please give permissions ", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void executeDownload() {


        permiso=true;

    }




    @Override
    public void success(String data, String Tipo) {

        if(Tipo=="1") {
            DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
            not_titulo = (TextView) mView.findViewById(R.id.not_titulo);
            txfecha = (TextView) mView.findViewById(R.id.txfecha);
            notDetalle = (WebView) mView.findViewById(R.id.Visor);
            jsonNoticia = data;
            Gson gson = new Gson();
            NoticiasModel getNoticiaModel = gson.fromJson(data, NoticiasModel.class);

            Date temp = null;
            try {
                temp = formatoInicial.parse(getNoticiaModel.Fecha);
            } catch (Exception e) {
                e.printStackTrace();
            }

            not_titulo.setText(getNoticiaModel.Titulo);
            txfecha.setText(formatoRender.format(temp));

            notDetalle.setWebViewClient(new MyWebViewClient());

            notDetalle.loadDataWithBaseURL("", getNoticiaModel.Descripcion, "text/html", "UTF-8", "");
            WebSettings settings = notDetalle.getSettings();
            settings.setJavaScriptEnabled(true);
        }else if(Tipo=="2"){
            jsonGaleria = data;
            DataDocs.clear();
            DataImg.clear();
            //int resultado=data.indexOf("TP03");
            Gson gson = new Gson();
            GetGaleriaModel getGaleriaModel = gson.fromJson(data, GetGaleriaModel.class);
            int count=getGaleriaModel.Count;
            //GlobalVariables.listaGaleria=getGaleriaModel.Data;
            if(count!=0){
                if(data.contains("TP01") ||data.contains("TP02")){
                    galeria_foto.setVisibility(View.VISIBLE);
                }else {
                    galeria_foto.setVisibility(View.GONE);
                }

                if(data.contains("TP03")){
                    rel_otros.setVisibility(View.VISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        checkSelfPermission();
                        //executeDownload();

                    } else {
                        Toast.makeText(getActivity(), "Download manager is not available", Toast.LENGTH_LONG).show();
                    }

                }else{
                    rel_otros.setVisibility(View.GONE);
                }

                for(int i=0;i<getGaleriaModel.Data.size();i++){
                    if(getGaleriaModel.Data.get(i).TipoArchivo.equals("TP03")){
                        rel_otros.setVisibility(View.VISIBLE);
                        DataDocs.add(getGaleriaModel.Data.get(i));
                    }else{
                        DataImg.add(getGaleriaModel.Data.get(i));
                    }
                }


                GlobalVariables.listaGaleria=DataImg;

                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                gridView.setLayoutManager(layoutManager);
                galeriaAdapter = new GaleriaAdapter(getActivity(),DataImg );
                gridView.setAdapter(galeriaAdapter);


                LinearLayoutManager horizontalManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                listView.setLayoutManager(horizontalManager);
                documentoAdapter = new DocumentoAdapter(getActivity(), DataDocs,permiso);
                listView.setAdapter(documentoAdapter);



            }else{
                mensaje.setVisibility(View.GONE);
                rel_otros.setVisibility(View.GONE);
                galeria_foto.setVisibility(View.GONE);
            }
        }else if(Tipo=="3"){
            jsonComentario =data;
            Gson gson = new Gson();
            GetComentModel getComentModel= gson.fromJson(data, GetComentModel.class);
            comentAdapter=new ComentRecAdapter(getComentModel.Data, getActivity());

            //ListView listaComentarios = (ListView) mView.findViewById(R.id.list_coment);

            final RecyclerView listaComentarios = (RecyclerView) mView.findViewById(R.id.list_coment);
            listaComentarios.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            listaComentarios.setLayoutManager(llm);

            listaComentarios.setAdapter(comentAdapter);

            //listaComentarios.setSelection(getComentModel.Data.size()-1);






        }





    }

    @Override
    public void successpost(String data, String Tipo) {

        Utils.closeSoftKeyBoard(getActivity());
        et_comentario.setText("");
        if(data.contains("-1")){
            Toast.makeText(getContext(),"Ocurrio un error al enviar su mensaje",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Comentario enviado",Toast.LENGTH_SHORT).show();
            GlobalVariables.count=5;
            GlobalVariables.isFragment=true;
            url= GlobalVariables.Url_base+"Comentario/getObs/"+codObs;
            final ActivityController obj = new ActivityController("get-2", url, FragmentDetalle.this,getActivity());
            obj.execute("");
        }





    }

    @Override
    public void error(String mensaje, String Tipo) {

    }



    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;

        }
    }

    public void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
