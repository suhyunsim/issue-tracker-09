package kr.codesquad.issuetracker09.web.issue.controller;

import kr.codesquad.issuetracker09.domain.*;
import kr.codesquad.issuetracker09.service.AssigneeService;
import kr.codesquad.issuetracker09.service.IssueLabelService;
import kr.codesquad.issuetracker09.service.IssueService;
import kr.codesquad.issuetracker09.web.comment.dto.GetCommentListResponseDTO;
import kr.codesquad.issuetracker09.web.issue.dto.GetAssigneeListResponseDTO;
import kr.codesquad.issuetracker09.web.issue.dto.GetIssueDetailResponseDTO;
import kr.codesquad.issuetracker09.web.issue.dto.PatchDetailRequestDTO;
import kr.codesquad.issuetracker09.web.label.dto.GetLabelListResponseDTO;
import kr.codesquad.issuetracker09.web.milestone.dto.GetMilestoneListResponseDTO;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/issues")
@RestController
public class IssueController {
    private static final Logger log = LoggerFactory.getLogger(IssueController.class);

    private IssueService issueService;
    private IssueLabelService issueLabelService;
    private AssigneeService assigneeService;

    public IssueController(IssueService issueService, IssueLabelService issueLabelService, AssigneeService assigneeService) {
        this.issueService = issueService;
        this.issueLabelService = issueLabelService;
        this.assigneeService = assigneeService;
    }

    @GetMapping("/{issue-id}/detail")
    public GetIssueDetailResponseDTO detail(@PathVariable(name = "issue-id") long issueId) throws NotFound {
        Issue issue = issueService.findById(issueId);
        GetIssueDetailResponseDTO detail = GetIssueDetailResponseDTO.builder()
                .issueId(issueId)
                .title(issue.getTitle())
                .contents(issue.getContents())
                .author(issue.getAuthor().getName())
                .open(issue.isOpen())
                .build();

        List<GetAssigneeListResponseDTO> getAssigneeListResponseDTOS = new ArrayList<>();
        List<Assignee> assignees = assigneeService.findAllAssigneeByIssueId(issueId);
        for (Assignee assignee : assignees) {
            getAssigneeListResponseDTOS.add(GetAssigneeListResponseDTO.builder()
                    .userId(assignee.getUser().getId())
                    .userName(assignee.getUser().getName())
                    .build());
        }
        detail.setAssignees(getAssigneeListResponseDTOS);

        List<GetCommentListResponseDTO> comments = new ArrayList<>();
        for (Comment comment : issue.getComments()) {
            comments.add(GetCommentListResponseDTO.builder()
                    .id(comment.getId())
                    .author(comment.getAuthor().getName())
                    .contents(comment.getContents())
                    .created(comment.getCreated())
                    .build());
        }
        detail.setComments(comments);

        Milestone milestone = issue.getMilestone();
        detail.setMilestones(GetMilestoneListResponseDTO.builder()
                .id(milestone.getId())
                .title(milestone.getTitle())
                .dueOn(milestone.getDueOn())
                .numberOfOpenIssue(milestone.getNumberOfOpenIssue())
                .numberOfClosedIssue(milestone.getNumberOfClosedIssue())
                .build());

        List<Label> labels = issueLabelService.findLabelsByIssueId(issueId);
        List<GetLabelListResponseDTO> labelDTOs = new ArrayList<>();
        for (Label label : labels) {
            labelDTOs.add(GetLabelListResponseDTO.builder()
                    .id(label.getId())
                    .title(label.getTitle())
                    .colorCode(label.getColorCode())
                    .build());
        }
        detail.setLabels(labelDTOs);
        return detail;
    }

    @PatchMapping("/{issue-id}/detail")
    public void editDetail(@PathVariable(name = "issue-id") Long issueId,
                           @RequestBody PatchDetailRequestDTO request,
                           HttpServletResponse response) {
        log.debug("[*] patch - issueId : {}, request : {}", issueId, request);
        if (issueService.editDetail(issueId, request)) {
            response.setStatus(HttpStatus.OK.value());
        }
        //TODO : editDetail이 false(실패)인 경우 에러 처리
    }

}
