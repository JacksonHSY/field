package approve;

import com.bstek.uflo.model.task.Task;
import com.yuminsoft.ams.system.service.uflo.impl.TaskServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/**.xml")
public class TestApplyInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestApplyInfo.class);

	
	@Value("${sys.code}")//系统编码
	private String sysCode;
	
	@Test 
	public void TestFindList(){
/*		RequestPage requestPage = new RequestPage();
		ReqIntegratedSearchVO vo  = new ReqIntegratedSearchVO();
		// 获取当前登录用户
		ResEmployeeVO currentUser = ShiroUtils.getCurrentUser();
		ResponsePage<ResIntegratedSearchVO> page = new ResponsePage<ResIntegratedSearchVO>();
		vo.setServiceCode(currentUser.getUsercode());
		vo.setServiceName(currentUser.getName());
		vo.setSysCode(sysCode);
//		String ip= WebUtils.retrieveClientIp(request);
		vo.setIp("127.0.0.1");
		vo.setPageNum(requestPage.getPage());
		vo.setPageSize(requestPage.getRows());
		vo.setSysCode(sysCode);
		PageResponse<ResIntegratedSearchVO> response = iIntegratedSearchExecuter.search(vo);
		LOGGER.info("返回借款系统综合查询 params:{} result:{}", JSON.toJSONString(vo), JSON.toJSONString(response));*/
	}
	
	
	@Autowired
	private TaskServiceImpl taskServiceImpl ;
	@Test 
	public void TestTask(){
		Task taks= taskServiceImpl.findTaskById(5108);
		System.out.println(taks.getProcessId());
	}
	

}
