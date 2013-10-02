package fi.vm.sade.tiedonsiirto.toinenaste;

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

/**
 * @author Antti Salonen
 */
public class TiedonSiirtoValidationTest {

    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    String sampleDir = "src/main/webapp/toinenaste/";
    String schemaDir = "src/main/webapp/toinenaste/";

    @Test
    public void valitut() throws IOException, SAXException {
        validate("valitut");
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
