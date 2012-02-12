package org.soap_wsdl_maker.plugin.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * @author wouldgo84@gmail.com
 * 
 * Its role is the page in the widzard plugin. 
 */

public class NewWsdlWizardPage extends WizardPage {
	private Text containerText, verbText, objText, operationText, providerUrlText, serviceHostText;
	private Table operationTable;
	private ISelection selection;
	private Button add, removeOperationBt;
	
	/**
	 * Constructor for NewWsdlWizardPage.
	 * 
	 * @param pageName
	 */
	public NewWsdlWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("WSDL Maker");
		setDescription("This wizard helps in wsdl creation.");
		this.selection = selection;
	}

	/**
	 * @see NewWsdlWizardPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 6;
		
		
		/* Adding the Project control here */
		Label label = new Label(container, SWT.NULL);
		label.setText("&Project:");

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText("Find...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		/* Adding the verb control here */
		label = new Label(container, SWT.NULL);
		label.setText("&Verb:");

		verbText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		verbText.setLayoutData(gd);
		verbText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText("");

		/* Adding the object control here */
		label = new Label(container, SWT.NULL);
		label.setText("&Object:");

		objText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		objText.setLayoutData(gd);
		objText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText("");
		
		/* Adding the service URL provider control here */
		label = new Label(container, SWT.NULL);
		label.setText("&Service URL Provider:");

		providerUrlText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		providerUrlText.setLayoutData(gd);
		providerUrlText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText("");
		
		/* Adding the Service Host provider control here */
		label = new Label(container, SWT.NULL);
		label.setText("&Service Host URL:");

		serviceHostText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		serviceHostText.setLayoutData(gd);
		serviceHostText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText("");
		
		/* Adding the operation node control here */
		label = new Label(container, SWT.NULL);
		label.setText("&Operation:");

		operationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		operationText.setLayoutData(gd);
		operationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		add = new Button(container, SWT.PUSH);
		add.setText("Add");
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addRowToList();
			}
		});

		/*
		 * Need to add empty label so the next two controls are pushed to the
		 * next line in the grid.
		 */

		label = new Label(container, SWT.NULL);
		label.setText("&Operations Summary:");
		
		/* Adding the operation summary table*/
		operationTable = new Table(container, SWT.BORDER | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_VERTICAL);
		operationTable.setLayoutData(gd);
	
		removeOperationBt = new Button(container, SWT.PUSH);
		removeOperationBt.setText("&Remove");
		removeOperationBt.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				removeOp();
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeOp();
			}
		});
		
		gd = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		removeOperationBt.setLayoutData(gd);
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
			}
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */
	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container.");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	private void addRowToList() {
		if (operationText.getText() != null && operationText.getText() != "") {
			TableItem item = new TableItem(operationTable, 0);
			item.setText(operationText.getText());

			operationText.setText("");
		}
	}
	
	private void removeOp() {
		if (operationTable != null && operationTable.getSelectionIndex() != -1) {
			operationTable.remove(operationTable.getSelectionIndex());
		}
	}

	/**
	 * Ensure that inputs are as expected.
	 */
	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));

		String verbName = getVerName(),
			objectName = getObjectName(),
			providerUrl = getProviderUrl(),
			serviceHost = getServiceHost();

		if (getContainerName().length() == 0) {
			updateStatus("It's mandatory select a project.");
			return;
		}

		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("The project must exist.");
			return;
		}

		if (!container.isAccessible()) {
			updateStatus("The project must be writeable.");
			return;
		}

		if (!verbName.matches("[A-Z][a-z]+")) {
			updateStatus("Verb must starts with a uppercase letter.");
			return;
		}

		if (!objectName.matches("[A-Z][A-Za-z_0-9]+")) {
			updateStatus("Object must starts with a uppercase letter.");
			return;
		}
		
		if (providerUrl.isEmpty()) {
			updateStatus("The provider URL is mandatory.");
			return;
		}
		
		if (serviceHost.isEmpty()) {
			updateStatus("The service Host URL is mandatory");
			return;
		}

		if (verbName.length() == 0 || 
				objectName.length() == 0 || 
				providerUrl.length() == 0 || 
				serviceHost.length() == 0) {
			updateStatus("All fields are mandatory.");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getVerName() {
		return verbText.getText();
	}

	public String getObjectName() {
		return objText.getText();
	}
	
	public String getProviderUrl() {
		return providerUrlText.getText();
	}

	public String getServiceHost() {
		return serviceHostText.getText();
	}

	public List<String> getOperationNames() {
		List<String> operationList = new ArrayList<String>();
		for (TableItem item : operationTable.getItems()) {
			operationList.add(item.getText());
		}
		return operationList;
	}
}