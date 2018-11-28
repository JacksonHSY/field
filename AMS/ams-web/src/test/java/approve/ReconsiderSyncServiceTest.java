/**
 * 
 */
package approve;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yuminsoft.ams.system.service.approve.ReconsiderSyncService;

/**
 * @author Jia CX
 * <p>2018年6月13日 上午9:50:51</p>
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/**.xml")
public class ReconsiderSyncServiceTest {
	
	@Autowired
	private ReconsiderSyncService reconsiderSyncService;
	
	@Test
	public void synchronizeEmpInfoTest() {
		reconsiderSyncService.synchronizeEmpInfo();
	}

}
