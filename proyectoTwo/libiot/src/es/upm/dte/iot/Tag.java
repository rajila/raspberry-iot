package es.upm.dte.iot;

public class Tag implements ITag {

	private String id;

	/**
	 * 
	 * @param id
	 */
	public Tag(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + "]";
	}

	@Override
	public String getIdentification() {
		return this.id;
	}

}