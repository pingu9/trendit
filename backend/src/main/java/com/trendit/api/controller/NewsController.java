package com.trendit.api.controller;

import com.trendit.api.response.LatestNewsGetRes;
import com.trendit.api.response.NewsGetRes;
import com.trendit.common.model.response.BaseRes;
import com.trendit.db.entity.News;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.trendit.api.service.NewsService;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/news")
@AllArgsConstructor
public class NewsController {

    NewsService newsService;

    @GetMapping("/latest")
    public ResponseEntity getLatestNews(String newsDate, @RequestParam(value = "newsAgencies", required = false)List<String> newsAgencies, Pageable pageable) {
        Page<News> data = newsService.getLatestNews(newsDate, newsAgencies, pageable);

        return ResponseEntity.status(200).body(LatestNewsGetRes.of(200, "Success", data));
    }

    @GetMapping("")
    @ApiOperation(value = "기사 조회", notes = "기사 조회합니다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "기사가 조회되었습니다"),
            @ApiResponse(code = 400, message = "입력 내용을 다시 확인해주세요"),
            @ApiResponse(code = 500, message = "오류가 발생했습니다")
    })
    public ResponseEntity<? extends BaseRes> getNews(@RequestParam("keywordId") long keywordId,
                                                     @RequestParam(value = "newsDate", required = false) String newsDate,
                                                     @RequestParam(value = "newsAgencies", required = false) List<String> newsAgencies,
                                                     Pageable pageable) {
        Page<News> news = newsService.getNews(keywordId, newsDate, newsAgencies, pageable);

        return ResponseEntity.status(200).body(NewsGetRes.of(200, "기사가 조회되었습니다", news));

    }
}
