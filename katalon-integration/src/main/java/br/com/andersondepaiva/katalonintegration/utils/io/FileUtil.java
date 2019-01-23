package br.com.andersondepaiva.katalonintegration.utils.io;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
import br.com.andersondepaiva.katalonintegration.model.CustomFile;

@Service
public class FileUtil {

	/**
	 * 
	 * List all the files and folders from a directory
	 * 
	 * @param directoryName to be listed
	 * 
	 */
	public void listFilesAndFolders(String directoryName) {
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			System.out.println(file.getName());
		}
	}

	/**
	 * 
	 * List all the files under a directory
	 * 
	 * @param directoryName to be listed
	 * 
	 */
	public List<CustomFile> listFiles(String directoryName, String extension) {
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		List<CustomFile> files = new ArrayList<CustomFile>();
		for (File file : fList) {
			if (file.isFile()) {
				if (file.getName().endsWith(extension) || Strings.isNullOrEmpty(extension)) {
					files.add(CustomFile.builder().name(FilenameUtils.getBaseName(file.getName()))
							.path(file.getAbsolutePath()).build());
				}
			}
		}

		return files;
	}

	/**
	 * 
	 * List all the folder under a directory
	 * 
	 * @param directoryName to be listed
	 * 
	 */
	public List<CustomFile> listFolders(String directoryName) {
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		List<CustomFile> folders = new ArrayList<CustomFile>();
		for (File file : fList) {
			if (file.isDirectory()) {
				folders.add(CustomFile.builder().name(file.getName()).path(file.getAbsolutePath()).build());
			}
		}

		return folders;
	}

	/**
	 * 
	 * List all files from a directory and its subdirectories
	 * 
	 * @param directoryName to be listed
	 * 
	 */
	public List<CustomFile> listFilesAndSubDirectories(String directoryName, String extension) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		List<CustomFile> files = new ArrayList<CustomFile>();

		if (fList == null)
			return files;

		for (File file : fList) {
			if (file.isFile()) {
				if (file.getName().endsWith(extension) || Strings.isNullOrEmpty(extension)) {
					files.add(CustomFile.builder().name(FilenameUtils.getBaseName(file.getName()))
							.path(file.getAbsolutePath()).build());
				}
			} else if (file.isDirectory()) {
				List<CustomFile> subFiles = listFilesAndSubDirectories(file.getAbsolutePath(), extension);
				if (subFiles != null && subFiles.size() > 0) {
					files.addAll(subFiles);
				}
			}
		}

		return files;
	}

	public Resource loadAsResource(String absolutePath) {
		try {
			Path file = Paths.get(absolutePath).toAbsolutePath().normalize();
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new BusinessException("Could not read file: " + absolutePath);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not read file: " + absolutePath, e);
		}
	}

}
