package gradesys;

public enum AuthPermissions {
	SUPER_USER("super"),
	USER("user"),
	STUDENT("student");
	
	String perms;
	
	AuthPermissions(String perms) {
		this.perms = perms;
	}
	
	public String toString() {
		return perms;
	}
}
