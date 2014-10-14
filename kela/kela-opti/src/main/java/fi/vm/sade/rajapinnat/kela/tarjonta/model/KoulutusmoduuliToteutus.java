/*
 * Copyright (c) 2012 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.rajapinnat.kela.tarjonta.model;

import javax.persistence.*;

/**
 * 
 * @author Markus
 */
@Entity
@Table(name="koulutusmoduuli_toteutus")
public class KoulutusmoduuliToteutus {
    
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue
    private Long id;
    
    @Column(name = "oid", unique=true)
    private String oid;
    @Column(name = "koulutus_uri")
    private String koulutusUri;
    @Column(name = "kandi_koulutus_uri")
    private String kandi_koulutus_uri;
    
    @Column(name = "alkamiskausi_uri")
    private String alkamiskausi_uri;
    @Column(name = "alkamisvuosi")
    private Integer alkamisvuosi;
    

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getKoulutusUri() {
		return koulutusUri;
	}

	public void setKoulutusUri(String koulutusUri) {
		this.koulutusUri = koulutusUri;
	}

	public String getKandi_koulutus_uri() {
		return kandi_koulutus_uri;
	}

	public void setKandi_koulutus_uri(String kandi_koulutus_uri) {
		this.kandi_koulutus_uri = kandi_koulutus_uri;
	}
	
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Koulutusmoduuli koulutusmoduuli;

	public Koulutusmoduuli getKoulutusmoduuli() {
		return koulutusmoduuli;
	}

	public void setKoulutusmoduuli(Koulutusmoduuli koulutusmoduuli) {
		this.koulutusmoduuli = koulutusmoduuli;
	}

	public String getAlkamiskausi_uri() {
		return alkamiskausi_uri;
	}

	public void setAlkamiskausi_uri(String alkamiskausi_uri) {
		this.alkamiskausi_uri = alkamiskausi_uri;
	}

	public Integer getAlkamisvuosi() {
		return alkamisvuosi;
	}

	public void setAlkamisvuosi(Integer alkamisvuosi) {
		this.alkamisvuosi = alkamisvuosi;
	}
	
}
