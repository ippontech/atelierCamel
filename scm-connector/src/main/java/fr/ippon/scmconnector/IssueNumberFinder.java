package fr.ippon.scmconnector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class IssueNumberFinder {
	private IssueNumberFinder() {}

	public static Long find(String commitMessage) {
		Matcher matcher = Pattern.compile("#([0-9]+)").matcher(commitMessage);
		return matcher.find() ? Long.valueOf(matcher.group(1)) : null;
	}
}
