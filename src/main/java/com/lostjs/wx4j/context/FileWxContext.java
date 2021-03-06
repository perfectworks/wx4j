package com.lostjs.wx4j.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by pw on 02/10/2016.
 */
public class FileWxContext extends AbstractPersistentWxContext {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private String filePath;

    private FileWxContext() {
    }

    public FileWxContext(String filePath) {
        super();
        this.filePath = filePath;
    }

    protected void write() {
        LOG.debug("write context to {}", filePath);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8"));
            writer.write(dumpToString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    protected void read() {
        FileInputStream fileInputStream = null;
        File file = new File(filePath);

        try {
            if (!file.isFile()) {
                file.createNewFile();
            }
            fileInputStream = new FileInputStream(file);
            String string = StreamUtils.copyToString(fileInputStream, Charset.forName("UTF-8"));
            loadFromString(string);
        } catch (IOException e) {
            LOG.error("", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    protected void internalClear() {
        new File(filePath).delete();
    }
}
