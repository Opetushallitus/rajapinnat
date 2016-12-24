package fi.vm.sade.rajapinnat.vtj.service.impl;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Kansalaisuus;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.tempuri.SoSoSoap;
import org.tempuri.TeeHenkilonTunnusKyselyResponse;

import java.io.IOException;

/**
 * User: tommiha
 * Date: 6/26/13
 * Time: 2:23 PM
 */

public class VtjServiceImpl implements VtjService {

    private static Logger logger = LoggerFactory.getLogger(VtjServiceImpl.class);

    private SoSoSoap soSoSoap;
    private String kayttajatunnus;
    private String salasana;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "vtj", key = "#hetu")
    public YksiloityHenkilo teeHenkiloKysely(String loppukayttaja, String hetu, boolean logMessage) {
        VTJHenkiloVastaussanoma vastaus = getVtjHenkiloVastaussanoma(loppukayttaja, hetu, 0);
        return convert(vastaus, logMessage);
    }

    private VTJHenkiloVastaussanoma getVtjHenkiloVastaussanoma(String loppukayttaja, String hetu, int depth) {
        TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult tunnusKyselyResult = soSoSoap.teeHenkilonTunnusKysely("OPHREK", kayttajatunnus, salasana, loppukayttaja, null, hetu, null, null, null, null, null, null, null);
        VTJHenkiloVastaussanoma vastaus = (VTJHenkiloVastaussanoma) tunnusKyselyResult.getContent().get(0);

        try {
            logger.debug("Response from VTJ for hetu '" + hetu + "': " + objectMapper.writeValueAsString(vastaus));
        } catch (IOException e) {
            logger.error("Couldn't log received message", e);
        }

        if (vastaus == null) {
            throw new NotFoundException("Invalid response from VTJ");
        }

        // paluukoodit: https://github.com/Opetushallitus/rajapinnat/blob/8e1faa038a61d67a4e98c4897bc9013aa218f81a/vtj/vtj-remote-api/src/main/resources/wsdl/VTJHenkilotiedotKatalogi.xsd#L608-L643
        String paluuKoodi = vastaus.getPaluukoodi() != null ? vastaus.getPaluukoodi().getKoodi() : null;

        // tarkistetaan onko henkilön hetu muuttunut
        if ("0002".equals(paluuKoodi)) {
            String uusiHetu = (vastaus.getHenkilo() != null && vastaus.getHenkilo().getHenkilotunnus() != null) ?
                    vastaus.getHenkilo().getHenkilotunnus().getValue() : null;
            if (uusiHetu != null) {
                if (!uusiHetu.equals(hetu)) {
                    if(depth > 1) {
                        // todennäköisesti virhe datassa, lopeta rekursio
                        throw new NotFoundException("Query with passive hetu should not return another passive hetu.");
                    }

                    logger.info("Hetu has changed for a person. Old: " + hetu + ", new: " + uusiHetu);
                    // haetaan tiedot uudestaan uudella hetulla
                    return getVtjHenkiloVastaussanoma(loppukayttaja, uusiHetu, ++depth);

                }
            }
            else {
                throw new NotFoundException("Invalid response from VTJ");
            }
        }
        // kaikki paluukoodit paitsi 0000 ja 0002 käsitellään virheinä
        else if (!"0000".equals(paluuKoodi)) {
            throw new NotFoundException("Could not find person.");
        }

        return vastaus;
    }

    private YksiloityHenkilo convert(VTJHenkiloVastaussanoma vastaus, boolean logMessage) {
        
        YksiloityHenkilo henkilo = new YksiloityHenkilo();
        Henkilo vtjHenkilo = vastaus.getHenkilo();
        
        if (logMessage) {
            try {
                logger.info(objectMapper.writeValueAsString(vtjHenkilo));
            } catch (IOException e) {
                logger.error("Couldn't log received message", e);
            }
        }
        
        henkilo.setEtunimi(vtjHenkilo.getNykyisetEtunimet().getEtunimet());
        henkilo.setSukunimi(vtjHenkilo.getNykyinenSukunimi().getSukunimi());
        if(!vtjHenkilo.getNykyisetEtunimet().equals(vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi()) &&
                !vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi().trim().contains(" ")) {
            henkilo.setKutsumanimi(vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi());
        }

        String turvakieltoTieto = vtjHenkilo.getTurvakielto().getTurvakieltoTieto();
        henkilo.setTurvakielto(turvakieltoTieto.equals("1") ? true : false);

        henkilo.setHetu(vtjHenkilo.getHenkilotunnus().getValue());

        henkilo.setSukupuoli(vtjHenkilo.getSukupuoli().getSukupuolikoodi());
        
        henkilo.setAidinkieliKoodi(vtjHenkilo.getAidinkieli().getKielikoodi());
        
        if (vtjHenkilo.getSahkopostiosoite() != null) {
            henkilo.setSahkoposti(vtjHenkilo.getSahkopostiosoite());
        }
        
        if (vtjHenkilo.getKansalaisuus() != null) {
            for (Kansalaisuus vtjKansalaisuus : vtjHenkilo.getKansalaisuus()) {
                henkilo.addKansalaisuusKoodi(vtjKansalaisuus.getKansalaisuuskoodi3());
            }
        }
        
        if (vtjHenkilo.getVakinainenKotimainenOsoite() != null) {
            StringBuffer postiOsoiteS = new StringBuffer();
            StringBuffer postiOsoiteR = new StringBuffer();
            
            if (vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS() != null) {
                postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS());
                postiOsoiteS.append(" ");
                postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatunumero());
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain() != null) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain());
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero() != null) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero());
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain() != null) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain());
                }
            }
            
            if (vtjHenkilo.getVakinainenKotimainenOsoite().getKatuR() != null) {
                postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS());
                postiOsoiteR.append(" ");
                postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatunumero());
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain() != null) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain());
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero() != null) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero());
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain() != null) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain());
                }
            }
            
            YksiloityHenkilo.OsoiteTieto kotimaanOsoite = henkilo.new OsoiteTieto(
                    "yhteystietotyyppi4",
                    postiOsoiteS.toString(),
                    postiOsoiteR.toString(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostinumero(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaS(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaR(),
                    "Suomi",
                    "Finland");
            henkilo.addOsoiteTieto(kotimaanOsoite);
        }
        
        if (vtjHenkilo.getVakinainenUlkomainenOsoite() != null) {
            YksiloityHenkilo.OsoiteTieto ulkomaanOsoite = henkilo.new OsoiteTieto(
                    "yhteystietotyyppi5",
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getUlkomainenLahiosoite(),
                    null,
                    null,
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getUlkomainenPaikkakunta(),
                    null,
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getValtioS(),
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getValtioR());
            henkilo.addOsoiteTieto(ulkomaanOsoite);
        }
        
        return henkilo;
    }

    public SoSoSoap getSoSoSoap() {
        return soSoSoap;
    }

    public void setSoSoSoap(SoSoSoap soSoSoap) {
        this.soSoSoap = soSoSoap;
    }

    public String getKayttajatunnus() {
        return kayttajatunnus;
    }

    public void setKayttajatunnus(String kayttajatunnus) {
        this.kayttajatunnus = kayttajatunnus;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }
}
