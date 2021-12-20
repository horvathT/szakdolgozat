package uml.papyrus.script.generator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.uml2.uml.Package;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import uml.papyrus.script.validator.ModelValidator;

public class ScriptGenerator {

	protected static final Bundle BUNDLE = FrameworkUtil.getBundle(ModelValidator.class);
	protected static final ILog LOGGER = Platform.getLog(BUNDLE);

	protected Package modelPackage;

	protected String filePath;

	public ScriptGenerator(Package modelPackage, String filePath) {
		this.modelPackage = modelPackage;
		this.filePath = filePath;
	}

	protected String appendLineSeparator(int numOfEmptyLines) {
		StringBuilder strb = new StringBuilder();
		if (numOfEmptyLines > 0) {
			for (int i = 0; i < numOfEmptyLines; ++i) {
				strb.append(appendLineSeparator());
			}
		}
		return strb.toString();
	}

	protected String appendLineSeparator() {
		return System.lineSeparator();
	}

	protected String appendTab(int numOfTabs) {
		StringBuilder strb = new StringBuilder();
		if (numOfTabs > 0) {
			for (int i = 0; i < numOfTabs; ++i) {
				strb.append(appendTab());
			}
		}
		return strb.toString();
	}

	protected String appendTab() {
		return "	";
	}

	protected String nullable(boolean isNullable) {
		if (isNullable) {
			return "NULL";
		}
		return "NOT NULL";
	}

	protected void writeToFile(String content, String path) {
		try (InputStream targetStream = new ByteArrayInputStream(content.getBytes())) {
			database.modeling.util.resource.EclipseResourceUtil.writeFile(path, targetStream);
		} catch (CoreException e) {
			LOGGER.error("Hiba történt a fájl írása során!", e);
		} catch (IOException e1) {
			LOGGER.error("Hiba történt a fájl írása során!", e1);
		}

	}

}
