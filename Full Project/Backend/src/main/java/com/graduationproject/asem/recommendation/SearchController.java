package com.graduationproject.asem.recommendation;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@CrossOrigin(origins = "http://localhost:5173") 
@RestController
@RequestMapping("/search")
public class SearchController {


    private final RestTemplate restTemplate = new RestTemplate();
    private final SearchLogService searchLogService;

    public SearchController(SearchLogService searchLogService) {
        this.searchLogService = searchLogService;
    }


    @PostMapping
    public void log(@RequestBody SearchRequest req) {
        // 1. Save the search log
        searchLogService.logSearch(req.getUserId(), req.getSearchQuery());
    }

}