package org.fortiss.smg.webrest.impl.jersey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.PackagesResourceConfig;

public class UriExtensionsConfig extends PackagesResourceConfig {

	private Map<String, MediaType> mediaTypeMap;
	 
    public UriExtensionsConfig()
    {
        super();
      
    }
 
    public UriExtensionsConfig(Map<String, Object> props)
    {
        super(props);
    }
 
    public UriExtensionsConfig(String[] paths)
    {
        super(paths);
    }
 
    @Override
    public Map<String, MediaType> getMediaTypeMappings()
    {
    	System.out.println("MEDIA TYPE MAPPING called");
        if (mediaTypeMap == null)
        {
            mediaTypeMap = new HashMap<String, MediaType>();
            mediaTypeMap.put("json", MediaType.APPLICATION_JSON_TYPE);
            mediaTypeMap.put("xml", MediaType.APPLICATION_XML_TYPE);
            mediaTypeMap.put("txt", MediaType.TEXT_PLAIN_TYPE);
            mediaTypeMap.put("html", MediaType.TEXT_HTML_TYPE);
            mediaTypeMap.put("xhtml", MediaType.APPLICATION_XHTML_XML_TYPE);
            MediaType jpeg = new MediaType("image", "jpeg");
            mediaTypeMap.put("jpg", jpeg);
            mediaTypeMap.put("jpeg", jpeg);
            mediaTypeMap.put("zip", new MediaType("application", "x-zip-compressed"));
        }
        return mediaTypeMap;
    }
}