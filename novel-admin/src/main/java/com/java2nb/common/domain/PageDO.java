package com.java2nb.common.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageDO<T> {

	private int offset;
	private int limit;
	private int total;
	private Map<String, Object> params;
	private String param;
	private List<T> rows;

	public PageDO() {
		super();
		this.offset = 0;
		this.limit = 10;
		this.total = 1;
		this.params = new HashMap<>();
		this.param = "";
		this.rows = new ArrayList<>();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "PageDO{" +
				"offset=" + offset +
				", limit=" + limit +
				", total=" + total +
				", params=" + params +
				", param='" + param + '\'' +
				", rows=" + rows +
				'}';
	}
}
