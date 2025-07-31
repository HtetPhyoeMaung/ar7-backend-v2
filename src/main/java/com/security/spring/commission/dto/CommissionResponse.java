package com.security.spring.commission.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.spring.commission.entity.AgentCommissionUser;
import com.security.spring.gamesoft.gameType.dto.GameTypeObj;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.report.dto.UserDetailObj;
import com.security.spring.user.dto.UserResponse;
import com.security.spring.user.dto.UserResponseObj;
import com.security.spring.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommissionResponse {
    private String message;
    private boolean status;
    private int statusCode;
    private GameTypeCommissionObj gameTypeCommissionObj;
    private List<GameTypeCommissionObj> gameTypeCommissionObjList;
    private List<CommissionObj> commissionObjList;
    private List<DownLineCommissionObj> downLineCommissionObjList;
    private List<CommissionConfirmObj> commissionConfirmObjList;
    private CommissionConfirmObj commissionConfirmObj;
    private CommissionObj commissionObj;
    private List<AgentCommissionUser> agentCommissionUserList;
    private List<UserWinLoseDetailObj> userWinLoseDetailObjList;
    private List<AgentCommissionResponse> agentCommissionResponseList;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}
