package fi.vm.sade.rajapinnat.ytj.service;

import fi.vm.sade.rajapinnat.ytj.api.YTJDTO;
import fi.vm.sade.rajapinnat.ytj.api.YTJOsoiteDTO;
import fi.ytj.YrityksenOsoiteV2DTO;
import fi.ytj.YrityksenYhteystietoDTO;
import fi.ytj.YritysHakuDTO;
import fi.ytj.YritysTiedotV2DTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuomas Katva
 */
public class YtjDtoMapperHelper {

    public YTJDTO mapYritysTiedotV2DTOtoYTJDTO(YritysTiedotV2DTO vastaus) {
        YTJDTO ytj = new YTJDTO();
        //Dont know if it is needed to get YrityksenHenkilo nimi if toiminimi does not exist
        ytj.setNimi(vastaus.getToiminimi().getToiminimi() != null ? vastaus.getToiminimi().getToiminimi() 
        : (vastaus.getYrityksenHenkilo() != null ? vastaus.getYrityksenHenkilo().getNimi() : null));
        ytj.setYtunnus(vastaus.getYritysTunnus().getYTunnus());
        ytj.setPostiOsoite(vastaus.getYrityksenPostiOsoite() != null ? mapYtjOsoite(vastaus.getYrityksenPostiOsoite()) : null);
        ytj.setKotiPaikka(vastaus.getKotipaikka() != null ? vastaus.getKotipaikka().getKoodi() : null);
        //If kayntiosoite-katu or postilokero is not null then try to map it
        ytj.setKayntiOsoite( vastaus.getYrityksenKayntiOsoite() != null ?  mapYtjOsoite(vastaus.getYrityksenKayntiOsoite()) : null);
        mapYhteysTiedot(vastaus, ytj);
        mapYritysmuotoAndToimiala(vastaus, ytj);
        return ytj;
    }

    private YTJOsoiteDTO mapYtjOsoite(YrityksenOsoiteV2DTO osoiteParam) {
        if (osoiteParam != null) {
        YTJOsoiteDTO osoite = new YTJOsoiteDTO();

        

        osoite.setKatu(getKatuOsoite(osoiteParam));
        osoite.setPostinumero(osoiteParam.getPostinumero());
        osoite.setToimipaikka(osoiteParam.getToimipaikka());
        osoite.setMaa(osoiteParam.getMaa());
        osoite.setMaakoodi(osoiteParam.getMaakoodi());
        return osoite;
        } else {
            return null;
        }
    }
    
    private void mapYhteysTiedot(YritysTiedotV2DTO yritysParam, YTJDTO yritys) {
        if (yritysParam.getYrityksenYhteystiedot() != null) {
        for (YrityksenYhteystietoDTO yhtTieto:yritysParam.getYrityksenYhteystiedot().getYrityksenYhteystietoDTO()) {
            //Yhteystieto lajit = 4 : www, 3 : email, 5 : matkapuhelin
            if (yhtTieto.getLaji().trim().equals("4")) {
                yritys.setWww(yhtTieto.getYhteysTieto());
            } else if (yhtTieto.getLaji().trim().equals("3")) {
                yritys.setSahkoposti(yhtTieto.getYhteysTieto());
            } else if (yhtTieto.getLaji().trim().equals("5")) {
                yritys.setPuhelin(yhtTieto.getYhteysTieto());
            }
            //Add other if's if needed
        }
        }
    }
    
    private void mapYritysmuotoAndToimiala(YritysTiedotV2DTO yritysParam, YTJDTO yritys) {
        if (yritysParam.getYritysmuoto() != null) {
        yritys.setYritysmuoto(yritysParam.getYritysmuoto().getSeloste());
        yritys.setYritysmuotoKoodi(yritysParam.getYritysmuoto().getKoodi());
        }
        if (yritysParam.getToimiala() != null) {
        yritys.setToimiala(yritysParam.getToimiala().getSeloste());
        yritys.setToimialaKoodi(yritysParam.getToimiala().getKoodi());
        }
    }

    private String getKatuOsoite(YrityksenOsoiteV2DTO osoiteParam) {

        if (osoiteParam.getKatu() != null) {
            String kokoKatuOsoite;

            kokoKatuOsoite = osoiteParam.getKatu() + " " + (osoiteParam.getTalo() != null ? osoiteParam.getTalo() : "") + " " 
            + (osoiteParam.getPorras() != null ? osoiteParam.getPorras() : "") +  " " + (osoiteParam.getHuoneisto() != null ? osoiteParam.getHuoneisto() : "");

            return kokoKatuOsoite;
        } else if (osoiteParam.getPostilokero() != null) {
            return osoiteParam.getPostilokero();
        } else {
            return null;
        }

    }

    public List<YTJDTO> mapYritysHakuDTOListToDtoList(List<YritysHakuDTO> vastaukset) {
        if (vastaukset != null) {
        List<YTJDTO> yritykset = new ArrayList<YTJDTO>();
        for (YritysHakuDTO vastaus : vastaukset) {
            yritykset.add(mapYritysHakuDTOToDto(vastaus));
        }


        return yritykset;
        } else{
            return null;
        }
    }

    public YTJDTO mapYritysHakuDTOToDto(YritysHakuDTO ytjParam) {
        if (ytjParam != null) {
        YTJDTO dto = new YTJDTO();
        dto.setNimi(ytjParam.getYritysnimi() != null ? ytjParam.getYritysnimi() : null);
        dto.setYtunnus(ytjParam.getYTunnus() != null ? ytjParam.getYTunnus() : null);


        return dto;
        } else {
            return null;
        }
    }
}
