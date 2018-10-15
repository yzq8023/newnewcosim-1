package com.hotent.platform.controller.form;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.annotion.Action;
import com.hotent.core.engine.FreemarkEngine;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.ContextUtil;
import com.hotent.core.util.DateFormatUtil;
import com.hotent.core.util.Dom4jUtil;
import com.hotent.core.util.ExceptionUtil;
import com.hotent.core.util.FileUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.auth.ISysUser;
import com.hotent.platform.dao.system.GlobalTypeDao;
import com.hotent.platform.model.form.BpmFormDef;
import com.hotent.platform.model.form.BpmFormField;
import com.hotent.platform.model.form.BpmFormTable;
import com.hotent.platform.model.form.BpmFormTemplate;
import com.hotent.platform.model.system.GlobalType;
import com.hotent.platform.service.bpm.BpmService;
import com.hotent.platform.service.bpm.BpmSubtableRightsService;
import com.hotent.platform.service.bpm.thread.MessageUtil;
import com.hotent.platform.service.form.BpmFormDefService;
import com.hotent.platform.service.form.BpmFormFieldService;
import com.hotent.platform.service.form.BpmFormHandlerService;
import com.hotent.platform.service.form.BpmFormRightsService;
import com.hotent.platform.service.form.BpmFormTableService;
import com.hotent.platform.service.form.BpmFormTemplateService;
import com.hotent.platform.service.form.FormUtil;
import com.hotent.platform.service.form.ParseReult;
import com.hotent.platform.xml.util.MsgUtil;

import freemarker.template.TemplateException;

/**
 * 对象功能:自定义表单 控制器类 开发公司:广州宏天软件有限公司 开发人员:xwy 创建时间:2011-12-22 11:07:56
 */
@Controller
@RequestMapping("/platform/form/bpmFormDef/")
public class BpmFormDefController extends BaseController {

	@Resource
	private BpmFormDefService service;
	@Resource
	private BpmFormTableService bpmFormTableService;
	@Resource
	private BpmFormFieldService bpmFormFieldService;
	@Resource
	private BpmFormTemplateService bpmFormTemplateService;

	@Resource
	private BpmFormRightsService bpmFormRightsService;
	@Resource
	private FreemarkEngine freemarkEngine;
	@Resource
	private BpmService bpmService;
	@Resource
	private BpmFormHandlerService bpmFormHandlerService;
	@Resource
	private GlobalTypeDao globalTypeDao;
	@Resource
	private BpmSubtableRightsService bpmSubtableRightsService;

	@RequestMapping("manage")
	@Action(description = "自定义表单管理页面", operateType = "自定义表单")
	public ModelAndView manage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();

