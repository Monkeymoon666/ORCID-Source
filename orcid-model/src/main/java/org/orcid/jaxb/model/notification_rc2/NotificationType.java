//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.22 at 06:46:00 PM BST 
//

package org.orcid.jaxb.model.notification_rc2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * Java class for null.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum NotificationType {

    //@formatter:off
    @XmlEnumValue("custom") CUSTOM,
    @XmlEnumValue("institutional_connection") INSTITUTIONAL_CONNECTION,
    @XmlEnumValue("permission")PERMISSION,
    @XmlEnumValue("amended") AMENDED;
    //@formatter:on

    public String value() {
        return name();
    }

    public static NotificationType fromValue(String v) {
        return valueOf(v);
    }

    @JsonValue
    public String jsonValue() {
        return this.name();
    }

}
