package com.simonrodriguez.srdeserializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class extends RandomAccessFile by manually handling little endianess.
 * @author Simon Rodriguez
 */
public class LittleEndianRandomAccessFile extends RandomAccessFile {

	/**
	 * Create a new LittleEndianRandomAccessFile
	 * @see RandomAccessFile initialisation
	 */
	public LittleEndianRandomAccessFile(String name, String mode)
			throws FileNotFoundException {
		super(name, mode);
	}

	/**
	 * Create a new LittleEndianRandomAccessFile
	 * @see RandomAccessFile initialisation
	 */
	public LittleEndianRandomAccessFile(File file, String mode)
			throws FileNotFoundException {
		super(file, mode);
	}
	
	/**
	 * Reads a long from the file, using little endianess.
	 * @return a long value read from the file
	 */
	public long readLeLong() throws IOException{
		byte[] buffer = new byte[4];
		this.read(buffer);
		long result = 0;
		for(int i = 0; i < 4; i++){
			//Reverse the byte
			result += (buffer[i] & 0xFF) << 8*i;
		}
		return result;
	}
	
	/**
	 * Reads a string of byte length size from the file.
	 * @param size: the size to read
	 * @return a String with the content read from the file
	 */
	public String readString(int size) throws IOException{
		byte[] buffer = new byte[size];
		this.read(buffer);
		return new String(buffer, "UTF-8");
	}

}
