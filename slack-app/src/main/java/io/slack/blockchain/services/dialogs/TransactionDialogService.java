package io.slack.blockchain.services.dialogs;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.dialog.DialogOpenRequest.DialogOpenRequestBuilder;
import com.github.seratch.jslack.api.methods.request.users.UsersListRequest.UsersListRequestBuilder;
import com.github.seratch.jslack.api.model.User;
import com.github.seratch.jslack.api.model.dialog.Dialog;
import com.github.seratch.jslack.api.model.dialog.DialogOption;

import io.slack.blockchain.commons.configurations.SlackConfigurationProperties;
import io.slack.blockchain.interactive.components.dialogs.exceptions.DialogOpenException;
import io.slack.blockchain.interactive.components.dialogs.factories.TransactionsDialogFactory;
import io.slack.blockchain.utils.converters.DialogUserConverter;
import lombok.Setter;

@Service
@Setter
public class TransactionDialogService implements DialogService {
	@Autowired
	private Slack slack;

	@Autowired
	private DialogUserConverter dialogUserConverter;

	@Autowired
	private TransactionsDialogFactory slackTransactionDialogFactory;

	@Autowired
	private DialogOpenResponseHandler dialogOpenResponseHandler;

	@Autowired
	private SlackConfigurationProperties slackConfigurationProperties;

	@Autowired
	private UsersListRequestBuilder usersListRequestBuilder;

	@Autowired
	private DialogOpenRequestBuilder dialogOpenRequestBuilder;

	@Override
	public void openDialog(final String triggerId) {
		try {
			final List<User> users = slack.methods()
					.usersList(usersListRequestBuilder.token(slackConfigurationProperties.getOauthToken()).build())
					.getMembers();

			final List<DialogOption> usersDialogOptions = dialogUserConverter.convert(users);
			final Dialog dialog = slackTransactionDialogFactory.createTransactionsDialog(usersDialogOptions);

			dialogOpenResponseHandler.handleDialogOpenResponse(slack.methods().dialogOpen(dialogOpenRequestBuilder
					.token(slackConfigurationProperties.getOauthToken()).triggerId(triggerId).dialog(dialog).build()));
		} catch (IOException | SlackApiException exception) {
			throw new DialogOpenException(exception);
		}
	}
}
