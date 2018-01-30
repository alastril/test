package my.myname.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The Class ConstraintValidatorImplWithAnnotation.
 * Validate {@link ClassforValidationTests} with help of annotation {@link AnnotationForValidation}
 */
public class ConstraintValidatorImplWithAnnotation implements ConstraintValidator<AnnotationForValidation, ClassforValidationTests>{

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(ClassforValidationTests arg0, ConstraintValidatorContext arg1) {
		return arg0.getName()!=null && arg0.getDt()!=null? true:false;
	}

}
