package io.slack.blockchain.controllers.exception.handler;

import static io.slack.blockchain.domain.messages.AttachmentStatusColor.DANGER;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.seratch.jslack.api.model.Attachment.AttachmentBuilder;
import com.google.gson.JsonSyntaxException;

import io.slack.blockchain.commons.AttachmentResponseFactory;
import io.slack.blockchain.domain.attachments.AttachmentResponse;
import io.slack.blockchain.interactive.components.dialogs.exceptions.DialogOpenException;
import io.slack.blockchain.services.dialogs.exceptions.DialogResponderException;
import io.slack.blockchain.services.dialogs.exceptions.MissingDialogSubmissionException;

@ControllerAdvice
@ResponseBody
public class SlackDialogExceptionHandler {
	private static final String DIALOG_OPEN_ERROR_MESSAGE = "An error occured while trying to open the transaction dialog window";
	private static final String INVALID_AMOUNT_RESPONSE_MESSAGE = "Invalid amount type specified. Please, note that only numbers are allowed!";
	private static final String INTERNAL_SERVER_ERROR_GENERIC_RESPONSE_MESSAGE = "Something went wrong! Please, try again!";

	@Autowired
	private AttachmentResponseFactory attachmentResponseFactory;

	@Autowired
	private AttachmentBuilder attachmentBuilder;

	@ExceptionHandler(DialogOpenException.class)
	public ResponseEntity<AttachmentResponse> handleDialogOpenException() {
		return createInternalServerErrorResponse(DIALOG_OPEN_ERROR_MESSAGE);
	}

	@ExceptionHandler(JsonSyntaxException.class)
	public ResponseEntity<AttachmentResponse> handleSubmittedTransactionInvalidDataException() {
		return badRequest().body(attachmentResponseFactory.createAttachmentResponse(
				attachmentBuilder.text(INVALID_AMOUNT_RESPONSE_MESSAGE).color(DANGER).build()));
	}

	@ExceptionHandler(DialogResponderException.class)
	public ResponseEntity<AttachmentResponse> handleDialogResponderxception() {
		return createInternalServerErrorResponse(INTERNAL_SERVER_ERROR_GENERIC_RESPONSE_MESSAGE);
	}

	@ExceptionHandler(MissingDialogSubmissionException.class)
	public ResponseEntity<AttachmentResponse> handleMissingDialogSubmissionException() {
		return createInternalServerErrorResponse(INTERNAL_SERVER_ERROR_GENERIC_RESPONSE_MESSAGE);
	}

	private ResponseEntity<AttachmentResponse> createInternalServerErrorResponse(final String responseMessage) {
		return status(INTERNAL_SERVER_ERROR).body(attachmentResponseFactory
				.createAttachmentResponse(attachmentBuilder.text(responseMessage).color(DANGER).build()));
	}
}
