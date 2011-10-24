package sk.seges.acris.reporting.server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = -5741075896170745723L;
    private static final String UNKNOWN_MIME_TYPE="application/x-unknown-mime-type";

	private static ConfigurationProvider servletDirectoryConfigurer;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response, false);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response, true);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response, boolean inline) throws IOException {
		File toDownload = null;
		try {
			toDownload = getFileToDownload(request, response);
		} catch (Exception e) {
			throw new IOException("Download failed");
		}

		byte[] buf = new byte[8192];
		ServletOutputStream outputStream = response.getOutputStream();

		String mimeType = getServletContext().getMimeType(toDownload.getAbsolutePath());

//		String mimeType = new MimetypesFileTypeMap().getContentType(toDownload);
		if (null == mimeType) {
			mimeType = UNKNOWN_MIME_TYPE;
		}

		response.setContentType(mimeType);
		if (inline) {
			response.setHeader("Content-Disposition", "inline; filename="
					+ toDownload.getName());
		} else {
			response.setHeader("Content-Disposition", "attachment; filename="
					+ toDownload.getName());
		}
		FileInputStream fis = new FileInputStream(toDownload);

		int length = 0;
		while ((length = fis.read(buf)) != -1) {
			outputStream.write(buf, 0, length);
		}

		try {

			outputStream.flush();
			outputStream.close();
			fis.close();

		} catch (IOException e) {
			fis.close();

			throw e;
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}
	
	protected static final String FILE_NAME_PARAMETER = "fileName";
	protected static final String USER_NAME_PARAMETER = "webId";

	protected File getFileToDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileName = request.getParameter(FILE_NAME_PARAMETER);
        if(null == fileName) {
            throw new IllegalArgumentException("No file specified.");
        }

        String webId = request.getParameter(USER_NAME_PARAMETER);
        if(null == webId || webId.isEmpty()) {
            throw new IllegalArgumentException("No webId specified.");
        }

        File userDir = getServletDirectoryConfigurer().resolveRootDirectoryPath(webId);
        
        if (!userDir.exists()) {
	        throw new IOException("Not valid user directory");
        }

        File resultFile = new File(userDir, fileName);

        if (!resultFile.exists()) {
	        throw new IOException("Not image file specified");
        }

        return resultFile;
	}

	public void setServletDirectoryConfigurer(ConfigurationProvider servletDirectoryConfigurer) {
		ImageServlet.servletDirectoryConfigurer = servletDirectoryConfigurer;
	}

	public ConfigurationProvider getServletDirectoryConfigurer() {
		return servletDirectoryConfigurer;
	}

}
