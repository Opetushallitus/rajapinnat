package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.ImportHenkilotiedotFault;
import fi.vm.sade.henkilo.service.TiedonSiirtoToinenAsteService;
import fi.vm.sade.henkilo.service.types.perusopetus.ROWSET;
import org.apache.cxf.annotations.SchemaValidation;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteServiceMock implements TiedonSiirtoToinenAsteService {
    @Override
    public void importHenkilotiedot(@WebParam(partName = "parameters", name = "ROWSET", targetNamespace = "http://service.henkilo.sade.vm.fi/types/perusopetus") ROWSET parameters) throws ImportHenkilotiedotFault {
        System.out.println("TiedonSiirtoToinenAsteServiceMock.importHenkilotiedot, ROWSET.ROW.1.POSTINUMERO: "+parameters.getROW().get(0).getPOSTINUMERO());
    }
}
