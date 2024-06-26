package org.orcid.core.groupIds.issn;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.orcid.core.utils.http.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IssnClient {

    private static final Logger LOG = LoggerFactory.getLogger(IssnClient.class);

    private static final String START_OF_STRING = "\u0098";
    private static final String STRING_TERMINATOR = "\u009C";

    private static final String RESOURCE_MAIN = "resource/ISSN/%issn";
    private static final String RESOURCE_KEY_TITLE = "resource/ISSN/%issn#KeyTitle";    
    
    @Resource
    private IssnPortalUrlBuilder issnPortalUrlBuilder;

    @Resource
    private HttpRequestUtils httpRequestUtils;

    public IssnData getIssnData(String issn) {
        if(StringUtils.isEmpty(issn)) {
            return null;
        }
        String json = null;
        try {
            LOG.debug("Extracting ISSN for " +  issn);
            // ensure any lower case x is X otherwise issn portal won't work
            json = getJsonDataFromIssnPortal(issn.toUpperCase());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            LOG.error("Error when getting the issn data from issn portal " + issn, e);
            return null;
        }
        try {
            if (json != null) {
                IssnData data = extractIssnData(issn.toUpperCase(), json);
                data.setIssn(issn);
                return data;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOG.warn("Error extracting issn data from json returned from issn portal "+ issn, e);
            return null;
        }
    }

    private IssnData extractIssnData(String issn, String json) throws JSONException {
        LOG.info("Extracting json data from " + issn);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("@graph");        
        if (jsonArray != null) {
            IssnData issnData = new IssnData();
            String name0 = null;
            // Look for mainTitle first
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("@id")) {
                    String idName = obj.getString("@id");
                    if (idName.equals(RESOURCE_MAIN.replace("%issn", issn))) {
                        LOG.debug("Found main resource for " + issn);
                        // Look for the mainTitle
                        if (obj.has("mainTitle")) {                            
                            String title = obj.getString("mainTitle");
                            String cleanTitle = cleanText(title);
                            issnData.setMainTitle(cleanTitle);
                            LOG.debug("Found mainTitle for '" + issn + "' " + cleanTitle);
                            return issnData;
                        } else if (obj.has("name")) {
                            LOG.debug("Found name array for " + issn);
                            // If the mainTitle is not available, look for the
                            // name array
                            Object nameObject = jsonArray.getJSONObject(i).get("name");
                            if (nameObject instanceof JSONArray) {
                                String title = jsonArray.getJSONObject(i).getJSONArray("name").getString(0);
                                String cleanTitle = cleanText(title);
                                issnData.setMainTitle(cleanTitle);
                                LOG.debug("Found name[0] for '" + issn + "' " + cleanTitle);
                                // Save the name[0] in case we can't find the KeyTitle
                                name0 = cleanTitle;
                            } else if (nameObject instanceof String) {
                                String title = jsonArray.getJSONObject(i).getString("name");
                                String cleanTitle = cleanText(title);
                                issnData.setMainTitle(cleanTitle);
                                LOG.debug("Found name[0] for '" + issn + "' " + cleanTitle);
                                // Save the name[0] in case we can't find the KeyTitle
                                name0 = cleanTitle;
                            } else {
                                LOG.warn("Unable to extract name[0], it is not a string nor an array for " + issn);
                                throw new IllegalArgumentException("Unable to extract name[0], it is not a string nor an array for " + issn);
                            }
                        } else {
                            LOG.warn("Unable to extract name, couldn't find the mainTitle nor the name[0] for " + issn);
                            throw new IllegalArgumentException("Unable to extract name, couldn't find the mainTitle nor the name[0] for " + issn);
                        }
                    }
                }
            }
            
            // If mainTitle is not found, Look for the KeyTitle element
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.has("@id")) {
                    String idName = obj.getString("@id");
                    if (idName.equals(RESOURCE_KEY_TITLE.replace("%issn", issn))) {                        
                        String title = obj.getString("value");
                        String cleanTitle = cleanText(title);
                        issnData.setMainTitle(cleanTitle);
                        LOG.debug("Found KeyTitle for '" + issn + "' " + cleanTitle);
                        return issnData;
                    }
                }
            }

            // If mainTitle and keyTitle are not available, return the name[0]
            if(StringUtils.isNotEmpty(name0)) {
                String cleanTitle = cleanText(name0);
                issnData.setMainTitle(cleanTitle);
                LOG.debug("Found name[0] for '" + issn + "' " + cleanTitle);
                return issnData;
            }
            
        }
        throw new IllegalArgumentException("Unable to extract name, couldn't find the Key Title nor the main resource for " + issn);
    }

    private String getJsonDataFromIssnPortal(String issn) throws IOException, InterruptedException, URISyntaxException {
        String issnUrl = issnPortalUrlBuilder.buildJsonIssnPortalUrlForIssn(issn);
        HttpResponse<String> response = httpRequestUtils.doGet(issnUrl);
        if (response.statusCode() != 200) {
            return null;
        }
        return response.body();
    }

    private String cleanText(String text) {
        return text.replaceAll("\\p{C}", "").replaceAll(START_OF_STRING, "").replaceAll(STRING_TERMINATOR, "");
    }

}
