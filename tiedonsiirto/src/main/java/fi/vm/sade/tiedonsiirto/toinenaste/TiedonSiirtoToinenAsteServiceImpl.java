package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.GenericFault;
import fi.vm.sade.henkilo.service.TiedonSiirtoToinenAsteService;
import fi.vm.sade.henkilo.service.types.perusopetus.GenericResponse;
import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.Hakijat;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.HakijatRequestParametersType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.jws.WebParam;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Business service for 2.aste tiedonsiirto.
 * For now implements WSDL generated interface, but will be used as a business service for other APIs, such as XML and EXEL,
 * because they use the same XSD generated domain in the end.
 *
 * TODO: auditlog, oikeudet
 *
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteServiceImpl implements TiedonSiirtoToinenAsteService {

    private static final Logger log = LoggerFactory.getLogger(TiedonSiirtoToinenAsteServiceImpl.class);
    @Autowired
    private TiedonSiirtoToinenAsteHenkiloIntegration henkiloIntegration;
    @Autowired
    private TiedonSiirtoToinenAsteTarjontaIntegration tarjontaIntegration;
    @Autowired
    private TiedonSiirtoToinenAsteSuoritusrekisteriIntegration suoritusrekisteriIntegration;

    @Override
    public GenericResponse importArvosanat(ROWSET data) throws GenericFault {
        Map<ROWSET.ROW, Exception> errors = new LinkedHashMap<ROWSET.ROW, Exception>();
        for (ROWSET.ROW row : data.getROW()) {
            log.info("import arvosanat row, hetu: "+row.getHETU()+", row: "+row);
            try {
                String henkiloOid = henkiloIntegration.findHenkiloOidFromAuthenticationService(row.getHETU());
                String komotoOid = tarjontaIntegration.findKomotoFromTarjonta(row);
                if (henkiloOid == null) throw new NullPointerException("no henkilo for hetu "+row.getHETU());
                if (komotoOid == null) throw new NullPointerException("no komoto for arvosana row "+row);
                // todo: pitäisi varmaan tässä pureskella arvosanoista todistukset, ja vasta niillä ampua suoritusrekisteriä
                suoritusrekisteriIntegration.importArvosanaToSuoritusRekisteri(henkiloOid, komotoOid, row);
            } catch (Exception e) {
                log.warn("failed to import arvosanat row, hetu: "+row.getHETU()+", row: "+row+", error: "+e, e);
                errors.put(row, e);
            }
        }
        return response(errors, data.getROW().size());
    }

    @Override
    public GenericResponse importHenkilotiedot(fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET data) throws GenericFault {
        Map<fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET.ROW, Exception> errors = new LinkedHashMap<fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET.ROW, Exception>();
        for (fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET.ROW row : data.getROW()) {
            log.info("import henkilotiedot row, hetu: "+row.getHETU()+", row: "+row);
            try {
                // check if henkilo already exists - todo: miten oikeasti pitäisi toimia? mergetä tiedot?
                String henkiloOid = henkiloIntegration.findHenkiloOidFromAuthenticationService(row.getHETU());
                if (henkiloOid != null) {
                    throw new Exception("henkilo already exists: "+row.getHETU());
                }

                // create henkilo
                henkiloOid = henkiloIntegration.importHenkilo(row);

                // create opiskeluoikeus data
                suoritusrekisteriIntegration.importOpiskeluoikeus(henkiloOid, row);

            } catch (Exception e) {
                log.warn("failed to import henkilotiedot row, hetu: "+row.getHETU()+", row: "+row+", error: "+e, e);
                errors.put(row, e);
            }
        }
        return response(errors, data.getROW().size());
    }

    @Override
    public Hakijat exportHakijat(@WebParam(partName = "parameters", name = "hakijatRequestParametersType", targetNamespace = "http://service.henkilo.sade.vm.fi/types/perusopetus/hakijat") HakijatRequestParametersType parameters) throws GenericFault {
        throw new RuntimeException("not impl"); // todo: vasta arvosana import työn alla
    }

    private GenericResponse response(Map<? extends Object,Exception> errors, int rows) {
        GenericResponse response = new GenericResponse();
        response.setErrorRowCount(BigInteger.valueOf(errors.size()));
        response.setSuccessRowCount(BigInteger.valueOf(rows -errors.size()));
        response.setResponseMessage("OK with "+errors.size()+" errors");
        for (Object row : errors.keySet()) {
            Exception error = errors.get(row);
            GenericResponse.ErrorRow errorRow = new GenericResponse.ErrorRow();
            try {
                errorRow.setHetu((String) row.getClass().getMethod("getHETU").invoke(row)); // en keksinyt parempaa tapaa hankkia hetua
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            errorRow.setErrorMessage(error.getMessage());
            response.getErrorRow().add(errorRow);
        }
        return response;
    }

    public void setHenkiloIntegration(TiedonSiirtoToinenAsteHenkiloIntegration henkiloIntegration) {
        this.henkiloIntegration = henkiloIntegration;
    }

    public void setTarjontaIntegration(TiedonSiirtoToinenAsteTarjontaIntegration tarjontaIntegration) {
        this.tarjontaIntegration = tarjontaIntegration;
    }

    public void setSuoritusrekisteriIntegration(TiedonSiirtoToinenAsteSuoritusrekisteriIntegration suoritusrekisteriIntegration) {
        this.suoritusrekisteriIntegration = suoritusrekisteriIntegration;
    }

    public GenericResponse validateAndImportArvosanatXml(InputStream xmlInputStream) throws IOException, SAXException, JAXBException, GenericFault {
        ROWSET arvosanaRowset = (ROWSET) validateAndUnmarshalXml(xmlInputStream, "toinenaste/perusopetusArvosanat.xsd", "fi.vm.sade.henkilo.service.types.perusopetus.arvosanat");
        return this.importArvosanat(arvosanaRowset);
    }

    public GenericResponse validateAndImportHenkilotiedotXml(InputStream xmlInputStream) throws IOException, SAXException, JAXBException, GenericFault {
        fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET hloROWSET = (fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET) validateAndUnmarshalXml(xmlInputStream, "toinenaste/perusopetusHenkilotiedot.xsd", "fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot");
        return this.importHenkilotiedot(hloROWSET);
    }

    private Object validateAndUnmarshalXml(InputStream xmlInputStream, String xsdPath, String jaxbPackage) throws SAXException, JAXBException {
        // prepare
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(getStreamSource(xsdPath));

        // validate and unmarshal xml to object
        StreamSource xmlSource = new StreamSource(xmlInputStream);
        JAXBContext jc = JAXBContext.newInstance(jaxbPackage);
        Unmarshaller um = jc.createUnmarshaller();
        um.setSchema(schema);
        return um.unmarshal(xmlSource);
    }

    private StreamSource getStreamSource(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL resource = cl.getResource(path);
        if (resource == null) throw new NullPointerException("failed to get stream from classpath resource: "+path);
        return new StreamSource(cl.getResourceAsStream(path), resource.toString());
    }


}
