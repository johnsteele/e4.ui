package org.eclipse.e4.ui.selection.tests;

import javax.inject.Inject;
import javax.inject.Named;

import junit.framework.TestCase;

import org.eclipse.e4.core.services.IDisposable;
import org.eclipse.e4.core.services.annotations.Optional;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.ContextInjectionFactory;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.ui.selection.ESelectionService;

public class SelectionTest extends TestCase {

	private static final String SEL_TWO = "two";
	private static final String SEL_ONE = "one";

	static class ConsumerPart {
		public String input;

		@Inject
		@Optional
		public void setInput(@Named(ESelectionService.SELECTION) String current) {
			input = current;
		}
	}

	static class ProviderPart extends ConsumerPart {
		private ESelectionService selectionService;

		@Inject
		public void setSelectionService(ESelectionService s) {
			selectionService = s;
		}

		public void setSelection(String s) {
			selectionService.setSelection(s);
		}
	}

	private IEclipseContext workbenchContext;

	public void testOnePartSelection() throws Exception {
		ProviderPart p = new ProviderPart();
		ContextInjectionFactory.inject(p, workbenchContext);

		assertNull(p.input);

		p.setSelection(SEL_ONE);
		assertEquals(SEL_ONE, p.input);
		p.setSelection(null);
		assertNull(p.input);
	}

	public void testThreePartSelection() throws Exception {
		IEclipseContext window = TestUtil.createContext(workbenchContext,
				"windowContext");
		workbenchContext.set(IContextConstants.ACTIVE_CHILD, window);
		ESelectionService windowService = (ESelectionService) window
				.get(ESelectionService.class.getName());

		IEclipseContext partOne = TestUtil.createContext(window, "partOne");
		window.set(IContextConstants.ACTIVE_CHILD, partOne);
		ProviderPart partOneImpl = new ProviderPart();
		ContextInjectionFactory.inject(partOneImpl, partOne);

		IEclipseContext partTwo = TestUtil.createContext(window, "partTwo");
		ConsumerPart partTwoImpl = new ConsumerPart();
		ContextInjectionFactory.inject(partTwoImpl, partTwo);

		IEclipseContext partThree = TestUtil.createContext(window, "partThree");
		ProviderPart partThreeImpl = new ProviderPart();
		ContextInjectionFactory.inject(partThreeImpl, partThree);

		assertNull(windowService.getSelection());
		assertNull(partOneImpl.input);
		assertNull(partTwoImpl.input);
		assertNull(partThreeImpl.input);

		partOneImpl.setSelection(SEL_ONE);
		assertEquals(SEL_ONE, windowService.getSelection());
		assertEquals(SEL_ONE, partOneImpl.input);
		assertNull(partTwoImpl.input);
		assertNull(partThreeImpl.input);

		partThreeImpl.setSelection(SEL_TWO);
		assertEquals(SEL_ONE, windowService.getSelection());
		assertEquals(SEL_ONE, partOneImpl.input);
		assertNull(partTwoImpl.input);
		assertEquals(SEL_TWO, partThreeImpl.input);

		window.set(IContextConstants.ACTIVE_CHILD, partTwo);
		assertNull(windowService.getSelection());
		assertEquals(SEL_ONE, partOneImpl.input);
		assertNull(partTwoImpl.input);
		assertEquals(SEL_TWO, partThreeImpl.input);

		window.set(IContextConstants.ACTIVE_CHILD, partThree);
		assertEquals(SEL_TWO, windowService.getSelection());
		assertEquals(SEL_ONE, partOneImpl.input);
		assertNull(partTwoImpl.input);
		assertEquals(SEL_TWO, partThreeImpl.input);
	}

	@Override
	protected void setUp() throws Exception {
		workbenchContext = createWorkbenchContext(Activator.getDefault()
				.getGlobalContext());
	}

	@Override
	protected void tearDown() throws Exception {
		if (workbenchContext instanceof IDisposable) {
			((IDisposable) workbenchContext).dispose();
		}
		workbenchContext = null;
	}

	private IEclipseContext createWorkbenchContext(IEclipseContext globalContext) {
		IEclipseContext wb = TestUtil.createContext(globalContext,
				"workbenchContext");
		return wb;
	}
}
