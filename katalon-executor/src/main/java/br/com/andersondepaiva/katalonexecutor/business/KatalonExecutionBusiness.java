package br.com.andersondepaiva.katalonexecutor.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import com.google.common.base.Joiner;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IKatalonExecutionBusiness;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IKatalonStudioBusiness;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IProjectBusiness;
import br.com.andersondepaiva.katalonexecutor.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonexecutor.dto.ResultCmdKatalonDto;
import br.com.andersondepaiva.katalonexecutor.model.KatalonExecution;
import br.com.andersondepaiva.katalonexecutor.model.StatusExecution;
import br.com.andersondepaiva.katalonexecutor.repository.IKatalonExecutionRepository;

@Service
public class KatalonExecutionBusiness extends Business<KatalonExecution, String, KatalonExecutionDto>
		implements IKatalonExecutionBusiness {

	private final IKatalonStudioBusiness katalonStudioBusiness;
	private final IProjectBusiness projectBusiness;

	@Autowired
	public KatalonExecutionBusiness(IKatalonExecutionRepository baseRepository,
			IKatalonStudioBusiness katalonStudioBusiness, IProjectBusiness projectBusiness) {
		super(baseRepository);
		this.katalonStudioBusiness = katalonStudioBusiness;
		this.projectBusiness = projectBusiness;
	}

	@Override
	public boolean executeTests(KatalonExecutionDto dto)
			throws IOException, InterruptedException, ReflectiveOperationException {
		List<String> params = java.util.Arrays.asList("cmd.exe", "/c", "katalon", "-noSplash", "-runMode", "console",
				"-retry", "0", "-projectPath", dto.getProject().getPath(), "-testSuitePath",
				dto.getTestSuite().getName(), "-executionProfile", dto.getProfile().getName(), "-browserType",
				dto.getBrowser());

		Optional<KatalonExecution> optionalKatalon = baseRepository.findById(dto.getId());

		// Não encontrou registro na base, cancela a execução e libera a fila
		if (!optionalKatalon.isPresent())
			return false;

		KatalonExecution katalonEntity = optionalKatalon.get();
		Update updateKatalonExecution = new Update();
		updateKatalonExecution.set("statusExecution", StatusExecution.EXECUTING);
		executeAtomicUpdate(updateKatalonExecution, katalonEntity.getId());

		projectBusiness.syncProject(katalonEntity.getProject());

		String reportTestPath = katalonEntity.getTestSuite().getPath().replace("\\Test Suites\\", "\\Reports\\");
		String logFilePath = Joiner.on(File.separator).join(katalonStudioBusiness.getWorkPath(),
				katalonEntity.getId() + ".txt");

		ResultCmdKatalonDto resultCmd = executeCmd(params, reportTestPath, logFilePath);

		updateKatalonExecution = new Update();
		updateKatalonExecution.set("statusExecution",
				resultCmd.getExitCode() == 0 ? StatusExecution.SUCCESS : StatusExecution.FAIL);
		updateKatalonExecution.set("pathReportTest", resultCmd.getPathReport());
		updateKatalonExecution.set("pathLogFile", logFilePath);
		executeAtomicUpdate(updateKatalonExecution, katalonEntity.getId());

		return true;
	}

	private ResultCmdKatalonDto executeCmd(List<String> cmd, String reportTestPath, String logFilePath)
			throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		processBuilder.redirectErrorStream(true);
		processBuilder.directory(new File(katalonStudioBusiness.getPathKatalonStudio()));

		Process process = processBuilder.start();

		ResultCmdKatalonDto result = new ResultCmdKatalonDto();

		Pattern pattern = Pattern.compile("[0-9]{8}_[0-9]{6}");

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		String fileReportName = null;
		StringBuilder consoleLog = new StringBuilder();

		while ((line = reader.readLine()) != null) {
			consoleLog.append(line);
			System.out.println(line);
		}

		reader.close();

		Matcher matcher = pattern.matcher(consoleLog);
		if (matcher.find()) {
			fileReportName = matcher.group();
		}

		writeToFileLog(consoleLog.toString(), logFilePath);

		consoleLog = null;

		result.setExitCode(process.waitFor());
		String pathReportZip = reportTestPath.replace(".groovy", File.separator + fileReportName);
		result.setPathReport(zipReport(pathReportZip));
		return result;
	}

	private String zipReport(String logFilePath) {
		if (new File(logFilePath).exists()) {
			String zipFile = logFilePath + ".zip";
			ZipUtil.pack(new File(logFilePath), new File(zipFile));
			return zipFile;
		}

		return null;

	}

	private void writeToFileLog(String traceExecution, String logFilePath) throws IOException {
		File logFile = new File(logFilePath);

		if (!logFile.exists())
			logFile.createNewFile();

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(traceExecution);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}