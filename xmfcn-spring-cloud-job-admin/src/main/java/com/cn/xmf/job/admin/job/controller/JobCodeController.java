package com.cn.xmf.job.admin.job.controller;

import com.cn.xmf.job.admin.job.dao.XxlJobInfoDao;
import com.cn.xmf.job.admin.job.dao.XxlJobLogGlueDao;
import com.cn.xmf.job.admin.core.model.XxlJobInfo;
import com.cn.xmf.job.admin.core.model.XxlJobLogGlue;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.job.core.glue.GlueTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * job code controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {
	
	@Resource
	private XxlJobInfoDao xxlJobInfoDao;
	@Resource
	private XxlJobLogGlueDao xxlJobLogGlueDao;

	@RequestMapping
	public String index(Model model, int jobId) {
		XxlJobInfo jobInfo = xxlJobInfoDao.loadById(jobId);
		List<XxlJobLogGlue> jobLogGlues = xxlJobLogGlueDao.findByJobId(jobId);

		if (jobInfo == null) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_gluetype_unvalid"));
		}

		// Glue类型-字典
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());

		model.addAttribute("jobInfo", jobInfo);
		model.addAttribute("jobLogGlues", jobLogGlues);
		return "jobcode/jobcode.index";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public ReturnT<String> save(Model model, int id, String glueSource, String glueRemark) {
		// valid
		if (glueRemark==null) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")) );
		}
		if (glueRemark.length()<4 || glueRemark.length()>100) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_remark_limit"));
		}
		XxlJobInfo existsJobinfo = xxlJobInfoDao.loadById(id);
		if (existsJobinfo == null) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		
		// update new code
		existsJobinfo.setGlueSource(glueSource);
		existsJobinfo.setGlueRemark(glueRemark);
		existsJobinfo.setGlueUpdatetime(new Date());
		xxlJobInfoDao.update(existsJobinfo);

		// user old code
		XxlJobLogGlue xxlJobLogGlue = new XxlJobLogGlue();
		xxlJobLogGlue.setJobId(existsJobinfo.getId());
		xxlJobLogGlue.setGlueType(existsJobinfo.getGlueType());
		xxlJobLogGlue.setGlueSource(glueSource);
		xxlJobLogGlue.setGlueRemark(glueRemark);
		xxlJobLogGlueDao.save(xxlJobLogGlue);

		// remove code backup more than 30
		xxlJobLogGlueDao.removeOld(existsJobinfo.getId(), 30);

		return ReturnT.SUCCESS;
	}
	
}
