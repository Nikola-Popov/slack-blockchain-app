package io.slack.blockchain.domain.dialog.contents;

import io.slack.blockchain.domain.dialog.submissions.ConfigurationDialogSubmission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationDialogContent extends DialogContent<ConfigurationDialogSubmission> {
	private final ConfigurationDialogSubmission configurationDialogSubmission;

	public ConfigurationDialogContent(DialogIdentityPayload dialogIdentityPayload,
			ConfigurationDialogSubmission configurationDialogSubmission) {
		super(dialogIdentityPayload);
		this.configurationDialogSubmission = configurationDialogSubmission;
	}

	@Override
	public ConfigurationDialogSubmission getDialogSubmission() {
		return configurationDialogSubmission;
	}
}
