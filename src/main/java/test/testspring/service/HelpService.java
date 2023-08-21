package test.testspring.service;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.reflection.XMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import test.testspring.DTO.HelpBoardDTO;
import test.testspring.DTO.HelpCommentDTO;
import test.testspring.DTO.SearchDTO;
import test.testspring.FIleUpload;
import test.testspring.domain.*;
import test.testspring.repository.HelpCommentRepository;
import test.testspring.repository.HelpRepository;
import test.testspring.repository.MemberRepository;
import test.testspring.repository.ProductRepository;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class HelpService {
    private HelpRepository helpRepository;
    private HelpCommentRepository helpCommentRepository;
    private MemberRepository memberRepository;

    @Value("${upload.helpImg}")
    String fileUploadPath;

    private FIleUpload fIleUpload;
    @Autowired
    public HelpService(HelpRepository helpRepository, HelpCommentRepository helpCommentRepository, MemberRepository memberRepository, FIleUpload fIleUpload) {
        this.helpCommentRepository = helpCommentRepository;
        this.helpRepository = helpRepository;
        this.memberRepository = memberRepository;
        this.fIleUpload = fIleUpload;
    }

    public Page<HelpBoard> getQuestionList(Pageable pageRequest, SearchDTO search) {
        String type = search.getSearchType();
        Page<HelpBoard> helpBoards;
        if(type!=null){
            String keyword = search.getKeyword();
            switch (type) {
                case "title":
                    helpBoards = helpRepository.findByTitleContaining(keyword,pageRequest);
                    break;
                case "titleOrContent":
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

    public HelpBoardDTO getQuestionContent(Long id) {

        HelpBoard helpBoard = helpRepository.findById(id).orElse(new HelpBoard());


        return new HelpBoardDTO(helpBoard);
    }

    public void addComment(HelpComment comment) {
        helpCommentRepository.save(comment);
    }

    public Page<HelpBoard> findByMemberId(Pageable pageRequest, String id,String title) {
        if(title!=null){
            return helpRepository.findByMemberIdAndTitleContaining(title, id, pageRequest);
        }else return helpRepository.findByMemberId(id,pageRequest);
    }

    public void insertHelp(String id, HelpBoard help) {
        help.setMember(Member.builder().id(id).build());
        helpRepository.save(help);
    }

    public HelpBoard findOne(Long id) {
        return helpRepository.findById(id).get();
    }

    public void modifyHelp(HelpBoard helpBoard) {
        helpRepository.save(helpBoard);
    }

    public void deleteHelp(Long id) {
        helpRepository.deleteById(id);
    }
}
