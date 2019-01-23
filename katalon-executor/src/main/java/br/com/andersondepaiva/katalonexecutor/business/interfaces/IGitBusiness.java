package br.com.andersondepaiva.katalonexecutor.business.interfaces;

public interface IGitBusiness {
	String clonePull(String uri, String dir, String username, String password, String branch);
}
