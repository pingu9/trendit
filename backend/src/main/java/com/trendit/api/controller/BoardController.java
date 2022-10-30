package com.trendit.api.controller;

import com.trendit.api.exception.PasswordMisMatchException;
import com.trendit.api.request.BoardPostReq;
import com.trendit.api.request.BoardUpdateReq;
import com.trendit.api.response.BoardGetRes;
import com.trendit.api.response.BoardPostRes;
import com.trendit.api.response.data.BoardData;
import com.trendit.api.service.BoardService;
import com.trendit.common.model.response.BaseRes;
import com.trendit.db.entity.Board;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/board")
@AllArgsConstructor  // 생성자 주입 방식 <> autowired 필드 주입 방식
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @ApiOperation(value = "글쓰기", notes = "댓글을 작성합니다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "글이 등록되었습니다"),
            @ApiResponse(code = 400, message = "입력 내용을 다시 확인해주세요"),
            @ApiResponse(code = 500, message = "오류가 발생했습니다")
    })
    public ResponseEntity<? extends BaseRes> postBoard(@Validated @RequestBody BoardPostReq boardPostReq,
                                                       BindingResult bindingResult) throws Exception{
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body(BaseRes.of(400, "입력 내용을 다시 확인해주세요"));
        }

        Board saveBoard = boardService.postBoard(boardPostReq);

        return ResponseEntity.status(200).body(BoardPostRes.of(200, "글이 등록되었습니다", saveBoard));

    }

    @PutMapping
    @ApiOperation(value = "글쓰기", notes = "댓글을 수정합니다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "글이 수정되었습니다"),
            @ApiResponse(code = 400, message = "입력 내용을 다시 확인해주세요"),
            @ApiResponse(code = 500, message = "오류가 발생했습니다")
    })

    public ResponseEntity updateBoard(@Validated @RequestBody BoardUpdateReq boardUpdateReq) throws Exception {
        boardService.updateBoard(boardUpdateReq);

        return ResponseEntity.status(200).body(BaseRes.of(200, "글이 수정되었습니다"));
    }

    @DeleteMapping("/{boardId}")
    @ApiOperation(value = "글쓰기", notes = "댓글을 삭제합니다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "글이 삭제되었습니다"),
            @ApiResponse(code = 400, message = "입력 내용을 다시 확인해주세요"),
            @ApiResponse(code = 500, message = "오류가 발생했습니다")
    })
    public ResponseEntity deleteBoard(@PathVariable long boardId, @RequestHeader(value = "Authorization") String password)
            throws Exception {
        boardService.deleteBoard(boardId, password);

        return ResponseEntity.status(200).body(BaseRes.of(200, "글이 삭제되었습니다"));
    }


    @GetMapping("/keyword/{keywordId}")
    @ApiOperation(value = "글 조회", notes = "초기 댓글 100개를 조회합니다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "초기 댓글 100개가 조회되었습니다"),
            @ApiResponse(code = 400, message = "입력 내용을 다시 확인해주세요"),
            @ApiResponse(code = 500, message = "오류가 발생했습니다")
    })

    public ResponseEntity<? extends BaseRes> getBoards(@PathVariable long keywordId) {
        List<BoardData> boardDataList = boardService.getBoards(keywordId);

        return ResponseEntity.status(200).body(BoardGetRes.of(200, "댓글 초기 100개 조회", boardDataList));
    }


    @GetMapping("/keyword/{keywordId}/last-board-id/{boardId}")
    @ApiOperation(value = "글 조회", notes = "추가 댓글 100개를 조회합니다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "추가 댓글 100개가 조회되었습니다"),
            @ApiResponse(code = 400, message = "입력 내용을 다시 확인해주세요"),
            @ApiResponse(code = 500, message = "오류가 발생했습니다")
    })
    public ResponseEntity<? extends BaseRes> getBoards(@PathVariable long keywordId, @PathVariable long boardId) {
        List<BoardData> boardDataList = boardService.getBoards(keywordId, boardId);

        return ResponseEntity.status(200).body(BoardGetRes.of(200, "댓글 추가 100개 조회", boardDataList));
    }

}
