package example.serializer;

public class Employee implements java.io.Serializable {
	private static final long serialVersionUID = 2938709844433835223L;
	public String name;
	public String address;
	transient public int SSN;
	public int number;

	public void mailCheck() {
		System.out.println("Mailing a check to " + name + " " + address);
	}
}
