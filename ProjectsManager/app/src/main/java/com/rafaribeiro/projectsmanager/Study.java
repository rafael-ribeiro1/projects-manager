package com.rafaribeiro.projectsmanager;

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
}
