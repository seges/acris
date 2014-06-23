package sk.seges.acris.rpc;

import javax.servlet.http.HttpServletRequest;

public interface ModulePathTranslation {

    String computeModuleBaseURL(HttpServletRequest request, String moduleBaseURL, String strongName);
}