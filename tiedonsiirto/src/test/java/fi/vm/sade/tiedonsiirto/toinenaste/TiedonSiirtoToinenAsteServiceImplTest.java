package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.types.perusopetus.GenericResponse;
import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoToinenAsteServiceImplTest {

    private static final String HETU_OID_FOUND = "151093-931S";
    private static final String HETU_OID_NULL = "151093-917A";
    private static final String HETU_OID_EXCEPTION = "151093-9712";
    private static final String HETU_ARVOSANA_IMPORT_FAILS = "151093-923H";
    private static final String HETU_TARJONTA_INTEGRATION_FAILS = "151093-923X";
    private static final String OID = "1.2.246.562.24.27470134096";

    @Test
    public void importArvosanat() throws Exception {

        // luodaan service jossa muut palvelut mockattu - todo: erota mock
        TiedonSiirtoToinenAsteServiceImpl service = new TiedonSiirtoToinenAsteServiceImpl();
        service.setHenkiloIntegration(new TiedonSiirtoToinenAsteHenkiloIntegration() {
            @Override
            public String findHenkiloOidFromAuthenticationService(String hetu) throws Exception {
                if (HETU_OID_NULL.equals(hetu)) return null;
                if (HETU_OID_EXCEPTION.equals(hetu)) throw new Exception("HETU_OID_EXCEPTION");
                return OID;
            }
        });
        service.setTarjontaIntegration(new TiedonSiirtoToinenAsteTarjontaIntegration(){
            public String findKomotoFromTarjonta(ROWSET.ROW arvosana) throws Exception {
                if (HETU_TARJONTA_INTEGRATION_FAILS.equals(arvosana.getHETU())) {
                    throw new Exception("HETU_TARJONTA_INTEGRATION_FAILS");
                }
                return "TESTKOMOTO";
            }
        });
        service.setSuoritusrekisteriIntegration(new TiedonSiirtoToinenAsteSuoritusrekisteriIntegration(){
            @Override
            public void importArvosanaToSuoritusRekisteri(String henkiloOid, String komotoOid, ROWSET.ROW row) {
                if (row.getHETU().equals(HETU_ARVOSANA_IMPORT_FAILS)) throw new RuntimeException("HETU_ARVOSANA_IMPORT_FAILS");
            }
        });

        // kutsutaan importtia, jossa seuraavanlaista dataa
        // * arvosana henkilölle, jota ei ole
        // * arvosana henkilölle, joka heittää poikkeuksen
        // * arvosana henkilölle, joka on olemassa
        // * arvosana, jonka import ei onnistu
        // * arvosana, jonka tarjonta-integraatio ei onnistu (komotoa ei löydy)
        ROWSET rowset = new ROWSET();
        rowset.getROW().add(TiedonSiirtoTstUtils.createSampleRowArvosana(HETU_OID_NULL));
        rowset.getROW().add(TiedonSiirtoTstUtils.createSampleRowArvosana(HETU_OID_EXCEPTION));
        rowset.getROW().add(TiedonSiirtoTstUtils.createSampleRowArvosana(HETU_OID_FOUND));
        rowset.getROW().add(TiedonSiirtoTstUtils.createSampleRowArvosana(HETU_ARVOSANA_IMPORT_FAILS));
        rowset.getROW().add(TiedonSiirtoTstUtils.createSampleRowArvosana(HETU_TARJONTA_INTEGRATION_FAILS));
        GenericResponse response = service.importArvosanat(rowset);

        // assertoidaan, että responsesta löytyy onnistuneet ja epäonnistuneet rivit
        // ...assertoidaan responsen yleiset tiedot
        assertEquals(1, response.getSuccessRowCount().intValue());
        assertEquals(4, response.getErrorRowCount().intValue());
        assertEquals("OK with 4 errors", response.getResponseMessage());
        // ...assertoidaan responsen virherivit
        assertEquals(HETU_OID_NULL, response.getErrorRow().get(0).getHetu());
        assertEquals("no henkilo for hetu "+HETU_OID_NULL, response.getErrorRow().get(0).getErrorMessage());
        assertEquals(HETU_OID_EXCEPTION, response.getErrorRow().get(1).getHetu());
        assertEquals("HETU_OID_EXCEPTION", response.getErrorRow().get(1).getErrorMessage());
        assertEquals(HETU_ARVOSANA_IMPORT_FAILS, response.getErrorRow().get(2).getHetu());
        assertEquals("HETU_ARVOSANA_IMPORT_FAILS", response.getErrorRow().get(2).getErrorMessage());
        assertEquals(HETU_TARJONTA_INTEGRATION_FAILS, response.getErrorRow().get(3).getHetu());
        assertEquals("HETU_TARJONTA_INTEGRATION_FAILS", response.getErrorRow().get(3).getErrorMessage());
    }

}