package fi.vm.sade.rajapinnat.kela.formaatti;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import fi.vm.sade.rajapinnat.kela.tkuva.data.TKUVAYHVA;
import fi.vm.sade.rajapinnat.kela.tkuva.service.KelaFtpService;
import fi.vm.sade.rajapinnat.kela.tkuva.util.KelaUtil;

/**
 * 
 * @author Jussi Jartamo
 * 
 */
@Configuration
@ContextConfiguration(classes = KelaMockFtpTesti.class)
@ImportResource({ "classpath:kela-testi-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class KelaMockFtpTesti {

    @Autowired
    FileSystem fileSystem;

    @Autowired
    KelaFtpService ftpService;

    @Value("${kela.ftp.path}")
    String kelaFtpPath;

    @Test
    public void testaaSijoitteluntulostenlahetystaMockkiPalvelimelle() {
        TKUVAYHVA tietue = new TKUVAYHVA.Builder().setHenkilotunnus("010478123X").setEtunimet("Älfö Ölfär")
                .setOppilaitos("").setLinjakoodi("").setSukunimi("Åke åkersön").setPoimintapaivamaara(new Date())
                .setValintapaivamaara(new Date()).setLukuvuosi(new Date()).setAjankohtaSyksy(true).build();

        ftpService.lahetaTiedosto(KelaUtil.createTiedostoNimiYhva14(new Date()),
                new ByteArrayInputStream(tietue.toByteArray()));

        Assert.notEmpty(fileSystem.listFiles(kelaFtpPath),
                "Camel-ftptiedonsiirto epäonnistui faketiedostojärjestelmään!");
    }
}
