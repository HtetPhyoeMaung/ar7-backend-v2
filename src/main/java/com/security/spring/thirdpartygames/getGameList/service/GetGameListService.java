package com.security.spring.thirdpartygames.getGameList.service;

import com.security.spring.thirdpartygames.getGameList.dto.GetGameListRequest;
import com.security.spring.thirdpartygames.getGameList.dto.GetGameListResponse;

public interface GetGameListService {
     GetGameListResponse getGameListConfig(GetGameListRequest data,String ar7id);

}
