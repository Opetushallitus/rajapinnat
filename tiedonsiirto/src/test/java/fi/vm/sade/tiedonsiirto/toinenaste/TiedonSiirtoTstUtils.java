package fi.vm.sade.tiedonsiirto.toinenaste;

import fi.vm.sade.henkilo.service.types.perusopetus.henkilotiedot.ROWSET;
import fi.vm.sade.koodisto.service.types.koodisto.Kausi;
import fi.vm.sade.koodisto.service.types.koodisto.Oppiaineetyleissivistava;

import java.math.BigInteger;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoTstUtils {
    public static ROWSET createSampleRowsetHenkilotiedot() {
        ROWSET rowset = new ROWSET();
        rowset.getROW().add(createSampleRowHlotiedot("111193-111V"));
        return rowset;
    }

    public static ROWSET.ROW createSampleRowHlotiedot(String hetu) {
        ROWSET.ROW row = new ROWSET.ROW();
        row.setVUOSI(BigInteger.valueOf(2013));
        row.setKAUSI(Kausi.S);
        row.setLAHTOKOULU("05536");
        row.setPOHJAKOULUTUS("1");
        row.setOPETUSKIELI("FI");
        row.setLUOKKA("9A");
        row.setLUOKKATASO(BigInteger.valueOf(10));
        row.setHETU(hetu);
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
        return row;
    }

    public static fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET createSampleRowsetArvosanat() {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET arvosanaRowset = new fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET();
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW arvosanaRow = createSampleRowArvosana();
        arvosanaRowset.getROW().add(arvosanaRow);
        return arvosanaRowset;
    }

    public static fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW createSampleRowArvosana(String hetu) {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW row = createSampleRowArvosana();
        row.setHETU(hetu);
        return row;
    }

    public static fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW createSampleRowArvosana() {
        fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW arvosanaRow = new fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET.ROW();
        arvosanaRow.setVUOSI(BigInteger.valueOf(2013));
        arvosanaRow.setKAUSI(Kausi.S);
        arvosanaRow.setLAHTOKOULU("05536");
        arvosanaRow.setLUOKKA("9A");
        arvosanaRow.setLUOKKATASO(BigInteger.valueOf(9));
        arvosanaRow.setHETU("111193-111V");
        arvosanaRow.setTODISTUS(BigInteger.valueOf(1));
        arvosanaRow.setAINE(Oppiaineetyleissivistava.A_1);
        arvosanaRow.setKIELI("FI");
        arvosanaRow.setTYYPPI("B");
        arvosanaRow.setARVOSANA(10);
        arvosanaRow.setERA("PKERA3_2013S_05536");
        return arvosanaRow;
    }
}
