package fi.vm.sade.rajapinnat.vtj.service.impl;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.Huollettava;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.VtjTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class VtjTestDataImpl implements VtjTestData{

    private static Random rand = new Random();
    private static int counter = 0;
    private static final List<YksiloityHenkilo> testData = new ArrayList<YksiloityHenkilo>();
    static {
        //hetu, etunimet, kutsumanimi, sukunimi, sukupuoli, random, sähköposti, katuosoiteS, katuosoiteR, postinumero, kaupunkiS, kaupunkiR, maaS, maaR
        testData.add(createNewYH("010156-9994", "Tobias Nikolas",              null,       "Siltanen",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010547-9991", "Milo Ingvald Rami",           null,       "Reponen",           "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010661-999E", "Veeti Valtteri",              null,       "Seppä",             "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010673-998H", "Meiju Sanna-Maria Taika",     null,       "Junnila",           "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010745-9983", "Claudia Heidi",               null,       "Gustafsson",        "2", false, null, "Mannerheimintie 45 A 12", "Mannerheiminväg  45 A 12", "00101", "Helsinki", "Helsingfors", "Suomi", "Finland", "246"));
        testData.add(createNewYH("010849-999Y", "Joel Niila",                  null,       "Ahonen",            "1", false, null, "Mannerheimintie 45 A 22", null, "00100", "Helsinki", null, "Suomi", null, "246"));
        testData.add(createNewYH("010873-9973", "Felix Jonathan",              null,       "Haapakoski",        "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010957-998T", "Vickan",                      null,       "Tuulispää",         "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010973-999Y", "Kaija",                       null,       "Tuulispää",         "1", false, null, "Mannerheimintie 45 A 22", "Mannerheiminväg  45 A 22", "00101", "Helsinki", "Helsingfors", "Suomi", "Finland", "246"));
        testData.add(createNewYH("010973-999Y", "Joni Kaj",                    null,       "Hiltunen",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("011066-998X", "Maija Evi",                   null,       "Puumalainen",       "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("011073-998R", "Silja",                       null,       "Loikkanen",         "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("020637-998V", "Jane Kukka-Maaria",           null,       "Putkonen",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("030668-998H", "Aliisa Lumia",                "Lumia",    "Pietikäinen",       "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("031042-9981", "Isabelle Riia",               null,       "Kivimäki",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("031173-999V", "Ismo",                        null,       "Neuvonen",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("031251-9990", "Mohamed Mohamed Mustafa",     null,       "Lindroos",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("040779-998T", "Pihla Kukka-Maria",           null,       "Haapakoski",        "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("040946-999X", "Simeon Samuli",               null,       "Aarnio",            "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050654-9997", "Helge Abel",                  null,       "Kiviniemi",         "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("080104A9997", "Eric Folke",                  null,       "Männikkö",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("130139-998E", "Ulla Rea",                    null,       "Pekkala",           "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("140977-998B", "Svetlana Päivi",              "Päivi",     "Männikkö",         "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("170479-999T", "Hans",                        null,       "Ollila",            "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("201100A9990", "Elias Kristian",              null,       "Männikkö",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("251102A996M", "Eriika Mailis",               null,       "Männikkö",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("260991-999R", "Ilmari Folke",                null,       "Huhtiistinen",      "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("261148-9987", "Sanelma Alice",               null,       "Ruuska Malila",     "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("270569-998E", "Kielo Janica",                null,       "Pietikäinen",       "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("040188-999A", "Ali Julian Fjalar",           null,       "Marttila",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050351-9980", "Maarita Alicia",              null,       "Holmberg",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("061191-998D", "Aliisa Maiju Rebecca",        null,       "Kujala",            "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("070356-999L", "Mikke Samuli",                null,       "Laaksonen",         "1", false, null, "Mannerheimintie 45A 33", "Mannerheiminväg 45 A 33", "00100", "Helsinki", "Helsingforss", "Suomi", "Finland", "246"));
        testData.add(createNewYH("160279-999J", "Matti Eemeli",                "Eemeli",    "Meikäläinen",      "1", false, null, "Mannerheimintie 45A 44", null, "00100", "Helsinki", null, "Suomi", null, "246"));
        testData.add(createNewYH("160498-9992", "Vili Jimmy",                  null,       "Saukkonen",         "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("171047-999C", "Allan Kent",                  null,       "Minkkinen",         "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("191148-999R", "Dmitri Jonathan",             "Jonathan",  "Kataja",           "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("200470-998C", "Nella Pihla-Roosa",           null,       "Junnila",           "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("201171-999A", "Elmo",                        null,       "Andersson",         "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("220576-9992", "Ali Allan",                   null,       "Holopainen",        "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("230197-999W", "Gabriel Jalo Päiviö",         null,       "Pekkala",           "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("101019-998L", "Beatrice Kukka-Maaria",       null,       "Hirvelä",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("290154-999N", "Erkki Taavetti Valtteri",     null,       "Hyvönen",           "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("301275-998K", "Sulevi Kukka-Maaria Maiju",   null,       "Männikkö",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("250589-999B", "Denis Usko",                  null,       "Järveläinen",       "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050369-999N", "Jalmari Elof Pontus",         null,       "Setälä",            "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("020894-9986", "Agneta Kukka-Maaria",         null,       "Saukkonen",         "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("291281-999H", "Dennis Kaj",                  null,       "Koljonen",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150151-998U", "Maj Evi",                     null,       "Lindell",           "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("251199-999S", "Eric Andrew",                 "Andrew",    "Pettersson",      "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("250678-999A", "Waldemar Olli-Pekka Andres ", "Andres",    "Haanpää",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("181003A999N", "Ronny Bengt Thomas",          "Bengt",     "Setälä",           "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150478-9987", "Minja Ella Nenna",            "Ella",      "Kolari",           "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("081078-999K", "Holger Raineri Kaino",        "Raineri",   "Soikkeli",        "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050815-998R", "Lemmikki Alise",             "Alise",     "Ronkainen",         "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("041248-999V", "Bengt Valtteri",              "Valtteri",  "Koskela",         "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("230896-9988", "Sofi Ira Kukka-Maaria",       "Ira",       "Lyytinen",        "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("270474-999Y", "Robert Yrjö",                 "Yrjö",      "Hannula",         "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("160582-998P", "Lahja Nicole",                "Nicole",    "Kontkanen ",       "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("110360-9986", "Maija Aniitta",               "Aniitta",   "Hurskainen",       "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("271062-999P", "Pekka Joel",                 "Joel",      "Rautiainen ",       "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("240984-999M", "Rikhard Niila",               "Niila",     "Huusko",           "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("130486-9980", "Linda Neea",                 "Neea",      "Kuusinen",          "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("090369-9998", "Lenni Håkan",                 "Håkan",     "Kaukonen",         "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("090271-998X", "Meiju Maiju",                 "Maiju",     "Haapakoski",      "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010449-998P", "Sonja Agneta/Aune Kukka",     null,       "von Braun de Karttunen", "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010522-998J", "Elina Kukka-Maaria",          null,       "Rossi",             "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150894-9877", "Teppo",                       "Teppo",    "Testaaja",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150894-967K", "Seppo",                       "Seppo",    "Testaaja",          "1", false, null, null, null, null, null, null, null, null, "246"));
        //Vetuma
        testData.add(createNewYH("010101-123N", "Teemu",                       "Teemu",    "Testaaja",          "1", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("210281-9988", "NORDEA",                      "NORDEA",   "DEMO",              "2", false,null, null, null, null, null, null, null, null, "246", asList(huollettava("Pekka", "Demo", "190306A9850"), huollettava("Liisa", "Demo", "190308A948A"))));
        testData.add(createNewYH("081181-9984", "ANNA",                        "ANNA",     "TESTI",             "2", false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010170-960F", "Maija",                       "Maija",    "Meikäläinen",       "2", false, null, null, null, null, null, null, null, null, "246"));
        //VTJ-update-tests static
        testData.add(createNewYH("020260-909P", "Matti",                       "Matti",    "Testinen",          "1", false, "matti.meikalainen@fromvtj.oph", "Keskikatu 100", "Central gatan 100", "98765", "Pohjanmaa", "Österbotnia", "Suomi", "Finland", "246"));
        testData.add(createNewYH("020260-9833", "Ville",                       null,       "Meikäläinen",       "1", false, "ville.meikalainen@fromvtj.oph", "Uusikatu 100", "Nya gatan 100", "12345", "Auramaa", "Åraland", "Suomi", "Finland", "246"));
        testData.add(createNewYH("020260-941R", "Teppo Seppo",                 "Teppo",    "Meikäläinen",       "1", false, "seppo.meikalainen@fromvtj.oph", "Vanhakatu 100", "Gamla gatan 100", "54321", "Kymimaa", "Kymilandet", "Suomi", "Finland", "246"));
        testData.add(createNewYH("020260-961C", "Matti Seppo",                 "Seppo",    "Matikainen",        "1", false, "seppo.matikainen@fromvtj.oph", "Vanhakatu 200", "Gamla gatan 200", "56321", "Hämemaa", "Hämelandet", "Suomi", "Finland", "246"));
    }

    private static String[] changingDataHetus = {"010150-969L", "010150-913T", "010150-979X"};
    private static String[] etunimet = {"Olavi Uolevi", "Eetu Aatu"};
    private static String[] sukunimet = {"Hakkarainen", "Tikkanen"};
    private static String[] katuosoitteet = {"Vanhakatu 1", "Keskikatu 2", "Uusikatu 3"};
    private static String[] kaupungit = {"Hesa", "Manse", "Suomen Chicago"};
    
    public YksiloityHenkilo teeHakuTestidatasta(String hetu) {
        for (YksiloityHenkilo yh : testData) {
            if (yh.getHetu().equalsIgnoreCase(hetu)) {
                YksiloityHenkilo result = new YksiloityHenkilo();
                result.setEtunimi(yh.getEtunimi());
                result.setSukunimi(yh.getSukunimi());
                result.setKutsumanimi(yh.getKutsumanimi());
                result.setHetu(yh.getHetu());
                result.setSukupuoli(yh.getSukupuoli());
                result.setSahkoposti(yh.getSahkoposti());
                if (yh.getOsoitteet() != null) {
                    for (YksiloityHenkilo.OsoiteTieto osoite : yh.getOsoitteet()) {
                        result.addOsoiteTieto(osoite);
                    }
                }
                for(String kansalaisuusKoodi : yh.getKansalaisuusKoodit()) {
                    result.addKansalaisuusKoodi(kansalaisuusKoodi);
                }
                result.setHuollettavat(yh.getHuollettavat());
                return result;
            }
        }
        for(String changingDatahetu : changingDataHetus) {
            if (hetu.matches(changingDatahetu)) {
                return createNewYH(hetu, null, null, null, "1", true, null, null, null, null, null, null, "Suomi", "Finland", "246");
            }
        }
        // Vastauksena pitää aina löytyä henkilö tai palauttaa virhe!!
        throw new NotFoundException("Could not find person.");
    }

    private static YksiloityHenkilo createNewYH(String hetu,
                                          String etunimi,
                                          String kutsumanimi,
                                          String sukunimi,
                                          String sukupuoli,
                                          boolean random,
                                          String sahkoposti,
                                          //Osoitetieto
                                          String katuosoiteS,
                                          String katuosoiteR,
                                          String postinumero,
                                          String kaupunkiS,
                                          String kaupunkiR,
                                          String maaS,
                                          String maaR,
                                          String kansalaisuusKoodi,
                                          List<Huollettava> huollettavat
                                          ) {
        YksiloityHenkilo yh = new YksiloityHenkilo();

        if (random) {
            //Names applied in a sequence to make things more predictable
            yh.setEtunimi(etunimet[counter % 2]);
            yh.setSukunimi(sukunimet[counter % 2]);
            counter++;
            yh.setKutsumanimi(yh.getEtunimi().split(" ")[randInt(0,1)]);
            yh.addOsoiteTieto(new YksiloityHenkilo.OsoiteTieto("yhteystietotyyppi4",
                                                 katuosoitteet[randInt(0,2)],
                                                 katuosoitteet[randInt(0,2)],
                                                 new Integer(randInt(10000, 20000)).toString(),
                                                 kaupungit[randInt(0,2)],
                                                 kaupungit[randInt(0,2)],
                                                 maaS,
                                                 maaR));
            yh.setSahkoposti((yh.getKutsumanimi() + "@fromvtj.oph").toLowerCase());
            yh.addKansalaisuusKoodi(kansalaisuusKoodi);
        }
        else {
            yh.setEtunimi(etunimi);
            yh.setSukunimi(sukunimi);
            yh.setKutsumanimi(kutsumanimi);
            yh.addKansalaisuusKoodi("246");
            if (katuosoiteS != null || postinumero != null || kaupunkiS != null || maaS != null ||
                    katuosoiteR != null || kaupunkiR != null || maaR != null) {
                yh.addOsoiteTieto(new YksiloityHenkilo.OsoiteTieto("yhteystietotyyppi4",
                                                      katuosoiteS,
                                                      katuosoiteR,
                                                      postinumero,
                                                      kaupunkiS,
                                                      kaupunkiR,
                                                      maaS,
                                                      maaR));
            }
            yh.setSahkoposti(sahkoposti);
            yh.setHuollettavat(huollettavat);
        }
        yh.setHetu(hetu);
        yh.setSukupuoli(sukupuoli);
        return yh;
    }

    private static YksiloityHenkilo createNewYH(String hetu,
                                                String etunimi,
                                                String kutsumanimi,
                                                String sukunimi,
                                                String sukupuoli,
                                                boolean random,
                                                String sahkoposti,
                                                //Osoitetieto
                                                String katuosoiteS,
                                                String katuosoiteR,
                                                String postinumero,
                                                String kaupunkiS,
                                                String kaupunkiR,
                                                String maaS,
                                                String maaR,
                                                String kansalaisuusKoodi
    ) {
        return createNewYH(hetu, etunimi, kutsumanimi, sukunimi, sukupuoli, random, sahkoposti, katuosoiteS, katuosoiteR, postinumero, kaupunkiS, kaupunkiR, maaS, maaR, kansalaisuusKoodi, new ArrayList<>());
    }


    private static Huollettava huollettava(String etunimet, String sukunimi, String hetu) {
        return new Huollettava(etunimet, sukunimi, hetu);
    }

    private static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
