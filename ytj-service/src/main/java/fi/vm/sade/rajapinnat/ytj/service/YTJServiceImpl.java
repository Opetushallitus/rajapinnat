/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.vm.sade.rajapinnat.ytj.service;


import fi.vm.sade.rajapinnat.ytj.api.*;
import fi.ytj.*;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
/**
 *
 * @author Tuomas Katva
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
    
    public void tstYtj() {
        try {
            YritysTiedot yt = new YritysTiedot();
            YritysTiedotSoap ytj = yt.getYritysTiedotSoap();
            tarkiste = this.createHashHex(this.createHashString());
            
            
            
            YritysHakutulos vastaus = ytj.wmYritysHaku("", 
                    "", 
                    false, 
                    "2255802-1", 
                    true, 
                    Kieli.FI, getAsiakastunnus(), 
                    aikaleima, 
                    tarkiste, 
                    tiketti);
            System.out.println("VASTAUS : " + vastaus.getTunnistusTiedot().getTunnistusStatus());
            
            for (YritysHakuDTO yritys : vastaus.getYritysHaku().getYritysHakuDTO()) {
                System.out.println("YRITYS : " + yritys.getYritysnimi());
                System.out.println("YRITYSMUOTO : " + yritys.getYritysmuoto());
            }
        } catch (Exception exp) {
            System.out.println("EXP : " + exp.toString());
        }
    }
    
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
        return getAsiakastunnus()+getSalainenavain()+aikaleima;
    }

    public List<YTJDTO> findByYNimi(String nimi) {
        try {
            YritysTiedot yt = new YritysTiedot();
            YritysTiedotSoap ytj = yt.getYritysTiedotSoap();
            tarkiste = this.createHashHex(this.createHashString());
            
            
            
            YritysHakutulos vastaus = ytj.wmYritysHaku(nimi, 
                    "", 
                    false, 
                    "", 
                    true, 
                    Kieli.FI, 
                    getAsiakastunnus(), 
                    aikaleima, 
                    tarkiste, 
                    tiketti);
            
              return mapper.mapYritysHakuDTOListToDtoList(vastaus.getYritysHaku().getYritysHakuDTO());
             
        } catch (Exception exp) {
            
            //TODO, add logging
            return null;
        }
    }

    public YTJDTO findByYTunnus(String ytunnus) {
        
        try {
            YritysTiedot yt = new YritysTiedot();
            YritysTiedotSoap ytj = yt.getYritysTiedotSoap();
            tarkiste = this.createHashHex(this.createHashString());
            
            YritysTiedotV2DTO vastaus = ytj.wmYritysTiedotV2(ytunnus,
                    Kieli.FI, 
                    asiakastunnus, 
                    aikaleima, 
                    tarkiste, 
                    tiketti);
            
                    
                    
           
            
               return mapper.mapYritysTiedotV2DTOtoYTJDTO(vastaus);
              
            
        } catch (Exception exp) {
            
            //TODO, add logging
            return null;
        }
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
