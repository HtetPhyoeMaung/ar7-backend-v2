package com.security.spring.gamesoft.getGameList.service;

import com.security.spring.gamesoft.getGameList.dto.GetGameListRequest;
import com.security.spring.gamesoft.getGameList.dto.GetGameListResponse;

public interface GetGameListService {
     GetGameListResponse getGameListConfig(GetGameListRequest data,String ar7id);

}
