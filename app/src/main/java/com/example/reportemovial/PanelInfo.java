package com.example.reportemovial;

import java.util.Date;

public class PanelInfo {
    private  int Total;
    private int Pendiente;
    private int Progreso;
    private int Atendido;
    private int Vial;
    private int Agua;
    private int Alumbrado;
    private int Arbol;

    public PanelInfo(){}

    public PanelInfo(int Total, int Pendiente, int Progreso, int Atendido,
                            int Vial, int Agua, int Alumbrado, int Arbol){
         this.Total = Total;
         this.Pendiente = Pendiente;
         this.Progreso = Progreso;
         this.Atendido = Atendido;
         this.Vial = Vial;
         this.Agua = Agua;
         this.Alumbrado = Alumbrado;
         this.Arbol = Arbol;
    }

    public int getTotal(){return Total;}

    public void setTotal(int Total){this.Total = Total;}

    public int getPendiente(){return Pendiente;}

    public void setPendiente(int Pendiente){this.Pendiente = Pendiente;}

    public int getProgreso(){return Progreso;}

    public void setProgreso(int Progreso){this.Progreso = Progreso;}

    public int getAtendido(){return Atendido;}

    public void setAtendido(int Atendido){this.Atendido = Atendido;}

    public int getVial(){return Vial;}

    public void setVial(int Vial){this.Vial = Vial;}

    public int getAgua(){return Agua;}

    public void setAgua(int Agua){this.Agua = Agua;}

    public int getAlumbrado(){return Alumbrado;}

    public void setAlumbrado(int Alumbrado){this.Alumbrado = Alumbrado;}

    public int getArbol(){return Arbol;}

    public void setArbol(int Arbol){this.Arbol = Arbol;}

}
