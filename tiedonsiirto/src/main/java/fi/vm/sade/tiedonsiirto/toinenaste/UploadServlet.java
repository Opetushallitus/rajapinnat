package fi.vm.sade.tiedonsiirto.toinenaste;

import com.google.gson.Gson;
import fi.vm.sade.henkilo.service.GenericFault;
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
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
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
                if (item.isFormField()) { // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    if (log.isDebugEnabled()) {
                        log.debug("field: " + item.getFieldName() + ", value: " + item.getString());
                    }
                } else { // Process form file field (input type="file").
                    if (log.isDebugEnabled()) {
                        log.debug("field: " + item.getFieldName() + ", type: " + item.getContentType() + ", filename: " + FilenameUtils.getName(item.getName()) + ", content: " + IOUtils.toString(item.getInputStream()));
                    }

                    if ("arvosanatXml".equals(item.getFieldName())) {
                        respondJson(response, getTiedonSiirtoToinenAsteService().validateAndImportArvosanatXml(item.getInputStream()));
                    } else if ("henkilotiedotXml".equals(item.getFieldName())) {
                        respondJson(response, getTiedonSiirtoToinenAsteService().validateAndImportHenkilotiedotXml(item.getInputStream()));
                    } else {
                        throw new ServletException("invalid upload, field '"+item.getFieldName()+"' not supported");
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException("error in upload", e);
        }
    }

    private void respondJson(HttpServletResponse response, GenericResponse result) throws IOException, SAXException, JAXBException, GenericFault {
        response.setContentType("application/json");
        response.getWriter().print(new Gson().toJson(result));
    }

    private TiedonSiirtoToinenAsteServiceImpl getTiedonSiirtoToinenAsteService() {

        // todo: oikea service + integraatiot + app context

        TiedonSiirtoToinenAsteServiceImpl service = new TiedonSiirtoToinenAsteServiceImpl();
        service.setTarjontaIntegration(new TiedonSiirtoToinenAsteTarjontaIntegration() {
            @Override
            public String findKomotoFromTarjonta(ROWSET.ROW arvosana) throws Exception {
                return "Peruskoulu, " + arvosana.getLUOKKATASO() + "-luokka, " + arvosana.getAINE();
            }
        });
        service.setHenkiloIntegration(new TiedonSiirtoToinenAsteHenkiloIntegration() {
            @Override
            public String findHenkiloOidFromAuthenticationService(String hetu) throws Exception {
                return hetu;
            }
        });
        service.setSuoritusrekisteriIntegration(new TiedonSiirtoToinenAsteSuoritusrekisteriIntegration() {
            @Override
            public void importArvosanaToSuoritusRekisteri(String henkiloOid, String komotoOid, ROWSET.ROW row) {
                // transform to suoritus json
                Map<String, Object> suoritus = new HashMap<String, Object>();
                suoritus.put("henkiloOid", henkiloOid);
                suoritus.put("arvosana", row.getARVOSANA());
                suoritus.put("komotoOid", komotoOid);
                suoritus.put("oppilaitos", row.getLAHTOKOULU());
                String suoritusJson = new Gson().toJson(suoritus);
                log.info("import suoritus to suoritusrekisteri, json: " + suoritusJson);

                // post to suoritusrekisteri
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {
                        throw new IOException("error importing to suoritusrekisteri, response body:\n" + IOUtils.toString(response.getBody()));
                    }
                });
                URI suoritusUri = restTemplate.postForLocation("http://localhost:8080/suoritusrekisteri/suoritukset", new HttpEntity<String>(suoritusJson, headers));
                log.info("import suoritus to suoritusrekisteri OK: " + suoritusUri);
            }
        });
        return service;
    }

}
