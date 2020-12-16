# rajapinnat #

Tämän alla rajapintoja ulkoisien järjestelmien ja opintopolun palveluiden välille

## vtj ##

Hetu-hakuihin käytetty rajapinta.
Ajaminen lokaalisti:
 * kopioi `DEV-security-context-backend.xml` ja `DEV-vtj-service.properties` (ilman DEV-etuliitettä) hakemistoon
 `$HOME/oph-configuration`
 * käynnistä palvelu ajamalla `vtj/vtj-service` -hakemistossa:
 `mvn install org.codehaus.cargo:cargo-maven2-plugin:run`

Palvelu käynnistyy oletusarvoisesti porttiin 8081, ja hetu-hakuja voi tehdä osoitteeseen:
`http://localhost:8081/vtj-service/resources/vtj/<hetu>`. Testidata löytyy luokasta `VtjTestDataImpl`.
 
## ytj ##

## tiedonsiirto ##

Koostepalvelu seuraaville toisen asteen massatiedonsiirroille:
(ohessa myös palvelut, joita ko yhteydessä tarvitaan)

 * Henkilötiedojen import (peruskoulun päättäneet)
    * (henkilo -palvelu)
 * Arvosanojen import (peruskoulun päättäneet)
    * (henkilo / tarjonta / suoritusrekisteri -palvelut)
 * Hakijoiden export (2.aste)
    * (henkilo / haku -palvelut)

Huom! Myöhemmin joko a) moduuliin tulee muitakin tiedonsiirtoja, tai b) moduulin nimi pitää vaihtaa
