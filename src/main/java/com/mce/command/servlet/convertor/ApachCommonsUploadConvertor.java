package com.mce.command.servlet.convertor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.mce.command.AbstractCommand;
import com.mce.command.Command;
import com.mce.command.CommandHandleException;
import com.mce.command.CommandTranslate;
import com.mce.command.UploadFile;
import com.mce.util.StringUtils;

public class ApachCommonsUploadConvertor extends AbstractCommandConvertor {
	private FileItemFactory factory = new DiskFileItemFactory();
	private ObjectMapper om = new ObjectMapper();
	protected Log logger = LogFactory.getLog(getClass().getName());
	private int tempSize;
	private String tempFolder;
	public static int DEFAULT_TEMP_SIZE = 51200000;

	public ApachCommonsUploadConvertor(Map<String, Class<? extends Command>> cmdMap) {
		this(cmdMap, DEFAULT_TEMP_SIZE);
	}

	public ApachCommonsUploadConvertor(Map<String, Class<? extends Command>> cmdMap, int tempSize) {
		this(cmdMap, tempSize, null);
	}

	public ApachCommonsUploadConvertor(Map<String, Class<? extends Command>> cmdMap, int tempSize,
			String tmpFolder) {
		super(cmdMap);
		this.tempSize = tempSize;
		this.tempFolder = tmpFolder;
	}

	public String getTempFolder() {
		return this.tempFolder;
	}

	public Command createCommand(HttpServletRequest req) throws Exception {
		ServletFileUpload upload = new ServletFileUpload(this.factory);
		upload.setFileSizeMax(this.tempSize);
		upload.setSizeMax(this.tempSize);
		UploadFileMapper um = createUploadFileMapper(req, upload);
		AbstractCommand mcmd = createCommand(um);
		return mcmd;
	}

	public AbstractCommand createCommand(UploadFileMapper um) throws Exception {
		InputStream is = um.commandTranslate;
		CommandTranslate ct = (CommandTranslate) this.om.readValue(is, CommandTranslate.class);
		Class<?> cmdType = findCommand(ct.getCommandName());
		if (!AbstractCommand.class.isAssignableFrom(cmdType)) {
			this.logger.error("Not Instanceof MultipartCommand object");
			throw new CommandHandleException(1001, "Not Instanceof MultipartCommand object");
		}
		Command cmd = (Command) this.om.readValue(ct.getContents(), cmdType);
		AbstractCommand mCmd = (AbstractCommand) cmd;
		mCmd.init(um.uploadFiles);
		Map<?, ?> maps = um.getContextParameters();
		if (!maps.isEmpty()) {
			Set<?> key = maps.keySet();
			Iterator<?> ikey = key.iterator();
			while (ikey.hasNext()) {
				String next = (String) ikey.next();
				String value = (String) maps.get(next);
				mCmd.addContextParams(next, value);
			}
		}
		return mCmd;
	}

	public UploadFileMapper createUploadFileMapper(HttpServletRequest req, ServletFileUpload upload)
			throws Exception {
		List<FileItem> items = upload.parseRequest(req);
		List<UploadFile> uFiles = new ArrayList();
		InputStream cmdIns = null;
		Map params = new HashMap();
		for (FileItem fi : items) {
			this.logger.info("FileItem name[" + fi.getName() + "]");
			if (fi.isFormField()) {
				String name = fi.getFieldName();
				if ("commandTranslate".equalsIgnoreCase(name)) {
					cmdIns = fi.getInputStream();
				} else if (!StringUtils.isNull(name)) {
					this.logger.info("name[" + name + "] value[" + fi.getString() + "]");
					params.put(name, fi.getString());
				}
			} else {
				UploadFile uf = new ApacheUploadFile(fi);
				uFiles.add(uf);
			}
		}
		if (cmdIns == null) {
			this.logger.error("not found CommandTranslate");
			throw new CommandHandleException(1000, "not found CommandTranslate");
		}
		return new UploadFileMapper(cmdIns, uFiles, params);
	}

	public boolean isSupport(HttpServletRequest req) {
		return ServletFileUpload.isMultipartContent(req);
	}

	private class UploadFileMapper {
		private Map<String, String> cMaps = new HashMap();
		private InputStream commandTranslate;
		private List<UploadFile> uploadFiles;

		public UploadFileMapper(InputStream commandTranslate,List<UploadFile> cmdIns, Map<String, String> uFiles) {
			this.commandTranslate = commandTranslate;
			this.uploadFiles = cmdIns;
			if (cMaps != null)
				this.cMaps = uFiles;
		}

		public Map<String, String> getContextParameters() {
			return this.cMaps;
		}
	}
}
