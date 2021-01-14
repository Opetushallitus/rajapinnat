package fi.vm.sade.rajapinnat.vtj.service.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VtjTestDataImplTest {

    @Test
    public void kutsumanimiYhdestaEtunimesta() {
        String etunimet = "Eetu";
        String kutsumanimi = VtjTestDataImpl.kutsumanimi(etunimet);
        assertEquals(etunimet, kutsumanimi);
    }

    @Test
    public void kutsumanimiMonestaEtunimesta() {
        String etunimet = "Eetu Erkki Etunimi";
        String kutsumanimi = VtjTestDataImpl.kutsumanimi(etunimet);
        assertEquals("Eetu", kutsumanimi);
    }

    @Test
    public void sukupuoliMiehenHetusta() {
        String hetu = "090670-999X";
        String sukupuoli = VtjTestDataImpl.sukupuoli(hetu);
        assertEquals("1", sukupuoli);
    }

    @Test
    public void sukupuoliNaisenHetusta() {
        String hetu = "220202A998H";
        String sukupuoli = VtjTestDataImpl.sukupuoli(hetu);
        assertEquals("2", sukupuoli);
    }

    @Test
    public void sukupuoliMaarittelemattomastaHetusta() {
        String hetu = "";
        String sukupuoli = VtjTestDataImpl.sukupuoli(hetu);
        assertEquals("", sukupuoli);
    }
}
