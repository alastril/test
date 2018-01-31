package my.myname.validation.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import my.myname.validation.validators.class_test.ClassforValidationTests;

@Component
public class ValidatorImpl implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return ClassforValidationTests.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
//		ClassforValidationTests targetCast = (ClassforValidationTests) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "ClassforValidationTests.NotNull");
	}

}
