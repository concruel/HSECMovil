package com.pango.hsec.hsec.model;

/**
 * Created by jcila on 23/04/2018.
 */

public class ObsFacilitoModel {
    public String CodObsFacilito;
    public String Tipo;
    public String CodPosicionGer ;
    public String CodPosicionSup;
    public String UbicacionExacta;
    public String Observacion;
    public String Accion;
    //public int Plazo;
    public String FecCreacion;
    public String FechaFin;
    public String RespAuxiliarDesc;
    /*public String FecModifica;
    public String UsuModifica ;*/
    public String RespAuxiliar;
    public String Estado;
    //public String Editable;
   // public String CodPersona;
    public String Persona;
    /*public String UrlObs;
    public String UrlPrew;*/

    public ObsFacilitoModel(){
        Tipo="A";
        Accion="";
        Observacion="";
        UbicacionExacta="";
        CodObsFacilito="";
    }
}
