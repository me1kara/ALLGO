package test.testspring.DTO;

import lombok.Data;
import test.testspring.domain.HelpComment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class HelpCommentDTO {
    private Long id;
    private Long boardId;
    private String content;
    private MemberDTO member;
    private LocalDateTime createdAt;
    private Long parentCommentId;
    private int depth;
    private List<HelpCommentDTO> childComments;  // 자식 코멘트 리스트 추가

    public HelpCommentDTO(HelpComment helpComment) {
        this.id = helpComment.getId();
        this.boardId = helpComment.getBoardId();
        this.content = helpComment.getContent();
        this.member = new MemberDTO(helpComment.getMember());
        this.createdAt = helpComment.getCreatedAt();
        this.parentCommentId = helpComment.getParentCommentId();
        this.depth = helpComment.getDepth();
        this.childComments = new ArrayList<>();  // 자식 코멘트 리스트 초기화
        // 대댓글 처리
        if (helpComment.getChildComments() != null) {
            for (HelpComment childComment : helpComment.getChildComments()) {
                HelpCommentDTO childCommentDTO = new HelpCommentDTO(childComment);
                this.childComments.add(childCommentDTO);
            }
        }
    }

    // Getters and Setters (if needed)
}

