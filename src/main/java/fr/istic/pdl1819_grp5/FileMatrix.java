package fr.istic.pdl1819_grp5;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;




public class FileMatrix {

	
	private String name;
	
	private String text;


	public FileMatrix(String name){
		this.name=name;
	}

	
	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

	public File saveCsv(String csvPath) throws IOException {
		File csv= new File(csvPath);
		FileWriter fr = new FileWriter(csv);
		fr.write(this.getText());
		fr.close();
		return csv;

	}

    public void append(String s) {
		this.text+=s;
    }
}

