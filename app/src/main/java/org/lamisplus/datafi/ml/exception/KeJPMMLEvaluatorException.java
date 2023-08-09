package org.lamisplus.datafi.ml.exception;

/**
 * Adopted from lightning scorer
 */
public class KeJPMMLEvaluatorException extends RuntimeException {
	
	public KeJPMMLEvaluatorException(String message) {
		super(message);
	}
	
	public KeJPMMLEvaluatorException(String message, Throwable cause) {
		super(message, cause);
	}
}
