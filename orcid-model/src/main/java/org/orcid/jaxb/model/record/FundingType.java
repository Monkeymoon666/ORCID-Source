/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.09 at 01:52:56 PM BST 
//

package org.orcid.jaxb.model.record;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>
 * Java class for orcid-type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * 
 */
@XmlType(name = "grantType")
@XmlEnum
public enum FundingType implements Serializable {
    @XmlEnumValue("grant")
    GRANT("grant"), 
    @XmlEnumValue("contract")
    CONTRACT("contract"), 
    @XmlEnumValue("award")
    AWARD("award"),
    @XmlEnumValue("salary-award")
    SALARY_AWARD("salary-award");
    private final String value;

    FundingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
    
    public static FundingType fromValue(String v) {
        for (FundingType c : FundingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
