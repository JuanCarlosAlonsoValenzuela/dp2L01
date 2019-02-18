
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.CoachRepository;
import domain.Coach;

@Component
@Transactional
public class StringToCoachConverter implements Converter<String, Coach> {

	@Autowired
	CoachRepository	coachRepository;


	@Override
	public Coach convert(String text) {

		Coach result = new Coach();
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.coachRepository.findOne(id);
			}

		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
