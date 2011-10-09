/**
 * 
 */
package sk.seges.acris.util;

import java.util.LinkedList;

/**
 * @author eldzi
 */
public class HTMLTextSplitter {
	private static final byte TEXT_PART_LENGTH = 100;
	private String header;
	private String rest;
	
	public HTMLTextSplitter(String text) {
		cutTextFromBeginning(text);
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getRest() {
		return rest;
	}
	
	private void cutTextFromBeginning(String text) {
		int length = calculateNearestPartEnd(TEXT_PART_LENGTH, text);
		String part = text.substring(0, length);
		
		LinkedList<String> startQueue = new LinkedList<String>();
		LinkedList<String> endQueue = new LinkedList<String>();		
		parseTags(part, startQueue, endQueue);
		
		rest = text.substring(length, text.length() - 1);
		if(startQueue.size() == endQueue.size()) {
			header = part;
		}
		else if(startQueue.size() > endQueue.size()) {
			header = addEndTags(part, startQueue, endQueue.size());
			rest = addStartTags(rest, endQueue, startQueue.size());
		} else {
			header = addStartTags(part, endQueue, startQueue.size());
			rest = addEndTags(rest, startQueue, endQueue.size());
		}
	}
	
	private int calculateNearestPartEnd(int approxLength, String text) {
		int length = text.length() < TEXT_PART_LENGTH ? text.length() : TEXT_PART_LENGTH;
		int sentenceEnd = text.indexOf(". ", length) + 1;
		int tagEnd = text.indexOf("</", length);
		
		return sentenceEnd < tagEnd ? sentenceEnd : tagEnd;
	}
	
	private void parseTags(String text, LinkedList<String> startQueue, LinkedList<String> endQueue) {
		int currentPosition = 0;
		int index;
		
		while(currentPosition < text.length()) {
			currentPosition = text.indexOf("<", currentPosition);
			if(currentPosition == -1)
				break;
			
			index = text.indexOf(">", currentPosition);
			if("/".equals(text.substring(currentPosition+1, currentPosition+2))) {
				endQueue.add(text.substring(currentPosition, index));
			} else {
				int spaceDelim = text.indexOf(" ", currentPosition);
				if(spaceDelim != -1 && spaceDelim < index)
					index = spaceDelim;
				
				if (index == -1) {
					startQueue.add(text.substring(0,currentPosition));
					break;
				} else {
					startQueue.add(text.substring(currentPosition, index));
				}
			}
			currentPosition = index;
		}
	}
	
	private String addEndTags(String text, LinkedList<String> queue, int count) {
		pollElements(queue, count);
		return text + buildTags(queue, "</");
	}
	
	private String addStartTags(String text, LinkedList<String> queue, int count) {
		pollElements(queue, count);
		return buildTags(queue, "<") + text;
	}
	
	private String buildTags(LinkedList<String> queue, String leftBracket) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < queue.size(); i++) {
			builder.append(leftBracket);
			builder.append(queue.poll());
			builder.append(">");
		}
		
		return builder.toString();
	}
	
	private void pollElements(LinkedList<String> queue, int count) {
		for(int i = 0; i < count; i++) {
			queue.poll();
		}
	}
}
