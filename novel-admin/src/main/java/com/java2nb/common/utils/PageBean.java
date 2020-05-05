package com.java2nb.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xiongxy
 */
public class PageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int total;
	private List<?> rows;

	public PageBean(List<?> list, int total) {
		this.rows = list;
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

}
