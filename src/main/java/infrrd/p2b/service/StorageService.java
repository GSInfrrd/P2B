package infrrd.p2b.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;


public interface StorageService {

	
	File uploadFile(File file);
	
	File uploadFile(MultipartFile file);
	
}
