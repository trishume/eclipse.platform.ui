package org.eclipse.jface.viewers;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.jface.*;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import java.util.*;
import java.util.List;

/**
 * A concrete viewer based on an SWT <code>Table</code>
 * control with checkboxes on each node.
 * <p>
 * This class is not intended to be subclassed outside the viewer framework. 
 * It is designed to be instantiated with a pre-existing SWT table control and configured
 * with a domain-specific content provider, label provider, element filter (optional),
 * and element sorter (optional).
 * </p>
 */
public class CheckboxTableViewer extends TableViewer implements ICheckable {

	/**
	 * List of check state listeners (element type: <code>ICheckStateListener</code>).
	 */
	private ListenerList checkStateListeners = new ListenerList(3);
/**
 * Creates a table viewer on a newly-created table control under the given parent.
 * The table control is created using the SWT style bits: <code>CHECK</code> and <code>BORDER</code>.
 * The table has one column.
 * The viewer has no input, no content provider, a default label provider, 
 * no sorter, and no filters.
 *
 * @param parent the parent control
 */
public CheckboxTableViewer(Composite parent) {
	this(parent, SWT.BORDER);
}
/**
 * Creates a table viewer on a newly-created table control under the given parent.
 * The table control is created using the given SWT style bits, plus the <code>CHECK</code> style bit.
 * The table has one column.
 * The viewer has no input, no content provider, a default label provider, 
 * no sorter, and no filters.
 *
 * @param parent the parent control
 * @param style SWT style bits
 */
public CheckboxTableViewer(Composite parent, int style) {
	this(createTable(parent, style));
}
/**
 * Creates a table viewer on the given table control.
 * The <code>SWT.CHECK</code> style bit must be set on the given table control.
 * The viewer has no input, no content provider, a default label provider, 
 * no sorter, and no filters.
 *
 * @param table the table control
 */
public CheckboxTableViewer(Table table) {
	super(table);
}
/* (non-Javadoc)
 * Method declared on ICheckable.
 */
public void addCheckStateListener(ICheckStateListener listener) {
	checkStateListeners.add(listener);
}
/**
 * Creates a new table control with one column.
 *
 * @param parent the parent control
 * @param style style bits
 * @return a new table control
 */
protected static Table createTable(Composite parent, int style) {
	return new Table(parent, SWT.CHECK | style);
}
/**
 * Notifies any check state listeners that a check state changed  has been received.
 * Only listeners registered at the time this method is called are notified.
 *
 * @param event a check state changed event
 *
 * @see ICheckStateListener#checkStateChanged
 */
private void fireCheckStateChanged(CheckStateChangedEvent event) {
	Object[] listeners = checkStateListeners.getListeners();
	for (int i = 0; i < listeners.length; ++i) {
		((ICheckStateListener) listeners[i]).checkStateChanged(event);
	}
}
/* (non-Javadoc)
 * Method declared on ICheckable.
 */
public boolean getChecked(Object element) {
	Widget widget = findItem(element);
	if (widget instanceof TableItem) {
		return ((TableItem) widget).getChecked();
	}
	return false;
}
/**
 * Returns a list of elements corresponding to checked table items in this
 * viewer.
 * <p>
 * This method is typically used when preserving the interesting
 * state of a viewer; <code>setCheckedElements</code> is used during the restore.
 * </p>
 *
 * @return the array of checked elements
 * @see #setCheckedElements
 */
public Object[] getCheckedElements() {
	TableItem[] children = getTable().getItems();
	ArrayList v = new ArrayList(children.length);
	for (int i = 0; i < children.length; i++) {
		TableItem item = children[i];
		if (item.getChecked())
			v.add(item.getData());
	}
	return v.toArray();
}
/**
 * Returns the grayed state of the given element.
 *
 * @param element the element
 * @return <code>true</code> if the element is grayed,
 *   and <code>false</code> if not grayed
 */
public boolean getGrayed(Object element) {
	Widget widget = findItem(element);
	if (widget instanceof TableItem) {
		return ((TableItem) widget).getGrayed();
	}
	return false;
}
/**
 * Returns a list of elements corresponding to grayed nodes in this
 * viewer.
 * <p>
 * This method is typically used when preserving the interesting
 * state of a viewer; <code>setGrayedElements</code> is used during the restore.
 * </p>
 *
 * @return the array of grayed elements
 * @see #setGrayedElements
 */
public Object[] getGrayedElements() {
	TableItem[] children = getTable().getItems();
	List v = new ArrayList(children.length);
	for (int i = 0; i < children.length; i++) {
		TableItem item = children[i];
		if (item.getGrayed())
			v.add(item.getData());
	}
	return v.toArray();
}
/* (non-Javadoc)
 * Method declared on StructuredViewer.
 */
public void handleSelect(SelectionEvent event) {
	if (event.detail == SWT.CHECK) {
		super.handleSelect(event); // this will change the current selection

		TableItem item = (TableItem) event.item;
		Object data = item.getData();
		if (data != null) {
			fireCheckStateChanged(new CheckStateChangedEvent(this, data, item.getChecked()));
		}
	} else
		super.handleSelect(event);
}
/* (non-Javadoc)
 * Method declared on Viewer.
 */
protected void preservingSelection(Runnable updateCode) {

	TableItem[] children = getTable().getItems();
	Set checked = new HashSet(children.length);
	Set grayed = new HashSet(children.length);

	for (int i = 0; i < children.length; i++) {
		TableItem item = children[i];
		Object data = item.getData();
		if (data != null) {
			if (item.getChecked())
				checked.add(data);
			if (item.getGrayed())
				grayed.add(data);
		}
	}

	super.preservingSelection(updateCode);

	children = getTable().getItems();
	for (int i = 0; i < children.length; i++) {
		TableItem item = children[i];
		Object data = item.getData();
		if (data != null) {
			item.setChecked(checked.contains(data));
			item.setGrayed(grayed.contains(data));
		}
	}
}
/* (non-Javadoc)
 * Method declared on ICheckable.
 */
public void removeCheckStateListener(ICheckStateListener listener) {
	checkStateListeners.remove(listener);
}
/**
 * Sets to the given value the checked state for all elements in this viewer.
 *
 * @param state <code>true</code> if the element should be checked,
 *  and <code>false</code> if it should be unchecked
 */
public void setAllChecked(boolean state) {
	TableItem[] children = getTable().getItems();
	for (int i = 0; i < children.length; i++) {
		TableItem item = children[i];
		item.setChecked(state);
	}
}
/**
 * Sets to the given value the grayed state for all elements in this viewer.
 *
 * @param state <code>true</code> if the element should be grayed,
 *  and <code>false</code> if it should be ungrayed
 */
public void setAllGrayed(boolean state) {
	TableItem[] children = getTable().getItems();
	for (int i = 0; i < children.length; i++) {
		TableItem item = children[i];
		item.setGrayed(state);
	}
}
/* (non-Javadoc)
 * Method declared on ICheckable.
 */
public boolean setChecked(Object element, boolean state) {
	Widget widget = findItem(element);
	if (widget instanceof TableItem) {
		((TableItem) widget).setChecked(state);
		return true;
	}
	return false;
}
/**
 * Sets which nodes are checked in this viewer.
 * The given list contains the elements that are to be checked;
 * all other nodes are to be unchecked.
 * <p>
 * This method is typically used when restoring the interesting
 * state of a viewer captured by an earlier call to <code>getCheckedElements</code>.
 * </p>
 *
 * @param elements the list of checked elements (element type: <code>Object</code>)
 * @see #getCheckedElements
 */
public void setCheckedElements(Object[] elements) {
	Set set = new HashSet(elements.length*2+1);
	for (int i = 0; i < elements.length; ++i) {
		set.add(elements[i]);
	}
	TableItem[] items = getTable().getItems();
	for (int i = 0; i < items.length; ++i) {
		TableItem item = items[i];
		Object element = item.getData();
		if (element != null) {
			boolean check = set.contains(element);
			// only set if different, to avoid flicker
			if (item.getChecked() != check) {
				item.setChecked(check);
			}
		}
	}
}
/**
 * Sets the grayed state for the given element in this viewer.
 *
 * @param element the element
 * @param state <code>true</code> if the item should be grayed,
 *  and <code>false</code> if it should be ungrayed
 * @return <code>true</code> if the element is visible and the gray
 *  state could be set, and <code>false</code> otherwise
 */
public boolean setGrayed(Object element, boolean state) {
	Widget widget = findItem(element);
	if (widget instanceof TableItem) {
		((TableItem) widget).setGrayed(state);
		return true;
	}
	return false;
}
/**
 * Sets which nodes are grayed in this viewer.
 * The given list contains the elements that are to be grayed;
 * all other nodes are to be ungrayed.
 * <p>
 * This method is typically used when restoring the interesting
 * state of a viewer captured by an earlier call to <code>getGrayedElements</code>.
 * </p>
 *
 * @param elements the array of grayed elements
 *
 * @see #getGrayedElements
 */
public void setGrayedElements(Object[] elements) {
	Set set = new HashSet(elements.length*2+1);
	for (int i = 0; i < elements.length; ++i) {
		set.add(elements[i]);
	}
	TableItem[] items = getTable().getItems();
	for (int i = 0; i < items.length; ++i) {
		TableItem item = items[i];
		Object element = item.getData();
		if (element != null) {
			boolean gray = set.contains(element);
			// only set if different, to avoid flicker
			if (item.getGrayed() != gray) {
				item.setGrayed(gray);
			}
		}
	}
}
}
