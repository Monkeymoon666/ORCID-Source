package org.orcid.jaxb.model.v3.release.search.expanded;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import io.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "results" })
@XmlRootElement(name = "expanded-search", namespace = "http://www.orcid.org/ns/expanded-search")
@ApiModel(value = "SearchV3_0")
public class ExpandedSearch implements Serializable {
    
    private static final long serialVersionUID = -1791045354400556107L;
    
    @XmlElement(name = "expanded-result", namespace = "http://www.orcid.org/ns/expanded-search")
    protected List<ExpandedResult> results;
    
    @XmlAttribute(name = "num-found")
    protected Long numFound;

    public List<ExpandedResult> getResults() {
        if (results == null) {
            results = new ArrayList<ExpandedResult>();
        }
        return this.results;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((results == null) ? 0 : results.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExpandedSearch other = (ExpandedSearch) obj;
        if (results == null) {
            if (other.results != null)
                return false;
        } else if (!results.equals(other.results))
            return false;
        return true;
    }

    public Long getNumFound() {
        return numFound;
    }

    public void setNumFound(Long numFound) {
        this.numFound = numFound;
    }
}
