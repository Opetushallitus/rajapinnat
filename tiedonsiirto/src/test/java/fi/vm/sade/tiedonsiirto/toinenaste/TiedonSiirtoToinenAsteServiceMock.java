package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.GenericFault;
import fi.vm.sade.henkilo.service.TiedonSiirtoToinenAsteService;
import fi.vm.sade.henkilo.service.types.perusopetus.GenericResponse;
import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.Hakijat;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.HakijatRequestParametersType;

import javax.jws.WebParam;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.math.BigInteger;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteServiceMock implements TiedonSiirtoToinenAsteService {

    @Override
    public GenericResponse importArvosanat(ROWSET data) throws GenericFault {
        System.out.println("TiedonSiirtoToinenAsteServiceMock.importArvosanat, ROWSET.ROW.0.HETU: "+data.getROW().get(0).getHETU());
        return genericResponse("OK");
    }

    @Override
    public GenericResponse importHenkilotiedot(fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET data) throws GenericFault {
        System.out.println("TiedonSiirtoToinenAsteServiceMock.importHenkilotiedot, ROWSET.ROW.0.POSTINUMERO: "+data.getROW().get(0).getPOSTINUMERO());
        return genericResponse("OK");
    }

    @Override
    public Hakijat exportHakijat(@WebParam(partName = "parameters", name = "hakijatRequestParametersType", targetNamespace = "http://service.henkilo.sade.vm.fi/types/perusopetus/hakijat") HakijatRequestParametersType parameters) throws GenericFault {
        System.out.println("TiedonSiirtoToinenAsteServiceMock.exportHakijat, oppilaitos: "+parameters.getOppilaitosnumero()+", ytunnus: "+ parameters.getYtunnus());
        return unmarshalXml("fi.vm.sade.henkilo.service.types.perusopetus.hakijat", "toinenaste/hakijat.xml");
    }

    private Hakijat unmarshalXml(String packageName, String xmlResource) {
        try {
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Unmarshaller um = jc.createUnmarshaller();
            return (Hakijat) um.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlResource));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private GenericResponse genericResponse(String message) {
        GenericResponse response = new GenericResponse();
        response.setResponseMessage(message);
        response.setSuccessRowCount(BigInteger.valueOf(0));
        response.setErrorRowCount(BigInteger.valueOf(0));
        return response;
    }

}
