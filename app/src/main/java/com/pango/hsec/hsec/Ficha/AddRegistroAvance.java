package com.pango.hsec.hsec.Ficha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.GamesMetadata;
import com.google.gson.Gson;
import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.IActivity;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.Utils;
import com.pango.hsec.hsec.adapter.GridViewAdapter;
import com.pango.hsec.hsec.adapter.ListViewAdapter;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.controller.WebServiceAPI;
import com.pango.hsec.hsec.model.AccionMejoraModel;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.ImageEntry;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.util.Compressor;
import com.pango.hsec.hsec.util.Picker;
import com.pango.hsec.hsec.util.ProgressRequestBody;
import com.pango.hsec.hsec.utilitario.InputFilterMinMax;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddRegistroAvance extends AppCompatActivity implements IActivity, Picker.PickListener,  ProgressRequestBody.UploadCallbacks {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText tx_avance,et_mensaje;
    Button decrement, increment,btnFechaInicio,btn_guardar,btn_eliminar;
    private ProgressDialog progress;
    boolean  Editable=false;
    private RecyclerView listView;
    private RecyclerView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<GaleriaModel> DataFiles;
    private ArrayList<GaleriaModel> DataImg;
    ActivityController activityTask;
    Spinner spinnerUsuario;
    String CodResponsable="",Responsable="",StrAccionmejora="", Errores="";
    ArrayList<Integer> Actives=new ArrayList();
    ArrayList<Maestro> usuario_data;
    TextView tx_titulo,sp2,tx3,tx4;


    ConstraintLayout ll_bar_carga;
    ProgressBar progressBar;
    ImageButton btncancelar;
    TextView txt_percent;
    Boolean cancel, enableSave=true;
    Call<String> request;
    long L,G,T;

    final String[] ACCEPT_MIME_TYPES = {
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
            "application/odt",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel"
    };
    AccionMejoraModel AddAccionMejora=new AccionMejoraModel();
    Gson gson;
    SimpleDateFormat df,dt;
    ArrayList<GaleriaModel> Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_registro_avance);
        tx_avance=findViewById(R.id.tx_avance);
        tx_avance.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "100")});
        decrement=findViewById(R.id.decrement);
        increment=findViewById(R.id.increment);
        btnFechaInicio=(Button) findViewById(R.id.btn_fecha);
        et_mensaje=findViewById(R.id.et_mensaje);
        btn_guardar=findViewById(R.id.btn_guardar);
        btn_eliminar=findViewById(R.id.btn_deleteAm);


        progressBar = findViewById(R.id.progressBar2);
        ll_bar_carga=findViewById(R.id.ll_bar_carga);
        ll_bar_carga.setVisibility(View.GONE);
        btncancelar= (ImageButton)findViewById(R.id.cancel_upload);
        txt_percent= (TextView)findViewById(R.id.txt_percent);
        //////////////
        ImageButton btnFotos=(ImageButton) findViewById(R.id.btn_addfotos);
        ImageButton btnFiles=(ImageButton) findViewById(R.id.btn_addfiles);

        spinnerUsuario=(Spinner) findViewById(R.id.sp_persona);
        gridView = (RecyclerView)  findViewById(R.id.grid);
        listView = (RecyclerView) findViewById(R.id.list);
        tx_titulo=findViewById(R.id.tx_titulo);

        sp2=findViewById(R.id.sp2);
        sp2.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Responsable"));
        //textView23.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Fecha Comprometida"));

        tx3=findViewById(R.id.tx3);
        tx3.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Porcentaje de avance"));

        tx4=findViewById(R.id.tx4);
        tx4.setText(Html.fromHtml("<font color="+ ContextCompat.getColor(this, R.color.colorRojo)+"> * </font>"+"Tarea realizada"));


        DataImg = new ArrayList<>();
        DataFiles = new ArrayList<>();
        Data = new ArrayList<>();
        gson = new Gson();
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dt = new SimpleDateFormat("dd 'de' MMMM");
        Bundle datos = this.getIntent().getExtras();
        AddAccionMejora= gson.fromJson(datos.getString("AccionMejora"), AccionMejoraModel.class);
        CodResponsable=datos.getString("CodResponsable");
        Responsable=datos.getString("Responsable");
        Editable=datos.getBoolean("Edit");
        if(Editable){
            tx_titulo.setText("Editar registro de atención");
        }else{
            tx_titulo.setText("Añadir registro de atención");
        }
        usuario_data= new ArrayList<>();
        String[] cod_Responsables=CodResponsable.split(";");
        String[] nom_Responsables=Responsable.split(";");
        for(int i=0;i<cod_Responsables.length;i++){
            usuario_data.add(new Maestro(cod_Responsables[i],nom_Responsables[i].split(":")[0]));
        }

        if(usuario_data.size()>1)usuario_data.add(0,new Maestro("-1","-  Seleccione  -"));
        ArrayAdapter adapterUsuario = new ArrayAdapter(this.getBaseContext(),R.layout.custom_spinner_item, usuario_data);
        adapterUsuario.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        spinnerUsuario.setAdapter(adapterUsuario);
        spinnerUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    AddAccionMejora.CodResponsable=usuario_data.get(position).CodTipo;
                    AddAccionMejora.Responsable=usuario_data.get(position).Descripcion;
                    //area = area_data.get(position).CodTipo;
                    //area_pos= String.valueOf(position);
                }else {
                    AddAccionMejora.CodResponsable="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnFotos.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            loadImage();
                                        }
                                    }
        );
        btnFiles.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setType("application/pdf");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
                                            startActivityForResult(Intent.createChooser(intent, "Seleccione un documento"), 1);
                                        }
                                    }
        );
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=tx_avance.getText().toString();
                int val_dec=100;
                if(!text.isEmpty()) val_dec=Integer.parseInt(text)-1;
                if(val_dec>=0&&val_dec<=100) {
                    //tx_avance.setText(val_dec+"");
                    tx_avance.setText(String.valueOf(val_dec), TextView.BufferType.EDITABLE);
                }
            }
        });
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=tx_avance.getText().toString();
                int val_dec=0;
                if(!text.isEmpty()) val_dec=Integer.parseInt(text)+1;
                if(val_dec>=0&&val_dec<=100) {
                    //tx_avance.setText(val_dec+"");
                    tx_avance.setText(String.valueOf(val_dec), TextView.BufferType.EDITABLE);

                }
            }
        });

        ////////////////////////////////////////////////
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date actual = myCalendar.getTime();
                AddAccionMejora.Fecha=df.format(actual);
                btnFechaInicio.setText(dt.format(actual));
            }
        };

        if(Editable){
            //btn_eliminar.setVisibility(View.VISIBLE);
            String url= GlobalVariables.Url_base+"AccionMejora/GetId/"+AddAccionMejora.Correlativo;
            ActivityController obj = new ActivityController("get", url, AddRegistroAvance.this,this);
            obj.execute("0");
        }
        else{
            Date fecha=new Date();
            myCalendar = Calendar.getInstance();
            fecha = myCalendar.getTime();
            AddAccionMejora.Fecha=df.format(fecha);
            AddAccionMejora.Responsable="";
            StrAccionmejora=gson.toJson(AddAccionMejora);
            Data=new ArrayList<>();
            setdata();
        }

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(AddRegistroAvance.this);
                builder.setTitle("¿Desea continuar?");
                builder.setIcon(R.drawable.erroricon);
                builder.setMessage("Está a punto de eliminar la accion de mejora.");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url= GlobalVariables.Url_base+"AccionMejora/Delete/"+AddAccionMejora.Correlativo;
                        ActivityController obj = new ActivityController("get", url, AddRegistroAvance.this,AddRegistroAvance.this);
                        obj.execute("2");
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cerrar diálogo
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public void outRegistroAvance(){
            boolean Nochangues=true;
            Gson gson = new Gson();
            Utils.closeSoftKeyBoard(this);
            AddAccionMejora.CodResponsable= ((Maestro) spinnerUsuario.getSelectedItem()).CodTipo;
            if(AddAccionMejora.CodResponsable.equals("-1")) {
                AddAccionMejora.CodResponsable="";
                AddAccionMejora.Responsable="";
            }
            AddAccionMejora.PorcentajeAvance=Integer.parseInt(tx_avance.getText().toString())+"";
            AddAccionMejora.Descripcion=et_mensaje.getText().toString();
            String FilesDelete="-";
            String Accionmejoras=gson.toJson(AddAccionMejora);
            if(!StrAccionmejora.equals(Accionmejoras)) Nochangues=false;
            else
            {
                ArrayList<GaleriaModel> DataInsert=new ArrayList<>();
                ArrayList<GaleriaModel> DataAll=new ArrayList<>();
                DataAll.addAll(DataImg);
                DataAll.addAll(DataFiles);
                //delete files
                if(Data.size()>0) {
                    String DeleteFiles = "";
                    for (GaleriaModel item : Data) {
                        boolean pass = true;
                        for (GaleriaModel item2 : DataAll) {
                            if (item.Correlativo == item2.Correlativo) {
                                pass = false;
                                continue;
                            }
                        }
                        if (pass) {
                            DeleteFiles += item.Correlativo + ";";
                          //  item.Estado = "E";
                        }
                    }
                    if(!DeleteFiles.equals("")) FilesDelete= DeleteFiles.substring(0,DeleteFiles.length()-1);
                }
                //Insert Files
                for (GaleriaModel item:DataAll) {
                    if(item.Correlativo==-1) {
                        DataInsert.add(item);
                    }
                }
                if(!FilesDelete.equals("-")||DataInsert.size()>0)Nochangues=false;
            }
            if(!Nochangues)
            {
                String Mensaje="Esta seguro de salir sin guardar cambios?\n";
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                //alertDialog.setCancelable(false);
                alertDialog.setTitle("Datos sin guardar");
                alertDialog.setIcon(R.drawable.warninicon);
                alertDialog.setMessage(Mensaje);

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
            else {

                if(!GlobalVariables.ObjectEditable) finish();
                else {
                    Intent intent = getIntent();
                    intent.putExtra("AccionMejora",gson.toJson(AddAccionMejora));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

    }
    public boolean ValifarFormulario(View view){
        String ErrorForm="";
        if(StringUtils.isEmpty(AddAccionMejora.CodResponsable) || AddAccionMejora.CodResponsable.equals("-1")) {ErrorForm+="*Responsable";}
        else if(StringUtils.isEmpty(AddAccionMejora.PorcentajeAvance)||AddAccionMejora.PorcentajeAvance.equals("0") ) {ErrorForm+="*Porcentaje de avance invalido";}
        else if(StringUtils.isEmpty(AddAccionMejora.Descripcion)) {ErrorForm+="*Tarea realizada";}
        else {
            ArrayList<GaleriaModel> DataAll=new ArrayList<>();
            DataAll.addAll(DataImg);
            DataAll.addAll(DataFiles);
            if(DataAll.isEmpty()) ErrorForm+="*Adjuntos,  una foto o archivo";
        }
        if(ErrorForm.isEmpty()) return true;
        else{


/*
            String Mensaje="Complete los siguientes campos obligatorios:\n\n"+ErrorForm;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Datos incorrectos");
            alertDialog.setIcon(R.drawable.warninicon);
            alertDialog.setMessage(Mensaje);

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
*/

            Snackbar.make(view, "El campo "+ErrorForm+" no puede estar vacío", Snackbar.LENGTH_LONG).show();



            return false;
        }
    }

    public void guardarData(View view) {
        if(!enableSave)return;
        Utils.closeSoftKeyBoard(AddRegistroAvance.this);
        AddAccionMejora.CodResponsable= ((Maestro) spinnerUsuario.getSelectedItem()).CodTipo;
        AddAccionMejora.PorcentajeAvance=Integer.parseInt(tx_avance.getText().toString())+"";
        AddAccionMejora.Descripcion=et_mensaje.getText().toString();
        if(ValifarFormulario(view))
        {
            Actives.clear();
            Errores="";
            enableSave=(false);
            cancel=false;
            btncancelar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            txt_percent.setText("");

            String Cabecera, FilesDelete;
            Cabecera=FilesDelete="-";
            String Accionmejoras=gson.toJson(AddAccionMejora);
            if(!StrAccionmejora.equals(Accionmejoras)) Cabecera=Accionmejoras;
            //edicion files
            gridViewAdapter.ProcesarImagens();

            ArrayList<GaleriaModel> DataInsert=new ArrayList<>();
            ArrayList<GaleriaModel> DataAll=new ArrayList<>();
            DataAll.addAll(DataImg);
            DataAll.addAll(DataFiles);


            //delete files
            if(Data.size()>0) {
                String DeleteFiles = "";
                for (GaleriaModel item : Data) {
                    boolean pass = true;
                    for (GaleriaModel item2 : DataAll) {
                        if (item.Correlativo == item2.Correlativo) {
                            pass = false;
                            continue;
                        }
                    }
                    if (pass) {
                        DeleteFiles += item.Correlativo + ";";
                        item.Estado = "E";
                    }
                }
                if(!DeleteFiles.equals("")) FilesDelete= DeleteFiles.substring(0,DeleteFiles.length()-1);
            }
            //Insert Files
            for (GaleriaModel item:DataAll) {
                boolean pass=false;
                for(GaleriaModel item2:Data)
                    if(item.Descripcion.equals(item2.Descripcion))
                        pass=true;
                if(item.Correlativo==-1) {
                    DataInsert.add(item);
                    if(!pass)Data.add(item);
                }
            }

            if(DataInsert.size()==0 && FilesDelete.equals("-")&& Cabecera.equals("-"))
            {
                enableSave=(true);
                Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            }
            else{
                Actives.add(0);
                ll_bar_carga.setVisibility(View.VISIBLE);
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GlobalVariables.Url_base)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                G=T=L=0;
                List<MultipartBody.Part> Files = new ArrayList<>();
                for (GaleriaModel item:DataInsert) {
                    T+=Long.parseLong(item.Tamanio);
                    Files.add(createPartFromFile(item));
                }
                request = service.PostAccionMejora("Bearer "+GlobalVariables.token_auth,createPartFromString(Cabecera),createPartFromString(FilesDelete),createPartFromString(AddAccionMejora.CodAccion),createPartFromString(AddAccionMejora.Correlativo),Files);
                if(T==0)onProgressUpdate();
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        onFinish();
                        if(response.isSuccessful()){
                            Actives.set(0,1);
                            String respt[]  = response.body().split(";"); //
                            for(int i =0;i<2;i++){
                                String rpt=respt[i];
                                if(!rpt.equals("-")){
                                    if(rpt.contains("-1")){
                                        Actives.add(-1);
                                        switch (i){
                                            case 0:
                                                Errores+="\nOcurrio un error al guardar cabecera";
                                                break;
                                            case 1:
                                                Errores+="\nOcurrio un error al eliminar imagenes/archivos";
                                                break;
                                        }
                                    }
                                    switch (i){
                                        case 0:
                                            if(AddAccionMejora.Correlativo.equals("-1"))
                                                AddAccionMejora.Correlativo = rpt;
                                            StrAccionmejora=gson.toJson(AddAccionMejora);
                                            break;
                                        case 1:
                                            ArrayList<GaleriaModel> temp= new ArrayList<>(Data);
                                            for (GaleriaModel item : Data) {
                                                if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                                                    temp.remove(item);
                                            }
                                            Data=temp;
                                            break;
                                    }
                                }
                            }

                            if(!respt[2].equals("-")){
                                Utils.DeleteCache(new Compressor(AddRegistroAvance.this).destinationDirectoryPath); //delete cache Files;
                                for (String file:respt[2].split(",")) {
                                    String[] datosf= file.split(":");
                                    for (GaleriaModel item:Data) {
                                        if(item.Descripcion.equals(datosf[0]))
                                        {
                                            item.Correlativo=Integer.parseInt(datosf[1]);
                                            if(item.Correlativo==-1) item.Estado="E";
                                            else {
                                                item.Estado="A";
                                                if(item.TipoArchivo.equals("TP01")) item.Url= "/Media/getImage/"+datosf[1]+"/Image.jpg";
                                                else if(item.TipoArchivo.equals("TP02")) item.Url= "/Media/Play/"+datosf[1]+"/Video.mp4";
                                                else item.Url= "/Media/Getfile/"+datosf[1]+"/"+datosf[0];
                                            }
                                        }
                                    }
                                }
                            }

                        }else{
                            if(response.code()==401){
                                Utils.reloadTokenAuth(AddRegistroAvance.this,AddRegistroAvance.this);
                                progressBar.setProgress(0);
                                txt_percent.setText(0+"%");
                            }
                            else{
                                Actives.set(0,-1);
                                Errores+="\nOcurrio un error interno de servidor";
                            }
                        }
                        if(!Actives.contains(0)) FinishSave();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if(!cancel){
                            Actives.set(0,-1);
                            if(t.getMessage().equals("timeout"))Errores+="\nConexión a servidor perdida, intente de nuevo";
                            else Errores+="\nOcurrio un error al intentar guardar los datos.";
                            if(!Actives.contains(0)) FinishSave();
                        }
                        else{
                            for(GaleriaModel item:Data)
                                if(item.Correlativo==-1)item.Estado="E";
                            loaddata();
                        }
                    }
                });
            }

        }
    }
    public void loaddata(){

        if(Data.size()>0)
        {
            DataImg.clear();
            DataFiles.clear();
            for (int i = 0; i < Data.size(); i++) {
                if (Data.get(i).TipoArchivo.equals("TP03")) {
                    DataFiles.add(Data.get(i));
                } else {
                    DataImg.add(Data.get(i));
                }
            }
         setdata();
        }
    }

    public void setdata(){

        if(!AddAccionMejora.CodResponsable.isEmpty()){
            int idrespoSpinner=GlobalVariables.indexOf(usuario_data,AddAccionMejora.CodResponsable);
            if(idrespoSpinner<0){
                idrespoSpinner=0;
                if(usuario_data.size()==1)
                usuario_data.add(0,new Maestro("-1","-  Seleccione  -"));
            }
            spinnerUsuario.setSelection(idrespoSpinner);
        }

        Date fecha=new Date();
        SimpleDateFormat dt = new SimpleDateFormat("dd 'de' MMMM");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            fecha = df.parse(AddAccionMejora.Fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnFechaInicio.setText(dt.format(fecha));
        tx_avance.setText(AddAccionMejora.PorcentajeAvance);
        et_mensaje.setText(AddAccionMejora.Descripcion);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, DataImg);
        gridView.setAdapter(gridViewAdapter);

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(horizontalManager);
        listViewAdapter = new ListViewAdapter(this, DataFiles);
        listView.setAdapter(listViewAdapter);
    }

    public void UpdateFiles (boolean opt){
        gridViewAdapter.ProcesarImagens();
        /*if(!gridViewAdapter.finaliceProceso())Log.d("Procesando :", "Esperando ");
        else Log.d("Procesado :", "Finalizado ");*/
        ArrayList<GaleriaModel> DataInsert=new ArrayList<>();
        ArrayList<GaleriaModel> DataAll=new ArrayList<>();


        DataAll.addAll(DataImg);
        DataAll.addAll(DataFiles);

        //delete files
        String DeleteFiles="";
        for (GaleriaModel item:Data) {
            boolean pass=true;
            for (GaleriaModel item2:DataAll) {
                if(item.Correlativo==item2.Correlativo){
                    pass=false;
                    continue;
                }
            }
            if(pass){
                DeleteFiles+=item.Correlativo+";";
                item.Estado="E";
            }
        }
//Insert Files
        for (GaleriaModel item:DataAll) {
            boolean pass=false;
            for(GaleriaModel item2:Data)
                if(item.Descripcion.equals(item2.Descripcion))
                    pass=true;
            if(item.Correlativo==-1) {
                DataInsert.add(item);
                if(!pass)Data.add(item);
            }
        }

        if(DeleteFiles.equals("")&&DataInsert.size()==0){
            if(opt) Toast.makeText(this, "No se detectaron cambios", Toast.LENGTH_LONG).show();
            else  FinishSave();
        }
        else{
//Delete Files
            if(!DeleteFiles.equals("")){
                Actives.add(0);
                String url= GlobalVariables.Url_base+"media/deleteAll/"+DeleteFiles.substring(0,DeleteFiles.length()-1);
                ActivityController obj = new ActivityController("get", url, AddRegistroAvance.this,this);
                obj.execute("1");
            }
            else Actives.add(1);
//Insert Files
            if(DataInsert.size()>0){

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GlobalVariables.Url_base)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI service = retrofit.create(WebServiceAPI.class);
                Actives.add(0);
                List<MultipartBody.Part> Files = new ArrayList<>();
                for (GaleriaModel item:DataInsert) {
                    Files.add(createPartFromFile(item));
                }
                Toast.makeText(AddRegistroAvance.this, "Subiendo Archivos, Espere..." , Toast.LENGTH_SHORT).show();

                request = service.uploadAllFile("Bearer "+GlobalVariables.token_auth,createPartFromString(AddAccionMejora.CodAccion),createPartFromString("TACME"),createPartFromString(AddAccionMejora.Correlativo+""),Files);
                progressBar.setVisibility(View.VISIBLE);
                ll_bar_carga.setVisibility(View.VISIBLE);


                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(response.isSuccessful()){
                            String respt  = response.body();
                            if(respt.contains("-1")){
                                Actives.set(2,-1);
                                Errores+="\nOcurrio un error al subir algunos archivos";


                            }
                            else  Actives.set(2,1);
                            Utils.DeleteCache(new Compressor(AddRegistroAvance.this).destinationDirectoryPath); //delete cache Files;
                            for (String file:respt.split(";")) {
                                String[] datosf= file.split(":");
                                for (GaleriaModel item:Data) {
                                    if(item.Descripcion.equals(datosf[0]))
                                    {
                                        item.Correlativo=Integer.parseInt(datosf[1]);
                                        if(item.Correlativo==-1) item.Estado="E";
                                        else {
                                            if(item.TipoArchivo.equals("TP01")) item.Url= "/Media/getImage/"+datosf[1]+"/Image.jpg";
                                            else if(item.TipoArchivo.equals("TP02")) item.Url= "/Media/Play/"+datosf[1]+"/Video.mp4";
                                            else item.Url= "/Media/Getfile/"+datosf[1]+"/"+datosf[0];
                                        }
                                    }
                                }
                            }

                        }else{
                            Actives.set(2,-1);
                            Errores+="\nOcurrio un error interno de servidor";
                        }
                        if(!Actives.contains(0)) FinishSave();
                        progressBar.setVisibility(View.GONE);
                        ll_bar_carga.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Actives.set(2,-1);
                        Errores+="\nFallo la subida de archivos";
                        if(!Actives.contains(0)) FinishSave();
                        progressBar.setVisibility(View.GONE);
                        ll_bar_carga.setVisibility(View.GONE);

                        for(GaleriaModel item:DataInsert){
                            item.Estado="E";
                        }

                    }
                });
            }
            else  Actives.add(1);
        }
    }

    @NonNull
    private RequestBody createPartFromString(String data){
        return RequestBody.create(MultipartBody.FORM,data);
    }

    @NonNull
    private MultipartBody.Part createPartFromFile(GaleriaModel item){
        ProgressRequestBody fileBody = new ProgressRequestBody(item, this,this);
        return  MultipartBody.Part.createFormData("image", item.Descripcion, fileBody);
    }

    boolean ok,fail;
    public void FinishSave(){
        int icon=R.drawable.confirmicon;
        String Mensaje="Se guardaron los datos correctamente";
        ok=false;
        fail=false;
        if(Actives.contains(1))ok=true;
        if(Actives.contains(-1)){
            fail=true;
            icon=R.drawable.erroricon;
            Mensaje="Ocurrio algun error interno, No se pudo guardar los datos.";
        }
        if(fail&&ok){
            Mensaje="Se guardo con los siguientes errores:\n\n";
            Mensaje+=Errores;//.replace("@","\n");
            icon=R.drawable.warninicon;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Desea Finalizar?");
        alertDialog.setIcon(icon);
        alertDialog.setMessage(Mensaje);

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ll_bar_carga.setVisibility(View.GONE);
                progressBar.setProgress(0);
                enableSave=(true);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(ok&&!fail)
                {
                    Intent intent = getIntent();
                    intent.putExtra("AccionMejora",gson.toJson(AddAccionMejora));
                    setResult(RESULT_OK, intent);
                    finish();
                }

                ll_bar_carga.setVisibility(View.GONE);
                progressBar.setProgress(0);
                enableSave=(true);
            }
        });
        loaddata();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        outRegistroAvance();
        //super.onBackPressed();
    }
    public void close(View view){outRegistroAvance();}

    public void escogerFecha(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        //datePickerDialog.getDatePicker().setMinDate(tempCalendar.getTimeInMillis());
        //tempCalendar.set(Calendar.MONTH, (new Date()).getMonth() + 1);
        //datePickerDialog.getDatePicker().setMaxDate(myCalendar2.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_CANCELED){ // action cancelled
        }
        if(resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            GaleriaModel temp= null;
            try {
                temp = Utils.getFilePath(this,uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            String [] exts=temp.Descripcion.split("\\.");
            switch (exts[exts.length-1]){
                case "pdf":case "doc":case "docx":case "ppt":case "pptx":case "xls":case "xlsx":case "odt":
                    listViewAdapter.add(temp);
                    break;
                default: Toast.makeText(this, "Archivo no permitido", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("NewApi")
    public void loadImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {Manifest.permission.CAMERA}, 1022);
        else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)requestPermissions(new String[] {android.Manifest.permission.RECORD_AUDIO}, 1033);
        else
        {
            new Picker.Builder(this, AddRegistroAvance.this, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setVideosEnabled(true)
                    .setLimit(10)
                    .build()
                    .startActivity();
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1011: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Para poder agregar archivos multimedia, necesita otorgar los persmisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1022: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Para poder utilizar la camara, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 1033: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the work
                    loadImage();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Para poder grabar un video, necesita otorgar los permisos solicitados", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onPickedSuccessfully(ArrayList<ImageEntry> images) {

        for (ImageEntry image:images) {
            gridViewAdapter.add(new GaleriaModel(image.path,image.isVideo?"TP02":"TP01",new File(image.path).length()+"",new File(image.path).getName())); //image.path.split("/")[image.path.split("/").length-1]
        }
    }

    @Override
    public void onCancel() {
        //Log.i(TAG, "User canceled picker activity");
        // Toast.makeText(getActivity(), "User canceld picker activtiy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(String data, String Tipo) {
        if(Tipo.equals("0"))  // load Accion mejora
        {
            Gson gson = new Gson();
            String idEdit=AddAccionMejora.Editable;
            AddAccionMejora = gson.fromJson(data, AccionMejoraModel.class);
            Data.addAll(AddAccionMejora.Files.Data);
            for (GaleriaModel item: Data) {
                if(item.TipoArchivo.equals("TP03")) DataFiles.add(item);
                else DataImg.add(item);
            }

            AddAccionMejora.Files=null;
            AddAccionMejora.Editable=idEdit;
            StrAccionmejora=gson.toJson(AddAccionMejora);
            setdata();
        }
        else if(Tipo.equals("1")){ //delete Files
            if(data.contains("false")){
                Actives.set(1,-1);
                Errores+="\nNo se pudo eliminar algunas imagenes/archivos";
            }
            else {
                Actives.set(1,1);
                ArrayList<GaleriaModel> temp= new ArrayList<>(Data);

                for (GaleriaModel item : Data) {
                    if(!StringUtils.isEmpty(item.Estado)&&item.Estado.equals("E"))
                        temp.remove(item);
                }
                Data=temp;
            }
            if(!Actives.contains(0)) FinishSave();
        }
        else if(Tipo.equals("2")){  //delete ACcion Mejora
            if(data.contains("-1")){
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Error!");
                alertDialog.setIcon(R.drawable.erroricon);
                alertDialog.setMessage("Ocurrio un error interno, intente de nuevo.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
            else{
                AddAccionMejora.Correlativo="E";
                Intent intent = getIntent();
                intent.putExtra("AccionMejora",gson.toJson(AddAccionMejora));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void successpost(String data, String Tipo) {
        //progressBar.setVisibility(View.GONE);
        progressBar.setProgress(100);
        ll_bar_carga.setVisibility(View.GONE);

        if(Tipo.equals("1")){
            if(data.contains("-1")){
                Actives.set(0,-1);
                FinishSave();
            }
            else{
                Editable=true;
                Actives.set(0,1);
                AddAccionMejora.Correlativo = data.substring(1, data.length() - 1);
                StrAccionmejora=gson.toJson(AddAccionMejora);
                //UpdateFiles(false);
            }
        }
    }

    @Override
    public void error(String mensaje, String Tipo) {

    }
    public void onProgressUpdate(){

        progressBar.setProgress(50);
        txt_percent.setText(50+"%");

    }
    @Override
    public void onProgressUpdate(long percentage) {
        if(percentage==0)L=G;
        G=L + percentage;
        int percent=(int)Math.round(100 * (double)G / (double)T);
        progressBar.setProgress(percent);
        txt_percent.setText(percent+"%");//String.format("%.2f", 100*(double)G / (double)T)+"%");
        if(percent==100){
            btncancelar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        btncancelar.setVisibility(View.GONE);
        progressBar.setProgress(100);
        txt_percent.setText("100%");
    }

    public void cancelUpload(View view) {
        if(request!=null){
            request.cancel();
            ll_bar_carga.setVisibility(View.GONE); enableSave=true;
            cancel=true;
        }
    }
}
