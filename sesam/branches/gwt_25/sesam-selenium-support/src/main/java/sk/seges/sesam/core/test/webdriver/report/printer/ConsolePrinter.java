package sk.seges.sesam.core.test.webdriver.report.printer;

import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.webdriver.report.model.CommandResult;
import sk.seges.sesam.core.test.webdriver.report.model.TestCaseResult;

public class ConsolePrinter implements ReportPrinter<TestCaseResult> {

	@Override
	public void initialize(TestCaseResult testInfo) {}

	@Override
	public void print(TestCaseResult testInfo) {
		//ConsolePrinter
		
		CommandResult commandResult = testInfo.getCommandResults().get(testInfo.getCommandResults().size() - 1);
		
		if (commandResult.getState().equals(SeleniumOperationState.AFTER)) {
			if (commandResult.isFailure()) {
				System.out.println("Failed: " + commandResult.getThrowableMessage());
			} else {
				System.out.println("Success with result " + commandResult.getResult());
			}
		} else {
			System.out.print(commandResult.getOperationDescription());
			if (commandResult.getParameters() != null && commandResult.getParameters().length > 0) {
				String params = " ";
				int i = 0;
				for (Object parameter: commandResult.getParameters()) {
					if (i > 0) {
						params += ", ";
					}

					if (parameter != null) {
						params += parameter.toString();
						i++;
					}
				}
				
				System.out.print(params);
			}
			System.out.println();
		}
	}

	@Override
	public void finish(TestCaseResult testInfo) {}
}