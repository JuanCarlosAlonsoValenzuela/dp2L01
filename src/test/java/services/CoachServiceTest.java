
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Coach;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CoachServiceTest extends AbstractTest {

	@Autowired
	private CoachService	coachService;


	@Test
	public void testCreate() {
		Coach coach = new Coach();

		coach = this.coachService.create();

		coach.setTitle("sasa");
		coach.setDescription("sasa");

		Coach saved = new Coach();
		saved = this.coachService.save(coach);
		Assert.isTrue(this.coachService.findAll().contains(saved));

	}

}
