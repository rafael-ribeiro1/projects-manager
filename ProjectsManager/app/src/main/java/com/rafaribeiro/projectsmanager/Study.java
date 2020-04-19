package com.rafaribeiro.projectsmanager;

import android.content.Context;

import java.util.ArrayList;

public class Study {

    private int idStu;
    private String nameStu;
    private String fromStu;
    private String link;
    private int nMod;

    private final int NMOD_DEFAULT = 0;

    public Study() {}
    public Study(int idStu, String nameStu, String fromStu, String link, int nMod) {
        this.idStu = idStu;
        this.nameStu = nameStu;
        this.fromStu = fromStu;
        this.link = link;
        this.nMod = nMod;
    }
    public Study(String nameStu, String fromStu, String link, int nMod) {
        this.nameStu = nameStu;
        this.fromStu = fromStu;
        this.link = link;
        this.nMod = nMod;
    }
    public Study(String nameStu, String fromStu, String link) {
        this.nameStu = nameStu;
        this.fromStu = fromStu;
        this.link = link;
        this.nMod = NMOD_DEFAULT;
    }

    public int getIdStu() {
        return idStu;
    }
    public void setIdStu(int idStu) {
        this.idStu = idStu;
    }
    public String getNameStu() {
        return nameStu;
    }
    public void setNameStu(String nameStu) {
        this.nameStu = nameStu;
    }
    public String getFromStu() {
        return fromStu;
    }
    public void setFromStu(String fromStu) {
        this.fromStu = fromStu;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public int getnMod() {
        return nMod;
    }
    public void setnMod(int nMod) {
        this.nMod = nMod;
    }

    public int getProgress(Context context) {
        DataBase db = new DataBase(context);
        if (this.nMod == -1) {
            return 100;
        } else if (this.nMod == 0) {
            return 1;
        } else {
            ArrayList<Module> modules = db.selectModulesOfStudy(this.idStu);
            int nRea = 0;
            for (int i = 0; i < modules.size(); i++) {
                if (modules.get(i).getState() == Module.DONE) nRea++;
            }
            return (nRea * 100) / modules.size();
        }
    }
}
