package com.reddot.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
//TODO: implement ids list
public class SearchCriteria {
    private int pageNo;
    private int pageSize;
    private String orderBy;
    private String sortDirection;
//    private List<Integer> ids;
}