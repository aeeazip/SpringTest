package com.umc.test.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseResponseStatus status;
}