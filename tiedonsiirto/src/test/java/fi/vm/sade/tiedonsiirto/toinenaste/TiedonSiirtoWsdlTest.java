package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.TiedonSiirtoToinenAsteService;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.Hakijat;
import fi.vm.sade.henkilo.service.types.perusopetus.hakijat.HakijatRequestParametersType;
import fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET;
import fi.vm.sade.koodisto.service.types.koodisto.Kausi;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;

import javax.xml.ws.soap.SOAPFaultException;
import java.math.BigInteger;

import static fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET.ROW;

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

    @Test
    public void importArvosanatValidation_happyPath() throws Exception {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET arvosanaRowset = createSampleRowsetArvosanat();
        getServiceProxy().importArvosanat(arvosanaRowset);
    }

    @Test(expected = SOAPFaultException.class)
    public void importArvosanatValidation_validationFails() throws Exception {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET arvosanaRowset = createSampleRowsetArvosanat();
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

    private fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET createSampleRowsetArvosanat() {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET arvosanaRowset = new fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET();
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW arvosanaRow = new fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW();
        arvosanaRow.setVUOSI(BigInteger.valueOf(2013));
        arvosanaRow.setKAUSI(Kausi.S);
        arvosanaRow.setLAHTOKOULU("05536");
        arvosanaRow.setLUOKKA("9A");
        arvosanaRow.setLUOKKATASO(BigInteger.valueOf(9));
        arvosanaRow.setHETU("111193-111V");
        arvosanaRow.setTODISTUS(BigInteger.valueOf(1));
        arvosanaRow.setAINE("A1");
        arvosanaRow.setKIELI("FI");
        arvosanaRow.setTYYPPI("B");
        arvosanaRow.setARVOSANA(10);
        arvosanaRow.setERA("PKERA3_2013S_05536");
        arvosanaRowset.getROW().add(arvosanaRow);
        return arvosanaRowset;
    }

    private <T> T getProxy(final Class<T> type, final String url) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(type);
        factory.setAddress(url);
        return (T) factory.create();
    }

}