package fi.vm.sade.rajapinnat.kela;

import fi.vm.sade.organisaatio.resource.api.TasoJaLaajuusDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class TasoJaLaajuusContainer {

    private static final Logger LOG = Logger.getLogger(TasoJaLaajuusContainer.class);

    private static final String ONLYALEMPI = "050";
    private static final String ALEMPIYLEMPI = "060";
    private static final String ONLYYLEMPI = "061";
    private static final String LAAKIS = "070";
    private static final String HAMMASLAAKIS = "071";
    private static final String EITASOA = "   ";

    private static final String ALEMPI_DEFAULT_LAAJUUS = "180";
    private static final String YLEMPI_DEFAULT_LAAJUUS = "120";

    private String tasoCode;
    private String komoId1;
    private String komoId2;

    public String getTasoCode() {
        return tasoCode;
    }

    public String getKomoId1() {
        return komoId1;
    }

    public String getKomoId2() {
        return komoId2;
    }


    public TasoJaLaajuusContainer laakis(String komoId) {
        tasoCode = LAAKIS;
        this.komoId1 = komoId;
        return this;
    }

    public TasoJaLaajuusContainer hammasLaakis(String komoId) {
        tasoCode = HAMMASLAAKIS;
        this.komoId1 = komoId;
        return this;
    }

    public TasoJaLaajuusContainer onlyAlempi(String komoId) {
        tasoCode = ONLYALEMPI;
        this.komoId1 = komoId;
        return this;
    }

    public TasoJaLaajuusContainer onlyYlempi(String komoId) {
        tasoCode = ONLYYLEMPI;
        this.komoId1 = komoId;
        return this;
    }

    public TasoJaLaajuusContainer alempiYlempi(String alempiKomoId, String ylempiKomoId) {
        tasoCode = ALEMPIYLEMPI;
        this.komoId1 = alempiKomoId;
        this.komoId2 = ylempiKomoId;
        return this;
    }

    public TasoJaLaajuusContainer eiTasoa() {
        tasoCode = EITASOA;
        return this;
    }

    public boolean isYlempi() { return ONLYYLEMPI.equals(tasoCode); }
    public boolean isLaakis() { return LAAKIS.equals(tasoCode); }
    public boolean isHammaslaakis() { return HAMMASLAAKIS.equals(tasoCode); }
    public boolean isAlempi() { return ONLYALEMPI.equals(tasoCode); }
    public boolean isAlempiYlempi() { return ALEMPIYLEMPI.equals(tasoCode); }

    public boolean hasTaso() {
        return this.tasoCode != null && EITASOA.equals(tasoCode) == false;
    }

    public TasoJaLaajuusDTO toDTO(TarjontaClient tarjontaClient) {
        TasoJaLaajuusDTO resp = new TasoJaLaajuusDTO();
        resp.setTasoCode(this.tasoCode);

        String laajuus1 = tarjontaClient.getLaajuus(this.komoId1);
        String laajuus2 = tarjontaClient.getLaajuus(this.komoId2);

        if(laajuus1 != null && laajuus1.contains("+")) {
            parseSumLaajuus(laajuus1, resp);
        } else if(laajuus2 != null && laajuus2.contains("+")) {
            parseSumLaajuus(laajuus2, resp);
        } else {
            resp.setLaajuus1(prefixLaajuus(laajuus1));
            resp.setLaajuus2(prefixLaajuus(laajuus2));
        }

        resp.setKomoId1(this.komoId1);
        resp.setKomoId2(this.komoId2);
        return resp;
    }

    private void parseSumLaajuus(String laajuus, TasoJaLaajuusDTO resp) {
        // Check for faulty input by user, taso code should be something else than simple alempi or ylempi with a laajuus that has a sum.
        if(ONLYALEMPI.equals(tasoCode)) {
            resp.setLaajuus1(ALEMPI_DEFAULT_LAAJUUS);
        } else if(ONLYYLEMPI.equals(tasoCode)) {
            resp.setLaajuus1(YLEMPI_DEFAULT_LAAJUUS);
        } else {
            String[] vals = parseLaajuus(laajuus);
            resp.setLaajuus1(prefixLaajuus(vals[0]));
            resp.setLaajuus2(prefixLaajuus(vals[1]));
        }
    }

    private String prefixLaajuus(String laajuus) {
        try {
            if (StringUtils.isNotEmpty(laajuus)) {
                return String.format("%03d", new Integer(laajuus));
            }
        } catch (Exception e) {
            LOG.error("Could not format with leading zeros. Laajuus:" + laajuus, e);
        }
        return null;
    }

    private String[] parseLaajuus(String laajuus) {
        String[] str = new String[2];

        try {
            if (laajuus != null) {
                if (laajuus.contains("/")) {
                    // case: 180+120/150 = laajuus1 = 330
                    String[] laajuusParts = laajuus.split("/");
                    int sum = Integer.parseInt(laajuusParts[0].substring(0, laajuusParts[0].indexOf('+'))) + Integer.parseInt(laajuusParts[1]);
                    str[0] = "" + sum;
                } else {
                    String[] laajuusParts = laajuus.split("\\+");
                    str[0] = laajuusParts[0];
                    if (laajuusParts.length > 1) {
                        // case: 180+120
                        str[1] = laajuusParts[1];
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Bad opintojen laajuus value:" + laajuus, e);
        }

        return str;
    }

}

