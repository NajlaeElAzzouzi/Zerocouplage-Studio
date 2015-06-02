/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */
 
package org.zerocouplage.zcstudio.newproject;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard class
 */
public class NewZCProjectWizard extends Wizard implements INewWizard
{
	public static final String copyright = "(c) Copyright IBM Corporation 2002.";	
	// wizard pages
	NewZCProjectMainPage newZCProjectPage;
	NewZCProjectSettingsPage settingsPage;

	
	// the model

	NewZCProjectModel model;
	
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	
	// flag indicated whether the wizard can be completed or not 
	
	protected boolean creationCompleted = false;

	// the workbench instance
	protected IWorkbench workbench;

	/**
	 * Constructor for  NewZCProjectMainWizard.
	 */
	public NewZCProjectWizard() {
		super();
		model = new NewZCProjectModel();
	}
	
	public void addPages()
	{
		newZCProjectPage = new NewZCProjectMainPage(workbench, selection);
		addPage(newZCProjectPage);
		settingsPage = new NewZCProjectSettingsPage("");
		addPage(settingsPage);
		
	}

	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) 
	{
		this.workbench = workbench;
		this.selection = selection;
		if (selection != null && !selection.isEmpty()) {
			Object obj = selection.getFirstElement();
			if (obj  instanceof IFolder) {
				IFolder folder = (IFolder) obj;				
				if (folder.getName().equals("Discounts"))
					model.discounted = true;				
			}
		}
	}

	public boolean canFinish()
	{
		// cannot completr the wizard from the first page
		if (this.getContainer().getCurrentPage() == newZCProjectPage) 
			return false;
		// based on the type of transport return the right flag			
		if (model.usePlane) return creationCompleted;
		return false;
	}
	
	public boolean performFinish() 
	{
		String summary = model.toString();
		MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
			"Holiday info", summary);
		try {
			model.creatEmptyProject();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
}