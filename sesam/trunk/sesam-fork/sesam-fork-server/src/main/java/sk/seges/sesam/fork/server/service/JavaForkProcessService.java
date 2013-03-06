/**
 * 
 */
package sk.seges.sesam.fork.server.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import sk.seges.sesam.fork.shared.domain.RemoteProcess;
import sk.seges.sesam.fork.shared.domain.RemoteProcessResponse;
import sk.seges.sesam.fork.shared.service.RemoteProcessService;

/**
 * @author ladislav.gazo
 * 
 */
public class JavaForkProcessService implements RemoteProcessService {

	@Override
	public RemoteProcessResponse execute(RemoteProcess remoteProcess) {
		try {
			Process process = Runtime.getRuntime().exec(remoteProcess.getCommand());
			String outString = readFromErrorStream(new BufferedInputStream(process.getInputStream()));
			String errorString = readFromErrorStream(new BufferedInputStream(process.getErrorStream()));
			int exitStatus = process.waitFor();
			
			RemoteProcessResponse response = new RemoteProcessResponse(outString, errorString, exitStatus);
			return response;
		} catch (Exception e) {
			return new RemoteProcessResponse(null, getStackTrace(e), 84);
		}
	}

	private String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	private String readFromErrorStream(BufferedInputStream errorStream)
			throws IOException {
		StringBuilder errorStringBuilder = new StringBuilder();
		int bytesRead = 0;
		byte[] buffer = new byte[1024];
		try {
			while ((bytesRead = errorStream.read(buffer)) != -1) {
				errorStringBuilder.append(new String(buffer, 0, bytesRead));
			}
			return errorStringBuilder.toString();
		} finally {
			if (errorStream != null) {
				try {
					errorStream.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
