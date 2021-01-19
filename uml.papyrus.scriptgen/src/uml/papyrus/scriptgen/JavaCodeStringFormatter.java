package uml.papyrus.scriptgen;

public class JavaCodeStringFormatter {

  public String appendLineSeparator(int numOfEmptyLines) {
    StringBuilder strb = new StringBuilder();
    if (numOfEmptyLines > 0) {
      for (int i = 0; i < numOfEmptyLines; ++i) {
        strb.append(appendLineSeparator());
      }
    }
    return strb.toString();
  }

  public String appendLineSeparator() {
    return System.lineSeparator();
  }

  public String appendTab(int numOfTabs) {
    StringBuilder strb = new StringBuilder();
    if (numOfTabs > 0) {
      for (int i = 0; i < numOfTabs; ++i) {
        strb.append("  ");
      }
    }
    return strb.toString();
  }

  public String appendTab() {
    StringBuilder strb = new StringBuilder();
    return strb.append("  ").toString();
  }


  public String formatImport(String value) {
    return "import " + value + ";";
  }

  public String createDeclaration(String name, String value) {
    return "public static final String " + name + " = \"" + value + "\";";
  }

  
}