		return mv;
	}

	/**
	 * 取得自定义表单分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 *            请求页数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看自定义表单分页列表", operateType = "自定义表单")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", defaultValue = "1") int page)
			throws Exception {
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		QueryFilter filter = new QueryFilter(request, "bpmFormDefItem");
		List<BpmFormDef> list = service.getAll(filter);
		Map<Long, Integer> publishedCounts = new HashMap<Long, Integer>();
		Map<Long, BpmFormDef> defaultVersions = new HashMap<Long, BpmFormDef>();
		for (int i = 0; i < list.size(); i++) {
			BpmFormDef formDef = list.get(i);
			Integer publishedCount = service.getCountByFormKey(formDef
					.getFormKey());
			publishedCounts.put(formDef.getFormDefId(), publishedCount);
			BpmFormDef defaultVersion = service
					.getDefaultVersionByFormKey(formDef.getFormKey());
			if (defaultVersion != null) {
				defaultVersions.put(formDef.getFormDefId(), defaultVersion);
			}
		}

		ModelAndView mv = this.getAutoView().addObject("bpmFormDefList", list)
				.addObject("publishedCounts", publishedCounts)
				.addObject("defaultVersions", defaultVersions)
				.addObject("categoryId", categoryId);
		return mv;
	}

	@RequestMapping("newVersion")
	@Action(description = "新建表单版本", operateType = "自定义表单")
	public void newVersion(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String preUrl = RequestUtil.getPrePage(request);
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		ResultMessage msg;
		try {
			service.newVersion(formDefId);
			msg = new ResultMessage(ResultMessage.Success, "新建表单版本成功!");
		} catch (Exception ex) {
			msg = new ResultMessage(ResultMessage.Fail, "新建表单版本失败!");
		}
		addMessage(msg, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 收集信息，为添加表单做准备
	 * 
	 * @param request
	 * @param response
	 * @param page
	 *            请求页数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("gatherInfo")
	@Action(description = "收集信息", operateType = "自定义表单")
	public ModelAndView gatherInfo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", defaultValue = "1") int page)
			throws Exception {
		long categoryId = RequestUtil.getLong(request, "categoryId");

		ModelAndView mv = this.getAutoView()
				.addObject("categoryId", categoryId);
		return mv;
	}

	/**
	 * 选择模板
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selectTemplate")
	@Action(description = "选择模板", operateType = "自定义表单")
	public ModelAndView selectTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subject = RequestUtil.getString(request, "subject");
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		String formDesc = RequestUtil.getString(request, "formDesc");
		Long tableId = RequestUtil.getLong(request, "tableId");
		int isSimple = RequestUtil.getInt(request, "isSimple", 0);
		String templatesId = RequestUtil.getString(request, "templatesId");

		ModelAndView mv = this.getAutoView();
		BpmFormTable table = bpmFormTableService.getById(tableId);

		if (table.getIsMain() == 1) {
			List<BpmFormTable> subTables = bpmFormTableService
					.getSubTableByMainTableId(tableId);
			List<BpmFormTemplate> mainTableTemplates = bpmFormTemplateService
					.getAllMainTableTemplate();
			List<BpmFormTemplate> subTableTemplates = bpmFormTemplateService
					.getAllSubTableTemplate();
			mv.addObject("mainTable", table).addObject("subTables", subTables)
					.addObject("mainTableTemplates", mainTableTemplates)
					.addObject("subTableTemplates", subTableTemplates);

		} else {
			List<BpmFormTable> subTables = new ArrayList<BpmFormTable>();
			subTables.add(table);
			List<BpmFormTemplate> subTableTemplates = bpmFormTemplateService
					.getAllSubTableTemplate();
			mv.addObject("subTables", subTables).addObject("subTableTemplates",
					subTableTemplates);
		}
		mv.addObject("subject", subject).addObject("categoryId", categoryId)
				.addObject("tableId", tableId).addObject("formDesc", formDesc)
				.addObject("isSimple", isSimple)
				.addObject("templatesId", templatesId);

		return mv;
	}

	/**
	 * 加载编辑器设计模式的模板列表
	 */
	@RequestMapping("chooseDesignTemplate")
	@Action(description = "选择编辑器设计模板", operateType = "自定义表单")
	public ModelAndView chooseDesignTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();
		String subject = RequestUtil.getString(request, "subject");
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		String formDesc = RequestUtil.getString(request, "formDesc");
		int isSimple = RequestUtil.getInt(request, "isSimple", 0);

		String templatePath = FormUtil.getDesignTemplatePath();
		String xml = FileUtil.readFile(templatePath + "designtemps.xml");
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		String reStr = "[";
		for (Element element : list) {
			String alias = element.attributeValue("alias");
			String name = element.attributeValue("name");
			String templateDesc = element.attributeValue("templateDesc");
			if (!reStr.equals("["))
				reStr += ",";
			reStr += "{name:'" + name + "',alias:'" + alias
					+ "',templateDesc:'" + templateDesc + "'}";
		}
		reStr += "]";
		mv.addObject("subject", subject).addObject("categoryId", categoryId)
				.addObject("formDesc", formDesc).addObject("temps", reStr)
				.addObject("isSimple", isSimple);
		return mv;
	}

	/**
	 * 编辑器设计表单
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("designEdit")
	@Action(description = "编辑器设计表单", operateType = "自定义表单")
	public ModelAndView designEdit(HttpServletRequest request) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId", 0);
		int isBack = RequestUtil.getInt(request, "isBack", 0);

		boolean isPublish = false;

		ModelAndView mv = getAutoView();
		BpmFormDef bpmFormDef = null;
		if (formDefId == 0) {
			bpmFormDef = new BpmFormDef();
			bpmFormDef
					.setCategoryId(RequestUtil.getLong(request, "categoryId"));
			bpmFormDef.setFormDesc(RequestUtil.getString(request, "formDesc"));
			bpmFormDef.setSubject(RequestUtil.getString(request, "subject"));
			bpmFormDef.setDesignType(BpmFormDef.DesignType_CustomDesign);
			String tempalias = RequestUtil.getString(request, "tempalias");
			String templatePath = FormUtil.getDesignTemplatePath();
			String reult = FileUtil
					.readFile(templatePath + tempalias + ".html");
			bpmFormDef.setHtml(reult);

			mv.addObject("canEditColumnNameAndType", true);
		} else {
			boolean canEditColumnNameAndType = true;
			bpmFormDef = service.getById(formDefId);
			Long tableId = bpmFormDef.getTableId();

			if (tableId > 0) {
				canEditColumnNameAndType = bpmFormTableService
						.getCanEditColumnNameTypeByTableId(bpmFormDef
								.getTableId());
				BpmFormTable bpmFormTable = bpmFormTableService
						.getById(tableId);
				mv.addObject("bpmFormTable", bpmFormTable);
				isPublish = true;
			}

			mv.addObject("canEditColumnNameAndType", canEditColumnNameAndType);
		}
		// 回退到编辑页面。
		if (isBack > 0) {
			Long categoryId = RequestUtil.getLong(request, "categoryId");
			String subject = RequestUtil.getString(request, "subject");
			String formDesc = RequestUtil.getString(request, "formDesc");
			String title = RequestUtil.getString(request, "tabTitle");
			String html = request.getParameter("html");

			bpmFormDef.setCategoryId(categoryId);
			bpmFormDef.setFormDesc(formDesc);
			bpmFormDef.setSubject(subject);
			bpmFormDef.setDesignType(BpmFormDef.DesignType_CustomDesign);
			bpmFormDef.setHtml(html);
			bpmFormDef.setTabTitle(title);
		}
		mv.addObject("bpmFormDef", bpmFormDef);
		mv.addObject("isPublish", isPublish);

		return mv;
	}

	/**
	 * 流程表单授权
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("rightsDialog")
	@Action(description = "流程表单授权", operateType = "自定义表单")
	public ModelAndView rightsDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		boolean isNodeAlter=RequestUtil.getBoolean(request, "isAlert");
		boolean isNodeRights = false;

		ModelAndView mv = this.getAutoView();
		// 是否针对流程节点授权。
		if (!isNodeAlter && StringUtil.isNotEmpty(nodeId) ) {
			Map<String, String> nodeMap = bpmService.getExecuteNodesMap(
					actDefId, true);
			mv.addObject("nodeMap", nodeMap);
			mv.addObject("nodeId", nodeId);
			mv.addObject("actDefId", actDefId);
			isNodeRights = true;
		}
		mv.addObject("formKey", formKey);
		mv.addObject("isNodeRights", isNodeRights);
		return mv;
	}

	/**
	 * 根据表Id和模版Id获取所有的控件定义。
	 * 
	 * @param request
	 * @param response
	 * @param templateId
	 *            表单模板Id
	 * @param tableId
	 *            自定义表Id
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("getControls")
	@Action(description = "获取表单控件", operateType = "自定义表单")
	public Map<String, String> getMacroTemplate(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "templateId") Long templateId,
			@RequestParam(value = "tableId") Long tableId)
			throws TemplateException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		BpmFormTemplate template = bpmFormTemplateService.getById(templateId);
		if (template != null) {
			template = bpmFormTemplateService.getByTemplateAlias(template
					.getMacroTemplateAlias());
			String macro = template.getHtml();
			BpmFormTable table = bpmFormTableService.getById(tableId);
			List<BpmFormField> fields = bpmFormFieldService
					.getByTableId(tableId);
			for (BpmFormField field : fields) {
				String fieldname = field.getFieldName();
				// 字段命名规则
				// 表类型(m:主表，s:子表) +":" + 表名 +“：” + 字段名称
				field.setFieldName((table.getIsMain() == 1 ? "m:" : "s:")
						+ table.getTableName() + ":" + field.getFieldName());
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("field", field);
				map.put(fieldname,
						freemarkEngine.parseByStringTemplate(data, macro
								+ "<@input field=field/>"));
			}
		}
		return map;
	}

	/**
	 * 删除自定义表单。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("delByFormKey")
	@Action(description = "删除自定义表单", operateType = "自定义表单")
	public void delByFormKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		Long formKey = RequestUtil.getLong(request, "formKey");
		int rtn = service.getFlowUsed(formKey);
		ResultMessage msg;
		if (rtn > 0) {
			msg = new ResultMessage(ResultMessage.Fail, "该表单已和流程进行了关联，不能被删除!");
		} else {
			try {
				service.delByFormKey(formKey);
				msg = new ResultMessage(ResultMessage.Success, "删除表单成功!");
			} catch (Exception e) {
				msg = new ResultMessage(ResultMessage.Fail, "删除表单失败!");
			}
		}
		addMessage(msg, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 编辑自定义表单。
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "编辑自定义表单", operateType = "自定义表单")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		ModelAndView mv = getAutoView();

		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String returnUrl = RequestUtil.getPrePage(request);
		BpmFormDef bpmFormDef = null;
		if (formDefId != 0) {
			bpmFormDef = service.getById(formDefId);
		} else {
			bpmFormDef = new BpmFormDef();
			bpmFormDef.setTableId(RequestUtil.getLong(request, "tableId"));
			bpmFormDef
					.setCategoryId(RequestUtil.getLong(request, "categoryId"));
			bpmFormDef.setFormDesc(RequestUtil.getString(request, "formDesc"));
			bpmFormDef.setSubject(RequestUtil.getString(request, "subject"));

			Long[] templateTableId = RequestUtil.getLongAryByStr(request,
					"templateTableId");
			Long[] templateId = RequestUtil.getLongAryByStr(request,
					"templatesId");
			String templatesId = getTemplateId(templateTableId, templateId);
			String reult = genTemplate(templateTableId, templateId);
			bpmFormDef.setHtml(reult);
			bpmFormDef.setTemplatesId(templatesId);
		}

		mv.addObject("bpmFormDef", bpmFormDef)
				.addObject("returnUrl", returnUrl);

		return mv;
	}

	/**
	 * 获得模板与表的对应关系
	 * 
	 * @param templateTablesId
	 * @param templatesId
	 * @return
	 */
	private String getTemplateId(Long[] templateTablesId, Long[] templatesId) {
		StringBuffer sb = new StringBuffer();
		if (BeanUtils.isEmpty(templateTablesId)
				|| BeanUtils.isEmpty(templatesId))
			return sb.toString();
		for (int i = 0; i < templateTablesId.length; i++) {
			for (int j = 0; j < templatesId.length; j++) {
				if (i == j) {
					sb.append(templateTablesId[i]).append(",")
							.append(templatesId[j]);
					break;
				}
			}
			sb.append(";");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 取得自定义表单明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看自定义表单明细", operateType = "自定义表单")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "formDefId");
		String preUrl = RequestUtil.getPrePage(request);
		BpmFormDef bpmFormDef = service.getById(id);
		return getAutoView().addObject("bpmFormDef", bpmFormDef).addObject(
				"returnUrl", preUrl);
	}

	/**
	 * 取得表fields, 如果是主表，同时取所有子表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getAllFieldsByTableId")
	public String getAllFieldsByTableId(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("tableId") Long tableId)
			throws Exception {
		StringBuffer sb = new StringBuffer();

		BpmFormTable mainTable = bpmFormTableService.getById(tableId);
		List<BpmFormField> mainTableFields = bpmFormFieldService
				.getByTableId(tableId);
		sb.append("{mainname:\"");
		// 表没有填写描述时，取表名
		sb.append(StringUtil.isEmpty(mainTable.getTableDesc()) ? mainTable
				.getTableName() : mainTable.getTableDesc());
		sb.append("\",mainid:");
		sb.append(tableId);
		sb.append(",mainfields:");
		JSONArray jArray = (JSONArray) JSONArray.fromObject(mainTableFields);
		String s = jArray.toString();
		sb.append(s);
		sb.append(",subtables:[");

		List<BpmFormTable> subTables = bpmFormTableService
				.getSubTableByMainTableId(tableId);
		for (int i = 0; i < subTables.size(); i++) {
			BpmFormTable subTable = subTables.get(i);
			Long subTableId = subTable.getTableId();
			List<BpmFormField> subFields = bpmFormFieldService
					.getByTableId(subTableId);
			if (i > 0)
				sb.append(",");
			sb.append("{name:\"");
			sb.append(StringUtil.isEmpty(subTable.getTableDesc()) ? subTable
					.getTableName() : subTable.getTableDesc());
			sb.append("\",id:");
			sb.append(subTableId);
			sb.append(",subfields:");
			JSONArray subArray = (JSONArray) JSONArray.fromObject(subFields);
			sb.append(subArray.toString());
			sb.append("}");
		}
		sb.append("]}");
		return sb.toString();
	}

	/**
	 * 根据表和表单定义ID获取表单权限信息。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getPermissionByTableFormKey")
	public Map<String, List<JSONObject>> getPermissionByTableFormKey(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		Map<String, List<JSONObject>> permission = bpmFormRightsService
				.getPermissionByTableFormKey(tableId, formKey);
		return permission;
	}

	/**
	 * 根据流程定义，任务节点，表单定义id获取权限信息。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getPermissionByFormNode")
	public Map<String, List<JSONObject>> getPermissionByFormNode(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		Map<String, List<JSONObject>> permission = bpmFormRightsService
				.getPermissionByFormNode(actDefId, nodeId, formKey);
		return permission;
	}

	/**
	 * 保存表单权限。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("savePermission")
	public void savePermission(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String permission = request.getParameter("permission");
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		JSONObject jsonObject = JSONObject.fromObject(permission);
		ResultMessage resultMessage = null;
		try {
			// 设置节点权限。
			if (StringUtil.isNotEmpty(nodeId)) {
				bpmFormRightsService
						.save(actDefId, nodeId, formKey, jsonObject);
			}
			// 根据表单key保存权限。
			else {
				bpmFormRightsService.save(formKey, jsonObject);
			}

			resultMessage = new ResultMessage(ResultMessage.Success,
					"表单权限保存成功!");
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,
						"表单权限保存失败:" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		out.print(resultMessage);
	}

	/**
	 * 根据模板产生html。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("genByTemplate")
	public void genByTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] templateTableId = RequestUtil.getLongAryByStr(request,
				"templateTableId");
		Long[] templateId = RequestUtil.getLongAryByStr(request, "templatesId");
		PrintWriter out = response.getWriter();
		String html = genTemplate(templateTableId, templateId);
		out.println(html);
	}

	/**
	 * 根据模板别名返回 编辑器设计模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getHtmlByAlias")
	public void getHtmlByAlias(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tempalias = RequestUtil.getString(request, "tempalias");
		PrintWriter out = response.getWriter();
		String templatePath = FormUtil.getDesignTemplatePath();
		String reult = FileUtil.readFile(templatePath + tempalias + ".html");
		out.println(reult);
	}

	/**
	 * 根据表和指定的html生成表单。
	 * 
	 * @param tableIds
	 * @param tableTemplateIds
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	private String genTemplate(Long[] tableIds, Long[] tableTemplateIds)
			throws TemplateException, IOException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tableIds.length; i++) {
			// 表
			Map<String, Object> fieldsMap = new HashMap<String, Object>();
			BpmFormTable table = bpmFormTableService.getById(tableIds[i]);
			List<BpmFormField> fields = bpmFormFieldService
					.getByTableId(tableIds[i]);
			fieldsMap.put("table", table);
			fieldsMap.put("fields", fields);
			for (BpmFormField field : fields) {
				field.setFieldName((table.getIsMain() == 1 ? "m:" : "s:")
						+ table.getTableName() + ":" + field.getFieldName());
			}
			// 模板
			BpmFormTemplate tableTemplate = bpmFormTemplateService
					.getById(tableTemplateIds[i]);
			BpmFormTemplate macroTemplate = bpmFormTemplateService
					.getByTemplateAlias(tableTemplate.getMacroTemplateAlias());
			String macroHtml = "";
			if (macroTemplate != null) {
				macroHtml = macroTemplate.getHtml();
			}
			String result = freemarkEngine.parseByStringTemplate(fieldsMap,
					macroHtml + tableTemplate.getHtml());
			if (table.getIsMain() == 1) {
				sb.append(result);
			} else {
				sb.append("<div type=\"subtable\" tableName=\"");
				sb.append(table.getTableName());
				sb.append("\">\n");
				sb.append(result);
				sb.append("</div>\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 发布
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("publish")
	@Action(description = "发布", operateType = "自定义表单")
	public void publish(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String prevPage = RequestUtil.getPrePage(request);
		ResultMessage resultObj = null;
		try {
			service.publish(formDefId, ContextUtil.getCurrentUser()
					.getFullname());
			resultObj = new ResultMessage(ResultMessage.Success, "发布版本成功!");
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = new ResultMessage(ResultMessage.Fail, e.getCause()
					.toString());
		}
		addMessage(resultObj, request);
		response.sendRedirect(prevPage);
	}

	/**
	 * 查看版本
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("versions")
	@Action(description = "查看版本", operateType = "自定义表单")
	public ModelAndView versions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = getAutoView();

		String formKey = request.getParameter("formKey");

		// 版本信息
		List<BpmFormDef> versions = service.getByFormKey(Long
				.parseLong(formKey));
		result.addObject("versions", versions).addObject("formName",
				versions.get(0).getSubject());
		return result;
	}

	/**
	 * 设置默认版本
	 * 
	 * @param request
	 * @param response
	 * @param formDefId
	 * @param formDefId
	 * @throws Exception
	 */
	@RequestMapping("setDefaultVersion")
	@Action(description = "设置默认版本", operateType = "自定义表单")
	public void setDefaultVersion(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("formDefId") Long formDefId,
			@RequestParam("formKey") Long formKey) throws Exception {
		ResultMessage resultObj = new ResultMessage(ResultMessage.Success,
				"设置默认版本成功!");
		String preUrl = RequestUtil.getPrePage(request);
		service.setDefaultVersion(formDefId, formKey);
		addMessage(resultObj, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 选择器
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selector")
	@Action(description = "选择器", operateType = "自定义表单")
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter queryFilter = new QueryFilter(request, "bpmFormDefItem");
		List<BpmFormDef> list = service.getPublished(queryFilter);
		ModelAndView mv = this.getAutoView().addObject("bpmFormDefList", list);
		return mv;
	}

	/**
	 * 根据表单定义id获取是否可以删除。
	 * 
	 * @param formDefId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getFlowUsed")
	public void getFlowUsed(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		long formDefId = RequestUtil.getLong(request, "formDefId");
		int rtn = service.getFlowUsed(formDefId);
		out.println(rtn);
	}

	/**
	 * 设计表。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "designTable", method = RequestMethod.POST)
	public ModelAndView designTable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String content = request.getParameter("content");
		System.out.println(content);
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		Long categoryId = RequestUtil.getLong(request, "categoryId");

		String subject = RequestUtil.getString(request, "subject");
		String formDesc = RequestUtil.getString(request, "formDesc");
		String tabTitle = RequestUtil.getString(request, "tabTitle");

		ParseReult result = FormUtil.parseHtmlNoTable(content, "", "");

		String tableName = RequestUtil.getString(request, "tableName");
		String tableComment = RequestUtil.getString(request, "tableComment");

		boolean canEditTableName = true;

		if (formDefId > 0) {
			BpmFormDef bpmFormDef = service.getById(formDefId);
			Long tableId = bpmFormDef.getTableId();
			if (tableId > 0) {
				canEditTableName = bpmFormTableService
						.getCanEditColumnNameTypeByTableId(tableId);
			}
		}

		ModelAndView mv = this.getAutoView();
		mv.addObject("result", result).addObject("content", content)
				.addObject("formDefId", formDefId)
				.addObject("subject", subject)
				.addObject("categoryId", categoryId)
				.addObject("formDesc", formDesc)
				.addObject("tabTitle", tabTitle)
				.addObject("tableName", tableName)
				.addObject("tableComment", tableComment)
				.addObject("canEditTableName", canEditTableName);
		return mv;
	}

	/**
	 * 对表单的html进行验证，验证html是否合法。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "validDesign", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> validDesign(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String html = request.getParameter("html");
		String tableName = RequestUtil.getString(request, "tableName");
		String tableComment = RequestUtil.getString(request, "tableComment");
		// boolean isTableExist=false;
		Long tableId = 0L;
		if (formDefId > 0) {
			BpmFormDef bpmFormDef = service.getById(formDefId);
			tableId = bpmFormDef.getTableId();
		}
		// 输入了主表名。
		// 验证主表名称。
		if (StringUtil.isNotEmpty(tableName)) {
			if (tableId > 0) {
				BpmFormTable bpmFormTable = bpmFormTableService
						.getById(tableId);
				// 输入的表名和原来的表名不一致的情况。
				if (tableName.equalsIgnoreCase(bpmFormTable.getTableName())) {
					boolean isTableExist = bpmFormTableService
							.isTableNameExisted(tableName);
					if (isTableExist) {
						map.put("valid", false);
						map.put("errorMsg", "表:" + tableName + ",在系统中已经存在!");
						return map;
					}
				}
			} else {
				boolean isTableExist = bpmFormTableService
						.isTableNameExisted(tableName);
				if (isTableExist) {
					map.put("valid", false);
					map.put("errorMsg", "表:" + tableName + ",在系统中已经存在!");
					return map;
				}
			}
		}

		ParseReult result = FormUtil.parseHtmlNoTable(html, tableName,
				tableComment);

		BpmFormTable bpmFormTable = result.getBpmFormTable();
		// 验证子表。
		String strValid = validSubTable(bpmFormTable, tableId);

		if (StringUtil.isNotEmpty(strValid)) {
			map.put("valid", false);
			map.put("errorMsg", strValid);
			return map;
		}
		// 验证表单是否有错。
		boolean rtn = result.hasErrors();
		if (rtn) {
			map.put("valid", false);
			map.put("errorMsg", result.getError());
		} else {
			map.put("valid", true);
			map.put("table", result.getBpmFormTable());
		}
		return map;

	}

	/**
	 * 验证子表系统中是否存在。
	 * 
	 * <pre>
	 * 	对表单的子表循环。
	 * 		1.表单还没有生成子表的情。
	 * 			验证子表表名系统中是否存在。
	 * 		2.已经生成子表。
	 * 			判断当前子表是否在原子表的列表中，如果不存在，则表示该子表为新添加的表，需要验证子表系统中是否已经存在。
	 * 
	 * </pre>
	 * 
	 * @param bpmFormTable
	 * @param tableId
	 * @return
	 */
	private String validSubTable(BpmFormTable bpmFormTable, Long tableId) {

		List<BpmFormTable> subTableList = bpmFormTable.getSubTableList();
		if (BeanUtils.isEmpty(subTableList))
			return "";
		String str = "";
		for (BpmFormTable subTable : subTableList) {
			String tableName = subTable.getTableName().toLowerCase();
			// 还没有生成表的情况。
			if (tableId == 0) {
				boolean isTableExist = bpmFormTableService
						.isTableNameExisted(tableName);
				if (isTableExist) {
					str += "子表:【" + tableName + "】系统中已经存在!<br/>";
				}
			}
			// 表已经生成的情况。
			else {
				BpmFormTable orginTable = bpmFormTableService
						.getTableById(tableId);
				List<BpmFormTable> orginSubTableList = orginTable
						.getSubTableList();
				Map<String, BpmFormTable> mapSubTable = new HashMap<String, BpmFormTable>();
				for (BpmFormTable table : orginSubTableList) {
					mapSubTable.put(table.getTableName().toLowerCase(), table);
				}
				// 原子表中不存在该表，表示该表为新添加的表。
				if (!mapSubTable.containsKey(tableName)) {
					boolean isTableExist = bpmFormTableService
							.isTableNameExisted(tableName);
					if (isTableExist) {
						str += "子表:【" + tableName + "】系统中已经存在!<br/>";
					}
				}
			}
		}
		return str;
	}

	/**
	 * 设计时对表单进行预览。
	 * 
	 * <pre>
	 * 	步骤：
	 * 	1.获取设计的html。
	 *  2.对设计的html解析。
	 *  3.生成相应的freemaker模版。
	 *  4.解析html模版输入实际的html。
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("preview")
	@Action(description = "编辑器设计表单预览", operateType = "自定义表单")
	public ModelAndView preview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId", 0);

		String html = "";
		String name = "";
		String comment = "";
		String title = "";
		// 在列表页面点击预览
		Map<String, Map<String, String>> permission = null;
		if (formDefId > 0) {
			BpmFormDef bpmFormDef = service.getById(formDefId);
			html = bpmFormDef.getHtml();
			name = bpmFormDef.getSubject();
			comment = bpmFormDef.getFormDesc();
			title = bpmFormDef.getTabTitle();
			permission = bpmFormRightsService.getByFormKeyAndUserId(
					bpmFormDef.getFormKey(), ContextUtil.getCurrentUserId(),
					"", "");
		}
		// 在编辑页面预览
		else {
			html = request.getParameter("html");
			name = RequestUtil.getString(request, "name");
			title = RequestUtil.getString(request, "title");
			comment = RequestUtil.getString(request, "comment");
		}

		ParseReult result = FormUtil.parseHtmlNoTable(html, name, comment);
		String outHtml = bpmFormHandlerService.obtainHtml(title, result,
				permission);
		ModelAndView mv = this.getAutoView();
		mv.addObject("html", outHtml);
		return mv;
	}

	/**
	 * 保存表单。
	 * 
	 * <pre>
	 * 	1.新建表单的情况，不发布。
	 * 		1.添加表单定义。
	 * 		2.添加表定义。
	 * 	2.新建表单发，发布。
	 * 		1.添加表单定义。
	 * 		2.添加表定义。
	 *  3.编辑表单，未发布的情况。
	 *  	1.编辑表单定义。
	 *  	2.删除表定义重新添加。
	 *  4.编辑表单，已经发布。
	 *  	1.编辑表单定义
	 *  	2.是否有多个版本，或者已经有数据。
	 *  		1.是只对表进行编辑。
	 *  		2.否则删除表重建。
	 * 
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "saveForm", method = RequestMethod.POST)
	public void saveForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		String subject = RequestUtil.getString(request, "subject");
		String formDesc = RequestUtil.getString(request, "formDesc");
		String tableName = RequestUtil.getString(request, "tableName");
		String tableComment = RequestUtil.getString(request, "tableComment");
		String title = RequestUtil.getString(request, "tabTitle");
		String html = request.getParameter("html");
		int publish = RequestUtil.getInt(request, "publish", 0);
		String json = request.getParameter("json");

		html = html.replace("？", "");
		html = html.replace("<p>﻿</p>", "");
		ParseReult result = FormUtil.parseHtmlNoTable(html, tableName,
				tableComment);

		ISysUser sysUser = ContextUtil.getCurrentUser();
		String userName = sysUser.getFullname();
		BpmFormDef bpmFormDef = null;
		if (formDefId == 0) {
			bpmFormDef = new BpmFormDef();
			bpmFormDef.setCategoryId(categoryId);
		} else {
			bpmFormDef = service.getById(formDefId);
		}

		ResultMessage resultMessage = null;
		String message = null;
		Long tableId = bpmFormDef.getTableId();
		boolean isTableExist = false;
		// 输入了主表名。
		// 验证主表名称。
		if (StringUtil.isNotEmpty(tableName)) {
			if (tableId > 0) {
				BpmFormTable bpmFormTable = bpmFormTableService
						.getById(tableId);
				// 输入的表名和原来的表名不一致的情况。
				if (!tableName.equalsIgnoreCase(bpmFormTable.getTableName())) {
					isTableExist = bpmFormTableService
							.isTableNameExisted(tableName);
				}
			} else {
				isTableExist = bpmFormTableService
						.isTableNameExisted(tableName);
			}
		}
		if (isTableExist) {
			resultMessage = new ResultMessage(ResultMessage.Fail, "表【"
					+ tableName + "】在数据库中已存在!");
			response.getWriter().print(resultMessage);
			return;
		}

		bpmFormDef.setSubject(subject);
		bpmFormDef.setFormDesc(formDesc);
		bpmFormDef.setTabTitle(title);
		bpmFormDef.setHtml(html);
		bpmFormDef.setTemplate(result.getTemplate());

		bpmFormDef.setPublishedBy(userName);

		message = (publish == 1) ? "表单发布成功!" : "保存表单成功!";

		resultMessage = new ResultMessage(ResultMessage.Success, message);
		try {
			// 是否发布
			boolean isPublish = (publish == 1);
			// 通过解析后的到的表对象。
			BpmFormTable bpmFormTable = result.getBpmFormTable();
			// 在designTable设置表的参数。
			setBpmFormTable(bpmFormTable, userName, json);
			service.saveForm(bpmFormDef, bpmFormTable, isPublish);
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail, "保存表单失败:"
						+ str);
			} else {
				message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		response.getWriter().print(resultMessage);
	}

	/**
	 * 根据json设置表的属性。
	 * 
	 * @param bpmFormTable
	 * @param userName
	 * @param json
	 */
	private void setBpmFormTable(BpmFormTable bpmFormTable, String userName,
			String json) {
		bpmFormTable.setPublishedBy(userName);
		bpmFormTable.setPublishTime(new Date());

		List<BpmFormTable> subTableList = bpmFormTable.getSubTableList();

		for (BpmFormTable subTable : subTableList) {
			subTable.setPublishedBy(userName);
			subTable.setPublishTime(new Date());
		}
		if (StringUtil.isEmpty(json))
			return;

		JSONObject jsonObject = JSONObject.fromObject(json);
		// 主表字段。
		JSONArray mainRows = jsonObject.getJSONArray("rows");
		Map<String, JSONObject> mainMap = new HashMap<String, JSONObject>();
		for (int i = 0; i < mainRows.size(); i++) {
			JSONObject jsonObj = mainRows.getJSONObject(i);
			String fieldName = jsonObj.getString("fieldName").toLowerCase();
			mainMap.put(fieldName, jsonObj);
		}

		Map<String, Map<String, JSONObject>> subMap = new HashMap<String, Map<String, JSONObject>>();
		JSONArray arySubTable = jsonObject.getJSONArray("subTable");
		for (int i = 0; i < arySubTable.size(); i++) {
			JSONObject jsonObj = arySubTable.getJSONObject(i);
			String tableName = jsonObj.getString("tableName").toLowerCase();
			JSONArray arySubRows = jsonObj.getJSONArray("rows");
			Map<String, JSONObject> rowMap = new HashMap<String, JSONObject>();
			for (int k = 0; k < arySubRows.size(); k++) {
				JSONObject rowObj = arySubRows.getJSONObject(k);
				rowMap.put(rowObj.getString("fieldName"), rowObj);
			}
			subMap.put(tableName, rowMap);
		}
		// 更新主表字段
		List<BpmFormField> mainFields = bpmFormTable.getFieldList();
		for (BpmFormField field : mainFields) {
			String fieldName = field.getFieldName().toLowerCase();
			if (mainMap.containsKey(fieldName)) {
				JSONObject mainJson = mainMap.get(fieldName);
				field.setIsList((short) mainJson.getInt("isList"));
				field.setIsRequired((short) mainJson.getInt("isRequired"));
				field.setIsFlowVar((short) mainJson.getInt("isFlowVar"));
				field.setIsAllowMobile((short) mainJson.getInt("isAllowMobile"));
			}
		}

		// 更新子表
		if (BeanUtils.isEmpty(subTableList)) {
			return;
		}
		for (BpmFormTable subTable : subTableList) {
			String tableName = subTable.getTableName().toLowerCase();
			if (!subMap.containsKey(tableName))
				continue;
			Map<String, JSONObject> mapSubField = subMap.get(tableName);
			List<BpmFormField> subFields = subTable.getFieldList();
			for (BpmFormField field : subFields) {
				String fieldName = field.getFieldName().toLowerCase();
				if (!mapSubField.containsKey(fieldName))
					continue;
				JSONObject subJson = mapSubField.get(fieldName);
				short isRequired = (short) subJson.getInt("isRequired");
				field.setIsRequired(isRequired);
				short isAllowMobile = (short) subJson.getInt("isAllowMobile");
				field.setIsAllowMobile(isAllowMobile);
			}
			subTable.setPublishedBy(userName);
			subTable.setPublishTime(new Date());
		}
	}

	/**
	 * 复制表单（克隆表单）
	 * 
	 * <pre>
	 * 1、获取要复制的表单对象；
	 * 2、重新生成表单ID（formDefId）、表单key（formKey）；
	 * 3、打开复制表单的设置页面，让用户设置重新设置『表单标题』、『表单分类』、『表单描述』；
	 * 4、保存以后，复制的表单和原表单使用同样的表，表可以添加字段和修改字段（修改的字段不能修改字段名和类型）。
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("copy")
	@Action(description = "复制表单", operateType = "自定义表单")
	public ModelAndView copy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId", 0);
		BpmFormDef bpmFormDef = null;

		if (formDefId > 0) {
			bpmFormDef = service.getById(formDefId);
		}
		Long cateId = bpmFormDef.getCategoryId();
		if (cateId != null && cateId != 0) {
			GlobalType globalType = globalTypeDao.getById(bpmFormDef
					.getCategoryId());
			bpmFormDef.setCategoryName(globalType.getTypeName());
		}
		ModelAndView mv = this.getAutoView();
		mv.addObject("bpmFormDef", bpmFormDef);
		return mv;
	}

	/**
	 * 保存克隆
	 * 
	 * @param request
	 * @param response
	 * @param po
	 * @throws Exception
	 */
	@RequestMapping("saveCopy")
	@Action(description = "保存克隆")
	public void saveCopy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		long formDefId = RequestUtil.getLong(request, "formDefId");
		String formName = RequestUtil.getString(request, "formName");
		Long typeId = RequestUtil.getLong(request, "typeId");
		String formDesc = RequestUtil.getString(request, "formDesc");

		BpmFormDef bpmFormDef = service.getById(formDefId);
		long oldFormKey = bpmFormDef.getFormKey();
		if (bpmFormDef != null) {
			if (!StringUtil.isEmpty(formName)) {
				bpmFormDef.setSubject(formName);
			}
			if (typeId > 0) {
				bpmFormDef.setCategoryId(typeId);
			}
			if (!StringUtil.isEmpty(formDesc)) {
				bpmFormDef.setFormDesc(formDesc);
			}
			long id = UniqueIdUtil.genId();
			bpmFormDef.setFormDefId(id);
			bpmFormDef.setFormKey(id);
			bpmFormDef.setIsPublished((short) 0);
			bpmFormDef.setIsDefault((short) 1);
			bpmFormDef.setVersionNo(1);
			try {
				service.copyForm(bpmFormDef, oldFormKey);
				writeResultMessage(writer, "复制表单成功!", ResultMessage.Success);
			} catch (Exception ex) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					ResultMessage resultMessage = new ResultMessage(
							ResultMessage.Fail, "复制表单失败:" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(ex);
					ResultMessage resultMessage = new ResultMessage(
							ResultMessage.Fail, message);
					response.getWriter().print(resultMessage);
				}
			}
		} else {
			writeResultMessage(writer, "未能获取到要复制的表单", ResultMessage.Fail);
			return;
		}
	}

	/**
	 * 导入表单源文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importForm")
	@Action(description = "导入表单源文件", operateType = "自定义表单")
	public void importForm(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("importInput");
		String str = FileUtil.inputStream2String(fileLoad.getInputStream());
		response.getWriter().print(str);
	}

	/**
	 * 导出表单源文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportForm")
	@Action(description = "导出表单", operateType = "自定义表单")
	public void exportForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String title = RequestUtil.getString(request, "title");
		String html = request.getParameter("html");
		if (StringUtil.isNotEmpty(title)) {
			html += "#content-title#" + title;
		}
		String subject = request.getParameter("subject") + ".html";
		response.setContentType("APPLICATION/OCTET-STREAM");
		String filedisplay = StringUtil.encodingString(subject, "GBK",
				"ISO-8859-1");
		response.addHeader("Content-Disposition", "attachment;filename="
				+ filedisplay);
		response.getWriter().write(html);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 初始化表单权限设置
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("initRights")
	public ResultMessage initRights(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResultMessage resultMessage = null;
		try {
			Long formKey = RequestUtil.getLong(request, "formkey", 0);
			bpmFormRightsService.deleteByFormKey(formKey);
			resultMessage = new ResultMessage(ResultMessage.Success,
					"重置表单权限设置成功!");
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,
						"重置表单权限设置失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
		return resultMessage;
	}

	/**
	 * 判断是否有子表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("isSubTable")
	public String isSubTable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer sb = new StringBuffer();
		Long formKey = RequestUtil.getLong(request, "formKey", 0);
		BpmFormDef bpmFormDef = service.getById(formKey);
		if (BeanUtils.isNotEmpty(bpmFormDef)
				&& BeanUtils.isNotEmpty(bpmFormDef.getTableId())) {
			List<BpmFormTable> list = bpmFormTableService
					.getSubTableByMainTableId(bpmFormDef.getTableId());
			if (BeanUtils.isNotEmpty(list)) {
				sb.append("{success:true,tableId:")
						.append(bpmFormDef.getTableId()).append("}");
			} else {
				sb.append("{success:false,msg:'该表单没有子表,不需要设置子表数据权限！'}");
			}
		} else {
			sb.append("{success:false,msg:'未获得表单信息！'}");
		}

		return sb.toString();
	}

	/**
	 * 流程表单子表授权
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("subRightsDialog")
	@Action(description = "流程表单子表授权", operateType = "自定义表单")
	public ModelAndView subRightsDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		Long tableId = RequestUtil.getLong(request, "tableId");

		ModelAndView mv = this.getAutoView();
		mv.addObject("actDefId", actDefId);
		mv.addObject("nodeId", nodeId);
		mv.addObject("formKey", formKey);
		mv.addObject("tableId", tableId);
		return mv;
	}

	/**
	 * 导出xml窗口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("export")
	@Action(description = " 导出xml窗口", operateType = "自定义表单")
	public ModelAndView export(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String formDefIds = RequestUtil.getString(request, "formDefIds");

		ModelAndView mv = this.getAutoView();
		mv.addObject("formDefIds", formDefIds);
		return mv;
	}

	/**
	 * 导出自定义表单XML
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportXml")
	@Action(description = "导出自定义表单XML", operateType = "自定义表单")
	public void exportXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] formDefIds = RequestUtil.getLongAryByStr(request, "formDefIds");

		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("bpmFormDef", true);
		map.put("bpmFormTable", RequestUtil.getBoolean(request, "bpmFormTable"));
		map.put("bpmFormDefOther",
				RequestUtil.getBoolean(request, "bpmFormDefOther"));
		map.put("bpmFormRights",
				RequestUtil.getBoolean(request, "bpmFormRights"));
		map.put("bpmTableTemplate",
				RequestUtil.getBoolean(request, "bpmTableTemplate"));

		if (BeanUtils.isNotEmpty(formDefIds)) {
			String fileName = "formDef_"
					+ DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
			String strXml = service.exportXml(formDefIds, map);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(fileName, "UTF-8") + ".xml");
			response.getWriter().write(strXml);
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	/**
	 * 导入自定义表单的XML。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importXml")
	@Action(description = "导入自定义表单", operateType = "自定义表单")
	public void importXml(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage message = null;
		try {
			service.importXml(fileLoad.getInputStream());
			message = new ResultMessage(ResultMessage.Success,
					MsgUtil.getMessage());
		} catch (Exception e) {
			e.getStackTrace();
			message = new ResultMessage(ResultMessage.Fail,
					"导入出错了，请检查导入格式是否正确或者导入的数据是否有问题！");
		}
		writeResultMessage(response.getWriter(), message);
	}
}
