

package com.bridgelabz.fundoonotes.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FundooException extends RuntimeException {

//	public FundooException(int value, String message) {
//		// TODO Auto-generated constructor stub
//		super(message);
//	}
//	
	private int statusCode;
	private String statusMessage;

	

}
