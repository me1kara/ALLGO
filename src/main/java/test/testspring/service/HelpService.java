package test.testspring.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.testspring.DTO.HelpCommentDTO;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.HelpBoard;
import test.testspring.domain.HelpComment;
import test.testspring.domain.Product;
import test.testspring.repository.HelpCommentRepository;
import test.testspring.repository.HelpRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class HelpService {
    private HelpRepository helpRepository;
    private HelpCommentRepository helpCommentRepository;
    @Autowired
    public HelpService(HelpRepository helpRepository, HelpCommentRepository helpCommentRepository) {
        this.helpCommentRepository = helpCommentRepository;
        this.helpRepository = helpRepository;
    }

    public Page<HelpBoard> findAll(Pageable pageRequest, SearchDTO search) {
        String type = search.getSearchType();
        Page<HelpBoard> helpBoards;
        if(type!=null){
            String keyword = search.getKeyword();
            switch (type) {
                case "title":
                    helpBoards = helpRepository.findByTitleContaining(keyword,pageRequest);
                    break;
                case "titleOrContent":
                    log.info("{}", search.getKeyword());
                    helpBoards = helpRepository.findByTitleContainingOrContentContaining(keyword,keyword,pageRequest);
                    break;
                case "writer":
                    helpBoards = helpRepository.findByMemberId(keyword,pageRequest);
                    break;
                default:
                    helpBoards = helpRepository.findAll(pageRequest);
                    break;
            }
        }else helpBoards = helpRepository.findAll(pageRequest);

        return helpBoards;
    }

    public HelpBoard viewQuestionContent(Long id) {
        HelpBoard helpBoard = helpRepository.findById(id).orElse(new HelpBoard());
        List<HelpComment> childComments = new ArrayList<>();

        for (HelpComment comment : helpBoard.getComments()) {
            if (comment.getParentCommentId() == null) {
                childComments.add(comment);
            }
        }
        helpBoard.setComments(childComments);
        return helpBoard;
    }

    public void addComment(HelpComment comment) {
        helpCommentRepository.save(comment);
    }
}
