package fi.vm.sade.rajapinnat.ytj.service;

import fi.vm.sade.rajapinnat.ytj.api.YTJDTO;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String yTunnus = "2255802-1";
        String hakuSana = "Proactum";
        
        YTJServiceImpl tst = new YTJServiceImpl();
       //YTJDTO ret =  tst.findByYTunnus(yTunnus);
       List<YTJDTO> rets = tst.findByYNimi(hakuSana); 
       for (YTJDTO ret : rets) {
       System.out.println("NIMI : " + ret.getNimi());
       }
    }
}
