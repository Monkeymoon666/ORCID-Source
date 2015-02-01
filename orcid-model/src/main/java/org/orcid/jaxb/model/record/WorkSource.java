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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Deprecated
@XmlRootElement(name = "source")
public class WorkSource extends OrcidIdBase implements Serializable {

    // This field indicates that the source is null on database
    // So -1 will be the same as a null value on the source
    public static String NULL_SOURCE_PROFILE = "NOT_DEFINED";

    private static final long serialVersionUID = 1L;

    private String sourceName;

    public WorkSource() {
        super();
    }

    public WorkSource(OrcidIdBase other) {
        super(other);
    }

    public WorkSource(String path) {
        super(path);
    }

    public WorkSource(String path, String sourceName) {
        setPath(path);
        setSourceName(sourceName);
    }

    @XmlTransient
    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

}
