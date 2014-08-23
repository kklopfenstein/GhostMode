package com.kklop.ghostmode.exception;

import com.kklop.angmengine.game.exception.GameException;

public class PropertyManagerException extends GameException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9211145325928977192L;

	public PropertyManagerException(Exception e) {
		super(e);
	}
	
	public PropertyManagerException(String e) {
		super(e);
	}
}
