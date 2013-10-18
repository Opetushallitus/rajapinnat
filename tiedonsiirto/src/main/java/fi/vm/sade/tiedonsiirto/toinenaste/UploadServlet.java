package fi.vm.sade.tiedonsiirto.toinenaste;

import com.google.gson.Gson;
import fi.vm.sade.henkilo.service.types.perusopetus.GenericResponse;
import fi.vm.sade.henkilo.service.types.perusopetus.arvosanat.ROWSET;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antti Salonen
 */
@WebServlet(urlPatterns = {"/upload"}, loadOnStartup = 1)
public class UploadServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UploadServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldname = item.getFieldName();
                    String fieldvalue = item.getString();
                    // ... (do your job here)
                } else {
                    // Process form file field (input type="file").
                    String fieldname = item.getFieldName();
                    String filename = FilenameUtils.getName(item.getName());
                    //InputStream xmlfilecontent = item.getInputStream();
                    //String xmlContent = IOUtils.toString(xmlfilecontent);
                    if (log.isDebugEnabled()) {
                        log.debug("content: "+IOUtils.toString(item.getInputStream()));
                    }
                    // ... (do your job here)

                    // todo: siirrä logiikka serviceen

                    // validate xml
                    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//                    Source xmlSource = new StreamSource(xmlContent);
                    Source xmlSource = new StreamSource(item.getInputStream());
                    //Source xsdSource = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("toinenaste/perusopetusArvosanat.xsd"));
//                    Schema schema = schemaFactory.newSchema(xsdSource);
                    String schemaPath = getServletContext().getRealPath("/WEB-INF/classes/toinenaste/perusopetusArvosanat.xsd");
                    log.info("schemaPath: "+schemaPath);
                    Schema schema = schemaFactory.newSchema(new File(schemaPath));
                    Validator validator = schema.newValidator();
                    validator.validate(xmlSource);

                    // unmarshal xml to object
                    JAXBContext jc = JAXBContext.newInstance("fi.vm.sade.henkilo.service.types.perusopetus.arvosanat");
                    Unmarshaller um = jc.createUnmarshaller();
                    ROWSET arvosanaRowset = (ROWSET) um.unmarshal(new StreamSource(item.getInputStream()));
                    log.info("ROWSET.ROWS: "+arvosanaRowset.getROW().size());

                    // call tiedonsiirto service
                    //TiedonSiirtoToinenAsteServiceImpl service = WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(TiedonSiirtoToinenAsteServiceImpl.class);
                    // todo: eihän meillä oo koko spring app kontekstia vielä eli tämä on erittäin temp
                    TiedonSiirtoToinenAsteServiceImpl service = new TiedonSiirtoToinenAsteServiceImpl();
                    service.setTarjontaIntegration(new TiedonSiirtoToinenAsteTarjontaIntegration(){
                        @Override
                        public String findKomotoFromTarjonta(ROWSET.ROW arvosana) throws Exception {
                            return "Peruskoulu, "+arvosana.getLUOKKATASO()+"-luokka, "+arvosana.getAINE();
                        }
                    });
                    service.setHenkiloIntegration(new TiedonSiirtoToinenAsteHenkiloIntegration() {
                        @Override
                        public String findHenkiloOidFromAuthenticationService(String hetu) throws Exception {
                            return hetu;
                        }
                    });
                    service.setSuoritusrekisteriIntegration(new TiedonSiirtoToinenAsteSuoritusrekisteriIntegration(){
                        @Override
                        public void importArvosanaToSuoritusRekisteri(String henkiloOid, String komotoOid, ROWSET.ROW row) {
                            // transform to suoritus json
                            Map<String,Object> suoritus = new HashMap<String, Object>();
                            suoritus.put("henkiloOid",henkiloOid);
                            suoritus.put("arvosana",row.getARVOSANA());
                            suoritus.put("komotoOid",komotoOid);
                            suoritus.put("oppilaitos",row.getLAHTOKOULU());
                            String suoritusJson = new Gson().toJson(suoritus);
                            log.info("import suoritus to suoritusrekisteri, json: "+suoritusJson);

                            // post to suoritusrekisteri
                            RestTemplate restTemplate = new RestTemplate();
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
                                @Override
                                public void handleError(ClientHttpResponse response) throws IOException {
                                    throw new IOException("error importing to suoritusrekisteri, response body:\n"+ IOUtils.toString(response.getBody()));
                                }
                            });
                            URI suoritusUri = restTemplate.postForLocation("http://localhost:8080/suoritusrekisteri/suoritukset", new HttpEntity<String>(suoritusJson,headers));
                            log.info("import suoritus to suoritusrekisteri OK: "+suoritusUri);
                        }
                    });
                    GenericResponse result = service.importArvosanat(arvosanaRowset);
                    response.setContentType("application/json");
                    response.getWriter().print(new Gson().toJson(result));
                }
            }
        } catch (Exception e) {
            throw new ServletException("error in upload", e);
        }
    }

}
