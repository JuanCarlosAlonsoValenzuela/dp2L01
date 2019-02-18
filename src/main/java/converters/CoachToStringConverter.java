
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Coach;

@Component
@Transactional
public class CoachToStringConverter implements Converter<Coach, String> {

	@Override
	public String convert(Coach coach) {
		String result;

		if (coach == null)
			result = null;
		else
			result = String.valueOf(coach.getId());
		return result;
	}

}
