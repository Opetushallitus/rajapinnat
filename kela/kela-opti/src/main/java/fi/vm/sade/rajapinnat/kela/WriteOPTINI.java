/*
 * Copyright (c) 2012 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.rajapinnat.kela;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.koodisto.service.types.common.KieliType;
import fi.vm.sade.koodisto.service.types.common.KoodiMetadataType;
import fi.vm.sade.koodisto.service.types.common.KoodiType;
import fi.vm.sade.organisaatio.api.model.types.OrganisaatioTyyppi;
import fi.vm.sade.organisaatio.api.search.OrganisaatioPerustieto;
import fi.vm.sade.rajapinnat.kela.tarjonta.model.Organisaatio;

/**
 * 
 * @author Markus
 */
@Component
@Configurable
public class WriteOPTINI extends AbstractOPTIWriter {

	private String FILEIDENTIFIER;
    private String ALKUTIETUE;
    private String LOPPUTIETUE;
    
	private final static String ERR_MESS_OPTINI_1 = "could not write oppilaitos %s : invalid values.";
	private final static String ERR_MESS_OPTINI_2 = "could not write toimipiste %s : invalid values.";
	private final static String ERR_MESS_OPTINI_3 = "incorrect OID : '%s'";
	private final static String ERR_MESS_OPTINI_4 = "OID cannot not be null";
	private final static String ERR_MESS_OPTINI_5 = "NIMI cannot not be null (%s)";
	private final static String ERR_MESS_OPTINI_6 = "NIMIL cannot not be null (%s)";
	private final static String ERR_MESS_OPTINI_7 = "Opplaitosnumero not found for organisaatio %s";

    public WriteOPTINI() {
        super();
    }
    
    private String getOrganisaatioLyhytNimi(
            OrganisaatioPerustieto curOrganisaatio, Organisaatio orgE, List<String> kielet) throws OPTFormatException {
        List<KoodiType> koodit = new ArrayList<KoodiType>();
        String opKoodi="";
        if (curOrganisaatio.getOrganisaatiotyypit().contains(OrganisaatioTyyppi.OPPILAITOS)) {
        	opKoodi=curOrganisaatio.getOppilaitosKoodi();
            koodit = orgContainer.getKoodisByArvoAndKoodisto(opKoodi, orgContainer.oppilaitosnumerokoodisto);
        } else if (curOrganisaatio.getOrganisaatiotyypit().contains(OrganisaatioTyyppi.TOIMIPISTE)) {
            opKoodi = String.format("%s%s", getOppilaitosNro(curOrganisaatio), getToimipisteenJarjNro(orgE));
            koodit = orgContainer.getKoodisByArvoAndKoodisto(opKoodi, orgContainer.toimipistekoodisto);
        }
        String lyhytNimi = "";
        if (koodit != null && !koodit.isEmpty()) {
            lyhytNimi = getLyhytNimiFromKoodi(koodit.get(0), kielet);
        }
        if (lyhytNimi.length() > 40) {
            lyhytNimi = lyhytNimi.substring(0, 40);
        }
        if (StringUtils.isEmpty(lyhytNimi.trim())) {
        	error(String.format(ERR_MESS_OPTINI_6, curOrganisaatio.getOid()+" "+curOrganisaatio.getNimi()+" tyyppi: "+curOrganisaatio.getOrganisaatiotyypit()+" koodi:"+opKoodi));
        }
        return StringUtils.rightPad(lyhytNimi, 40);
    }

    private String getLyhytNimiFromKoodi(KoodiType koodi,
            List<String> kielet) {
        KoodiMetadataType kmdt = null;
        String nimi = "";
        if (kielet.contains(kieliFi)) {
            kmdt = getKoodiMetadataForLanguage(koodi, KieliType.FI);
        } else if (kielet.contains(kieliSv)) {
            kmdt = getKoodiMetadataForLanguage(koodi, KieliType.SV);
        } else if (kielet.contains(kieliEn)) {
            kmdt = getKoodiMetadataForLanguage(koodi, KieliType.EN);
        }
        if (kmdt == null) {
            kmdt = getAvailableKoodiMetadata(koodi);
        }
        if (kmdt != null) {
            nimi = kmdt.getLyhytNimi();
        }
        return nimi;
    }

    private String getOrganisaatioNimi(
            OrganisaatioPerustieto curOrganisaatio, List<String> kielet) throws OPTFormatException {
        String nimi = "";
        if (kielet.contains(kieliFi) && curOrganisaatio.getNimi("fi") != null) {
            nimi = curOrganisaatio.getNimi("fi");//getNimiFi();
        } else if (kielet.contains(kieliSv) && curOrganisaatio.getNimi("sv") != null) {
            nimi = curOrganisaatio.getNimi("sv");
        } else if (kielet.contains(kieliEn) && curOrganisaatio.getNimi("en") != null) {
            nimi = curOrganisaatio.getNimi("en");
        }
        if (nimi.isEmpty()) {
            nimi = getAvailableName(curOrganisaatio);
        }
        if (nimi.length() > 180) {
            nimi = nimi.substring(0, 180);
        }
        if (StringUtils.isEmpty(nimi.trim())) {
        	error(String.format(ERR_MESS_OPTINI_5, curOrganisaatio.getOid()+" "+curOrganisaatio.getNimi()));
        }
        return StringUtils.rightPad(nimi, 180);
    }

