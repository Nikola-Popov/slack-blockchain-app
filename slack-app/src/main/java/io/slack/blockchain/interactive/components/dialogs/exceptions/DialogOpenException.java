package io.slack.blockchain.interactive.components.dialogs.exceptions;

public class DialogOpenException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DialogOpenException(String message) {
		super(message);
	}

	public DialogOpenException(Throwable cause) {
		super(cause);
	}
}
