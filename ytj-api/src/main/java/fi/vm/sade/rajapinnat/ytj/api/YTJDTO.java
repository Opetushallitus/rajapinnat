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
}