    private String getAvailableName(OrganisaatioPerustieto curOrganisaatio) {
        if (curOrganisaatio.getNimi("fi") != null) {
            return curOrganisaatio.getNimi("fi");
        }
        if (curOrganisaatio.getNimi("sv") != null) {
            return curOrganisaatio.getNimi("sv");
        }
        if (curOrganisaatio.getNimi("en") != null) {
            return curOrganisaatio.getNimi("en");
        }
        return "";
    }

    private String getSisainenKoodi(Organisaatio orgE) throws OPTFormatException  {
    	return numFormatter(""+orgE.getNimi().getId(), 10, "sisainen koodi");
    }

	@Override
	public void composeRecords() throws IOException, UserStopRequestException {
		for (OrganisaatioPerustieto curOppilaitos : this.orgContainer.getOppilaitokset()) {
			try {
				writeRecord(curOppilaitos);
			} catch (OPTFormatException e) {
				LOG.error(String.format(ERR_MESS_OPTINI_1, curOppilaitos.getOid()+" "+curOppilaitos.getNimi()));
			} 
		}

		for (OrganisaatioPerustieto curToimipiste : this.orgContainer.getToimipisteet()) {
			try {
				writeRecord(curToimipiste);
			} catch (OPTFormatException e) {
				LOG.error(String.format(ERR_MESS_OPTINI_2, curToimipiste.getOid()+" "+curToimipiste.getNimi()));
			}
		}
	}
	
	@Override
	public String composeRecord(Object... args) throws OPTFormatException {
		OrganisaatioPerustieto curOrganisaatio = (OrganisaatioPerustieto) args[0];
        Organisaatio orgE = kelaDAO.findOrganisaatioByOid(curOrganisaatio.getOid());
        String record = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s",//12 fields + EOL
                getSisainenKoodi(orgE),//Sisainen koodi
                StringUtils.leftPad("", 5),//OPE_OPPILNRO
                StringUtils.leftPad("", 2),//OPE_OPJNO
                _getOppilaitosNro(curOrganisaatio),//OPPILNRO
                StringUtils.leftPad("", 3),//KIELI
                getOrganisaatioNimi(curOrganisaatio, orgE.getKielet()),//Nimi
                getOrganisaatioLyhytNimi(curOrganisaatio, orgE, orgE.getKielet()),//Nimen lyhenne
                StringUtils.leftPad(DEFAULT_DATE, 10),//Viimeisin paivityspaiva
                getOrgOid(curOrganisaatio), //Org OID
                StringUtils.leftPad("", 8),//Viimeisin paivittaja
                "X",//Nimi on virallinen
                StringUtils.leftPad(DEFAULT_DATE, 10),//Alkupaiva, voimassaolon alku
                StringUtils.leftPad(DEFAULT_DATE, 10),//Loppupaiva, voimassaolon loppu
                "\n");
        return record;
	}

	private String _getOppilaitosNro(OrganisaatioPerustieto curOrganisaatio) throws OPTFormatException {
    	String opnro = "";
        if (curOrganisaatio.getOrganisaatiotyypit().contains(OrganisaatioTyyppi.TOIMIPISTE) 
                && !curOrganisaatio.getOrganisaatiotyypit().contains(OrganisaatioTyyppi.OPPILAITOS)) {
        	StringUtils.leftPad("", 5);
        } 
        if (curOrganisaatio.getOrganisaatiotyypit().contains(OrganisaatioTyyppi.OPPILAITOS)) {
        	opnro = StringUtils.leftPad(curOrganisaatio.getOppilaitosKoodi(), 5);
        }
        if (null == opnro || StringUtils.isEmpty(opnro)) {
        	error(String.format(ERR_MESS_OPTINI_7, curOrganisaatio.getOid()+" "+curOrganisaatio.getNimi()));
        }
        return opnro;
    }
	
	private String getOrgOid(OrganisaatioPerustieto org) throws OPTFormatException {
		if(null==org.getOid()) {
			error(String.format(ERR_MESS_OPTINI_4));
		}
		String oid = org.getOid().substring(org.getOid().lastIndexOf('.') + 1);
		if (oid == null || oid.length() == 0) {
			error(String.format(ERR_MESS_OPTINI_3, org.getOid()+" "+org.getNimi()));
		}
		return strFormatter(oid, 22, "OID");
	}
	
	@Value("${OPTINI.alkutietue}")
    public void setAlkutietue(String alkutietue) {
        this.ALKUTIETUE = alkutietue;
    }
	
	@Value("${OPTINI.lopputietue}")
    public void setLopputietue(String lopputietue) {
        this.LOPPUTIETUE = lopputietue;
    }
	
	@Value("${OPTINI.fileIdentifier:OPTINI}")
    public void setFileIdentifier(String fileIdentifier) {
        this.FILEIDENTIFIER = fileIdentifier;
    }

	@Override
	public String getAlkutietue() {
		return ALKUTIETUE;
	}

	@Override
	public String getLopputietue() {
		return LOPPUTIETUE;
	}

	@Override
	public String getFileIdentifier() {
		return FILEIDENTIFIER;
	}
}
