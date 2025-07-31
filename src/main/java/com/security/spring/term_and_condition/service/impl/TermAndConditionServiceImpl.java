package com.security.spring.term_and_condition.service.impl;

import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.CustomAlreadyExistException;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.term_and_condition.dto.TermAndConditionDto;
import com.security.spring.term_and_condition.dto.TermAndConditionRequest;
import com.security.spring.term_and_condition.dto.TermAndConditionResponse;
import com.security.spring.term_and_condition.dto.TermAndConditionType;
import com.security.spring.term_and_condition.entity.TermAndCondition;
import com.security.spring.term_and_condition.repo.TermAndConditionRepo;
import com.security.spring.term_and_condition.service.TermAndConditionService;
import com.security.spring.utils.Constraint;
import com.security.spring.utils.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermAndConditionServiceImpl implements TermAndConditionService {
    private final TermAndConditionRepo termAndConditionRepo;
    private final JWTService jwtService;
    private final ObjectMapper objectMapper;
    @Override
    public TermAndConditionResponse saveTermAndCondition(String token, TermAndConditionRequest termAndConditionRequest) {
        checkIsAdminRole(token);
        if (termAndConditionRepo.existsByType(termAndConditionRequest.getTermAndConditionType())){
            throw new CustomAlreadyExistException("Term And Condition is exists with this type : "+termAndConditionRequest.getTermAndConditionType());
        }
        TermAndCondition termAndCondition = TermAndCondition.builder()
                .context(termAndConditionRequest.getContext())
                .type(termAndConditionRequest.getTermAndConditionType())
                .build();
        TermAndCondition savedTermAndCondition = termAndConditionRepo.save(termAndCondition);
        return TermAndConditionResponse.builder()
                .status(true)
                .message(Constraint.CREATED_SUCCESS_MESSAGE)
                .termAndCondition(objectMapper.mapToTermAndConditionDto(savedTermAndCondition))
                .build();
    }

    private void checkIsAdminRole(String token) {
        String ar7Id = jwtService.extractUsername(token.substring(7));
        if (!ar7Id.startsWith("AY")){
            throw new UnauthorizedException("Unauthorized Role.");
        }
    }

    @Override
    public TermAndConditionResponse updateTermAndCondition(String token, Long id, TermAndConditionRequest termAndConditionRequest) {
       checkIsAdminRole(token);
       TermAndCondition existTermAndCondition = termAndConditionRepo.findById(id).orElseThrow(()->
               new DataNotFoundException("Term And Condition not found by ID : "+id));

       existTermAndCondition.setContext(termAndConditionRequest.getContext());
       TermAndCondition updatedTermAndCondition = termAndConditionRepo.save(existTermAndCondition);
        return TermAndConditionResponse.builder()
                .status(true)
                .message(Constraint.UPDATED_SUCCESS_MESSAGE)
                .termAndCondition(objectMapper.mapToTermAndConditionDto(updatedTermAndCondition))
                .build();
    }

    @Override
    public TermAndConditionResponse getTermAndConditionById(String token, Long id) {
        checkIsAdminRole(token);
        TermAndCondition existTermAndCondition = termAndConditionRepo.findById(id).orElseThrow(()->
                new DataNotFoundException("Term And Condition not found by ID : "+id));
        return TermAndConditionResponse.builder()
                .status(true)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .termAndCondition(objectMapper.mapToTermAndConditionDto(existTermAndCondition))
                .build();
    }

    @Override
    public TermAndConditionResponse deleteTermAndConditionById(String token, Long id) {
       checkIsAdminRole(token);
       TermAndCondition existTermAndCondition = termAndConditionRepo.findById(id).orElseThrow(()->
                new DataNotFoundException("Term And Condition not found by ID : "+id));
       termAndConditionRepo.delete(existTermAndCondition);
        return TermAndConditionResponse.builder()
                .status(true)
                .message(Constraint.DELETED_SUCCESS_MESSAGE)
                .termAndCondition(objectMapper.mapToTermAndConditionDto(existTermAndCondition))
                .build();
    }

    @Override
    public TermAndConditionResponse getAllTermAndCondition(String token) {
        checkIsAdminRole(token);
        List<TermAndConditionDto> termAndConditionDtoList = termAndConditionRepo.findAll().stream()
                .map(objectMapper::mapToTermAndConditionDto).toList();
        return TermAndConditionResponse.builder()
                .status(true)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .termAndConditionList(termAndConditionDtoList)
                .build();
    }

    @Override
    public TermAndConditionResponse getByType(String token, TermAndConditionType termAndConditionType) {
        TermAndCondition existTermAndCondition = termAndConditionRepo.findByType(termAndConditionType).orElseThrow(()->
                new DataNotFoundException("Term And Condition not found by Type : "+termAndConditionType));
        return TermAndConditionResponse.builder()
                .status(true)
                .message(Constraint.RETRIEVE_SUCCESS_MESSAGE)
                .termAndCondition(objectMapper.mapToTermAndConditionDto(existTermAndCondition))
                .build();
    }
}
