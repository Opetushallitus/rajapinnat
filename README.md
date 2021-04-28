# HUOM! OBS! ATTN! #

---

TÄMÄ REPO EI OLE ENÄÄ KÄYTÖSSÄ.

VTJ-rajapinta on siirretty [omaan repoonsa](https://github.com/Opetushallitus/vtj). YTJ-kirjasto puolestaan on nyt osa [organisaatiopalvelua](https://github.com/Opetushallitus/organisaatio/). Aikoinaan rajapinnat-repoon kuulunut Kela-service on poistunut käytöstä.

---

# rajapinnat #

Tämän alla rajapintoja ulkoisien järjestelmien ja opintopolun palveluiden välille

## vtj ##

Hetu-hakuihin käytetty rajapinta.
Ajaminen lokaalisti:
 * kopioi (tai linkitä) `DEV-security-context-backend.xml` ja `DEV-vtj-service.properties` (ilman DEV-etuliitettä) hakemistoon
 `$HOME/oph-configuration`
 * käännä koodi: `mvn install`
 * käynnistä palvelu ajamalla `vtj/vtj-service` -hakemistossa:
 `mvn org.codehaus.cargo:cargo-maven2-plugin:run`

Palvelu käynnistyy oletusarvoisesti porttiin 8081, ja hetu-hakuja voi tehdä osoitteeseen:
`http://localhost:8081/vtj-service/resources/vtj/<hetu>`. Testidata löytyy luokasta `VtjTestDataImpl`.
Testitunnuksen löytyvät tiedostosta `DEV-security-context-backend.xml`.
 
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
