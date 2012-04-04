package fi.vm.sade.rajapinnat.ytj.service;

import fi.vm.sade.rajapinnat.ytj.api.YTJDTO;
import fi.vm.sade.rajapinnat.ytj.api.YTJOsoiteDTO;
import fi.ytj.YrityksenOsoiteV2DTO;
import fi.ytj.YritysHakuDTO;
import fi.ytj.YritysTiedotV2DTO;
import fi.ytj.YrityksenYhteystietoDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuomas Katva
 */
public class YtjDtoMapperHelper {

    public YTJDTO mapYritysTiedotV2DTOtoYTJDTO(YritysTiedotV2DTO vastaus) {
        YTJDTO ytj = new YTJDTO();

        ytj.setNimi(vastaus.getToiminimi().getToiminimi());
        ytj.setYtunnus(vastaus.getYritysTunnus().getYTunnus());
        ytj.setPostiOsoite(mapYtjOsoite(vastaus.getYrityksenPostiOsoite()));
        //If kayntiosoite-katu or postilokero is not null then try to map it
        ytj.setKayntiOsoite(vastaus.getYrityksenKayntiOsoite() != null && vastaus.getYrityksenKayntiOsoite().getKatu() != null || vastaus.getYrityksenKayntiOsoite().getPostilokero() != null ? mapYtjOsoite(vastaus.getYrityksenKayntiOsoite()) : null);
        mapYhteysTiedot(vastaus, ytj);
        mapYritysmuotoAndToimiala(vastaus, ytj);
        return ytj;
    }

    private YTJOsoiteDTO mapYtjOsoite(YrityksenOsoiteV2DTO osoiteParam) {
        YTJOsoiteDTO osoite = new YTJOsoiteDTO();

        osoite.setPostilokero(osoiteParam.getPostilokero());

        osoite.setKatu(getKatuOsoite(osoiteParam));
        osoite.setPostinumero(osoiteParam.getPostinumero());
        osoite.setToimipaikka(osoiteParam.getToimipaikka());
        osoite.setMaa(osoiteParam.getMaa());
        osoite.setMaakoodi(osoiteParam.getMaakoodi());
        return osoite;
    }
    
    private void mapYhteysTiedot(YritysTiedotV2DTO yritysParam, YTJDTO yritys) {
        for (YrityksenYhteystietoDTO yhtTieto:yritysParam.getYrityksenYhteystiedot().getYrityksenYhteystietoDTO()) {
            if (yhtTieto.getLaji().trim().equals("4") || yhtTieto.getSeloste().trim().equalsIgnoreCase("www")) {
                yritys.setWww(yhtTieto.getYhteysTieto());
            } else if (yhtTieto.getLaji().trim().equals("3") || yhtTieto.getSeloste().trim().equalsIgnoreCase("S�hk�posti")) {
                yritys.setSahkoposti(yhtTieto.getYhteysTieto());
            } else if (yhtTieto.getLaji().trim().equals("5") || yhtTieto.getSeloste().trim().equalsIgnoreCase("Matkapuhelin")) {
                yritys.setPuhelin(yhtTieto.getYhteysTieto());
            }
            //Add other if's if needed
        }
    }
    
    private void mapYritysmuotoAndToimiala(YritysTiedotV2DTO yritysParam, YTJDTO yritys) {
        yritys.setYritysmuoto(yritysParam.getYritysmuoto().getSeloste());
        yritys.setYritysmuotoKoodi(yritysParam.getYritysmuoto().getKoodi());
        yritys.setToimiala(yritysParam.getToimiala().getSeloste());
        yritys.setToimialaKoodi(yritysParam.getToimiala().getKoodi());
    }

    private String getKatuOsoite(YrityksenOsoiteV2DTO osoiteParam) {

        if (osoiteParam.getKatu() != null) {
            String kokoKatuOsoite = "";

            kokoKatuOsoite = osoiteParam.getKatu() + " " + (osoiteParam.getTalo() != null ? osoiteParam.getTalo() : "") + " " + (osoiteParam.getPorras() != null ? osoiteParam.getPorras() : "") +  " " + (osoiteParam.getHuoneisto() != null ? osoiteParam.getHuoneisto() : "");

            return kokoKatuOsoite;
        } else {
            return null;
        }

    }

    public List<YTJDTO> mapYritysHakuDTOListToDtoList(List<YritysHakuDTO> vastaukset) {
        List<YTJDTO> yritykset = new ArrayList<YTJDTO>();
        for (YritysHakuDTO vastaus : vastaukset) {
            yritykset.add(mapYritysHakuDTOToDto(vastaus));
        }


        return yritykset;
    }

    public YTJDTO mapYritysHakuDTOToDto(YritysHakuDTO ytjParam) {
        YTJDTO dto = new YTJDTO();
        dto.setNimi(ytjParam.getYritysnimi());
        dto.setYtunnus(ytjParam.getYTunnus());


        return dto;
    }
}
