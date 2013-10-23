package fi.vm.sade.tiedonsiirto.toinenaste;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoXmlSchemaValidationTest {

    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    String sampleDir = "src/main/resources/toinenaste/";
    String schemaDir = "src/main/resources/toinenaste/";

    public static void main(String[] args) throws IOException {
        // temp lataa tuoreet skeemat koodistosta sorsien alle
        String koodistos[] = new String[]{"oppilaitosnumero","posti","maatjavaltiot2","kunta","sukupuoli","kieli","kausi","pohjakoulutustoinenaste","opetuspisteet","hakukohteet",};
        for (String koodisto : koodistos) {
            FileUtils.copyURLToFile(new URL("https://itest-virkailija.oph.ware.fi/koodisto-service/rest/"+koodisto+".xsd"), new File("C:\\work\\GitHub\\rajapinnat\\tiedonsiirto\\src\\main\\webapp\\toinenaste\\koodisto\\"+koodisto+".xsd"));
            System.out.println("copied "+koodisto);
        }
    }

    @Test
    public void hakijat() throws IOException, SAXException {
        validate("hakijat");
    }

    @Test
    public void perusopetusHenkiloTiedot() throws IOException, SAXException {
        validate("perusopetusHenkiloTiedot");
    }

    @Test
    public void perusopetusArvosanat() throws IOException, SAXException {
        validate("perusopetusArvosanat");
    }

    private void validate(final String what) throws SAXException, IOException {
        Source xmlFile = new StreamSource(new File(sampleDir + what + ".xml"));
        Schema schema = schemaFactory.newSchema(new File(schemaDir + what + ".xsd"));
        Validator validator = schema.newValidator();
        validator.validate(xmlFile);
    }


}
