package com.trendit.api.controller;

import com.trendit.api.exception.DuplicatedKeywordException;
import com.trendit.api.request.BoardPostReq;
import com.trendit.api.request.KeywordCompanyPostReq;
import com.trendit.api.request.KeywordPostReq;
import com.trendit.api.service.BoardService;
import com.trendit.api.service.KeywordService;
import com.trendit.api.service.WebClientService;
import com.trendit.common.model.response.BaseRes;
import com.trendit.api.response.KeywordListGetRes;
import com.trendit.api.response.data.KeywordData;
import com.trendit.api.service.KeywordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/keyword")
@AllArgsConstructor
public class KeywordController {
    private final KeywordService keywordService;
    private final WebClientService webClientService;

    @GetMapping("")
    @ApiOperation(value = "키워드 조회", notes = "입력한 문자열을 포함한 키워드들을 리스트로 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200,message = "키워드 조회에 성공했습니다."),
            @ApiResponse(code=400, message = "해당하는 키워드가 없습니다."),
            @ApiResponse(code=500, message = "서버 에러 발생.")
    })
    public ResponseEntity getKeywordList(@RequestParam("keyword")String keyword){
        List<KeywordData> data = keywordService.getKeywordList(keyword);
        return ResponseEntity.status(200).body(KeywordListGetRes.of(200,"Success",data));
    }

    @PostMapping
    @ApiOperation(value = "키워드 추가", notes = "키워드를 추가합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "키워드가 추가되었습니다."),
            @ApiResponse(code = 400, message = "중복된 키워드입니다."),
            @ApiResponse(code = 400, message = "요청 값이 잘못되었습니다."),
            @ApiResponse(code = 500, message = "서버 에러 발생.")
    })
    public ResponseEntity addKeyword(@Validated @RequestBody KeywordPostReq keywordPostReq, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body(BaseRes.of(400, "요청 값이 잘못되었습니다."));
        }

        try {
            keywordService.addKeyword(keywordPostReq);
            webClientService.addUserDictionary(keywordPostReq.getKeyword());
        } catch (DuplicatedKeywordException duplicatedKeywordException) {
            return ResponseEntity.status(400).body(BaseRes.of(400, "중복된 키워드입니다."));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(500).body(BaseRes.of(500, "서버 에러 발생."));
        }
        return ResponseEntity.status(200).body(BaseRes.of(200, "키워드가 추가되었습니다."));
    }


    @PostMapping("/company")
    @ApiOperation(value = "회사 타입의 키워드 추가", notes = "키워드를 추가합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "키워드가 추가되었습니다."),
            @ApiResponse(code = 400, message = "중복된 키워드입니다."),
            @ApiResponse(code = 400, message = "요청 값이 잘못되었습니다."),
            @ApiResponse(code = 500, message = "서버 에러 발생.")
    })
    public ResponseEntity addKeywordCompany(@Validated @RequestBody KeywordCompanyPostReq keywordCompanyPostReq, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body(BaseRes.of(400, "요청 값이 잘못되었습니다."));
        }

        try {
            keywordService.addKeywordCompany(keywordCompanyPostReq);
            webClientService.addUserDictionary(keywordCompanyPostReq.getCompanyName());
        } catch (DuplicatedKeywordException duplicatedKeywordException) {
            return ResponseEntity.status(400).body(BaseRes.of(400, "중복된 키워드입니다."));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(500).body(BaseRes.of(500, "서버 에러 발생."));
        }
        return ResponseEntity.status(200).body(BaseRes.of(200, "키워드가 추가되었습니다."));
    }
}
