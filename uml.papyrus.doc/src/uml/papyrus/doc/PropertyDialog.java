package uml.papyrus.doc;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.uml2.uml.Property;

public class PropertyDialog extends Dialog {
  String title;
  List<Property> selectedElements;


  public PropertyDialog(Shell parent, String title, List<Property> selectedElements) {
    super(parent);
    this.title = title;
    this.selectedElements = selectedElements;
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);

    Composite namesContainer = new Composite(container, SWT.NONE);
    namesContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    namesContainer.setLayout(new GridLayout(2, false));

    for (Property element : selectedElements) {
      Label quickSetLabel = new Label(namesContainer, SWT.NONE);
      quickSetLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      quickSetLabel.setText(element.getName());
    }

    return container;
  }
}
