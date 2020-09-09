package fr.istic.pdl1819_grp5;

import java.util.Set;

public class UrlMatrix
{

	private String link;
    private Set<FileMatrix> fileMatrixSet;

	public UrlMatrix(String link){
		this.link = link;
	}

	public String getLink() {
		return link;
	}

    public Set<FileMatrix> getFileMatrix() {
        return fileMatrixSet;
    }

    public void addFileMatrix(FileMatrix fileMatrix){
		fileMatrixSet.add(fileMatrix);
	}

	public UrlMatrix setFilesMatrix(Set<FileMatrix> fileMatrixSet) {
		this.fileMatrixSet = fileMatrixSet;
		return null;
	}
}

