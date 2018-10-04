package si.fri.algotest.entities;

/**
 * Name and abreviation of an entity
 * @author tomaz
 */
public class NameAndAbrev {
  
  public static final String AS = " AS ";

  private String name;
  private String abrev;

  public NameAndAbrev(String name, String abrev) {
    this.name = name;
    this.abrev = abrev;
  }
  
  public NameAndAbrev(String nameAndAbrev) {
    String parts [] = nameAndAbrev.split(AS);
    name = parts[0];
    
    if (parts.length > 1)
      abrev = parts[1];
    else
      abrev = name;
  }
  
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAbrev() {
    if (abrev==null || abrev.isEmpty())
      return name;
    else
      return abrev;
  }

  public void setAbrev(String abrev) {
    this.abrev = abrev;
  }

  @Override
  public String toString() {
    String abrv = (abrev == null || abrev.isEmpty()) ? name : abrev;
    return getName() + AS + abrv;
  }

  @Override
  public boolean equals(Object obj) {
    return this.toString().equals(obj.toString());
  }
  
}