package br.com.andersondepaiva.katalonintegration.business.interfaces;

import java.util.List;

public interface IGitBusiness {
	String clonePull(String uri, String dir, String username, String password, String branch);

	List<String> getAllBranches(String uri, String dir, String username, String password);

	List<String> getAllBranches(String uri, String username, String password);
}
