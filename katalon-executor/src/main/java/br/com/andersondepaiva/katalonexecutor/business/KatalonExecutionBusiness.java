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

import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import com.google.common.base.Joiner;

import br.com.andersondepaiva.core.business.Business;
import br.com.andersondepaiva.core.infra.exception.model.BusinessException;
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
		List<String> params = buildCmdParams(dto);

		Optional<KatalonExecution> optionalKatalon = baseRepository.findById(dto.getId());

		// Não encontrou registro na base, cancela a execução e libera a fila
		if (!optionalKatalon.isPresent())
			return false;

		KatalonExecution katalonEntity = optionalKatalon.get();
		Update updateKatalonExecution = new Update();
		updateKatalonExecution.set("statusExecution", StatusExecution.EXECUTING);
		executeAtomicUpdate(updateKatalonExecution, katalonEntity.getId());

		projectBusiness.syncProject(katalonEntity.getProject());

		String reportTestPath = katalonEntity.getTestSuite().getPath().replace(
				katalonEntity.getProject().getPath() + String.format("%sTest Suites%s", File.separator, File.separator),
				katalonEntity.getProject().getPath() + String.format("%sReports%s", File.separator, File.separator));
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

	private List<String> buildCmdParams(KatalonExecutionDto dto) {
		if (SystemUtils.IS_OS_WINDOWS) {
			return java.util.Arrays.asList("cmd.exe", "/c", "katalon", "-noSplash", "-runMode", "console", "-retry",
					"0", "-projectPath", dto.getProject().getPath(), "-testSuitePath", dto.getTestSuite().getName(),
					"-executionProfile", dto.getProfile().getName(), "-browserType", dto.getBrowser());
		}

		if (SystemUtils.IS_OS_LINUX) {
			return java.util.Arrays.asList("bash", "-c", String.format(
					"./katalon --args -noSplash -runMode=console retry=0 -projectPath=\"%s\" -testSuitePath=\"%s\" -executionProfile=\"%s\" -browserType=\"%s\"",
					dto.getProject().getPath(), dto.getTestSuite().getName(), dto.getProfile().getName(),
					dto.getBrowser()));
		}

		throw new BusinessException("Unidentified Operating System");
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