/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.vm.sade.rajapinnat.ytj.service;

import fi.vm.sade.rajapinnat.ytj.api.YTJDTO;
import fi.vm.sade.rajapinnat.ytj.api.YTJKieli;
import fi.vm.sade.rajapinnat.ytj.api.exception.YtjConnectionException;
import fi.ytj.Kieli;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Tuomas Katva
 */
public class YTJServiceImplTest {
    
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

    /**
     * Test of findByYNimi method, of class YTJServiceImpl.
     * 
     * Both of these tests must fail because no client id or secret key has
     * been given
     */
    @Test(expected = YtjConnectionException.class)
    public void testFindByYNimi() throws Exception {
        System.out.println("findByYNimi");
        String nimi = "Helsingin";
        boolean naytaPassiiviset = false;
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
        List result = instance.findByYNimi(nimi, naytaPassiiviset, kieli);
        
    }

    /**
     * Test of findByYTunnus method, of class YTJServiceImpl.
     */
    @Test(expected = YtjConnectionException.class)
    public void testFindByYTunnus() throws Exception {
        System.out.println("findByYTunnus");
        String ytunnus = "1111111-1";
        YTJKieli kieli = YTJKieli.FI;
        YTJServiceImpl instance = new YTJServiceImpl();
     
        YTJDTO result = instance.findByYTunnus(ytunnus, kieli);
        assertEquals("Diibadaa", result.getNimi().trim());
        
    }


  
}
