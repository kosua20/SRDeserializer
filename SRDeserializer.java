package com.simonrodriguez.srdeserializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class performs the deserialization of an archive created using NSSerializer class
 * @see http://blog.simonrodriguez.fr/articles/06-09-2015_nsfilewrapper_serializedrepresentation.html
 * @author Simon Rodriguez
 */
public class SRDeserializer {
	/**
	 * The archive file (java.io File item)
	 */
	private File file;
	/**
	 * The LittleEndianRandomAccessFile used to read the content of the archive file
	 */
	private LittleEndianRandomAccessFile raf = null;
	
	/**
	 * Initialisation : create a SRDeserializer for the file given in argument.
	 * @param directorythe directory containing the archive file to extract
	 * @param filename the name of the archive file to extract
	 */
	public SRDeserializer(String directory, String filename) {
		this.file = new File(directory+filename);
		try {
			this.raf = new LittleEndianRandomAccessFile(this.file, "r");
		} catch (FileNotFoundException e){
			System.out.println("Error with the RAF");
		}
	}
	
	/**
	 * @return a boolean denoting the success of the deserialization
	 */
	public boolean deserialize(){
		System.out.println("Deserializing file at path " + file.getAbsolutePath() + "...");
		boolean result = false;
		try {
			raf.seek(3*4);
			SRFile rootDirectory = new SRFile("root");
			rootDirectory.setDirectory(true);
			rootDirectory.setFullSize(raf.length());
			rootDirectory.setContentSize(rootDirectory.getFullSize());
			
			File destinationFile = new File(this.file.getParentFile().getAbsolutePath() + File.separator + "Export - " + this.file.getName().replaceAll(".aquariidata", ""));
			String destination = destinationFile.getAbsolutePath();
			System.out.println("Destination: " + destination);
			result = deserializeDirectory(rootDirectory,destination);
			
		} catch (Exception e) {
			result = false;
		} finally {
			if(raf != null){
				try {
					raf.close();
				} catch (IOException e) {
					System.out.println("Error closin the RAF.");
				}
			}
		}
		return result;			
	}
	
	/**
	 * @param file the SRFile representing the directory to extract
	 * @param destination the destination where the directory should be extracted
	 * @return a boolean denoting the success of the deserialization
	 * @throws IOException when an error is encountered when creating the directory or writing one of its files on the host filesystem
	 */
	private boolean deserializeDirectory(SRFile file, String destination) throws IOException{
		File destinationFile = new File(destination);
		destinationFile.mkdirs();
		boolean result = true;
		long beginning = raf.getFilePointer() - 4;
		int items = (int)raf.readLeLong();
		System.out.println("Number of items to extract: " + items);
		
		SRFile[] files = new SRFile[items];
		for(int item = 0; item<items;item++){
			int size = (int)raf.readLeLong();
			String name = raf.readString(size);
			files[item] = new SRFile(name);
		}
		for(int item = 0; item<items;item++){
			long sizeFull = raf.readLeLong();
			files[item].setFullSize(sizeFull);
		}
		
		for(SRFile file1 : files){
			
				int type = (int)raf.readLeLong();
				file1.setDirectory(type == 3);
				if(file1.isDirectory()){
					result = result && deserializeDirectory(file1,destination + File.separator + file1.getName());
				} else {
					result = result && deserializeFile(file1,destination + File.separator + file1.getName());
				}
		}
		raf.seek(beginning+file.getFullSize());
		return result;
	}
	
	/**
	 * @return a boolean denoting the success of the deserialization
	 * @throws IOException when an error is encountered when writing the file on the host filesystem
	 */
	private boolean deserializeFile(SRFile file, String destination) throws IOException{
		long contentSize = raf.readLeLong();
		boolean shouldPad = (contentSize == 2147483648L || contentSize == -2147483648L);
		if(shouldPad){
			file.setContentSize(raf.readLeLong());
			file.setPaddingSize(raf.readLeLong());
		} else {
			file.setContentSize(contentSize);
			file.setPaddingSize(0L);
		}
		raf.seek(raf.getFilePointer() + file.getPaddingSize());
		long begin = raf.getFilePointer();
		boolean result = true;
		if(!(file.getName().equals(".") || file.getName().equals(".DS_STORE") || file.getName().equals("__@PreferredName@__") || file.getName().equals("__@UTF8PreferredName@__"))){ 
			result = writeFileToDisk(file, destination);
		}
		raf.seek(begin+file.getContentSize());
		System.out.println("Extracting file " + file.toString());
		return result;
	}
	
	/**
	 * @return a boolean denoting the success of the writing operation
	 * @throws IOException when an error is encountered whhile writing the file on the host filesystem
	 */
	private boolean writeFileToDisk(SRFile file, String destination) throws IOException{
		FileOutputStream fOS = null;
		boolean result = false;
		File exportFile = new File(destination);
		long pointerBegin = raf.getFilePointer();
		try {
			fOS =  new FileOutputStream(exportFile);
			long remainingSize = file.getContentSize();
			while (remainingSize > 0){
				//Buffer
				byte[] store = new byte[1024];
				int read = raf.read(store);
				if(read == -1){
					System.out.println("Premature end of file");
				}
				remainingSize = remainingSize - read;
				fOS.write(store);
				
			}
			//Truncating the extra zeroes at the end of the file
			fOS.getChannel().truncate(file.getContentSize());
			result = true;
		} catch (FileNotFoundException e){
			System.out.println("File not found");
			result = false;
		} finally {
			if(fOS != null){ fOS.close();}
			raf.seek(pointerBegin+file.getContentSize());
		}
		return result;
	}
	
	/*private void printBuffer(byte[] buffer){
		for (byte b : buffer) {
			System.out.print(b);
		}
		//System.out.println();
	}*/

}
