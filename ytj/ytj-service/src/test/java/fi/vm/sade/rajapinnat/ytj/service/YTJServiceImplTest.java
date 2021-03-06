/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.vm.sade.rajapinnat.ytj.service;

import fi.vm.sade.rajapinnat.ytj.api.YTJDTO;
import fi.vm.sade.rajapinnat.ytj.api.YTJKieli;
import fi.vm.sade.rajapinnat.ytj.api.exception.YtjConnectionException;
import fi.ytj.Kieli;

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 *
 * @author Tuomas Katva
 */
public class YTJServiceImplTest {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public YTJServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testHashHex() throws Exception {
        String str = "Str to be hashed";
        YTJServiceImpl instance = new YTJServiceImpl();
        String hex = instance.createHashHex(str);
        assertTrue(hex != null);
    }
    /**
     * Test of findByYNimi method, of class YTJServiceImpl.
     * 
     * Both of these tests must fail because no client id or secret key has
     * been given
     */
    @Test(expected = YtjConnectionException.class)
    public void testFindByYNimiWithoutCredentialsFails() throws Exception {
        String nimi = "Helsingin";
        boolean naytaPassiiviset = false;
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
        List result = instance.findByYNimi(nimi, naytaPassiiviset, kieli);
    }

    /*Ignore this test so that YTJ is not called every time when project is compiled
     * This test call YTJ-service with test credentials, note that
     * credentials have maximum daily call limit so test is ignored by default
     */
    @Ignore
    @Test
    public void testFindByNimiSuccess() {
        String nimi = "Katva";
        boolean naytaPassiiviset = false;
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
        instance.setAsiakastunnus("XX");
        instance.setSalainenavain("XX");
        try {
            List<YTJDTO> results = instance.findByYNimi(nimi,naytaPassiiviset,kieli);
            log.info("Got {} results", results.size());
            /*for (YTJDTO dto:results) {
                log.info("Organisaatio nimi: {}",dto.getNimi());
            }*/
            assertTrue(results.size() > 0);
        } catch (Exception exp) {
            log.info("Exception in findByNimiSuccess : {}",exp.toString());
            assertTrue(false);
        }
    }

    @Test(expected = YtjConnectionException.class)
    public void testFindByYTunnusBatchWithoutCredentialsFails() throws Exception {
        List<String> ytunnus = new ArrayList<String>(){{add("1111111-1");}};
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
        List<YTJDTO> result = instance.findByYTunnusBatch(ytunnus, kieli);
        assertEquals("Diibadaa", result.get(0).getNimi().trim()); //shouldn't reach this point
    }

    // Test of findByYTunnusBatch method, of class YTJServiceImpl
    @Ignore
    @Test
    public void testFindByYTunnusBatchSuccess() throws Exception {
        List<String> ytunnus = new ArrayList<String>(){{add("0313471-7");add("0201256-6");add("2189312-7");}};
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
        instance.setAsiakastunnus("XX");
        instance.setSalainenavain("XX");
        try {
            List<YTJDTO> result = instance.findByYTunnusBatch(ytunnus, kieli);
            assertEquals("Helsingin yliopisto".toLowerCase(), result.get(0).getNimi().trim().toLowerCase());
            assertEquals("Helsingin kaupunki".toLowerCase(), result.get(1).getNimi().trim().toLowerCase());
            assertEquals("Mikkelin Ammattikorkeakoulu Oy".toLowerCase(), result.get(2).getNimi().trim().toLowerCase());
        } catch (Exception exp) {
            log.info("Exception in findByYTunnus : {}",exp.toString());
            assertTrue(false);
        }
    }


    /**
     * Test of findByYTunnus method, of class YTJServiceImpl.
     */
    @Test(expected = YtjConnectionException.class)
    public void testFindByYTunnusWithoutCredentialsFails() throws Exception {
        String ytunnus = "1111111-1";
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
        YTJDTO result = instance.findByYTunnus(ytunnus, kieli);
        assertEquals("Diibadaa", result.getNimi().trim()); //shouldn't reach this point
    }

    /*Ignore this test so that YTJ is not called every time when project is compiled
     * This test call YTJ-service with test credentials, note that
     * credentials have maximum daily call limit so test is ignored by default
     */
    @Ignore
    @Test
    public void testFindByYTunnusSuccess() throws Exception {
        String ytunnus = "0313471-7"; // Helsingin yliopisto
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
        instance.setAsiakastunnus("XX");
        instance.setSalainenavain("XX");
        try {
            YTJDTO result = instance.findByYTunnus(ytunnus, kieli);
            assertEquals("Helsingin yliopisto".toLowerCase(), result.getNimi().trim().toLowerCase());
        } catch (Exception exp) {
            log.info("Exception in findByYTunnus : {}",exp.toString());
            assertTrue(false);
        }
    }

}
