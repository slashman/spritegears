package net.slashware.swing;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class SwingInformBox extends JTextArea{
	
	public void clear(){
		boolean wait = false;
		do {
			try {
				setText("");
				wait = false;
			}  catch (Error e){
				wait = true;
			}
		} while (wait);
	}
	
	public boolean isEditable(){
		return false;
	}
	
	public boolean isFocusable(){
		return false;
	}
	
	public synchronized void addText(String txt){
		boolean wait = false;
		do {
			try {
				if (getText().length() > 1024)
					setText(getText().substring(0, 1023));
				setText("> "+txt+"\n"+getText());
				wait = false;
			}  catch (Error e){
				wait = true;
			}
		} while (wait);
	}
}
