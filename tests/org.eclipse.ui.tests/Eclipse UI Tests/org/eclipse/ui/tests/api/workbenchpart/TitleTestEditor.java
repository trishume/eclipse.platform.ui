/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tests.api.workbenchpart;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.layout.CellLayout;
import org.eclipse.ui.internal.layout.Row;
import org.eclipse.ui.part.EditorPart;

/**
 * @since 3.0
 */
public class TitleTestEditor extends EditorPart {

    Composite composite;

    Text title;

    Text name;

    Text contentDescription;

    Label titleLabel;

    Label nameLabel;

    Label cdLabel;

    /**
     * 
     */
    public TitleTestEditor() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor) {

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#doSaveAs()
     */
    public void doSaveAs() {

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {

        if (!(input instanceof IFileEditorInput))
            throw new PartInitException(
                    "Invalid Input: Must be IFileEditorInput");
        setSite(site);
        setInput(input);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#isDirty()
     */
    public boolean isDirty() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        CellLayout layout = new CellLayout(2).setColumn(0, Row.fixed())
                .setColumn(1, Row.growing());
        composite.setLayout(layout);

        Label firstLabel = new Label(composite, SWT.NONE);
        firstLabel.setText("Title");
        title = new Text(composite, SWT.BORDER);
        title.setText(getTitle());

        title.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setTitle(title.getText());
            }
        });

        Label secondLabel = new Label(composite, SWT.NONE);
        secondLabel.setText("Name");
        name = new Text(composite, SWT.BORDER);
        name.setText(getPartName());
        name.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setPartName(name.getText());
            }
        });

        Label thirdLabel = new Label(composite, SWT.NONE);
        thirdLabel.setText("Content");
        contentDescription = new Text(composite, SWT.BORDER);
        contentDescription.setText(getContentDescription());
        contentDescription.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setContentDescription(contentDescription.getText());
            }
        });

        Label tlLabel = new Label(composite, SWT.NONE);
        tlLabel.setText("getTitle() = ");
        titleLabel = new Label(composite, SWT.NONE);

        Label nmLabel = new Label(composite, SWT.NONE);
        nmLabel.setText("getPartName() = ");
        nameLabel = new Label(composite, SWT.NONE);

        Label descLabel = new Label(composite, SWT.NONE);
        descLabel.setText("getContentDescription() = ");
        cdLabel = new Label(composite, SWT.NONE);

        updateLabels();

        addPropertyListener(new IPropertyListener() {
            /* (non-Javadoc)
             * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
             */
            public void propertyChanged(Object source, int propId) {
                updateLabels();
            }
        });
    }

    private void updateLabels() {
        titleLabel.setText(getTitle());
        nameLabel.setText(getPartName());
        cdLabel.setText(getContentDescription());
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#setFocus()
     */
    public void setFocus() {
        composite.setFocus();

    }

}
