package org.eclipse.ui.tweaklets.nomrutabs;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.tweaklets.TabBehaviourMRU;
import org.eclipse.ui.themes.ColorUtil;

public class NoMRUTabs extends TabBehaviourMRU {

	public boolean enableMRUTabVisibility() {
		return false;
	}
	
	public boolean sortEditorListAlphabetically() {
		return false;
	}
	
	public Font createVisibleEditorsFont(Display display, Font originalFont) {
        FontData fontData[] = originalFont.getFontData();
        return new Font(display, fontData);
	}

	public Font createInvisibleEditorsFont(Display display, Font originalFont) {
        FontData fontData[] = originalFont.getFontData();
        return new Font(display, fontData);
	}
	
	public Color createVisibleEditorsColor(Display display, RGB originalForegroundColor, RGB originalBackgroundColor) {
		RGB dimmedRGB = ColorUtil.blend(originalForegroundColor, originalBackgroundColor, 60);
		return new Color(display, dimmedRGB);
	}
	
}
