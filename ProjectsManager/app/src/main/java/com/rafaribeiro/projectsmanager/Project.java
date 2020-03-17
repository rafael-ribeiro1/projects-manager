package com.rafaribeiro.projectsmanager;

import java.util.ArrayList;

public class Project implements State {

    private int idProj;
    private String nameProj;
    private String abstractProj;
    private String descProj;
    private String funcProj;
    private int state;
    private int nReq;

    private final int NREQ_DEFAULT = 0;

    private final String SEPARATOR = "/0//1/";

    public Project() {}
    public Project(int idProj, String nameProj, String abstractProj, String descProj, String funcProj, int state, int nReq) {
        this.idProj = idProj;
        this.nameProj = nameProj;
        this.abstractProj = abstractProj;
        this.descProj = descProj;
        this.funcProj = funcProj;
        this.state = state;
        this.nReq = nReq;
    }
    public Project(String nameProj, String abstractProj, String descProj, String funcProj, int state) {
        this.nameProj = nameProj;
        this.abstractProj = abstractProj;
        this.descProj = descProj;
        this.funcProj = funcProj;
        this.state = state;
        this.nReq = NREQ_DEFAULT;
    }

    public int getIdProj() {
        return idProj;
    }
    public void setIdProj(int idProj) {
        this.idProj = idProj;
    }
    public String getNameProj() {
        return nameProj;
    }
    public void setNameProj(String nameProj) {
        this.nameProj = nameProj;
    }
    public String getAbstractProj() {
        return abstractProj;
    }
    public void setAbstractProj(String abstractProj) {
        this.abstractProj = abstractProj;
    }
    public String getDescProj() {
        return descProj;
    }
    public void setDescProj(String descProj) {
        this.descProj = descProj;
    }
    public String getFuncProj() {
        return funcProj;
    }
    public void setFuncProj(String funcProj) {
        this.funcProj = funcProj;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public int getnReq() {
        return nReq;
    }
    public void setnReq(int nReq) {
        this.nReq = nReq;
    }

    public String[] getFunctionalities() {
        if (this.funcProj.trim().equals("")) {
            return new String[0];
        } else {
            return this.funcProj.split(SEPARATOR);
        }
    }

}
