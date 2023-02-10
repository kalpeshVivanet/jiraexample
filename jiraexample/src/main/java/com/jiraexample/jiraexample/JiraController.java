package com.jiraexample.jiraexample;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RestController
public class JiraController {
    @RequestMapping("/")
    public String index() {
        return "Hello world kalpeshddd";
    }

    private static final String username = "kalpeshchouhan2@gmail.com";
    private static final String password = "ap6mLN3z29cyyoS5fbAi0F20";
    private static final String jiraBaseURL = "https://mykalpeshchouhan.atlassian.net/rest/api/2/issue/";
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    public JiraController() {
        restTemplate = new RestTemplate();
        httpHeaders = createHeadersWithAuthentication();
    }

    private HttpHeaders createHeadersWithAuthentication() {
        String plainCreds = username + ":" + password;
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCreds.getBytes());
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        return headers;
    }

    @RequestMapping(value = "/template/products")
    public ResponseEntity getIssue(String issueId) {
        String url = jiraBaseURL + "issue/" + issueId;
        System.out.println(url);
        HttpEntity<?> requestEntity = new HttpEntity(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                String.class);
    }

    public ResponseEntity createIssue(String key, String summary, String description, String issueType) {
        String createIssueJSON = createCreateIssueJSON(key, summary, description,
                issueType);

        String url = jiraBaseURL + "issue";

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>(createIssueJSON,
                httpHeaders);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                String.class);

    }

    private String createCreateIssueJSON(String key, String summary, String description, String issueType) {
        String createIssueJSON = "{\"fields\":{\"project\":{\"key\":\"$KEY\"},\"summary\":\"$SUMMARY\",\"description\":\"$DESCRIPTION\",\"issuetype\": {\"name\": \"$ISSUETYPE\"}";

        createIssueJSON = createIssueJSON.replace("$KEY", key);
        createIssueJSON = createIssueJSON.replace("$SUMMARY", summary);
        createIssueJSON = createIssueJSON.replace("$DESCRIPTION", description);
        return createIssueJSON.replace("$ISSUETYPE", issueType);
    }
}
