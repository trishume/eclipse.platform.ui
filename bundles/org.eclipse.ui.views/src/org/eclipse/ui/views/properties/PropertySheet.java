/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Markus Alexander Kuppe (Versant Corp.) - https://bugs.eclipse.org/248103
 *******************************************************************************/
package org.eclipse.ui.views.properties;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.views.ViewsPlugin;
import org.eclipse.ui.internal.views.properties.PropertiesMessages;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.part.ShowInContext;

/**
 * Main class for the Property Sheet View.
 * <p>
 * This standard view has id <code>"org.eclipse.ui.views.PropertySheet"</code>.
 * </p>
 * <p>
 * Note that property <it>sheets</it> and property sheet pages are not the
 * same thing as property <it>dialogs</it> and their property pages (the property
 * pages extension point is for contributing property pages to property dialogs).
 * Within the property sheet view, all pages are <code>IPropertySheetPage</code>s.
 * </p>
 * <p>
 * Property sheet pages are discovered by the property sheet view automatically
 * when a part is first activated. The property sheet view asks the active part
 * for its property sheet page; this is done by invoking 
 * <code>getAdapter(IPropertySheetPage.class)</code> on the part. If the part 
 * returns a page, the property sheet view then creates the controls for that
 * property sheet page (using <code>createControl</code>), and adds the page to 
 * the property sheet view. Whenever this part becomes active, its corresponding
 * property sheet page is shown in the property sheet view (which may or may not
 * be visible at the time). A part's property sheet page is discarded when the
 * part closes. The property sheet view has a default page (an instance of 
 * <code>PropertySheetPage</code>) which services all parts without a property
 * sheet page of their own.
 * </p>
 * <p>
 * The workbench will automatically instantiates this class when a Property
 * Sheet view is needed for a workbench window. This class is not intended
 * to be instantiated or subclassed by clients.
 * </p>
 *
 * @see IPropertySheetPage
 * @see PropertySheetPage
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
public class PropertySheet extends PageBookView implements ISelectionListener, IShowInTarget, IShowInSource {
    /**
     * No longer used but preserved to avoid api change
     */
    public static final String HELP_CONTEXT_PROPERTY_SHEET_VIEW = IPropertiesHelpContextIds.PROPERTY_SHEET_VIEW;

    /**
     * The initial selection when the property sheet opens
     */
    private ISelection bootstrapSelection;

    /**
     * The current selection of the property sheet
     */
    private ISelection currentSelection;

    /**
     * The current part for which this property sheets is active
     */
	private IWorkbenchPart currentPart;

	/**
	 * Whether this property sheet instance is pinned or not 
	 */
	private IAction pinPropertySheetAction;

	/**
	 * Whether this property view instance is hidden or not 
	 */
    private boolean hidden;
	
    /**
     * Creates a property sheet view.
     */
    public PropertySheet() {
        super();
        pinPropertySheetAction = new PinPropertySheetAction();
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     * Returns the default property sheet page.
     */
    protected IPage createDefaultPage(PageBook book) {
        IPageBookViewPage page = (IPageBookViewPage) ViewsPlugin.getAdapter(this,
                IPropertySheetPage.class, false);
        if(page == null) {
        	page = new PropertySheetPage();
        }
        initPage(page);
        page.createControl(book);
        return page;
    }

	/**
     * The <code>PropertySheet</code> implementation of this <code>IWorkbenchPart</code>
     * method creates a <code>PageBook</code> control with its default page showing.
     */
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
         
        pinPropertySheetAction.addPropertyChangeListener(new IPropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent event) {
				if (IAction.CHECKED.equals(event.getProperty())) {
					updateContentDescription();
				}
			}
		});
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuManager.add(pinPropertySheetAction);

		IToolBarManager toolBarManager = getViewSite().getActionBars()
				.getToolBarManager();
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		toolBarManager.add(pinPropertySheetAction);
		
        getSite().getPage().getWorkbenchWindow().getWorkbench().getHelpSystem()
				.setHelp(getPageBook(),
						IPropertiesHelpContextIds.PROPERTY_SHEET_VIEW);
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchPart.
     */
    public void dispose() {
        // run super.
        super.dispose();

        // remove ourselves as a selection listener
        getSite().getPage().removeSelectionListener(this);
        
        currentPart = null;
        currentSelection = null;
        pinPropertySheetAction = null;
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     */
    protected PageRec doCreatePage(IWorkbenchPart part) {
        // Get a custom property sheet page but not if the part is also a
		// PropertySheet. In this case the child property sheet would
		// accidentally reuse the parent's property sheet page.
    	if(part instanceof PropertySheet) {
    		return null;
    	}
        IPropertySheetPage page = (IPropertySheetPage) ViewsPlugin.getAdapter(part,
                IPropertySheetPage.class, false);
        if (page != null) {
            if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
            page.createControl(getPageBook());
            return new PageRec(part, page);
        }

        // Use the default page		
        return null;
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     */
    protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
        IPropertySheetPage page = (IPropertySheetPage) rec.page;
        page.dispose();
        rec.dispose();
    }

    /* (non-Javadoc)
     * Method declared on PageBookView.
     * Returns the active part on the same workbench page as this property 
     * sheet view.
     */
    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
            bootstrapSelection = page.getSelection();
            return page.getActivePart();
        } 
        return null;
    }

    /* (non-Javadoc)
     * Method declared on IViewPart.
     */
    public void init(IViewSite site) throws PartInitException {
   		site.getPage().addSelectionListener(this);
   		super.init(site);
    }

    /* (non-Javadoc)
	 * @see org.eclipse.ui.part.ViewPart#saveState(org.eclipse.ui.IMemento)
	 * since 3.4
	 */
	public void saveState(IMemento memento) {
		// close all but the primary/parent property sheet
		String secondaryId = getViewSite().getSecondaryId();
		if (null == secondaryId) {
			super.saveState(memento);
		} else {
			getViewSite().getPage().hideView(this);
		}
	}

    /* (non-Javadoc)
     * Method declared on PageBookView.
     * The property sheet may show properties for any view other than this view.
     */
    protected boolean isImportant(IWorkbenchPart part) {
		// See Bug 252887...explicitly exclude the Help view as a
		// participant
		boolean isHelpView = "org.eclipse.help.ui.HelpView".equals(part.getSite().getId()); //$NON-NLS-1$
		return !isPinned() && part != this && !isHelpView;
    }

    /* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#partClosed(org.eclipse.ui.IWorkbenchPart)
	 * since 3.4
	 */
	public void partClosed(IWorkbenchPart part) {
		if (isPinned() && part.equals(currentPart)) {
			pinPropertySheetAction.setChecked(false);
		}
		super.partClosed(part);
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#partVisible(org.eclipse.ui.IWorkbenchPart)
	 * since 3.4
	 */
	protected void partVisible(IWorkbenchPart part) {
	    if(hidden && this == part){
            hidden = false;
            // now we are visible again. Do not need to propagate this event to parent.
            return;
        }
        
	    super.partVisible(part);
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.PageBookView#partHidden(org.eclipse.ui.IWorkbenchPart)
     * since 3.4
     */
    protected void partHidden(IWorkbenchPart part) {
        if(this == part){
            hidden = true;
        }
        // if we are pinned, then we are not interested if parts are hidden, if
        // our target part is hidden, we should still show whatever content we
        // have been pinned on
        if (!isPinned()) {
            super.partHidden(part);     
        }
    }	
    
	/**
     * The <code>PropertySheet</code> implementation of this <code>IPartListener</code>
     * method first sees if the active part is an <code>IContributedContentsView</code>
     * adapter and if so, asks it for its contributing part.
     */
    public void partActivated(IWorkbenchPart part) {
        if(hidden) {
            if (this == part) {
                hidden = false;
            }
            // If we are hidden (or it's our own event), ignore event.
            return;
        }
    	// Look for a declaratively-contributed adapter - including not yet loaded adapter factories.
    	// See bug 86362 [PropertiesView] Can not access AdapterFactory, when plugin is not loaded.
        IContributedContentsView view = (IContributedContentsView) ViewsPlugin.getAdapter(part,
                IContributedContentsView.class, true);
        IWorkbenchPart source = null;
        if (view != null) {
			source = view.getContributingPart();
		}
        if (source != null) {
			super.partActivated(source);
		} else {
			super.partActivated(part);
		}

        if(isImportant(part)) {
        	currentPart = part;
        	// reset the selection (to allow selectionChanged() accept part change for empty selections)
        	currentSelection = null;
        }
        
        // When the view is first opened, pass the selection to the page		
        if (bootstrapSelection != null) {
            IPropertySheetPage page = (IPropertySheetPage) getCurrentPage();
            if (page != null) {
				page.selectionChanged(part, bootstrapSelection);
			}
            bootstrapSelection = null;
        }
    }

    /* (non-Javadoc)
     * Method declared on ISelectionListener.
     * Notify the current page that the selection has changed.
     */
    public void selectionChanged(IWorkbenchPart part, ISelection sel) {
        // we ignore null selection, or if we are pinned, or our own selection or same selection
		if (sel == null || !isImportant(part) || sel.equals(currentSelection)) {
			return;
		}
		
		// we ignore selection if we are hidden OR selection is coming from another source as the last one
		if(hidden && (part == null || !part.equals(currentPart))){
		    return;
		}
        
        currentPart = part;
        currentSelection = sel;
        
        // pass the selection to the page		
        IPropertySheetPage page = (IPropertySheetPage) getCurrentPage();
        if (page != null) {
			page.selectionChanged(currentPart, currentSelection);
		}
        
        updateContentDescription();
    }

	private void updateContentDescription() {
		if (isPinned() && currentPart != null) {
			setContentDescription(NLS.bind(PropertiesMessages.Selection_description, currentPart.getTitle()));
		} else {
			setContentDescription(""); //$NON-NLS-1$
		}
	}
    
    /**
	 * The <code>PropertySheet</code> implementation of this
	 * <code>PageBookView</code> method handles the <code>ISaveablePart</code>
	 * adapter case by calling <code>getSaveablePart()</code>.
	 * 
	 * @since 3.2
	 */
	protected Object getViewAdapter(Class key) {
		if (ISaveablePart.class.equals(key)) {
			return getSaveablePart();
		}
		return super.getViewAdapter(key);
	}

	/**
	 * Returns an <code>ISaveablePart</code> that delegates to the source part
	 * for the current page if it implements <code>ISaveablePart</code>, or
	 * <code>null</code> otherwise.
	 * 
	 * @return an <code>ISaveablePart</code> or <code>null</code>
	 * @since 3.2
	 */
	protected ISaveablePart getSaveablePart() {
		IWorkbenchPart part = getCurrentContributingPart();
		if (part instanceof ISaveablePart) {
			return (ISaveablePart) part;
		}
		return null;
	}
	
	/**
	 * @return whether this property sheet is currently pinned
	 * @since 3.4
	 */
	public boolean isPinned() {
		return pinPropertySheetAction != null && pinPropertySheetAction.isChecked();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.4
	 */
	public ShowInContext getShowInContext() {
		return new PropertyShowInContext(currentPart, currentSelection);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.4
	 */
	public boolean show(ShowInContext aContext) {
		if (!isPinned()
				&& aContext instanceof PropertyShowInContext) {
			PropertyShowInContext context = (PropertyShowInContext) aContext;
			partActivated(context.getPart());
			selectionChanged(context.getPart(), context.getSelection());
			return true;
		}
		return false;
	}

	/***
	 * @param pinned Whether this sheet should be pinned
	 * @since 3.4
	 */
	public void setPinned(boolean pinned) {
		pinPropertySheetAction.setChecked(pinned);
		updateContentDescription();
	}
}