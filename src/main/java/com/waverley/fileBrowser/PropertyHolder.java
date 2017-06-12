package com.waverley.fileBrowser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Andrey on 6/8/2017.
 */

@Component
@PropertySource("classpath:copiedProject/properties/application.properties")
public class PropertyHolder {

    @Value("${localURL}")
    private String localURL;// = "H:/workspace/SharedLocal";
    @Value("${remouteURL}")
    private String remouteURL;//= "smb://DESKTOP-N3GMKA8/Shared/";
    @Value("${userLogin}")
    private String userLogin;// = "user1";
    @Value("${userPassword}")
    private String userPassword;// = "user1";

    public String getLocalURL() {
        return localURL;
    }

    public String getRemouteURL() {
        return remouteURL;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

}
