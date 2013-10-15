package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.generic.rest.CachingRestClient;
import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * Integrations to other services for 2.aste tiedonsiirto. Note! Chop when needed
 *
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteIntegrations {

    @Value("${cas.service.authentication-service}")
    private String authenticationServiceUrl;
    private CachingRestClient restClient = new CachingRestClient();

    public String getHenkiloOidFromAuthenticationService(String hetu) throws Exception {
        Map<String,String> henkilo = restClient.get(authenticationServiceUrl+"/resources/henkilo/byHetu/"+hetu, Map.class);
        return henkilo.get("oidHenkilo");
    }

    public void importArvosanaToSuoritusRekisteri(String henkiloOid, ROWSET.ROW row) {
        throw new RuntimeException("not impl"); // todo:
    }
}
