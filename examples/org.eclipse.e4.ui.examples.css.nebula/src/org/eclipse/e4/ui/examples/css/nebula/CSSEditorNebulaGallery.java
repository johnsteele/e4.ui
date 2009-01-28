package org.eclipse.e4.ui.examples.css.nebula;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;

public class CSSEditorNebulaGallery extends AbstractCSSNebulaEditor {

	public void createContent(Composite parent) {

		Image itemImage = new Image(parent.getDisplay(), Program.findProgram(
				"jpg").getImageData());
		Gallery gallery = new Gallery(parent, SWT.V_SCROLL | SWT.MULTI);

		// Renderers
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(56);
		gr.setItemWidth(72);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);

		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer(ir);

		int nbGalleryItemSelected = 0;
		int nbTotalGalleryItemSelected = 2;
		List selectedItems = new ArrayList();

		for (int g = 0; g < 2; g++) {
			GalleryItem group = new GalleryItem(gallery, SWT.NONE);
			group.setText("Group " + g);
			group.setExpanded(true);

			for (int i = 0; i < 50; i++) {
				GalleryItem item = new GalleryItem(group, SWT.NONE);
				if (nbGalleryItemSelected < nbTotalGalleryItemSelected) {
					nbGalleryItemSelected++;
					selectedItems.add(item);
				}
				if (itemImage != null) {
					item.setImage(itemImage);
				}
				item.setText("Item " + i);
			}
		}
		gallery.setSelection((GalleryItem[]) selectedItems
				.toArray(new GalleryItem[0]));

		// if (itemImage != null)
		// itemImage.dispose();
		// display.dispose();
	}

	public static void main(String[] args) {
		CSSEditorNebulaGallery editor = new CSSEditorNebulaGallery();
		editor.display();
	}
}
