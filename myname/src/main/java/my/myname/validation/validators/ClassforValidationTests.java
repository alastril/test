package my.myname.validation.validators;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;


@Component
@AnnotationForValidation
public class ClassforValidationTests implements ValidationTestMarker {

	private long id;
	@NotNull
	@Size(min=2)
	private String name;
	private DateTime dt;

	public DateTime getDt() {
		return dt;
	}

	public void setDt(DateTime dt) {
		this.dt = dt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "id:" + getId() + ", name:" + getName() + ", date:" + getDt();
	}

}
