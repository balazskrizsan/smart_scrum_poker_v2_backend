package org.kbalazs.common.io_module.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService
{
    public void saveString(String destinationFile, String content) throws IOException
    {
        Files.writeString(Paths.get(destinationFile), content);
    }

    public String readString(String sourceFile) throws IOException
    {
        return Files.readString(Paths.get(sourceFile));
    }
}
