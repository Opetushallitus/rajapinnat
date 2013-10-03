package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.TiedonSiirtoToinenAsteService;
import fi.vm.sade.henkilo.service.TiedonSiirtoToinenAsteService_Service;
import fi.vm.sade.henkilo.service.types.perusopetus.ROWSET;
import fi.vm.sade.koodisto.service.types.koodisto.Kausi;
import org.apache.cxf.jaxws.JaxWsClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mortbay.jetty.Server;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

import java.math.BigInteger;

import static fi.vm.sade.henkilo.service.types.perusopetus.ROWSET.ROW;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoWsdlTest {

    private static int port = (int) (7000 + Math.random() * 1000);  // port 7000-7999
    private static Server server;

    @Test
    public void importHenkilotiedotValidation_happyPath() throws Exception {
        ROWSET rowset = createSampleRowset();
        getServiceProxy().importHenkilotiedot(rowset);
    }

    @Test(expected = SOAPFaultException.class)
    public void importHenkilotiedotValidation_missingMandatoryElement() throws Exception {
        ROWSET rowset = createSampleRowset();
        rowset.getROW().get(0).setKANSALAISUUS(null); // pakollinen -> exception
        getServiceProxy().importHenkilotiedot(rowset);
    }

    @Test(expected = SOAPFaultException.class)
    public void importHenkilotiedotValidation_invalidValueInEnumeration() throws Exception {
        ROWSET rowset = createSampleRowset();
        rowset.getROW().get(0).setPOSTINUMERO("12345"); // tämmöstä ei oo koodistossa -> exception
        getServiceProxy().importHenkilotiedot(rowset);
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

    private ROWSET createSampleRowset() {
        ROWSET rowset = new ROWSET();
        ROW row = new ROW();
        row.setVUOSI(BigInteger.valueOf(2013));
        row.setKAUSI(Kausi.S);
        row.setLAHTOKOULU("05536");
        row.setPOHJAKOULUTUS("1");
        row.setOPETUSKIELI("FI");
        row.setLUOKKA("9A");
        row.setLUOKKATASO(BigInteger.valueOf(10));
        row.setHETU("111193-111V");
        row.setSUKUPUOLI("1");
        row.setSUKUNIMI("Virtanen");
        row.setETUNIMET("Ville Valtteri");
        row.setKUTSUMANIMI("Valtteri");
        row.setKOTIKUNTA("240");
        row.setAIDINKIELI("FI");
        row.setKANSALAISUUS("246");
        row.setLAHIOSOITE("Kaduntie 156");
        row.setPOSTINUMERO("20520");
        row.setMAA("246");
        row.setMATKAPUHELIN("040 1234567");
        row.setMUUPUHELIN("5278091");
        row.setERA("PKERA1_2013S_05536");
        rowset.getROW().add(row);
        return rowset;
    }

    private <T> T getProxy(final Class<T> type, final String url) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(type);
        factory.setAddress(url);
        return (T) factory.create();
    }

}