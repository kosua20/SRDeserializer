package com.simonrodriguez.srdeserializer;

/**
 * Represents a file extracted from the NSSerialized archive.
 * @author Simon Rodriguez
 */
public class SRFile {

	/**
	 * The name of the item
	 */
	private String name;
	/**
	 * Denotes if the element is a directory or not
	 */
	private boolean isDirectory;
	/**
	 * The full size of the item, including padding and content
	 * @see http://blog.simonrodriguez.fr/articles/06-09-2015_nsfilewrapper_serializedrepresentation.html
	 */
	private long fullSize;
	/**
	 * The size of the padding area of the item in the archive file.
	 */
	private long paddingSize;
	/**
	 * The size of the content part of the item in the archive file.
	 */
	private long contentSize;
	
	/**
	 * Initialisation
	 */
	public SRFile(String name) {
		this.name = name;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public long getFullSize() {
		return fullSize;
	}

	public void setFullSize(long fullSize) {
		this.fullSize = fullSize;
	}

	public long getPaddingSize() {
		return paddingSize;
	}

	public void setPaddingSize(long paddingSize) {
		this.paddingSize = paddingSize;
	}

	public long getContentSize() {
		return contentSize;
	}

	public void setContentSize(long contentSize) {
		this.contentSize = contentSize;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "SRFile [name=" + name + ", isDirectory=" + isDirectory
				+ ", fullSize=" + fullSize + ", paddingSize=" + paddingSize
				+ ", contentSize=" + contentSize + "]";
	}
	

}
