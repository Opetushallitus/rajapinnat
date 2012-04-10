/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.vm.sade.rajapinnat.ytj.service;

import fi.vm.sade.rajapinnat.ytj.api.YTJDTO;
import fi.vm.sade.rajapinnat.ytj.api.YTJKieli;
import fi.vm.sade.rajapinnat.ytj.api.YTJService;
import fi.ytj.*;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Tuomas Katva
 * 
 * TODO: Add logging to the project
 */
public class YTJServiceImpl implements YTJService {

    private String asiakastunnus = "";
    private String salainenavain = "";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String HASH_ALGORITHM = "SHA-1";
    private static final String ENCODING = "UTF-8";
    private String aikaleima = "";
    private String tarkiste = "";
    private String tiketti = "";
    private YtjDtoMapperHelper mapper = new YtjDtoMapperHelper();

    private String createHashHex(String strToHash) {
        try {
            byte[] strBytes = strToHash.getBytes(ENCODING);
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] digestBytes = md.digest(strBytes);


            return Hex.encodeHexString(digestBytes).toUpperCase();
        } catch (Exception exp) {
            System.out.println("Exception : " + exp.toString());
            return null;
        }
    }

    private String createHashString() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        aikaleima = df.format(new Date());
        return getAsiakastunnus() + getSalainenavain() + aikaleima;
    }

    @Override
    public List<YTJDTO> findByYNimi(String nimi, boolean naytaPassiiviset, YTJKieli kieli) {
        

            Kieli kiali = getKieli(kieli);

            YritysTiedot yt = new YritysTiedot();
            YritysTiedotSoap ytj = yt.getYritysTiedotSoap();
            tarkiste = this.createHashHex(this.createHashString());



            YritysHakutulos vastaus = ytj.wmYritysHaku(nimi,
                    "",
                    false,
                    "",
                    naytaPassiiviset,
                    kiali,
                    getAsiakastunnus(),
                    aikaleima,
                    tarkiste,
                    tiketti);

            return mapper.mapYritysHakuDTOListToDtoList(vastaus.getYritysHaku().getYritysHakuDTO());

        
    }
    
    private Kieli getKieli(YTJKieli kieliParam) {
        Kieli selectedLang;
            switch (kieliParam) {
                case EN:
                    selectedLang = Kieli.EN;
                    break;

                case SV:
                    selectedLang = Kieli.SV;
                    break;

                default:

                    selectedLang = Kieli.FI;
                    break;



            }
            return selectedLang;
    }

    @Override
    public YTJDTO findByYTunnus(String ytunnus, YTJKieli kieli) {

       
            Kieli kiali = getKieli(kieli);
            YritysTiedot yt = new YritysTiedot();
            YritysTiedotSoap ytj = yt.getYritysTiedotSoap();
            tarkiste = this.createHashHex(this.createHashString());

            YritysTiedotV2DTO vastaus = ytj.wmYritysTiedotV2(ytunnus,
                    kiali,
                    asiakastunnus,
                    aikaleima,
                    tarkiste,
                    tiketti);





            return mapper.mapYritysTiedotV2DTOtoYTJDTO(vastaus);


        
    }

    /**
     * @return the asiakastunnus
     */
    public String getAsiakastunnus() {
        return asiakastunnus;
    }

    /**
     * @param asiakastunnus the asiakastunnus to set
     */
    public void setAsiakastunnus(String asiakastunnus) {
        this.asiakastunnus = asiakastunnus;
    }

    /**
     * @return the salainenavain
     */
    public String getSalainenavain() {
        return salainenavain;
    }

    /**
     * @param salainenavain the salainenavain to set
     */
    public void setSalainenavain(String salainenavain) {
        this.salainenavain = salainenavain;
    }
}
