package com.kcbgroup.main.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @ AUTHOR MKOECH
 **/
@Getter
@Setter
@Builder
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
