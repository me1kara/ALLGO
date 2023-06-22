package test.testspring.DTO;

import lombok.Builder;
import lombok.Data;
import test.testspring.domain.HelpBoard;
import test.testspring.domain.HelpComment;
import test.testspring.domain.HelpImg;
import test.testspring.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HelpBoardDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private boolean resolved;
    private Member member;
    private List<HelpCommentDTO> comments;
    private List<HelpImg> helpImgList;

    public HelpBoardDTO(HelpBoard helpBoard) {
        this.id = helpBoard.getId();
        this.title = helpBoard.getTitle();
        this.content = helpBoard.getContent();
        this.createdAt = helpBoard.getCreatedAt();
        this.resolved = helpBoard.isResolved();
        this.member = helpBoard.getMember();
        this.helpImgList = helpBoard.getImgList();
        this.comments = helpBoard.getComments().stream().map(HelpCommentDTO::new).collect(Collectors.toList());
    }


}

