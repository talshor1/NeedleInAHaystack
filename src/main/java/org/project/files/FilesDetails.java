package org.project.files;

import java.util.List;

public class FilesDetails {
    private List<String> filesList;

    public FilesDetails(List<String> filesList){
        this.filesList = filesList;
    }

    public List<String> getFilesList(){
        return filesList;
    }
}
