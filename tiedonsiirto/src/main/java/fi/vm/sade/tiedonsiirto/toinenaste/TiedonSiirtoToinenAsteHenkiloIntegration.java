package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.generic.rest.CachingRestClient;
import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * Integrations to henkilo services for 2.aste tiedonsiirto.
 *
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteHenkiloIntegration {

    @Value("${cas.service.authentication-service}")
    private String authenticationServiceUrl;
    private CachingRestClient restClient = new CachingRestClient();

    public String findHenkiloOidFromAuthenticationService(String hetu) throws Exception {
        Map<String,String> henkilo = restClient.get(authenticationServiceUrl+"/resources/henkilo/byHetu/"+hetu, Map.class);
        return henkilo.get("oidHenkilo");
    }

}
