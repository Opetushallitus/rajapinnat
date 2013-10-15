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

import javax.jws.WebParam;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteServiceImpl implements TiedonSiirtoToinenAsteService {

    private static final Logger log = LoggerFactory.getLogger(TiedonSiirtoToinenAsteServiceImpl.class);
    // TODO: auditlog?
    @Autowired
    private TiedonSiirtoToinenAsteIntegrations integrations;

    @Override
    public GenericResponse importArvosanat(ROWSET data) throws GenericFault {
        Map<ROWSET.ROW, Exception> errors = new LinkedHashMap<ROWSET.ROW, Exception>();
        for (ROWSET.ROW row : data.getROW()) {
            log.info("import arvosanat row, hetu: "+row.getHETU()+", row: "+row);
            try {
                String henkiloOid = integrations.getHenkiloOidFromAuthenticationService(row.getHETU());
                if (henkiloOid == null) throw new NullPointerException("no henkilo for hetu "+row.getHETU());
                integrations.importArvosanaToSuoritusRekisteri(henkiloOid, row);
            } catch (Exception e) {
                log.warn("failed to import arvosanat row, hetu: "+row.getHETU()+", row: "+row+", error: "+e);
                errors.put(row, e);
            }
        }
        return response(data, errors);
    }

    private GenericResponse response(ROWSET data, Map<ROWSET.ROW, Exception> errors) {
        GenericResponse response = new GenericResponse();
        response.setErrorRowCount(BigInteger.valueOf(errors.size()));
        response.setSuccessRowCount(BigInteger.valueOf(data.getROW().size()-errors.size()));
        response.setResponseMessage("OK with "+errors.size()+" errors");
        for (ROWSET.ROW row : errors.keySet()) {
            Exception error = errors.get(row);
            GenericResponse.ErrorRow errorRow = new GenericResponse.ErrorRow();
            errorRow.setHetu(row.getHETU());
            errorRow.setErrorMessage(error.getMessage());
            response.getErrorRow().add(errorRow);
        }
        return response;
    }

    @Override
    public GenericResponse importHenkilotiedot(fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET data) throws GenericFault {
        throw new RuntimeException("not impl"); // todo:
    }

    @Override
    public Hakijat exportHakijat(@WebParam(partName = "parameters", name = "hakijatRequestParametersType", targetNamespace = "http://service.henkilo.sade.vm.fi/types/perusopetus/hakijat") HakijatRequestParametersType parameters) throws GenericFault {
        throw new RuntimeException("not impl"); // todo:
    }

    public void setIntegrations(TiedonSiirtoToinenAsteIntegrations integrations) {
        this.integrations = integrations;
    }
}
