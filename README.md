# rajapinnat #

Tämän alla rajapintoja ulkoisien järjestelmien ja opintopolun palveluiden välille

## kela ##

## vtj ##

Hetu hakuihin käytetty rajapinta.
 * Käynnistä palvelu VtjServiceTomcatilla
 * configuroi oph-configuration hakemisto. (vtj.production.env=false rupeaa palauttamaan testi hetuilla tietoa.)
 
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
