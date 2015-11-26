# SRDeserializer
A quick extractor for NSFileWrapper serialized archives.
##Utilisation
To extract the archive at path `i_am_a/path/to_the/archive`, use
  
  SRDeserializer deSer = new SRDeserializer("i_am_a/path/to_the", "archive");
	if(!deSer.deserialize()){
	  //Error
	}
	//Success
	
No guarantee of efficiency or safety,.... This is completely dependant of the current private implementation of NSFilewrapper serializedRepresentation method by Apple.
More info on SRDeserializer : [blog article](http://blog.simonrodriguez.fr/articles/06-09-2015_nsfilewrapper_serializedrepresentation.html)
More info on NSFileWrapper : [developer documentation](https://developer.apple.com/library/mac/documentation/Cocoa/Reference/ApplicationKit/Classes/NSFileWrapper_Class/)

##License
You can do what you want with this. If you feel nice, credit me :)

##About me
Feel free to visit my website [simonrodriguez.fr](http://simonrodriguez.fr) or my [blog](http://blog.simonrodirguez.fr).
