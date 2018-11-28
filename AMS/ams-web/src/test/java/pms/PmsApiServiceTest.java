package pms;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yuminsoft.ams.system.service.approve.StaffOrderTaskService;
import com.yuminsoft.ams.system.service.pms.PmsApiService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/**.xml")
public class PmsApiServiceTest
{
    
    private static final Logger log = LoggerFactory.getLogger(PmsApiServiceTest.class);
    
    @Autowired
    private PmsApiService pmsApiService;
    
    @Autowired
    private StaffOrderTaskService staffOrderTaskService;
    
    @Value("${sys.code}")//系统编码
    private String sysCode;

    @Test
    public void test()
    {
        fail("Not yet implemented");
    }
    
    /**
     * 
     * 根据员工号获取组长测试
     * 
     * @author JiaCX
     * @date 2017年8月3日 下午5:00:05
     */
    @Test
    public void findGroupLeaderByUserCodeTest(){
//        ReqParamVO reqParamVO = new ReqParamVO();
//        reqParamVO.setLoginUser("chenlp");
//        reqParamVO.setOrgTypeCode("check");
//        ResEmployeeVO resEmployeeVO = pmsApiService.findGroupLeaderByUserCode(reqParamVO);
//        if(null != resEmployeeVO){
//            log.info("***********************"+resEmployeeVO.getUsercode());
//        }
    }
    
    /**
     * 测试同步员工
     * 
     * @author Jia CX
     * @date 2017年12月27日 下午5:43:07
     * @notes
     * 
     */
    @Test
    public void synchronizeEmpInfoNewTest() {
    	staffOrderTaskService.synchronizeEmpInfoNew();
    }

}
