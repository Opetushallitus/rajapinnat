package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.TiedonSiirtoToinenAsteService;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.Hakijat;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.HakijatRequestParametersType;
import fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;

import javax.xml.ws.soap.SOAPFaultException;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoWsdlTest {

    private static int port = (int) (7000 + Math.random() * 1000);  // port 7000-7999
    private static Server server;

    @Test
    public void importHenkilotiedotValidation_happyPath() throws Exception {
        ROWSET rowset = TiedonSiirtoTstUtils.createSampleRowsetHenkilotiedot();
        getServiceProxy().importHenkilotiedot(rowset);
    }

    @Test(expected = SOAPFaultException.class)
    public void importHenkilotiedotValidation_missingMandatoryElement() throws Exception {
        ROWSET rowset = TiedonSiirtoTstUtils.createSampleRowsetHenkilotiedot();
        rowset.getROW().get(0).setKANSALAISUUS(null); // pakollinen -> exception
        getServiceProxy().importHenkilotiedot(rowset);
    }

    @Test(expected = SOAPFaultException.class)
    public void importHenkilotiedotValidation_invalidValueInEnumeration() throws Exception {
        ROWSET rowset = TiedonSiirtoTstUtils.createSampleRowsetHenkilotiedot();
        rowset.getROW().get(0).setPOSTINUMERO("12345"); // tämmöstä ei oo koodistossa -> exception
        getServiceProxy().importHenkilotiedot(rowset);
    }

    @Test
    public void importArvosanatValidation_happyPath() throws Exception {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET arvosanaRowset = TiedonSiirtoTstUtils.createSampleRowsetArvosanat();
        getServiceProxy().importArvosanat(arvosanaRowset);
    }

    @Test(expected = SOAPFaultException.class)
    public void importArvosanatValidation_validationFails() throws Exception {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET arvosanaRowset = TiedonSiirtoTstUtils.createSampleRowsetArvosanat();
        arvosanaRowset.getROW().get(0).setHETU("111193 - 111V");
        getServiceProxy().importArvosanat(arvosanaRowset);
    }

    @Test
    public void exportHakijat_happyPath() throws Exception {
        HakijatRequestParametersType req = new HakijatRequestParametersType();
        req.setOppilaitosnumero("06451");
        Hakijat hakijat = getServiceProxy().exportHakijat(req);
        Assert.assertEquals(new Float(7.58), hakijat.getHakija().get(0).getHakemus().getLukiontasapisteet());
        Assert.assertEquals("Lagmans skola", hakijat.getHakija().get(0).getHakemus().getHakutoiveet().getHakutoive().get(0).getOpetuspisteennimi());
    }

    @BeforeClass
    public static void start() throws Exception {
        server = JettyTstUtils.startJettyWithCxf(port, "application-context.xml");
    }

    @AfterClass
    public static void stop() throws Exception {
        JettyTstUtils.stop(server);
    }

    private TiedonSiirtoToinenAsteService getServiceProxy() {
        return getProxy(TiedonSiirtoToinenAsteService.class, "http://localhost:"+port+"/cxf/services/TiedonSiirtoToinenAsteService");
    }

    private <T> T getProxy(final Class<T> type, final String url) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(type);
        factory.setAddress(url);
        return (T) factory.create();
    }

}