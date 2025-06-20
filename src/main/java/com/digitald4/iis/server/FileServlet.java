package com.digitald4.iis.server;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.exception.DD4StorageException.ErrorCode;
import com.digitald4.common.model.FileReference;
import com.digitald4.iis.model.Appointment;
import com.digitald4.iis.model.License;
import com.digitald4.iis.storage.NurseStore;
import com.digitald4.iis.storage.UserStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet(name = "files", description = "Handles file related requests", urlPatterns = "/files/*")
@MultipartConfig(fileSizeThreshold=1024*1024*10, maxFileSize=1024*1024*32, maxRequestSize=1024*1024*32)
public class FileServlet extends com.digitald4.common.server.FileServlet {
  public FileServlet() {
    userStore = new UserStore(daoProvider, new NurseStore(daoProvider, null));
  }

  @Override
  public void postUpload(HttpServletRequest request, FileReference reference) {
    String type = request.getParameter("entityType").toLowerCase();
    String id = request.getParameter("entityId");
    switch (type) {
      case "license":
        daoProvider.get().update(License.class, id, license -> license.setFileReference(reference));
        break;
      case "appointment":
        daoProvider.get().update(
            Appointment.class, Long.parseLong(id), app -> app.addAttachment(reference));
        break;
      default:
        throw new DD4StorageException("Unknown type for upload: " + type, ErrorCode.BAD_REQUEST);
    }
  }

  @Override
  protected String getFileId(HttpServletRequest request) throws ServletException, IOException {
    String entityType = request.getParameter("entityType").toLowerCase();
    String entityId = request.getParameter("entityId");
    String fileName = getFileName(request);
    return String.format("%s-%s-%s", entityType, entityId, fileName);
  }
}
