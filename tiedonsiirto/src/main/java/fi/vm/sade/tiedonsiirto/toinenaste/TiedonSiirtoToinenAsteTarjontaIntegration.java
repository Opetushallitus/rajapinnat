package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.generic.rest.CachingRestClient;
import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * Integrations to tarjonta services for 2.aste tiedonsiirto.
 *
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteTarjontaIntegration {
    public String findKomotoFromTarjonta(ROWSET.ROW arvosana) throws Exception {
        throw new RuntimeException("not impl");
    }
}
