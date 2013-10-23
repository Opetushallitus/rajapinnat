package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;

/**
 * Integrations to suoritusrekisteri services for 2.aste tiedonsiirto.
 *
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteSuoritusrekisteriIntegration {

    public void importArvosanaToSuoritusRekisteri(String henkiloOid, String komotoOid, ROWSET.ROW row) {
        throw new RuntimeException("not impl"); // todo:
    }

    public void importOpiskeluoikeus(String henkiloOid, fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET.ROW hlotiedot) {
        throw new RuntimeException("not impl"); // todo:
    }
}
