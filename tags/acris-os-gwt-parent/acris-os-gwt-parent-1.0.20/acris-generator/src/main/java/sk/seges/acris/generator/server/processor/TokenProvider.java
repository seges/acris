package sk.seges.acris.generator.server.processor;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.io.StringFile;

@Component
@Scope("prototype")
public class TokenProvider {

	private static final String TOKEN_DIRECTORY = "tokens";
	private static final String TOKEN_NAME = "token_";
	private static final String LANG_DELIMITER = " ";
	private static final String PATH_PREFIX = System.getProperty("java.io.tmpdir") + File.separator;

	private static final Logger log = Logger.getLogger(TokenProvider.class);

	private static final long MAX_PROCESSING_TIME = 120 * 10 * 60 * 1000; // 10 mins

	public TokenProvider() {

	}

	private int getLastNumberForProcessing() {
		ArrayList<Integer> tokens = getSortedTokenFiles();

		if (tokens == null || tokens.size() == 0) {
			return 0;
		}

		return tokens.get(tokens.size() - 1);
	}

	private ArrayList<Integer> getSortedTokenFiles() {
		File tokensDirectory = new File(PATH_PREFIX + TOKEN_DIRECTORY);

		if (!tokensDirectory.exists()) {
			log.warn("Tokens directory '" + tokensDirectory + "' does not exists. There is no token to be processed");
			return null;
		}

		if (!tokensDirectory.isDirectory()) {
			log.warn("Tokens directory '" + tokensDirectory
					+ "' is not valid directory. There is no token to be processed");
			return null;
		}

		File[] tokenFiles = tokensDirectory.listFiles(new TokensFilenameFilter());

		if (tokenFiles == null || tokenFiles.length == 0) {
			log.warn("Tokens directory '" + tokensDirectory + "' does not contains any token files");
			return null;
		}

		ArrayList<Integer> tokens = new ArrayList<Integer>();

		for (File tokenFile : tokenFiles) {
			if (System.currentTimeMillis() - tokenFile.lastModified() > MAX_PROCESSING_TIME) {
				log.info("Token '" + tokenFile.getName() + "' is older then maximum processing time ("
						+ MAX_PROCESSING_TIME + " ms) and will be deleted");
				tokenFile.delete();
			} else {
				String tokenFileName = tokenFile.getName().replaceAll(TOKEN_NAME, ""); 
				tokens.add(Integer.valueOf(tokenFileName));
			}
		}

		Collections.sort(tokens);

		return tokens;
	}

	public synchronized void setTokenForProcessing(final GeneratorToken generatorToken) {
		int currentFileIndex = getLastNumberForProcessing() + 1;

		final StringFile nextFile = new StringFile(PATH_PREFIX + TOKEN_DIRECTORY, TOKEN_NAME + currentFileIndex);

		if (log.isInfoEnabled()) {
			log.info("Creating file '" + nextFile.getAbsolutePath() + "' for nice url '" + generatorToken.getNiceUrl() + "'");
		}

		final File tokensDir = new File(PATH_PREFIX + TOKEN_DIRECTORY);
		if (!tokensDir.exists() && !tokensDir.mkdirs()) {
			log.error("Unable to create directory structure fot the file '" + nextFile.getAbsolutePath()
					+ "'. Current niceurl is " + generatorToken.getNiceUrl());
			return;
		}

		try {
			if (!nextFile.createNewFile()) {
				log.error("Unable to create empty file '" + nextFile.getAbsolutePath() + "'. Current niceurl is "
								+ generatorToken.getNiceUrl());
				return;
			}
		} catch (final IOException ioe) {
			log.error("Unable to create empty file '" + nextFile.getAbsolutePath() + "'. Current niceurl is " + generatorToken.getNiceUrl(),
					ioe);
			return;
		}

		try {
			nextFile.writeTextToFile(generatorToken.getLanguage()
					+ LANG_DELIMITER + generatorToken.getWebId());
		} catch (final IOException ioe) {
			log.error("Unable to write content to the token file '" + nextFile.getAbsolutePath()
					+ "'. Current token is " + generatorToken.getNiceUrl(), ioe);
		}
	}

	public synchronized GeneratorToken getTokenForProcessing() {
		ArrayList<Integer> tokens = getSortedTokenFiles();

		if (tokens == null || tokens.size() == 0) {
			return null;
		}

		StringFile tokenFile = new StringFile(PATH_PREFIX + TOKEN_DIRECTORY, TOKEN_NAME + tokens.get(0).toString());

		String generatorToken = null;

		try {
			generatorToken = tokenFile.readTextFromFile();
		} catch (IOException ioe) {
			log.error("Unable to read content from the token file '" + tokenFile.getAbsolutePath() + "'", ioe);
			return null;
		} finally {
			if (!tokenFile.delete()) {
				log.warn("Unable to delete token file '" + tokenFile.getAbsolutePath() + "'.");
			}
		}

		if (generatorToken == null) {
			return null;
		}

		StringTokenizer tokenizer = new StringTokenizer(generatorToken, LANG_DELIMITER);
		
		if (!tokenizer.hasMoreTokens()) {
			return null;
		}
		
		String lang = tokenizer.nextToken();

		if (!tokenizer.hasMoreTokens()) {
			return null;
		}

		String webId = tokenizer.nextToken();

		GeneratorToken lastGeneratorToken = new GeneratorToken();
		lastGeneratorToken.setLanguage(lang);
		lastGeneratorToken.setWebId(webId);

		return lastGeneratorToken;
	}

	private class TokensFilenameFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			return name.startsWith(TOKEN_NAME);
		}
	}
}