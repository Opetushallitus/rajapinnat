/*
 *  License?
 */
package fi.vm.sade.rajapinnat.ytj.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mlyly
 */
@XmlRootElement(name = "YTJ")
@XmlAccessorType(XmlAccessType.FIELD)
public class YTJDTO {

    @XmlElement(required = true)
    private Integer versio = 1;

    private String nimi;
    private String ytunnus;
    private String yritysmuoto;
    private String yrityksenKieli;
    private YTJOsoiteDTO postiOsoite;
    private YTJOsoiteDTO kayntiOsoite;
    private String sahkoposti;
    private String www;
    private String puhelin;
    private String faksi;
    

    public Integer getVersio() {
        return versio;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getYtunnus() {
        return ytunnus;
    }

    public void setYtunnus(String ytunnus) {
        this.ytunnus = ytunnus;
    }

    /**
     * @return the yritysmuoto
     */
    public String getYritysmuoto() {
        return yritysmuoto;
    }

    /**
     * @param yritysmuoto the yritysmuoto to set
     */
    public void setYritysmuoto(String yritysmuoto) {
        this.yritysmuoto = yritysmuoto;
    }

    /**
     * @return the yrityksenKieli
     */
    public String getYrityksenKieli() {
        return yrityksenKieli;
    }

    /**
     * @param yrityksenKieli the yrityksenKieli to set
     */
    public void setYrityksenKieli(String yrityksenKieli) {
        this.yrityksenKieli = yrityksenKieli;
    }

    /**
     * @return the postiOsoite
     */
    public YTJOsoiteDTO getPostiOsoite() {
        return postiOsoite;
    }

    /**
     * @param postiOsoite the postiOsoite to set
     */
    public void setPostiOsoite(YTJOsoiteDTO postiOsoite) {
        this.postiOsoite = postiOsoite;
    }

    /**
     * @return the kayntiOsoite
     */
    public YTJOsoiteDTO getKayntiOsoite() {
        return kayntiOsoite;
    }

    /**
     * @param kayntiOsoite the kayntiOsoite to set
     */
    public void setKayntiOsoite(YTJOsoiteDTO kayntiOsoite) {
        this.kayntiOsoite = kayntiOsoite;
    }

    /**
     * @return the sahkoposti
     */
    public String getSahkoposti() {
        return sahkoposti;
    }

    /**
     * @param sahkoposti the sahkoposti to set
     */
    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    /**
     * @return the www
     */
    public String getWww() {
        return www;
    }

    /**
     * @param www the www to set
     */
    public void setWww(String www) {
        this.www = www;
    }

    /**
     * @return the puhelin
     */
    public String getPuhelin() {
        return puhelin;
    }

    /**
     * @param puhelin the puhelin to set
     */
    public void setPuhelin(String puhelin) {
        this.puhelin = puhelin;
    }

    /**
     * @return the faksi
     */
    public String getFaksi() {
        return faksi;
    }

    /**
     * @param faksi the faksi to set
     */
    public void setFaksi(String faksi) {
        this.faksi = faksi;
    }
}
