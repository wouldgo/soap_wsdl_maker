package org.soap_wsdl_maker.plugin.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.soap_wsdl_maker.refs.PlaceHolder;
import org.soap_wsdl_maker.refs.Template;

/**
 * @author would84@gmail.com
 * 
 * Its role is to create a new file resource in the provided container. 
 * If the container resource (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target container.
 * 
 **/

public class NewWsdl extends Wizard implements INewWizard {
	private NewWsdlWizardPage innerpage;
	private ISelection selection;

	/**
	 * Constructor for NewWsdl.
	 */
	public NewWsdl() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		innerpage = new NewWsdlWizardPage(selection);
		addPage(innerpage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. I will create an operation and run it using wizard as execution context.
	 * 
	 */
	public boolean performFinish() {
		final String containerName = innerpage.getContainerName(),
				verbName = innerpage.getVerName(),
				objectName = innerpage.getObjectName(),
				providerUrl = innerpage.getProviderUrl(),
				serviceHost = innerpage.getServiceHost();

		final List<String> operationList = innerpage.getOperationNames();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(verbName, objectName, providerUrl, serviceHost, operationList, containerName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Verify the path passed and creates that.
	 * 
	 * @param container the selected project
	 * @param path the path
	 * @param monitor the plugin monitor
	 * @param toAdd value to add at the end of path
	 * @return the created path
	 * @throws CoreException if something goes wrong
	 */
	private Path checkAndCreatePath(IContainer container, Path path, IProgressMonitor monitor, String toAdd) throws CoreException {
		if(toAdd != null && toAdd.length() > 0) {
			path = (Path) path.append(System.getProperty("file.separator") + toAdd);
		}

		if(!container.exists(path)) {
			IFolder fold = container.getFolder(path);
			fold.create(true, true, monitor);
		}

		return path;
	}

	/**
	 * Method for obtain creation file's path
	 * 
	 * @param root the root dir inside of the project
	 * @param areaName the name of the functional area
	 * @param fileName with extension
	 * @param functionName the functional name
	 * @param container il progetto a cui appartiene il file da creare
	 * @param monitor the plugin monitor
	 * @return the needed path
	 * @throws CoreException if something goes wrong
	 */
	private Path getPath(String root, String fileName, String cart, IContainer container, IProgressMonitor monitor) throws CoreException {
		Path path = new Path(root);
		path = checkAndCreatePath(container, path, monitor, null);
		path = checkAndCreatePath(container, path, monitor, cart);

		path = (Path) path.append(System.getProperty("file.separator") + fileName);		
		return path;
	}

	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents.
	 */
	private void doFinish(String verbName, String objectName, String providerUrl, String serviceHost, List<String> operationList,
			String containerName, IProgressMonitor monitor) throws CoreException {
		String serviceFileName = verbName+ "_" +objectName+ "_service.wsdl";		
		monitor.beginTask("Creating " + serviceFileName, IProgressMonitor.UNKNOWN);

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));

		if (!resource.exists() || !(resource instanceof IContainer)) {
			IStatus status = new Status(IStatus.ERROR, "org.soap_wsdl_maker.plugin.wsdlmaker", 
					IStatus.OK, "The project \"" + containerName + "\" doesn't exist.", null);
			throw new CoreException(status);
		}
		IContainer container = (IContainer) resource;

		Path serviceFilePath = getPath(root.getName(), serviceFileName, "wsdl", container, monitor);
		final IFile serviceFile = container.getFile(serviceFilePath);
		try {
			InputStream stream = openContentStream(Template.getResource(Template.SERVICE_TEMPLATE), verbName, objectName, providerUrl, serviceHost, operationList);
			if (serviceFile.exists()) {
				serviceFile.setContents(stream, true, true, monitor);
			} else {
				serviceFile.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, "Service Creation", IStatus.OK, e.getMessage(), null);
			throw new CoreException(status);
		}
		monitor.worked(1);

		String interfaceFileName = verbName+ "_" +objectName+ "_interface.wsdl";
		Path interfaceFilePath = getPath(root.getName(), interfaceFileName, "wsdl", container, monitor);
		final IFile interfaceFile = container.getFile(interfaceFilePath);
		try {
			InputStream stream = openContentStream(Template.getResource(Template.INTERFACE_TEMPLATE), verbName, objectName, providerUrl, serviceHost, operationList);
			if (interfaceFile.exists()) {
				interfaceFile.setContents(stream, true, true, monitor);
			} else {
				interfaceFile.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, "Interface Creation", IStatus.OK, e.getMessage(), null);
			throw new CoreException(status);	
		}
		monitor.worked(1);

		monitor.done();
	}

	/**
	 * Creates the stream with completed wsdls.
	 * 
	 */
	private InputStream openContentStream(String resourceName, 
			String verbText, String objText, String providerUrl, String serviceHost, List<String> operationList) throws CoreException {
		String verbTextLc = verbText.toLowerCase();
		String objTextLc = objText.toLowerCase();

		String line = null;
		StringBuffer sb = new StringBuffer();

		InputStream input = this.getClass().getClassLoader().getResourceAsStream(resourceName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		try {
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.VERB), verbText);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.OBJECT), objText);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.PROVIDER_URL), providerUrl);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.SERVICE_HOST), serviceHost);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.VERB_LOWERCASE), verbTextLc);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.OBJECT_LOWERCASE), objTextLc);

				if(resourceName.equals(Template.getResource(Template.INTERFACE_TEMPLATE))) {
					if(line.indexOf(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_TYPES)) > -1) {
						line = line.replace(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_TYPES), "");
						sb.append(subcontentCompletion(Template.OPERATIONTYPES_TEMPLATE, verbText, objText, 
								providerUrl, operationList));

					} else if(line.indexOf(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_MESSAGES)) > -1) {
						line = line.replace(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_MESSAGES), "");
						sb.append(subcontentCompletion(Template.OPERATIONMESSAGES_TEMPLATE, verbText, objText, 
								providerUrl, operationList));

					} else if(line.indexOf(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_PORT)) > -1) {
						line = line.replace(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_PORT), "");
						sb.append(subcontentCompletion(Template.OPERATIONPORT_TEMPLATE, verbText, objText, 
								providerUrl, operationList));

					} else if(line.indexOf(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_BINDING)) > -1) {
						line = line.replace(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION_BINDING), "");
						sb.append(subcontentCompletion(Template.OPERATIONBINDING_TEMPLATE, verbText, objText, 
								providerUrl, operationList));

					}
				}

				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, "Problem in file generation", IStatus.OK, e.getMessage(), null);
			throw new CoreException(status);
		} catch (CoreException e) {
			throw e;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				IStatus status = new Status(IStatus.ERROR, "Problem in file generation", IStatus.OK, e.getMessage(), null);
				throw new CoreException(status);
			}
		}

		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	private StringBuffer subcontentCompletion(Template aTemplate, String verbText, 
			String objText,	String providerUrl, List<String> operationList) 
					throws CoreException {

		StringBuffer theBuffer = new StringBuffer();
		for (String anOperation : operationList) {
			InputStream is = openContentStreamForAnOperation(Template.getResource(aTemplate), 
					verbText, objText, providerUrl, anOperation);

			BufferedReader subReader = new BufferedReader(new InputStreamReader(is));
			String subLine = "";
			try {
				while ((subLine = subReader.readLine()) != null) {
					theBuffer.append(subLine);
					theBuffer.append(System.getProperty("line.separator"));
				}
			} catch (IOException e) {
				IStatus status = new Status(IStatus.ERROR, "Problem in file generation", IStatus.OK, e.getMessage(), null);
				throw new CoreException(status);
			}			
		}
		return theBuffer;
	}

	/**
	 * Creates the text chunk for an operation.
	 */
	private InputStream openContentStreamForAnOperation(String resourceName,
			String verbText, String objText, 
			String providerUrl, String operationName) throws CoreException {
		String verbTextLc = verbText.substring(0, 1) + verbText.substring(1).toLowerCase();
		String objTextLc = objText.substring(0, 1) + objText.substring(1).toLowerCase();

		String line = null;
		StringBuffer sb = new StringBuffer();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(resourceName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		try {
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.VERB), verbText);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.OBJECT), objText);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.PROVIDER_URL), providerUrl);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.OPERATION), operationName);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.VERB_LOWERCASE), verbTextLc);
				line = line.replaceAll(PlaceHolder.getPlaceHolder(PlaceHolder.OBJECT_LOWERCASE), objTextLc);

				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, "Problem in file generation", IStatus.OK, e.getMessage(), null);
			throw new CoreException(status);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				IStatus status = new Status(IStatus.ERROR, "Problem in file generation", IStatus.OK, e.getMessage(), null);
				throw new CoreException(status);
			}
		}
		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	/**
	 * I will accept the selection in the workbench to see if we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}