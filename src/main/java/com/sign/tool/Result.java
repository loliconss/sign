package com.sign.tool;

import org.springframework.util.StringUtils;

public class Result {
	
	/**
	 * 排序使用
	 */
	int tempId;
	
	/**
	 * 帖子id
	 */
	String post_id;

	public int getTempId() {
		return tempId;
	}

	public void setTempId(int tempId) {
		this.tempId = tempId;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	@Override
	public String toString() {
		return "Result [tempId=" + tempId + ", post_id=" + post_id + "]";
	}

	public Result(int tempId, String post_id) {
		super();
		this.tempId = tempId;
		this.post_id = post_id;
	}

	public Result() {
		super();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((post_id == null) ? 0 : post_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		if(obj instanceof Result) {
			Result other = (Result) obj;
			if(equalsStr(this.post_id, other.post_id)) {
				return true;
			}
		}
		return false;
	}

	private boolean equalsStr(String post_id2, String post_id3) {
		 if(StringUtils.isEmpty(post_id2) && StringUtils.isEmpty(post_id3)){
	            return true;
	        }
	        if(!StringUtils.isEmpty(post_id2) && post_id2.equals(post_id3)){
	            return true;
	        }
	        return false;
	}
	

}
