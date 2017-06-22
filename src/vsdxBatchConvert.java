
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import com.mxgraph.io.mxVsdxCodec;
import com.mxgraph.online.Utils;

public class vsdxBatchConvert
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File folder = selectFile("Select folder", "vsdx");

		if (folder != null)
		{
			try
			{
				execute(folder);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Batch converts .vsdx files in the specified folder
	 */
	public static void execute(File folder) throws IOException
	{
		System.out.println(folder);
		
		try (Stream<Path> filePathStream=Files.walk(folder.toPath()))
		{
		    filePathStream.forEach(filePath ->
		    {
		        if (Files.isRegularFile(filePath))
		        {
		        	if (filePath.toString().endsWith(".vsdx"))
		        	{
		    			byte[] fileData = null;

						try
						{
							fileData = Files.readAllBytes(filePath);
						}
						catch (IOException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    			
		    			if (fileData != null)
		    			{
		    				mxVsdxCodec vdxCodec = new mxVsdxCodec();
		    				String xml = null;

		    				try
		    				{
		    					xml = vdxCodec.decodeVsdx(fileData, Utils.CHARSET_FOR_URL_ENCODING);
		    				}
		    				catch (ParserConfigurationException e)
		    				{
		    					// TODO Auto-generated catch block
		    					e.printStackTrace();
		    				}
		    				catch (IllegalArgumentException e)
		    				{
		    					// TODO Auto-generated catch block
		    					e.printStackTrace();
		    				}
		    				catch (SAXException e)
		    				{
		    					// TODO Auto-generated catch block
		    					e.printStackTrace();
		    				}
		    				catch (TransformerException e)
		    				{
		    					// TODO Auto-generated catch block
		    					e.printStackTrace();
		    				}
							catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				if (xml != null)
		    				{
		    					String path = StringUtils.substringBefore(filePath.toString(), ".");

		    					try (PrintWriter out = new PrintWriter(path + ".xml"))
		    					{
		    					    out.println(xml);
		    					}
								catch (FileNotFoundException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		    				}
		    			}

		        	}
		        }
		    });
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Shows a file dialog.
	 */
	public static File selectFile(String title, String extension)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle(title);

		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION)
		{
			return chooser.getSelectedFile();
		}

		return null;
	}
}
