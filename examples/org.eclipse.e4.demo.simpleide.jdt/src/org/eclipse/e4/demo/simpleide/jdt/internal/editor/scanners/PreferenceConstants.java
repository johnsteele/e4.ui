package org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners;

public class PreferenceConstants {
	/**
	 * Preference key suffix for bold text style preference keys.
	 *
	 * @since 2.1
	 */
	public static final String EDITOR_BOLD_SUFFIX= "_bold"; //$NON-NLS-1$
	
	/**
	 * Preference key suffix for italic text style preference keys.
	 *
	 * @since 3.0
	 */
	public static final String EDITOR_ITALIC_SUFFIX= "_italic"; //$NON-NLS-1$

	/**
	 * Preference key suffix for strikethrough text style preference keys.
	 *
	 * @since 3.1
	 */
	public static final String EDITOR_STRIKETHROUGH_SUFFIX= "_strikethrough"; //$NON-NLS-1$

	/**
	 * Preference key suffix for underline text style preference keys.
	 *
	 * @since 3.1
	 */
	public static final String EDITOR_UNDERLINE_SUFFIX= "_underline"; //$NON-NLS-1$
	
	/**
	 * A named preference prefix for semantic highlighting preferences.
	 *
	 * @since 3.0
	 */
	public static final String EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX="semanticHighlighting."; //$NON-NLS-1$

	/**
	 * A named preference suffix that controls a semantic highlighting's color.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 *
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 3.0
	 */
	public static final String EDITOR_SEMANTIC_HIGHLIGHTING_COLOR_SUFFIX=".color"; //$NON-NLS-1$
	
	/**
	 * A named preference suffix that controls if semantic highlighting has the text attribute bold.
	 * <p>
	 * Value is of type <code>Boolean</code>: <code>true</code> if bold.
	 * </p>
	 *
	 * @since 3.0
	 */
	public static final String EDITOR_SEMANTIC_HIGHLIGHTING_BOLD_SUFFIX=".bold"; //$NON-NLS-1$

	/**
	 * A named preference suffix that controls if semantic highlighting has the text attribute italic.
	 * <p>
	 * Value is of type <code>Boolean</code>: <code>true</code> if italic.
	 * </p>
	 *
	 * @since 3.0
	 */
	public static final String EDITOR_SEMANTIC_HIGHLIGHTING_ITALIC_SUFFIX=".italic"; //$NON-NLS-1$

	/**
	 * A named preference suffix that controls if semantic highlighting has the text attribute strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>: <code>true</code> if strikethrough.
	 * </p>
	 *
	 * @since 3.1
	 */
	public static final String EDITOR_SEMANTIC_HIGHLIGHTING_STRIKETHROUGH_SUFFIX=".strikethrough"; //$NON-NLS-1$

	/**
	 * A named preference suffix that controls if semantic highlighting has the text attribute underline.
	 * <p>
	 * Value is of type <code>Boolean</code>: <code>true</code> if underline.
	 * </p>
	 *
	 * @since 3.1
	 */
	public static final String EDITOR_SEMANTIC_HIGHLIGHTING_UNDERLINE_SUFFIX=".underline"; //$NON-NLS-1$

}
