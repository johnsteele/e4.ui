importPackage(Packages.org.eclipse.swt);
importPackage(Packages.org.eclipse.swt.widgets);
importPackage(Packages.org.eclipse.swt.layout);

var argumentTypes = [ "org.eclipse.swt.widgets.Composite" ];
                   
function construct (composite) {
   composite.setLayout(new FillLayout());
   var label = new Label(composite, SWT.NONE);
   label.setText("Hello World");
   var button = new Button(composite, SWT.PUSH);
   button.setText("Push me");
   button.addListener(SWT.Selection, function(){ label.setText("pushed"); });
}
