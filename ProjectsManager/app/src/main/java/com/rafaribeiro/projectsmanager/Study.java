package com.rafaribeiro.projectsmanager;

public class Study {

    private int idStu;
    private String nameStu;
    private String from;
    private String link;
    private int nMod;

    private final int NMOD_DEFAULT = 0;

    public Study() {}
    public Study(int idStu, String nameStu, String from, String link, int nMod) {
        this.idStu = idStu;
        this.nameStu = nameStu;
        this.from = from;
        this.link = link;
        this.nMod = nMod;
    }
    public Study(String nameStu, String from, String link) {
        this.nameStu = nameStu;
        this.from = from;
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
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
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
