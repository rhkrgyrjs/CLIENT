package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class JHintTextField extends JTextField 
{  
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	private Font gainFont = new Font("Tahoma", Font.PLAIN, 11);  
	private Font lostFont = new Font("Tahoma", Font.ITALIC, 11);  
	   
	public JHintTextField(final String hint) 
	{
		setText(hint);  
	    setFont(lostFont);  
	    setForeground(Color.GRAY);  
	    this.addFocusListener(new FocusAdapter() 
	    {  
	    	@Override  
	         public void focusGained(FocusEvent e) 
	    	 {  
                setFont(gainFont);  
    			setForeground(Color.BLACK);  
	    		 if (getText().equals(hint) || getText().length()==0) 
	             {  
	    			 setText("");  
	             } 
	         }  
	   
	    	@Override  
	        public void focusLost(FocusEvent e) 
	    	{  
	    		if (getText().length() == 0) 
	    		{  
	    			setFont(lostFont);
	    			setText(hint);  
	    			setForeground(Color.GRAY);  
	    		} 
	    		else 
	    		{  
	    			setFont(gainFont);  
	    			setForeground(Color.BLACK);  
	    		}  
	       }  
		    });  
	}  
}